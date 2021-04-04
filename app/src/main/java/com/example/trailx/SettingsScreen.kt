package com.example.trailx

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SettingsScreen : AppCompatActivity() {
    private lateinit var fullname_tv: EditText
    private lateinit var age_tv: EditText
    private lateinit var gender_tv: EditText
    private lateinit var email_tv: EditText
    private lateinit var height_tv: EditText
    private lateinit var weight_tv: EditText
    private lateinit var password_et: EditText
    private var clickCountName : Int = 0
    private var clickCountAge : Int = 0
    private var clickCountGender : Int = 0
    private var clickCountEmail : Int = 0
    private var clickCountHeight : Int = 0
    private var clickCountWeight : Int = 0
    private var clickCountPassword : Int = 0
    lateinit var userFinal: User

    @SuppressLint("LogNotTimber")
    private fun readUserInfo() {
        val userFirebase = Firebase.auth.currentUser
        val database = Firebase.database.reference
        val email = userFirebase?.email
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val user = childSnapshot.getValue(User::class.java)
                    if (user?.email == email) {
                        val name = user?.name
                        val age = user?.age.toString()
                        val gender = user?.gender
                        val height = user?.height.toString()
                        val weight = user?.weight.toString()
                        val password = user?.password
                        fullname_tv.setText(name)
                        age_tv.setText(age)
                        gender_tv.setText(gender)
                        height_tv.setText(height)
                        weight_tv.setText(weight)
                        email_tv.setText(email)
                        password_et.setText(password)
                    }
                }
            }

            @SuppressLint("LogNotTimber")
            override fun onCancelled(error: DatabaseError) {
                Log.d("Hey", "We had a problem here")
            }
        }
        database.addValueEventListener(postListener)

        var users: List<User>? = null
        database.child("users").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    users = result.children.map { snapshot ->
                        snapshot.getValue(User::class.java)!!
                    }
                }
                users.let { users ->
                    users?.forEach { user ->
                        if (user.email == email) {
                            Log.d("User", "User found")
                            userFinal = user
                            val name = user.name
                            val age = user.age.toString()
                            val gender = user.gender
                            val height = user.height.toString()
                            val weight = user.weight.toString()
                            val password = user?.password
                            fullname_tv.setText(name)
                            age_tv.setText(age)
                            gender_tv.setText(gender)
                            height_tv.setText(height)
                            weight_tv.setText(weight)
                            email_tv.setText(email)
                            password_et.setText(password)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmail():Boolean {
        val email = email_tv.text.toString()
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

    private fun setupUIViews() {
        fullname_tv = findViewById<EditText>(R.id.full_name_edit_view)
        fullname_tv.isEnabled = false
        age_tv = findViewById<EditText>(R.id.age_edit_view)
        age_tv.isEnabled = false
        gender_tv = findViewById<EditText>(R.id.gender_edit_view)
        gender_tv.isEnabled = false
        email_tv = findViewById<EditText>(R.id.email_edit_view)
        email_tv.isEnabled = false
        height_tv = findViewById<EditText>(R.id.height_edit_view)
        height_tv.isEnabled = false
        weight_tv = findViewById<EditText>(R.id.weight_edit_view)
        weight_tv.isEnabled = false
        password_et = findViewById<EditText>(R.id.password_edit_view)
        password_et.isEnabled = false
    }

    private fun checkPW():Boolean {
        var check = false
        var number = false
        var upperCase = false
        var character = false
        var ch:Char
        val password = password_et.text.toString()
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

    private fun checkH():Boolean {
        var check = false
        val height = height_tv.text.toString()
        if (Integer.valueOf(height) in 50..300)
        {
            check = true
        }
        else {
            Toast.makeText(this, "Invalid height.", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    private fun checkW():Boolean {
        var check = false
        val weight = weight_tv.text.toString()
        if (Integer.valueOf(weight) in 20..500)
        {
            check = true
        }
        else
        {
            Toast.makeText(this, "Invalid weight.", Toast.LENGTH_SHORT).show()
        }
        return check
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_screen)
        setupUIViews()
        readUserInfo()
        supportActionBar?.hide()
        val back_to_home_bt_bar = findViewById<Button>(R.id.back_to_home_bt_settings)
        back_to_home_bt_bar.setOnClickListener{
            val intent_back_to_home_bt_bar = Intent(this, HomeScreen::class.java)
            startActivity(intent_back_to_home_bt_bar)
        }
        val settings_bt_bar = findViewById<Button>(R.id.settings_bt_settings)
        settings_bt_bar.setOnClickListener{
            val intent_settings_bt_bar = Intent(this, SettingsScreen::class.java)
            startActivity(intent_settings_bt_bar)
        }
        val discover_new_trails_bt_bar = findViewById<Button>(R.id.discover_new_trails_bt_settings)
        discover_new_trails_bt_bar.setOnClickListener{
            val intent_discover_new_trails_bt_bar = Intent(this, DiscoverNewTrailsScreen::class.java)
            startActivity(intent_discover_new_trails_bt_bar)
        }
        val active_trail_bt_bar = findViewById<Button>(R.id.active_trail_bt_settings)
        active_trail_bt_bar.setOnClickListener{
            val intent_active_trail_bt_bar = Intent(this, ActiveTrailScreen::class.java)
            startActivity(intent_active_trail_bt_bar)
        }
        val my_trails_bt_bar = findViewById<Button>(R.id.my_trails_bt_settings)
        my_trails_bt_bar.setOnClickListener{
            val intent_my_trails_bt_bar = Intent(this, MyTrailsScreen::class.java)
            startActivity(intent_my_trails_bt_bar)
        }
        val music_bt_bar = findViewById<Button>(R.id.music_bt_settings)
        music_bt_bar.setOnClickListener {
            val intent_music_bt_bar = Intent(this, MusicScreen::class.java)
            startActivity(intent_music_bt_bar)
        }

        val nameEdit = findViewById<Button>(R.id.edit_full_name)
        nameEdit.setOnClickListener {
            if (clickCountName == 0) {
                fullname_tv.isEnabled = true
                nameEdit.setBackgroundResource(R.drawable.save_bt_settings)
                clickCountName = 1
            }
            else {
                fullname_tv.isEnabled = false
                val databaseReference = Firebase.database.reference
                userFinal.username.let { it1 ->
                    databaseReference.child("users").child(it1).child("name").setValue(fullname_tv.text.toString())
                    Log.d("User Update", "Successful")
                }
                Log.d("User Update", "I did enter")
                clickCountName = 0
                nameEdit.setBackgroundResource(R.drawable.edit_bt)
            }
        }

        val ageEdit = findViewById<Button>(R.id.edit_age)
        ageEdit.setOnClickListener {
            if (clickCountAge == 0) {
                age_tv.isEnabled = true
                ageEdit.setBackgroundResource(R.drawable.save_bt_settings)
                clickCountAge = 1
            }
            else {
                age_tv.isEnabled = false
                val databaseReference = Firebase.database.reference
                userFinal.username.let { it1 ->
                    databaseReference.child("users").child(it1).child("age").setValue(age_tv.text.toString())
                    Log.d("User Update", "Successful")
                }
                clickCountAge = 0
                ageEdit.setBackgroundResource(R.drawable.edit_bt)
            }
        }

        val emailEdit = findViewById<Button>(R.id.edit_email)
        emailEdit.setOnClickListener {
            if (clickCountEmail == 0) {
                email_tv.isEnabled = true
                emailEdit.setBackgroundResource(R.drawable.save_bt_settings)
                clickCountEmail = 1
            }
            else {
                if(checkEmail()) {
                    email_tv.isEnabled = false
                    val databaseReference = Firebase.database.reference
                    userFinal.username.let { it1 ->
                        databaseReference.child("users").child(it1).child("email")
                            .setValue(email_tv.text.toString())
                        Log.d("User Update", "Successful")
                    }
                    var user = Firebase.auth.currentUser
                    user.updateEmail(email_tv.toString())
                    clickCountEmail = 0
                    emailEdit.setBackgroundResource(R.drawable.edit_bt)
                }
            }
        }

        val genderEdit = findViewById<Button>(R.id.edit_gender)
        genderEdit.setOnClickListener {
            if (clickCountGender == 0) {
                genderEdit.setBackgroundResource(R.drawable.save_bt_settings)
                gender_tv.isEnabled = true
                clickCountGender = 1
            }
            else {
                var gender = gender_tv.text.toString()
                if (gender == "Female" || gender == "Male" || gender == "Other") {
                    gender_tv.isEnabled = false
                    val databaseReference = Firebase.database.reference
                    userFinal.username.let { it1 ->
                        databaseReference.child("users").child(it1).child("gender")
                            .setValue(gender_tv.text.toString())
                        Log.d("User Update", "Successful")
                    }
                    clickCountGender = 0
                    genderEdit.setBackgroundResource(R.drawable.edit_bt)
                }
                else {
                    Toast.makeText(this, "Invalid gender.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val heightEdit = findViewById<Button>(R.id.edit_height)
        heightEdit.setOnClickListener {
            if (clickCountHeight == 0) {
                heightEdit.setBackgroundResource(R.drawable.save_bt_settings)
                height_tv.isEnabled = true
                clickCountHeight = 1
            }
            else {
                if (checkH()) {
                    height_tv.isEnabled = false
                    val databaseReference = Firebase.database.reference
                    userFinal.username.let { it1 ->
                        databaseReference.child("users").child(it1).child("height")
                            .setValue(height_tv.text.toString())
                        Log.d("User Update", "Successful")
                    }
                    clickCountHeight = 0
                    heightEdit.setBackgroundResource(R.drawable.edit_bt)
                }
            }
        }

        val weightEdit = findViewById<Button>(R.id.edit_weight)
        weightEdit.setOnClickListener {
            if (clickCountWeight == 0) {
                weightEdit.setBackgroundResource(R.drawable.save_bt_settings)
                weight_tv.isEnabled = true
                clickCountWeight = 1
            }
            else {
                if (checkW()) {
                    weight_tv.isEnabled = false
                    val databaseReference = Firebase.database.reference
                    userFinal.username.let { it1 ->
                        databaseReference.child("users").child(it1).child("weight")
                            .setValue(weight_tv.text.toString())
                        Log.d("User Update", "Successful")
                    }
                    clickCountWeight = 0
                    weightEdit.setBackgroundResource(R.drawable.edit_bt)
                }
            }
        }

        val passwordEdit = findViewById<Button>(R.id.edit_password)
        passwordEdit.setOnClickListener {
            if (clickCountPassword == 0) {
                passwordEdit.setBackgroundResource(R.drawable.save_bt_settings)
                password_et.isEnabled = true
                clickCountPassword = 1
            }
            else {
                if (checkPW()) {
                    password_et.isEnabled = false
                    val databaseReference = Firebase.database.reference
                    userFinal.username.let { it1 ->
                        databaseReference.child("users").child(it1).child("password")
                            .setValue(password_et.text.toString())
                        Log.d("User Update", "Successful")
                    }
                    clickCountPassword = 0
                    passwordEdit.setBackgroundResource(R.drawable.edit_bt)
                }
            }
        }
    }
}