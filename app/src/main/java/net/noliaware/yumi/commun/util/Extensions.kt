package net.noliaware.yumi.commun.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import net.noliaware.yumi.BuildConfig
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.data.remote.dto.ErrorDTO
import net.noliaware.yumi.commun.data.remote.dto.SessionDTO
import net.noliaware.yumi.commun.domain.model.SessionData
import net.noliaware.yumi.feature_login.presentation.controllers.LoginActivity
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun generateToken(timestamp: String, methodName: String, randomString: String): String {
    return "noliaware|$timestamp|${methodName}|${timestamp.reversed()}|$randomString".sha256()
}

fun getCommonWSParams(sessionData: SessionData) = mapOf(
    LOGIN to sessionData.login,
    APP_VERSION to BuildConfig.VERSION_NAME,
    DEVICE_ID to sessionData.deviceId,
    SESSION_ID to sessionData.sessionId,
    SESSION_TOKEN to sessionData.sessionToken
)

suspend fun <T> FlowCollector<Resource<T>>.handleSessionAndFailureIfAny(
    session: SessionDTO?,
    sessionData: SessionData,
    error: ErrorDTO?
): Boolean {

    var errorType: ErrorType = ErrorType.SYSTEM_ERROR

    session?.let { sessionDTO ->
        sessionData.apply {
            sessionId = sessionDTO.sessionId
            sessionToken = sessionDTO.sessionToken
        }
        errorType = ErrorType.RECOVERABLE_ERROR
    }

    error?.let { errorDTO ->
        emit(
            Resource.Error(
                errorType = errorType,
                errorMessage = errorDTO.errorMessage
            )
        )

        return true
    }

    return false
}

fun Fragment.handleSharedEvent(sharedEvent: UIEvent) {

    when (sharedEvent) {

        is UIEvent.ShowSnackBar -> {

            val message = if (!sharedEvent.errorMessage.isNullOrEmpty()) {

                sharedEvent.errorMessage

            } else {

                when (sharedEvent.errorType) {
                    ErrorType.NETWORK_ERROR -> getString(R.string.error_no_network)
                    ErrorType.SYSTEM_ERROR -> getString(R.string.error_contact_support)
                    ErrorType.RECOVERABLE_ERROR -> getString(R.string.error_contact_support)
                    ErrorType.NONE -> ""
                }
            }

            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}

fun Fragment.redirectToLoginScreen(sharedEvent: UIEvent) {
    if (sharedEvent is UIEvent.ShowSnackBar) {
        if (sharedEvent.errorType == ErrorType.SYSTEM_ERROR) {
            activity?.finish()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }
    }
}

suspend inline fun <T> handleWSResult(
    result: Resource<T>,
    stateFlow: MutableStateFlow<ViewModelState<T>>,
    eventFlow: MutableSharedFlow<UIEvent>
) {

    when (result) {
        is Resource.Success -> {
            result.data?.let {
                stateFlow.value = ViewModelState()
            }
        }
        is Resource.Loading -> {
            stateFlow.value = ViewModelState()
        }
        is Resource.Error -> {
            stateFlow.value = ViewModelState()
            eventFlow.emit(UIEvent.ShowSnackBar(result.errorType))
        }
    }
}

fun <T : Fragment> T.withArgs(vararg pairs: Pair<String, Any?>) = apply { arguments = bundleOf(*pairs) }

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun Context.drawableIdByName(resIdName: String?): Int {
    resIdName?.let {
        return resources.getIdentifier(it, "drawable", packageName)
    }
    throw Resources.NotFoundException()
}

fun View.getStatusBarHeight() = convertDpToPx(24)

fun View.measureWrapContent() {
    measure(
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    )
}

fun View.layoutToTopLeft(left: Int, top: Int) {
    val right = left + measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToTopRight(right: Int, top: Int) {
    val left = right - measuredWidth
    val bottom = top + measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomLeft(left: Int, bottom: Int) {
    val right = left + measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.layoutToBottomRight(right: Int, bottom: Int) {
    val left = right - measuredWidth
    val top = bottom - measuredHeight
    layout(left, top, right, bottom)
}

fun View.convertDpToPx(dpValue: Int): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dpValue.toFloat(),
    context.resources.displayMetrics
).toInt()

fun RecyclerView.onItemClicked(
    onClick: ((position: Int, view: View) -> Unit)? = null,
    onLongClick: ((position: Int, view: View) -> Unit)? = null
) {
    this.addOnChildAttachStateChangeListener(RecyclerItemClickListener(this, onClick, onLongClick))
}

fun Context.showKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.ime())
    }
}

fun Context.hideKeyboard() {
    (this as? Activity)?.let {
        WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.ime())
    }
}

fun View.getKeyboardHeight(): Int? {

    /*WindowInsetsCompat
        .toWindowInsetsCompat(View.rootWindowInsets)
        .getInsets(WindowInsetsCompat.Type.ime()).bottom

     */

    return ViewCompat.getRootWindowInsets(this)?.getInsets(WindowInsetsCompat.Type.ime())?.bottom
}

inline fun <reified T : View> View.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Activity.find(id: Int): T = findViewById(id)
inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
inline fun <reified T : View> RecyclerView.ViewHolder.find(id: Int): T =
    itemView.findViewById(id) as T

inline fun <reified T : View> View.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Activity.findOptional(id: Int): T? = findViewById(id) as? T
inline fun <reified T : View> Fragment.findOptional(id: Int): T? = view?.findViewById(id) as? T
inline fun <reified T : View> RecyclerView.ViewHolder.findOptional(id: Int): T? =
    itemView.findViewById(id) as? T

fun String.sha256(): String {
    return try {
        val md = MessageDigest.getInstance("SHA-256")
        val messageDigest = md.digest(this.toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        hashtext
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
}

@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getDrawableCompat(@DrawableRes drawableRes: Int): Drawable {
    return AppCompatResources.getDrawable(this, drawableRes)!!
}

@CheckResult
fun Drawable.tint(@ColorInt color: Int): Drawable {
    val tintedDrawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(tintedDrawable, color)
    return tintedDrawable
}

@CheckResult
fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable {
    return tint(context.getColorCompat(color))
}

fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)