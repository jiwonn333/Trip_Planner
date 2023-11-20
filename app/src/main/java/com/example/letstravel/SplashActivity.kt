package com.example.letstravel

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        moveMainActivity(1);
    }

    private fun moveMainActivity(sec: Int) {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivityKt::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }, (1000 * sec).toLong())
    }
}