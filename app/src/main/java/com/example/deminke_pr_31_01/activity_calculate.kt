package com.example.deminke_pr_31_01

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class activity_calculate : AppCompatActivity() {
    private lateinit var seekBar: SeekBar
    private lateinit var textView: TextView

    private lateinit var Srock: EditText
    private lateinit var Endless: TextView

    private  val PREFS_NAME = "credit_data"
    private  val KEY_SUM = "sum"
    private  val KEY_MOUTH_PAY = "mouth_pay"
    private  val KEY_TIME = "time"
    lateinit var pref: SharedPreferences
    var b : Int=50000
    var a : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_credit_calculate)
        textView=findViewById(R.id.bar)
        seekBar = findViewById(R.id.seek_bar)
        Srock = findViewById(R.id.srock)
        Endless = findViewById(R.id.endless)
        seekBar.min=30000
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Этот метод вызывается при каждом изменении положения ползунка
                if(Srock.text.toString()!=""){
                    var a = Srock.text.toString().toInt()
                    if(a>0){
                        Endless.text=(b/a).toString()+"руб\n Ежемесячная плата"
                    }
                }
                textView.text="Вы хотите взять: $progress"
                b=progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Вызывается когда пользователь начинает перемещать ползунок
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Вызывается когда пользователь отпускает ползунок

            }
        })
        Srock.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Вызывается перед изменением текста
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Вызывается во время изменения текста
                if(Srock.text.toString()!=""){
                    a = Srock.text.toString().toInt()
                    if(a>0){
                        Endless.text=((b/a)).toString()+"руб\n Ежемесячная плата"

                    }
                }
                else{
                    Endless.text="Ежемесячная плата"
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    fun back(view: View){
            val intent = Intent(this@activity_calculate, activity_bank::class.java)
            startActivity(intent)
            finish()

    }

    fun rass(view: View) {
        if(Endless!=null){
            val pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val ed = pref.edit()
            if(a<=12){
            ed.putString("sum", calculateUnder1Year(b.toDouble(),a).toString())
                ed.putString("mouth_pay", ((calculateUnder1Year(b.toDouble(),a))/a).toString())}
            else{
                if(a<=24){

                    ed.putString("sum", calculate1To2Years(b.toDouble(),a).toString())
                    ed.putString("mouth_pay", ((calculate1To2Years(b.toDouble(),a))/a).toString())
                }
                else{
                    ed.putString("sum", calculateOver2Years(b.toDouble(),a).toString())
                    ed.putString("mouth_pay", ((calculateOver2Years(b.toDouble(),a))/a).toString())
                }
            }

            ed.putString("time", Srock.text.toString())
            ed.apply()
        val intent = Intent(this@activity_calculate, CalcActivity::class.java)
        startActivity(intent)
        finish()}
        else{
            val alert = AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("У вас есть незаполненые поля")
                .setPositiveButton("Ok",null)
                .create()
                .show()
        }

    }

    private fun saveData(totalSum: Int, months: Int) {
        val pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = pref.edit()

        val monthlyPayment = totalSum / months

        editor.putString(KEY_SUM, totalSum.toString())
        editor.putString(KEY_MOUTH_PAY, monthlyPayment.toString())
        editor.putString(KEY_TIME, months.toString())

        findViewById<TextView>(R.id.sum).text = pref.getString("sum", b.toString())
        findViewById<TextView>(R.id.mouth).text = pref.getString("mouth_pay", a.toString())
        findViewById<TextView>(R.id.time).text = pref.getString("time", Srock.toString())
        editor.apply()
    }

    private fun calculateUnder1Year(b: Double, a: Int): Double {
        val basePayment = b
        val interest = b * 0.059 // 5.9%
        return basePayment + interest
    }

    // б) От 1 до 2 лет
    private fun calculate1To2Years(b: Double, a: Int): Double {
        // Первые 12 месяцев по ставке 5.9%
        val firstYearPayment = calculateUnder1Year(b, 12)
        val paidInFirstYear = firstYearPayment * 12

        // Оставшаяся сумма после первого года
        val remainingSum = b - paidInFirstYear
        val remainingMonths = a - 12

        // Платеж за оставшиеся месяцы по ставке 5.1%
        val basePayment = b / a
        val interest = remainingSum * 0.051 // 5.1%

        return basePayment + interest
    }

    // в) Более 2 лет
    private fun calculateOver2Years(b: Double, a: Int): Double {
        // Первые 12 месяцев по ставке 5.9%
        val firstYearPayment = calculateUnder1Year(b, 12)
        val paidInFirstYear = firstYearPayment * 12

        // Следующие 12 месяцев по ставке 5.1%
        val remainingAfterFirstYear = b - paidInFirstYear
        val secondYearPayment = calculate1To2Years(b, 24) // Расчет для 24 месяцев
        val paidInSecondYear = secondYearPayment * 12
        val totalPaidIn2Years = paidInFirstYear + paidInSecondYear

        // Оставшаяся сумма после двух лет
        val remainingSum = b - totalPaidIn2Years
        val remainingMonths = a - 24

        // Платеж за оставшиеся месяцы по ставке 4.2%
        val basePayment = b / a
        val interest = remainingSum * 0.042 // 4.2%

        return basePayment + interest
    }


}