package com.example.trailx

//Necessary imports
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : AppCompatActivity() {
    //Necessary variables
    private lateinit var auth: FirebaseAuth
    private var success = false
    private lateinit var forgotpassword:TextView
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var loginBT:Button

    //Function called on Start
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser!= null) {
            reload()
        }
    }

    //Function that is invoked on the creation of the Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_login_screen)
        setupUIViews()
        supportActionBar?.hide()

        //Login button
        val login_bt_HS = findViewById<Button>(R.id.login_bt_HS)
        login_bt_HS.setOnClickListener{
            authenticate()
        }

        //Forgot password button
        val forgotPassword = findViewById<TextView>(R.id.forgot_password)
        forgotPassword.setOnClickListener {
            if (username.text.toString().isNotEmpty()) {
                Firebase.auth.sendPasswordResetEmail(username.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                            Toast.makeText(
                                baseContext, "Password reset email sent",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            else {
                Toast.makeText(baseContext, "Please enter email.",
                Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Sign in using Firebase Authentication
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's informatio
                    success = true
                    val intent_login_bt_HS = Intent(this, HomeScreen::class.java)
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(intent_login_bt_HS)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun reload(){

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            success = true
        }
    }

    private fun authenticate() {
        if (validate()) {
            logincheck()
        }
    }

    //Connecting the variables to the XML tags
    fun setupUIViews() {
        forgotpassword = findViewById(R.id.forgot_password)
        username = findViewById(R.id.username_tb)
        password = findViewById(R.id.password_tb)
        loginBT = findViewById(R.id.login_bt_HS)
    }

    //Function to check if the username and email field has been left empty
    private fun validate():Boolean {
        var check = false
        val name = username.text.toString()
        val pw = password.text.toString()
        if (name.isEmpty() || pw.isEmpty())
        {
            Toast.makeText(this, "Please enter all the details.", Toast.LENGTH_SHORT).show()
        }
        else
        {
            check = true
        }
        return check
    }

    //Function to check if the correct details have been entered
    private fun logincheck() {
        //take data from database
        val name = username.text.toString()
        val pw = password.text.toString()
        signIn(name, pw)
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}