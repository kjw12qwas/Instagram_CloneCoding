package com.example.jaewonstagram.widget

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(private val context: Context) {
        private val USER_UID = "user_uid"

        fun getUserUid(): String? {
            return getDefaultSharedPreferences().getString(USER_UID, null)
        }

        fun setUserUid(uid: String) {
            getDefaultSharedPreferences().edit().putString(USER_UID, uid).apply()
        }

        private fun getDefaultSharedPreferences(): SharedPreferences {
            return context.getSharedPreferences(getDefaultSharedPreferencesName(), getDefaultSharedPreferencesMode())
        }
        private fun getDefaultSharedPreferencesName(): String {
            return context.packageName.toString() + "_preferences"
        }
        private fun getDefaultSharedPreferencesMode(): Int {
            return Context.MODE_PRIVATE
        }

}