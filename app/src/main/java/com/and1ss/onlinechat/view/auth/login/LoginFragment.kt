package com.and1ss.onlinechat.view.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.and1ss.onlinechat.R
import com.and1ss.onlinechat.view.auth.ActivityChanger
import com.and1ss.onlinechat.view.auth.FragmentChanger
import com.and1ss.onlinechat.view.auth.register.RegisterFragment
import com.and1ss.onlinechat.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.ConnectException

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var backButton: ImageButton
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        backButton = view.findViewById(R.id.back_button)
        loginButton = view.findViewById(R.id.login_button)
        registerButton = view.findViewById(R.id.register_button)

        loginEditText = view.findViewById(R.id.login_input)
        passwordEditText = view.findViewById(R.id.password_input)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginEditText.setText(viewModel.login)
        passwordEditText.setText(viewModel.password)

        // TODO: add text watchers to editTexts to update viewmodel
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        loginButton.setOnClickListener {
            lifecycle.coroutineScope.launch {
                viewModel.login = loginEditText.text.toString()
                viewModel.password = passwordEditText.text.toString()

                try {
                    viewModel.login()
                } catch (e: ConnectException) {
                    showConnectionFailedToast()
                } catch (e: Exception) {
                    showInvalidCredentialsToast()
                }
            }
        }

        registerButton.setOnClickListener {
            (activity as? FragmentChanger)?.replaceFragment(RegisterFragment.newInstance())
        }

        viewModel.loginConfirmation.observe(viewLifecycleOwner) {
            when (it) {
                LoginViewModel.State.LOGGED_IN -> transitToMainActivity()
                LoginViewModel.State.FAILED ->
                    Toast.makeText(
                        requireContext(),
                        "Failed to log in",
                        Toast.LENGTH_SHORT
                    ).show()
                else -> {
                }
            }
        }
    }

    private fun showInvalidCredentialsToast() {
        Toast.makeText(
            requireContext(),
            R.string.incorrect_credentials_toast,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showConnectionFailedToast() {
        Toast.makeText(
            requireContext(),
            R.string.failed_to_connect_toast,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun transitToMainActivity() {
        context
            ?.let { MainActivity.newIntent(it) }
            ?.let { (activity as? ActivityChanger)?.transitToActivity(it) }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}