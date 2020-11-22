package com.wlp.ibolletta.util

import android.content.Context
import android.util.Log
import com.wlp.ibolletta.model.UserProfile

class SharedPrefs(val context : Context)
{
    fun isLoggIn() : Boolean{
        Log.d("#isLoggIn","get ${context.getSharedPreferences("prefs",0).getBoolean("IsLoggIn",false)}")
        return context.getSharedPreferences("prefs",0).getBoolean("IsLoggIn",false)
    }

    fun isLoggIn(value : Boolean) : Unit{
        context.getSharedPreferences("prefs",0).edit().putBoolean("IsLoggIn", value).apply()
        Log.d("#isLoggIn", "set ${value}")
    }

    fun token() : String{
        Log.d("#token", "get ${context.getSharedPreferences("prefs",0).getString("token","")!!}")
        return context.getSharedPreferences("prefs",0).getString("token","")!!
    }

    fun token(value : String) : Unit{
        context.getSharedPreferences("prefs",0).edit().putString("token", value).apply()
        Log.d("#token", "set ${value}")
    }

    fun email() : String{
        Log.d("#email", "get ${context.getSharedPreferences("prefs",0).getString("email","")!!}")
        return context.getSharedPreferences("prefs",0).getString("email","")!!
    }

    fun email(value : String) : Unit{
        context.getSharedPreferences("prefs",0).edit().putString("email", value).apply()
        Log.d("#email", "set ${value}")

    }

    fun notify() : String{
        Log.d("#notify", "get ${context.getSharedPreferences("prefs",0).getString("notify","")!!}")
        return context.getSharedPreferences("prefs",0).getString("notify","")!!
    }

    fun notify(value : String) : Unit{
        context.getSharedPreferences("prefs",0).edit().putString("notify", value).apply()
        Log.d("#notify", "set ${value}")
    }

    fun userProfile() : UserProfile{
        //Log.d("#userProfile", "get ${context.getSharedPreferences("prefs",0).getString("email","")!!}")
        return UserProfile(context.getSharedPreferences("prefs",0).getString("userProfile.id","")!!
            , context.getSharedPreferences("prefs",0).getString("userProfile.nickname","")!!
            , context.getSharedPreferences("prefs",0).getString("userProfile.email","")!!
            , context.getSharedPreferences("prefs",0).getString("userProfile.avatarname","")!!
            , context.getSharedPreferences("prefs",0).getString("userProfile.avatarcolor","")!!)
    }

    fun userProfile(value : UserProfile) : Unit{
        context.getSharedPreferences("prefs",0).edit().putString("userProfile.id", value.id).apply()
        context.getSharedPreferences("prefs",0).edit().putString("userProfile.nickname", value.nickname).apply()
        context.getSharedPreferences("prefs",0).edit().putString("userProfile.email", value.email).apply()
        context.getSharedPreferences("prefs",0).edit().putString("userProfile.avatarname", value.avatarname).apply()
        context.getSharedPreferences("prefs",0).edit().putString("userProfile.avatarcolor", value.avatarcolor).apply()
        Log.d("#userProfile", "set ${value}")

    }



}