package com.example.trailx

//Necessary imports
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_screen.*

class RegisterScreen : AppCompatActivity() {
    //Variables necessary for a successful registration
    private lateinit var auth: FirebaseAuth
    private var success = false
    private lateinit var userFullName: EditText
    private lateinit var userEmail: EditText
    private lateinit var userAge: EditText
    private lateinit var userHeight: EditText
    private lateinit var userWeight: EditText
    private lateinit var userUserName: EditText
    private lateinit var userPassword: EditText
    private lateinit var userConfPassword: EditText
    private lateinit var registerBT:Button
    private lateinit var gender: RadioGroup
    private lateinit var genderradioButton: RadioButton
    private lateinit var reg_tv: TextView
    private lateinit var fullname_tv: TextView
    private lateinit var age_tv: TextView
    private lateinit var gender_tv: TextView
    private lateinit var email_tv: TextView
    private lateinit var height_tv: TextView
    private lateinit var weight_tv: TextView
    private lateinit var username_tv: TextView
    private lateinit var password_tv: TextView
    private lateinit var confPassword_tv: TextView
    private lateinit var reg_iv: ImageView
    private lateinit var user: User
    private var database = UserDatabase()

    //Function executed on start
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser!= null) {
            reload()
        }
    }

    //Function executed on the creation of the Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        //Retrieving the User information
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        FirebaseAuth.AuthStateListener{ auth ->
            val user = auth.currentUser
            if (user!= null) {
                val email = findViewById<TextView>(R.id.email_tv_my_account)
                email.text = user.email
            }
        }
        setContentView(R.layout.activity_register_screen)
        setupUIViews()
        supportActionBar?.hide()

        //Register button
        val reg_bt_HS = findViewById<Button>(R.id.register_bt_HS)
        reg_bt_HS.setOnClickListener {
            val fullname = userFullName.text.toString()
            val email = userEmail.text.toString()
            val age = userAge.text.toString()
            val height = userHeight.text.toString()
            val weight = userWeight.text.toString()
            val username = userUserName.text.toString()
            val password = userPassword.text.toString()
            var userGender = ""

            //Validity checks
            if (validate() && checkEmail() && checkHW() && checkPCP() && checkPW() && checkUN()) {
                val selectedId = gender.checkedRadioButtonId
                genderradioButton = findViewById<RadioButton>(selectedId)
                if (selectedId != -1) {
                    userGender = genderradioButton.text.toString()
                }
                //upload data to database
                user = User(username, fullname, age.toInt(),userGender, email, height.toInt(),  weight.toInt(), password)
                createUser(email, password)
                database.writeNewUser(user)
                Toast.makeText(this@RegisterScreen, "Registration Successful.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterScreen, HomeScreen::class.java))
            }
        }
    }

    //Creating User in the Firebase Authentication System
    @SuppressLint("LogNotTimber")
    private fun createUser (email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success")
                    updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    //Function on reload
    private fun reload(){

    }

    //Function to update UI
    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            success = true
        }
    }

    //Setting up the variables to access the XML tags
    private fun setupUIViews() {
        userFullName = findViewById<EditText>(R.id.user_fullname)
        userEmail = findViewById<EditText>(R.id.user_email)
        userAge = findViewById<EditText>(R.id.user_age)
        userHeight = findViewById<EditText>(R.id.user_height)
        userWeight = findViewById<EditText>(R.id.user_weight)
        userUserName = findViewById<EditText>(R.id.user_username)
        userPassword = findViewById(R.id.user_password)
        userConfPassword = findViewById<EditText>((R.id.user_conf_password))
        registerBT = findViewById<Button>(R.id.register_bt_HS)
        gender = findViewById<RadioGroup>(R.id.gender_RG)
        reg_tv = findViewById<TextView>(R.id.reg_tv)
        fullname_tv = findViewById<TextView>(R.id.full_name_tv)
        age_tv = findViewById<TextView>(R.id.age_tv)
        gender_tv = findViewById<TextView>(R.id.gender_tv)
        email_tv = findViewById<TextView>(R.id.email_tv)
        height_tv = findViewById<TextView>(R.id.height_tv)
        weight_tv = findViewById<TextView>(R.id.weight_tv)
        username_tv = findViewById<TextView>(R.id.username_tv)
        password_tv = findViewById<TextView>(R.id.password_tv)
        confPassword_tv = findViewById<TextView>(R.id.conf_password_tv)
        reg_iv = findViewById<ImageView>(R.id.reg_image)
    }

    //Function to check whether the fields are empty
    private fun validate():Boolean {
        var check = false
        val flag: Boolean
        val fullname = userFullName.text.toString()
        val email = userEmail.text.toString()
        val age = userAge.text.toString()
        val height = userHeight.text.toString()
        val weight = userWeight.text.toString()
        val username = userUserName.text.toString()
        val password = userPassword.text.toString()
        val confpassword = userConfPassword.text.toString()
        //copy up until here and paste in onClickListener if needed to add to database
        val selectedId = gender.checkedRadioButtonId
        genderradioButton = findViewById(selectedId)
        flag = selectedId == -1
        if (fullname.isEmpty() || email.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty() || username.isEmpty() || password.isEmpty() || flag || confpassword.isEmpty())
        {
            Toast.makeText(this, "Please enter all the details.", Toast.LENGTH_SHORT).show()
        }
        else
        {
            check = true
        }
        return check
    }

    //Function to check Height and Weight
    private fun checkHW():Boolean {
        var check = false
        val height = userHeight.text.toString()
        val weight = userWeight.text.toString()
        if (Integer.valueOf(height) in 50..300 && Integer.valueOf(weight) >= 20 && Integer.valueOf(weight) <= 500)
        {
            check = true
        }
        else
        {
            Toast.makeText(this, "Invalid weight/height.", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    //Function to check email entered
    private fun checkEmail():Boolean {
        val email = userEmail.text.toString()
        var check = false
        for (i in email.indices)
        {
            if (email[i] == '@')
            {
                check = true
            }
        }
        if (!check)
        {
            Toast.makeText(this, "Invalid email ID.", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    //Function to check password entered
    private fun checkPW():Boolean {
        var check = false
        var number = false
        var upperCase = false
        var character = false
        var ch:Char
        val password = userPassword.text.toString()
        if (password.length >= 8)
        {
            for (i in password.indices)
            {
                ch = password[i]
                if (Character.isDigit(ch))
                {
                    number = true
                }
                if (Character.isUpperCase(ch))
                {
                    upperCase = true
                }
                if (ch == '.' || ch == '_' || ch == '-')
                {
                    character = true
                }
            }
            if (number && upperCase && character)
            {
                check = true
            }
            else
            {
                Toast.makeText(this, "Invalid password, enter again", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "Invalid password, enter again", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    //Function to check username entered
    private fun checkUN():Boolean {
        var check = true
        var ch:Char
        val username = userUserName.text.toString()
        if (username.length >= 6)
        {
            for (i in username.indices)
            {
                ch = username[i]
                if (!Character.isDigit(ch) && !Character.isAlphabetic(ch.toInt()))
                {
                    check = false
                    Toast.makeText(this, "Invalid username, enter again", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else
        {
            Toast.makeText(this, "Invalid username, enter again", Toast.LENGTH_SHORT).show()
            check = false
        }
        return check
    }

    //Function to check whether the same password was entered in the password and confirm password field
    private fun checkPCP():Boolean {
        var check = false
        val password = userPassword.text.toString()
        val confpassword = userConfPassword.text.toString()
        if (confpassword == password)
        {
            check = true
        }
        else
        {
            Toast.makeText(this, "Password confirmation incorrect. Enter again.", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}