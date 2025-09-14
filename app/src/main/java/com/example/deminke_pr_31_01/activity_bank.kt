package com.example.deminke_pr_31_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class activity_bank : AppCompatActivity() {
    lateinit var login: EditText
    lateinit var password: EditText
    val Key_time = "time"
    val Key_mouth_pay="mouth_pay"
    val Key_sum="sum"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bank)
        login=findViewById(R.id.Login);
        password=findViewById(R.id.password);
    }

    fun sing_in(view: View){
        if(login.text.toString().isNotEmpty()&&password.text.toString().isNotEmpty()) {
            val intent = Intent(this@activity_bank, activity_calculate::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val alert = AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("У вас есть незаполненые поля")
                .setPositiveButton("Ok",null)
                .create()
                .show()
        }

    }


}