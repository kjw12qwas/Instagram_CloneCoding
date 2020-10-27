package com.example.jaewonstagram

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var usernameView : TextInputEditText
    lateinit var userPassword1View : TextInputEditText
    lateinit var userPassword2View : TextInputEditText
    lateinit var signUp : Button
    lateinit var signIn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView(this@MainActivity)
        setupListener(this)
    }
    fun setupListener(activity: Activity) {
        signUp.setOnClickListener {
            register(this@MainActivity)
        }
        signIn.setOnClickListener {
            val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
            val token = sp.getString("login_sp", "")
            Log.d("abc","token : " + token)

        }
    }

    fun register(activity: Activity){
        val username = usernameView.text.toString()
        val password1 = userPassword1View.text.toString()
        val password2 = userPassword2View.text.toString()

        (application as MasterApplication).service.register(username, password1, password2).enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("ohhoohoo",t.toString())
                Toast.makeText(activity,"가입에 실패 하였습니다." ,Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful){
                    Toast.makeText(activity,"가입에 성공하였습니다.", Toast.LENGTH_LONG).show()
                    val user = response.body()
                    val token = user!!.token!!
                    saveUserToken(token,activity)
                }
            }

        })
    }

    fun saveUserToken(token : String, activity : Activity) {
        val sp = activity.getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("login_sp", token)
        editor.commit()
    }

    fun initView(activity: Activity){
        usernameView = activity.findViewById(R.id.Sign_in_id)
        userPassword1View = activity.findViewById(R.id.Sign_in_pw)
        userPassword2View = activity.findViewById(R.id.Sign_in_again_pw)
        signUp = activity.findViewById(R.id.SignUp)
        signIn = activity.findViewById(R.id.SignIn)
    }

    fun getUserName() : String{
        return usernameView.text.toString()
    }

    fun getUserPassword1() : String {
        return userPassword1View.text.toString()
    }

    fun getUserPassword2() : String{
        return userPassword2View.text.toString()
    }
}