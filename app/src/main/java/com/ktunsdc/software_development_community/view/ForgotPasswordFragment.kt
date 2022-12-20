package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentForgotPasswordBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.FormControl
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.ForgotPasswordViewModel


class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var viewModel:ForgotPasswordViewModel
    private val loadingAlertDialog=CustomAlertDialog()
    private val passwordAlertDialog=CustomAlertDialog()

    private val formControl=FormControl()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_forgot_password,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotPasswordFragment=this
        viewModel=ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        loadingAlertDialog.create(this,R.layout.alert_dialog_loading)
        passwordAlertDialog.create(this,R.layout.alert_dialog_password_reset)
        loadObserve()
        resetObserve(view)
        formControl.emailFocusListener(binding.txtMail,binding.layoutMail)

    }

    fun resetPassword(email:String){
        binding.layoutMail.helperText=formControl.validEmail(binding.txtMail)

        val validEmail=binding.layoutMail.helperText==null
        if(validEmail){
            viewModel.resetPassword(email)
        }
        else Toast.makeText(context,"Lütfen alanı kontrol edin!!!",Toast.LENGTH_LONG).show()
    }
    fun toSignin(){
        val action=ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }
    private fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it){
                loadingAlertDialog.open()
            }else{
                loadingAlertDialog.close()
            }
        }
    }
    private fun resetObserve(view: View){
        viewModel.message.observe(viewLifecycleOwner){
            if(it){
                passwordAlertDialog.open()
                passwordAlertDialog.button(R.id.btnReset).setOnClickListener {
                    passwordAlertDialog.close()
                    val action=ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToSigninFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }else
            {
                Toast.makeText(context,"Bu e-posta adresi sistemde kayıtlı değil!!!", Toast.LENGTH_LONG).show()
            }
        }
    }

}