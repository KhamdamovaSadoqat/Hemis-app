package com.software.hemis.utils.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref constructor(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(NAME, MODE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var universityUrl: String?
        get() = preferences.getString("baseUrl", "https://student.hemis.uz/rest/v1/")
        set(value) = preferences.edit() {
            if (value != null) {
                it.putString("baseUrl", value)
            }
        }

    var token: String?
        get() = preferences.getString("token", "")
        set(value) = preferences.edit {
            if (value != null) {
                it.putString("token", value)
            }
        }

    var refreshToken: String?
        get() = preferences.getString("refreshToken", "")
        set(value) = preferences.edit {
            if (value != null) {
                it.putString("refreshToken", value)
            }
        }

    var isFirstOpen: Boolean?
        get() = preferences.getBoolean("isFirstOpen", false)
        set(value) = preferences.edit() {
            if (value != null) {
                it.putBoolean("isFirstOpen", value)
            }
        }

    var currentSemester: Int?
        get() = preferences.getInt("currentSemester", 0)
        set(value) = preferences.edit {
            if (value != null)
                it.putInt("currentSemester", value)
        }

    var currentSemesterName: String?
        get() = preferences.getString("currentSemesterName", "")
        set(value) = preferences.edit {
            if (value != null)
                it.putString("currentSemesterName", value)
        }

    var currentWeek: Int?
        get() = preferences.getInt("currentWeek", 0)
        set(value) = preferences.edit {
            if (value != null)
                it.putInt("currentWeek", value)
        }

    var weekMinId : Int?
        get() = preferences.getInt("weekMinId", 0)
        set(value) = preferences.edit {
            if (value != null)
                it.putInt("weekMinId", value)
        }

    var weekMaxId: Int?
        get() = preferences.getInt("weekMaxId", 0)
        set(value) = preferences.edit {
            if (value != null)
                it.putInt("weekMaxId", value)
        }


    companion object {
        private const val NAME = "AgroLeasingApp"
        private const val MODE = Context.MODE_PRIVATE
    }


}