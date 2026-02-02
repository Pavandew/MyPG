package com.example.searchroom.authScreen.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.searchroom.R
import com.example.searchroom.authScreen.viewModel.LoginViewModel
import com.example.searchroom.databinding.FragmentLoginBinding
import com.example.searchroom.guestScreen.GuestActivity
import com.example.searchroom.hostScreen.HostActivity
import com.example.searchroom.utils.disable
import com.example.searchroom.utils.enable
import com.example.searchroom.utils.hide
import com.example.searchroom.utils.show
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"

    private val userType: String by lazy {
        requireContext()
            .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            .getString("selected_role", "GUEST") ?: "GUEST"
    }

    private val viewModel : LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Received userType: $userType")

        // Observe state changes from ViewModel
        observeState()

        // Login with email and password
        setupEmailLogin()

        // Login with Google
        setupGoogleLogin()

        // Forget Password
        binding.loginForgetPassword.setOnClickListener {
            forgetPassword()
        }

        // Navigate to SignUp (role only matters during first-time signup)
        binding.loginSignUpTv.setOnClickListener {
            val action = LoginFragmentDirections
                .actionLoginFragmentToSignUpFragment(userType)
            findNavController().navigate(action)
        }
    }

    // Observe state changes from ViewModel
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.uiState.collect { state ->

                    setLoading(state.isLoading)
                    Log.d(TAG, "ObserverState ")

                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: $it")
                        viewModel.clearLoginError()
                    }

                    state.navigateTo?.let { type ->
                        openDashboardByType(type)
                        Log.d(TAG, "Navigate to: $type")
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                        viewModel.clearLoginNavigation()
                    }
                }
            }
        }
    }

    // Login with email and password
    private fun setupEmailLogin() {
        binding.loginLoginBtn.setOnClickListener {

            Log.d(TAG, "Login Clicked")
            val email = binding.loginEmailEt.text?.toString()?.trim().orEmpty()
            val pass = binding.loginPasswordEt.text?.toString()?.trim().orEmpty()

            viewModel.loginWithMail(email, pass)
        }
    }

    // Login with Google
    private fun setupGoogleLogin() {
        binding.loginGoogleBtn.setOnClickListener {
            Log.d(TAG, "Google Login Clicked")
            viewModel.loginWithGoogle(activity = requireActivity(), webClientId = getString(R.string.default_web_client_id))
        }
    }

    private fun forgetPassword() {
        val email = binding.loginEmailEt.text?.toString()?.trim()

        if(email.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Enter your email first", Toast.LENGTH_SHORT).show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Reset link sent to your email", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Reset link sent to your email")
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loginProgressBar.show()
            binding.loginLoginBtn.disable()
            binding.loginGoogleBtn.disable()
            binding.loginSignUpTv.isEnabled = false
        } else {
            binding.loginProgressBar.hide()
            binding.loginLoginBtn.enable()
            binding.loginGoogleBtn.enable()
            binding.loginSignUpTv.isEnabled = true
        }
    }

    // Navigate to Dashboard based on userType
    private fun openDashboardByType(userType: String) {
        val target =
            if (userType == "HOST") HostActivity::class.java else GuestActivity::class.java

        val intent = Intent(requireActivity(), target).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
