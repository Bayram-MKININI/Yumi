package net.noliaware.yumi.controllers

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import net.noliaware.yumi.R
import net.noliaware.yumi.model.ACCOUNTS_LIST_FRAGMENT_TAG
import net.noliaware.yumi.model.DataManager
import net.noliaware.yumi.model.LOGIN
import net.noliaware.yumi.utils.inflate
import net.noliaware.yumi.views.LoginParentView
import net.noliaware.yumi.views.LoginView
import net.noliaware.yumi.views.PasswordView
import java.net.NetworkInterface

class LoginFragment : Fragment() {

    private lateinit var dataManager: DataManager
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private var loginParentView: LoginParentView? = null
    private val passwordIndexes = arrayListOf<Int>()

    private val loginFragmentViewModel: LoginFragmentViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(context?.applicationContext as Application)
            .create(LoginFragmentViewModel::class.java)
    }

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

        val userPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferencesEditor = userPrefs.edit()

        dataManager = DataManager.get()
        dataManager.login = userPrefs.getString(LOGIN, "")!!

        setUpEvents()

        initWithLogin()
    }

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

    private fun initWithLogin() {
        if (dataManager.login.isEmpty())
            return

        loginFragmentViewModel.callInitWebservice()
    }

    private fun setUpEvents() {

        loginFragmentViewModel.initLoaded.observe(viewLifecycleOwner) {
            loginParentView?.fillPadViewWithData(DataManager.get().keyboardSymbols)
        }

        loginFragmentViewModel.connectLoaded.observe(viewLifecycleOwner) {
            activity?.finish()
            startActivity(Intent(context, MainActivity::class.java))
        }

        loginFragmentViewModel.errorResponseLiveData.observe(viewLifecycleOwner) { errorResponse ->

            val errorMessage = when {

                errorResponse.errorCode > 0 -> {
                    getString(R.string.error_message, errorResponse.message, errorResponse.retries)
                }
                errorResponse.errorCode == -11 -> {
                    getString(R.string.error_no_network)
                }
                else -> {
                    getString(R.string.error_contact_support)
                }
            }

            Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private val loginViewCallback: LoginView.LoginViewCallback by lazy {
        object : LoginView.LoginViewCallback {
            override fun onLoginEntered(login: String) {

                preferencesEditor.putString(LOGIN, login)
                preferencesEditor.apply()

                dataManager.login = login

                loginParentView?.displayPasswordView()

                initWithLogin()
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

                val fragmentTransaction = childFragmentManager.beginTransaction()
                val accountsListFragment = AccountsListFragment()
                accountsListFragment.show(fragmentTransaction, ACCOUNTS_LIST_FRAGMENT_TAG)

                if (passwordIndexes.isEmpty())
                    return

                loginFragmentViewModel.callConnectWebserviceWithIndexes(passwordIndexes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginParentView = null
    }
}