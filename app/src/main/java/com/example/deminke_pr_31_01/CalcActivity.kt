package com.example.deminke_pr_31_01

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class CalcActivity : AppCompatActivity() {
    lateinit var pref: SharedPreferences
    private lateinit var sum: TextView
    private lateinit var mouth: TextView
    private lateinit var time: TextView

    private  val PREFS_NAME = "credit_data"
    private  val KEY_SUM = "sum"
    private  val KEY_MOUTH_PAY = "mouth_pay"
    private val KEY_TIME = "time"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calcul)

        // Загрузка сохраненных данных
        loadSavedData()


    }

    private fun loadSavedData() {

        val pref = getSharedPreferences("credit_data", MODE_PRIVATE)
        findViewById<TextView>(R.id.sum).text ="Сумма кредита: "+ pref.getString("sum", "Не указано").toString().toDouble().roundToInt()
        findViewById<TextView>(R.id.mouth).text ="Ежемесячный платеж: "+pref.getString("mouth_pay", "Не указано").toString().toDouble().roundToInt()
        findViewById<TextView>(R.id.time).text ="Срок кредита: "+ pref.getString("time", "Не указано")
    }

    fun reg(view: View){
        val intent = Intent(this@CalcActivity, activity_bank::class.java)
        startActivity(intent)
        finish()

    }
}