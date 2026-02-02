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
import androidx.navigation.fragment.navArgs
import com.example.searchroom.R
import com.example.searchroom.guestScreen.GuestActivity
import com.example.searchroom.authScreen.viewModel.SignUpViewModel
import com.example.searchroom.databinding.FragmentSignUpBinding
import com.example.searchroom.hostScreen.HostActivity
import com.example.searchroom.utils.NetworkUtils
import com.example.searchroom.utils.disable
import com.example.searchroom.utils.enable
import com.example.searchroom.utils.hide
import com.example.searchroom.utils.show
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private val TAG = "SignUpFragment"

    private val args: SignUpFragmentArgs by navArgs()
//    val userType = args.userType
    private val userType: String by lazy { args.userType }
//
//    private val userType: String by lazy {
//        requireContext()
//            .getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
//            .getString("selected_role", "GUEST") ?: "GUEST"
//    }

    private val viewModel: SignUpViewModel by viewModels()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val userType = args.userType
        Log.d(TAG, "Received userType: $userType")


        // observer State
        observeState()

        // Set Signup button
        setUpSignUpBtn()

        // Google SignUp Click and navigate
        setUpGoogleSignUp()

        // Go to Login
        binding.signUpLoginTv.setOnClickListener {
            val action =
                SignUpFragmentDirections.actionSignUpFragmentToLoginFragment(userType)
            findNavController().navigate(action)
        }

    }

    private fun setUpSignUpBtn() {

        binding.signUpBtn.setOnClickListener {

            Log.d(TAG, "Sign Up Clicked")
            viewModel.signUpWithEmailPassword(
                name = binding.signUpNameEt.text?.toString()?.trim().orEmpty(),
                email = binding.signUpEmailEt.text?.toString()?.trim().orEmpty(),
                password = binding.signUpPasswordEt.text?.toString()?.trim().orEmpty(),
                rePassword = binding.signUpRePasswordEt.text?.toString()?.trim().orEmpty(),
                userType = userType
            )
        }
    }

    private fun setUpGoogleSignUp() {
//        val userType = args.userType

        // Google SignUp
        binding.signUpGoogleBtn.setOnClickListener {
            if (!NetworkUtils.isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d(TAG, "Google Sign Up Clicked")
            viewModel.googleSignIn(
                activity = requireActivity(),
                webClientId = getString(R.string.default_web_client_id),
                selectedUserType = userType
            )
        }
    }

    // Observe state changes from ViewModel
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    setLoading(state.isLoading)

                    state.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "Error: $it")
                        viewModel.clearError()
                    }

                    state.navigateTo?.let { type ->
                        openDashboardByType(type)
                        Log.d(TAG, "Navigate to: $type")
                        Toast.makeText(requireContext(), "Signup successful! Logged in", Toast.LENGTH_SHORT).show()
                        viewModel.clearNavigation()
                    }
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.signUpProgressBar.show()
            binding.signUpBtn.disable()
            binding.signUpGoogleBtn.disable()
            binding.signUpLoginTv.isEnabled = false
        } else {
            binding.signUpProgressBar.hide()
            binding.signUpBtn.enable()
            binding.signUpGoogleBtn.enable()
            binding.signUpLoginTv.isEnabled = true
        }
    }

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
