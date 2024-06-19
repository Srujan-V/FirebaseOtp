package com.otp.verificationapplications

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.otp.verificationapplications.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    var ver_Id = ""

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val pattern = Regex("^[6-9][0-9]{9}\$")


        binding.submit.setOnClickListener {
            progres(true)
            if (binding.number.text.toString().isNullOrEmpty()) {
                binding.error.text = "Please enter number"
                progres(false)
                return@setOnClickListener
            }
            if (binding.number.text.toString().length < 10) {
                progres(false)
                binding.error.text = "Please enter right number"
                return@setOnClickListener
            }

            if (!pattern.matches(binding.number.text.toString())) {
                progres(false)
                binding.error.text = "Please enter right number"
                return@setOnClickListener
            }
            //  progres(false)
            binding.error.text = ""
            //  if (otp.text.isNullOrEmpty()){
            setUp(binding.number.text.toString())
            //  }else{
            //     verifyOtp(otp.text.toString())
            // }

        }


    }

    private fun progres(b: Boolean) {
        binding.progress.visibility = View.VISIBLE
        if (b) {
            binding.progress.visibility = View.VISIBLE
        } else {
            binding.progress.visibility = View.GONE
        }
    }


    private fun setUp(number: String) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91${number}",
            60, // Timeout duration
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Verification failed
                    Toast.makeText(
                        this@MainActivity,
                        "Wrong OTP",
                        Toast.LENGTH_SHORT
                    ).show()
                    progres(false)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Code sent successfully
                    ver_Id = verificationId
                    progres(false)
                    Toast.makeText(
                        this@MainActivity,
                        "OTP Sent Successfully",
                        Toast.LENGTH_SHORT
                    ).show()


                    val intent = Intent(this@MainActivity, OTPActivity::class.java)
                    intent.putExtra("verId", ver_Id)
                    startActivity(intent)
                }
            }
        )
        /* val options = PhoneAuthOptions.newBuilder(auth!!)
             .setPhoneNumber("+91${number}") // Phone number to verify
             .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
             .setActivity(this) // Activity (for callback binding)
             .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                 override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                     System.out.println("uhdifgu_________" + p0)
                 }

                 override fun onVerificationFailed(p0: FirebaseException) {
                     System.out.println("uhdifgu_________" + p0)
                 }

             }
             )
             .build()
          PhoneAuthProvider.verifyPhoneNumber(options)*/
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
