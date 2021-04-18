package com.studio.googlelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private val TAG = "SignInActivity"
    private lateinit var mStatusTextView:TextView
    private lateinit var mSignInClientInButton:SignInButton
    private lateinit var mSignInClientOutButton:LinearLayout
    private lateinit var mSignInClient:GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mSignInClient = GoogleSignIn.getClient(this, gso)

        mStatusTextView = this.findViewById<TextView>(R.id.status)
        mSignInClientInButton = this.findViewById<SignInButton>(R.id.sign_in_button)
        mSignInClientOutButton = this.findViewById<LinearLayout>(R.id.sign_out_and_disconnect)

        findViewById<SignInButton>(R.id.sign_in_button).setOnClickListener{
            signIn()
        }
    }


    private fun signIn() {
        val signInIntent: Intent = mSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }

    private fun updateUI(account: GoogleSignInAccount?){
        if (account != null) {
            mStatusTextView.text = getString(R.string.signed_in_fmt, account.displayName)
            mSignInClientInButton.visibility = View.GONE
            mSignInClientOutButton.visibility = View.VISIBLE
        } else {
            mStatusTextView.setText(R.string.signed_out)
            mSignInClientInButton.visibility = View.VISIBLE
            mSignInClientOutButton.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.e(TAG, "account: " + account!!.idToken)
            val acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto = acct.photoUrl
                Log.e(TAG, "personName: $personName")
                Log.e(TAG, "personGivenName: $personGivenName")
                Log.e(TAG, "personFamilyName: $personFamilyName")
                Log.e(TAG, "personEmail: $personEmail")
                Log.e(TAG, "personId: $personId")
                Log.e(TAG, "personPhoto: $personPhoto")
            }
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
}