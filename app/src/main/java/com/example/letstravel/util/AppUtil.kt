package com.example.letstravel.util

import android.content.Context
import android.widget.Toast

class AppUtil {
    companion object {
        fun showToast(context: Context?, msg: String) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}