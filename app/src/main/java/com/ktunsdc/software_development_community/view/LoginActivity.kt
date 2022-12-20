package com.ktunsdc.software_development_community.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.appbar.MaterialToolbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.ActivityLoginBinding
import com.ktunsdc.software_development_community.util.Singleton

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var toolbar: MaterialToolbar
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Software_Development_Community)
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)

        sharedPreferences=this.getSharedPreferences("com.ktunsdc.software_development_community", MODE_PRIVATE)


        val currentUser=Singleton.auth.currentUser
        currentUser?.let {
            if(currentUser.isEmailVerified){
                val memberInfo=sharedPreferences.getBoolean( "memberInfo",false)
                if(memberInfo){
                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}