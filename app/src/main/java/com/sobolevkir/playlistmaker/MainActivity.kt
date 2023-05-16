package com.sobolevkir.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_library).setOnClickListener {
            Toast.makeText(this@MainActivity, "Вы нажали кнопку МЕДИАТЕКА", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.button_search).setOnClickListener {
            Toast.makeText(this@MainActivity, "Вы нажали кнопку ПОИСК", Toast.LENGTH_SHORT).show()
        }

        //Нажатие на третью кнопку реализую через анонимный класс
        val settingsButton = findViewById<Button>(R.id.button_settings)
        val settingsButtonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Вы нажали кнопку НАСТРОЙКИ", Toast.LENGTH_SHORT).show()
            }
        }
        settingsButton.setOnClickListener(settingsButtonClickListener)
        /* findViewById<Button>(R.id.button_settings).setOnClickListener {
            Toast.makeText(this@MainActivity, "Вы нажали кнопку НАСТРОЙКИ", Toast.LENGTH_SHORT).show()
        }*/

    }
}