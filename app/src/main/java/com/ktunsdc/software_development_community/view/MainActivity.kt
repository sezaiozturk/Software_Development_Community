package com.ktunsdc.software_development_community.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.ActivityMainBinding
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    var currentMember:Member?=null

     lateinit var binding:ActivityMainBinding
     private lateinit var viewModel: MainViewModel
     private val loadingAlertDialog=CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Software_Development_Community)

        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController=navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        viewModel=ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getCurrentMember()
        loadingAlertDialog.mainCreate(this,R.layout.alert_dialog_loading)
        loadObserve()
        currentMemberObserve()
    }
    private fun currentMemberObserve(){
        viewModel.currentMember.observe(this){
            it.let {
                currentMember=it
            }
        }
    }
    private fun loadObserve(){
        viewModel.load.observe(this){
            if(it) loadingAlertDialog.open()
            else loadingAlertDialog.close()
        }
    }
}