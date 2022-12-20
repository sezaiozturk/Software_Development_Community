package com.ktunsdc.software_development_community.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TableRow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.FragmentProfileBinding
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.MainViewModel
import com.ktunsdc.software_development_community.viewmodel.ProfileViewModel
import java.io.Serializable


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private lateinit var viewModel:ProfileViewModel
    private lateinit var topAppBar:MaterialToolbar
    private val contactAlertDialog=CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_profile,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this)[ProfileViewModel::class.java]
        topAppBar=binding.materialToolbar
        (activity as MainActivity).binding.bottomNavigationView.visibility=View.VISIBLE
        contactAlertDialog.create(this,R.layout.alert_dialog_contact)
        currentMemberObserve()
        if(arguments!=null){
            arguments?.let {
                val userUid=ProfileFragmentArgs.fromBundle(it).userUid
                val control=ProfileFragmentArgs.fromBundle(it).control
                if(control==1){
                    topAppBar.menu.findItem(R.id.more).isVisible = false
                    viewModel.getUser(userUid)
                }else{
                    topAppBar.menu.findItem(R.id.more).isVisible = true
                    binding.member=(activity as MainActivity).currentMember
                }
            }
        }
        topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    val action=ProfileFragmentDirections.actionProfileFragmentToMemberInformationFragment2(1)
                    this.findNavController().navigate(action)
                    true
                }
                R.id.contact -> {
                    contactAlertDialog.open()
                    contactAlertDialog.button(R.id.btnMail).setOnClickListener {
                        val intent=Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","ktunsdc@gmail.com",null))
                        startActivity(Intent.createChooser(intent,"Mesaj gönder!"))
                    }
                    contactAlertDialog.button(R.id.btnInstagram).setOnClickListener {
                        val url = "https://www.instagram.com/ktun.yazilimgelistirme/"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    contactAlertDialog.button(R.id.btnTwitter).setOnClickListener {
                        val url = "https://twitter.com/KTUNyazilimGT"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    contactAlertDialog.button(R.id.btnDiscord).setOnClickListener {
                        val url = "https://discord.gg/NgKXpsyatr"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }
                    contactAlertDialog.imageButton(R.id.btnClose).setOnClickListener {
                        contactAlertDialog.close()
                    }
                    true
                }
                R.id.exit -> {
                    Singleton.auth.signOut()
                    val intent=Intent(context,LoginActivity::class.java)
                    startActivity(intent)
                    (activity as MainActivity).finish()
                    true
                }
                else -> false
            }
        }
    }
    fun currentMemberObserve(){
        viewModel.user.observe(viewLifecycleOwner){
            it?.let {
                binding.member=it
            }
        }
    }
}