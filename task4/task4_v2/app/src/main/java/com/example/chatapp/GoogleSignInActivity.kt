package com.example.chatapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInActivity : AppCompatActivity() {


    lateinit var googleSignInButon : SignInButton
    private lateinit var  googleSignInClient: GoogleSignInClient
    private  lateinit var firebaseAuth : FirebaseAuth

    private companion object{
       private const val RC_SIGN_IN : Int = 100
       private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)


        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        googleSignInButon = findViewById(R.id.google_button)

        googleSignInButon.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null)
        {
            startActivity(Intent(this@GoogleSignInActivity,ChatActivity::class.java))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN)
        {
            val accuntTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accuntTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            }
            catch (e:Exception)
            {

            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener { authResult ->
            val firebaseUser =firebaseAuth.currentUser

            val  uid = firebaseAuth.uid
            val email = firebaseUser!!.email

            startActivity(Intent(this@GoogleSignInActivity,ChatActivity::class.java))
            finish()

        }
            .addOnFailureListener {
                Toast.makeText(this@GoogleSignInActivity,"Something wrong",Toast.LENGTH_LONG).show()
            }

    }


}