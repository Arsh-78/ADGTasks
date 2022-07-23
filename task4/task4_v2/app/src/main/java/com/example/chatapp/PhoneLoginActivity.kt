package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.databinding.ActivityPhoneLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneLoginActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var  storedVerificationId : String
    lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var binding: ActivityPhoneLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        binding.getOtpButton.setOnClickListener {
            var phno : String = binding.phoneNo.text.toString().trim()

            if(!phno.isEmpty())
            {
                /*sendVerificationCode("+91$phno")*/
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91$phno")
                    .setTimeout(60L,TimeUnit.SECONDS)
                    .setActivity(this).setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            else
            {
                Toast.makeText(applicationContext,"Enter Phone Number",Toast.LENGTH_LONG).show()
            }
        }

        binding.phonesignUpButton.setOnClickListener {
            var otp : String = binding.otp.text.toString().trim()
            if(!otp.isEmpty())
            {
                verifyVerificationCode(otp)
            }
            else
            {
                Toast.makeText(applicationContext,"Enter OTP",Toast.LENGTH_LONG).show()
            }
        }

        callbacks = object  : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code : String? = p0.smsCode
                if(code != null)
                {
                    binding.otp.setText(code)
                }
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId,code!!)
                signIn(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext,"Something Went Wrong",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                storedVerificationId = p0
                resendToken = p1
                binding.phoneNo.visibility = View.GONE
                binding.getOtpButton.visibility = View.GONE
                binding.otp.visibility = View.VISIBLE
                binding.phonesignUpButton.visibility = View.VISIBLE
            }

        }
    }

    private fun  sendVerificationCode(phoneNo : String)
    {
       PhoneAuthProvider.getInstance().verifyPhoneNumber(
           phoneNo,
           60,TimeUnit.SECONDS,this,callbacks
       )
    }
    private  fun verifyVerificationCode(code : String)
    {
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storedVerificationId,code)
        signIn(credential)
    }
    private fun signIn(credential: PhoneAuthCredential)
    {
        auth.signInWithCredential(credential).addOnCompleteListener(this) {
            task ->
            if(task.isComplete)
            {
                val user : FirebaseUser? = task.result?.user

                val intent = Intent(applicationContext,ChatActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                if (task.exception is FirebaseAuthInvalidCredentialsException)
                {
                    Toast.makeText(applicationContext,"Code Entered is wrong",Toast.LENGTH_LONG).show()
                    binding.otp.setText("")

                }
            }
        }
    }
}