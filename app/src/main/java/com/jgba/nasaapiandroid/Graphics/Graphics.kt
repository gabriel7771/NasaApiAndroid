package com.jgba.nasaapiandroid.Graphics

import androidx.appcompat.app.AppCompatActivity

class Graphics : AppCompatActivity() {

    fun onChangeTransition(){
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        finish()
    }
}