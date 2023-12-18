package com.mk.mydiary.utils.appext

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.shortToast(m:String){
    Toast.makeText(this, m, Toast.LENGTH_SHORT).show()
}
fun Context.longToast(m:String){
    Toast.makeText(this, m, Toast.LENGTH_LONG).show()
}
fun Context.newScreen(c: Class<*>) {
    startActivity(Intent(this, c))
}