package com.ktunsdc.software_development_community.util

import android.annotation.SuppressLint
import android.util.Patterns
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.SearchAdapter
import com.ktunsdc.software_development_community.model.Member

class FormControl {
    fun emailFocusListener(editText:TextInputEditText,layout: TextInputLayout){
        editText.setOnFocusChangeListener { view, focus ->
            if(!focus){
                layout.helperText=validEmail(editText)
            }
        }
    }
    fun validEmail(editText:TextInputEditText):String?{
        val emailText=editText.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            return "geçersiz e-posta adresi"
        }
        return null
    }

    fun passwordFocusListener(editText:TextInputEditText,layout: TextInputLayout){
        editText.setOnFocusChangeListener { view, focus ->
            if(!focus){
                layout.helperText=validPassword(editText)
            }
        }
    }
    fun validPassword(editText:TextInputEditText):String?{
        val passwordText=editText.text.toString()
        if(passwordText.length<6){
            return "en az 6 haneli şifre gerekli"
        }
        return null
    }
    fun textFocusListener(editText:TextInputEditText, layout: TextInputLayout){
        editText.setOnFocusChangeListener { view, focus ->
            if(!focus){
                layout.helperText=validText(editText)
            }
        }
    }
    fun validText(editText:TextInputEditText):String?{
        val text=editText.text.toString()
        if(text.isEmpty()){
            return "gerekli"
        }
        return null
    }
    fun autoTextFocusListener(editText:AutoCompleteTextView, layout: TextInputLayout){
        editText.setOnFocusChangeListener { view, focus ->
            if(!focus){
                layout.helperText=validAutoText(editText)
            }
        }
    }
    fun validAutoText(editText:AutoCompleteTextView):String?{
        val text=editText.text.toString()
        if(text.isEmpty()){
            return "gerekli"
        }
        return null
    }
}