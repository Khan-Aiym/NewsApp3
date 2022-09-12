package com.example.homework41

import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.homework41.OTP_Receiver.OtpReceiverListener
import com.example.homework41.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.*
import java.util.concurrent.TimeUnit

class LoginFragment constructor() : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null
    private var otpReceiver: OTP_Receiver? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    public override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonNumber()
        quit()
    }

    private fun quit() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                public override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun buttonNumber() {
        println("buttonNumber()")
        binding.btnSend.setOnClickListener{
            val data: String = Objects.requireNonNull(
                binding.tiedNumber.text
            ).toString().trim { it <= ' ' }
            println("data   ---= = = $data")
            if (TextUtils.isEmpty(data)) {
                binding.tilNumber.error = "Введите номер правильно"
                return@setOnClickListener
            } else {
                binding.line1.visibility = View.GONE
                binding.tilNumber.error = null
                binding.tilCode.visibility = View.VISIBLE
            }
            binding.btnSend.text = "Отправить код"
            val finalNumber: String = "+996" + data
            println("finalNumber----$finalNumber country ")
            register(finalNumber.trim { it <= ' ' })
            checkingSMSCode("")
            }
    }

    private fun checkingSMSCode(otp: String) {
        otpAutoReceiver()
        binding.btnSend.setOnClickListener{
            val mOtp: String = Objects.requireNonNull(
                binding.tiedCode.text
            ).toString().trim { it <= ' ' }
            println("Code   ----- $otp")
            if (mOtp.isEmpty() || mOtp.length < 6) {
                binding.tilCode.error = "Введите правильный код"
                binding.tiedCode.requestFocus()
                return@setOnClickListener
            } else {
                binding.tilCode.error = null
                verifyCode(mOtp)
            }
        }
    }

    private fun verifyCode(mOtp: String) {
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential((verificationId)!!, mOtp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                requireActivity(),
                OnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        showSnackbar("signInWithCredential:success")
                        val navController: NavController = findNavController(
                            requireActivity(),
                            R.id.nav_host_fragment_activity_main
                        )
                        navController.navigate(R.id.boardFragment)
                    } else {
                        showSnackbar("signInWithCredential:failure" + task.exception)
                    }
                }
            )
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            public override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                showSnackbar("super.onCodeAutoRetrievalTimeOut(s);-----$s")
            }

            public override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                showSnackbar("onVerificationCompleted:$phoneAuthCredential")
                val code: String? = phoneAuthCredential.smsCode
                showSnackbar("String code = phoneAuthCredential.getSmsCode();$code")
            }

            public override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    showSnackbar("onVerificationFailed $e")
                } else if (e is FirebaseTooManyRequestsException) {
                    showSnackbar("onVerificationFailed $e")
                }
                showSnackbar("onVerificationFailed $e")
            }

            public override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                showSnackbar("verificationId$verificationId")
                verificationId = s
                showSnackbar("onCodeSent:$s")
            }
        }

    private fun showSnackbar(s: String) {
        println(s)
        val snackbar: Snackbar = Snackbar.make(requireView(), s, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    private fun register(phoneNumber: String?) {
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder((mAuth)!!)
            .setPhoneNumber((phoneNumber)!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun otpAutoReceiver() {
        showSnackbar("otp------")
        otpReceiver = OTP_Receiver()
        requireActivity().registerReceiver(
            otpReceiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        )
        otpReceiver!!.initListener(object : OtpReceiverListener {
            public override fun onOtpSuccess(otp: String?) {
                binding.tiedCode.setText(otp)
                showSnackbar("otp ---success---")
            }

            public override fun otpTimeOut() {
                showSnackbar("something wroth with you")
            }
        })
    }

    private fun resendForOtp() {
        buttonNumber()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (otpReceiver != null) {
            requireActivity().unregisterReceiver(otpReceiver)
        }
    }
}