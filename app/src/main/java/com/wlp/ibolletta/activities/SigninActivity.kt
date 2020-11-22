package com.wlp.ibolletta.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.wlp.ibolletta.R
import com.wlp.ibolletta.domain.AuthObj
import com.wlp.ibolletta.models.UserSingIn
import com.wlp.ibolletta.services.SigninService
import com.wlp.ibolletta.util.ToastCustom
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.SigninProgBar

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        manageSpinner(true, View.INVISIBLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun onSigninBtnClicked(view : View)
    {
        val user : UserSingIn = UserSingIn(nicnameSigninTxt.text.toString()
            , nameSigninTxt.text.toString()
            ,passSigninTxt.text.toString())
        manageSpinner(false, View.VISIBLE)
        hideKeyboard()
        callSigninUser(user)
    }

    fun callSigninUser(user : UserSingIn)
    {
        runOnUiThread {
            SigninService.signinUser(this
                , user,
                { esito: Boolean, messaggio: String ->
                    if (esito) {
                        try {
                            AuthObj.isLoggIn = true
                            ToastCustom.show(this@SigninActivity,getString(R.string.email_check))
                            manageSpinner(true, View.INVISIBLE)
                            finish()

                        } catch (e: Exception) {
                            ToastCustom.show(this@SigninActivity,getString(R.string.signin_error ,e.message))
                            manageSpinner(true, View.INVISIBLE)
                        }

                    } else {
                        ToastCustom.show(this@SigninActivity,getString(R.string.signin_failed ,messaggio))
                        manageSpinner(true, View.INVISIBLE)
                    }
                })
        }
    }

    fun manageSpinner(enable: Boolean, visibility : Int)
    {
        SigninProgBar.visibility = visibility;

        nameSigninTxt.isEnabled    = enable
        passSigninTxt.isEnabled   = enable
        nicnameSigninTxt.isEnabled   = enable
        SigninBtn.isEnabled = enable
    }


    fun hideKeyboard(){
        val inputManager : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText)
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)
    }
}