package net.noliaware.yumi.presentation.controllers

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.*
import net.noliaware.yumi.commun.util.DataError
import net.noliaware.yumi.commun.util.inflate
import net.noliaware.yumi.commun.util.withArgs
import net.noliaware.yumi.presentation.views.LoginParentView
import net.noliaware.yumi.presentation.views.LoginView
import net.noliaware.yumi.presentation.views.PasswordView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.NetworkInterface

class LoginFragment : Fragment() {

    private lateinit var userPrefs: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private var loginParentView: LoginParentView? = null
    private val viewModel by viewModel<LoginFragmentViewModel>()
    private val passwordIndexes = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.login_layout, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginParentView = view as LoginParentView
        loginParentView?.loginView?.callback = loginViewCallback
        loginParentView?.passwordView?.callback = passwordViewCallback

        userPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferencesEditor = userPrefs.edit()

        collectFlows()
    }

    fun getAndroidId(): String = Settings.Secure.getString(
        context?.applicationContext?.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    private fun getMac(): String? =
        try {
            NetworkInterface.getNetworkInterfaces()
                .toList()
                .find { networkInterface ->
                    networkInterface.name.equals(
                        "wlan0",
                        ignoreCase = true
                    )
                }
                ?.hardwareAddress
                ?.joinToString(separator = ":") { byte -> "%02X".format(byte) }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    private fun collectFlows() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.eventFlow.collectLatest { sharedEvent ->

                loginParentView?.setLoginViewProgressVisible(false)

                when (sharedEvent) {

                    is UIEvent.ShowSnackBar -> {

                        val message =
                            when (sharedEvent.dataError) {
                                DataError.NETWORK_ERROR -> getString(R.string.error_no_network)
                                DataError.SYSTEM_ERROR -> getString(R.string.error_contact_support)
                                DataError.NONE -> ""
                            }

                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.initStateFlow.collect { vmState ->
                vmState.data?.let { initData ->

                    preferencesEditor.putString(DEVICE_ID, initData.deviceId)
                    preferencesEditor.apply()

                    loginParentView?.setLoginViewProgressVisible(false)

                    loginParentView?.displayPasswordView()
                    loginParentView?.fillPadViewWithData(initData.keyboard)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.connectStateFlow.collect { vmState ->
                vmState.data?.let { connectData ->

                    if (connectData.managedAccounts.isNotEmpty()) {
                        AccountsListFragment()
                            .withArgs(MANAGED_ACCOUNTS_DATA to connectData.managedAccounts)
                            .show(
                                childFragmentManager.beginTransaction(),
                                ACCOUNTS_LIST_FRAGMENT_TAG
                            )
                    } else {
                        activity?.finish()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        intent.putExtra(CONNECT_DATA, connectData)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private val loginViewCallback: LoginView.LoginViewCallback by lazy {
        object : LoginView.LoginViewCallback {
            override fun onLoginEntered(login: String) {

                preferencesEditor.putString(LOGIN, login)
                preferencesEditor.apply()

                loginParentView?.setLoginViewProgressVisible(true)
                viewModel.callInitWebservice(
                    getAndroidId(),
                    userPrefs.getString(DEVICE_ID, null),
                    login
                )
            }
        }
    }

    private val passwordViewCallback: PasswordView.PasswordViewCallback by lazy {
        object : PasswordView.PasswordViewCallback {

            override fun onPadClickedAtIndex(index: Int) {
                if (passwordIndexes.size >= 6)
                    return

                passwordIndexes.add(index)
                loginParentView?.fillSecretDigitAtIndex(passwordIndexes.size - 1)
            }

            override fun onConfirmButtonPressed() {
                if (passwordIndexes.isEmpty())
                    return
                viewModel.callConnectWebserviceWithIndexes(passwordIndexes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginParentView = null
    }
}