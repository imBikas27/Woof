package com.example.woof.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.example.woof.MainActivity
import com.example.woof.R
import com.example.woof.repo.Response
import com.example.woof.viewmodel.AppViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignIn : Fragment() {

    private lateinit var signInEmail: TextInputEditText
    private lateinit var signInPassword: TextInputEditText
    private lateinit var signInPasswordLayout: TextInputLayout
    private lateinit var signInProgressbar: LottieAnimationView
    private lateinit var signInBtn: Button
    private val emailPattern by lazy { "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+" }
    private var appViewModel: AppViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        signInEmail = view.findViewById(R.id.signInEmail)
        signInPassword = view.findViewById(R.id.signInPassword)
        signInPasswordLayout = view.findViewById(R.id.signInPasswordLayout)
        signInBtn = view.findViewById(R.id.signInButton)
        signInProgressbar = view.findViewById(R.id.signInProgressbar)

        signInBtn.setOnClickListener {
            signIn()
        }

        return view
    }

    private fun signIn() {
        signInProgressbar.visibility = View.VISIBLE

        val email = signInEmail.text.toString()
        val password = signInPassword.text.toString()
        var allRight = true

        if (email.isEmpty()) {
            signInEmail.error = "Enter your email address"
            allRight = false
        }
        if (password.isEmpty()) {
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
            signInPassword.error = "Enter your password"
            allRight = false
        }
        if (!email.matches(emailPattern.toRegex())) {
            signInEmail.error = "Enter valid email address"
            allRight = false
        }
        if (password.length < 6) {
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
            signInPassword.error = "Enter password more than 6 characters"
            allRight = false
        }

        if (!allRight) {
            signInProgressbar.visibility = View.GONE
            Toast.makeText(
                requireActivity().applicationContext,
                "Fill up your details properly",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            appViewModel!!.login(email, password)
            appViewModel!!.response.observe(requireActivity()
            ) {
                when (it) {
                    is Response.Success -> {
                        signInProgressbar.visibility = View.GONE
                        requireActivity().startActivity(Intent(requireActivity().applicationContext, MainActivity::class.java))
                    }
                    is Response.Failure -> {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            it.errorMassage,
                            Toast.LENGTH_LONG
                        ).show()
                        signInProgressbar.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }

    }

}