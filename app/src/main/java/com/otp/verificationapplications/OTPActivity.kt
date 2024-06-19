package com.otp.verificationapplications

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.otp.verificationapplications.databinding.ActivityOtpactivityBinding

class OTPActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    lateinit var binding: ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpactivity)
//        setContentView(R.layout.activity_otpactivity)
        auth = FirebaseAuth.getInstance()
        val verId = intent.getStringExtra("verId")

        binding.submit.setOnClickListener {
            progres(true)
            if (binding.otp.text.toString().isNullOrEmpty()) {
                binding.error.text = "Please Enter OTP"
                progres(false)
                return@setOnClickListener
            }

            verifyOtp(binding.otp.text.toString(), verId)
        }
    }

    private fun verifyOtp(otp: String, verId: String?) {

        val credential = PhoneAuthProvider.getCredential(verId!!, otp)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    progres(false)
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    binding.sucess.visibility = View.VISIBLE
                    Handler().postDelayed({
                        startActivity(Intent(this@OTPActivity, MainActivity::class.java))
                        binding.sucess.visibility = View.GONE
                        finish()
                    }, 1000)
                    Toast.makeText(
                        this@OTPActivity,
                        "OTP verified Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = task.result?.user
                } else {
                    progres(false)
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@OTPActivity,
                        "signInWithCredential:failure" + task.exception,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }

    }

    private fun progres(b: Boolean) {

        if (b) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.GONE
        }
    }
}