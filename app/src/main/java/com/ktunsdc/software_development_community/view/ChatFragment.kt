package com.ktunsdc.software_development_community.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.ChatAdapter
import com.ktunsdc.software_development_community.databinding.FragmentChatBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment() {
    private lateinit var binding:FragmentChatBinding
    private lateinit var viewModel:ChatViewModel
    private val loadAlertDialog=CustomAlertDialog()
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var adapter:ChatAdapter
    private lateinit var mActivity:MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity=activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_chat,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!mActivity.currentMember!!.status){
            val action=ChatFragmentDirections.actionChatFragmentToLockFragment()
            Navigation.findNavController(view).navigate(action)

        }
        else{
            mActivity.binding.bottomNavigationView.visibility=View.GONE


            viewModel=ViewModelProvider(this)[ChatViewModel::class.java]
            viewModel.getMessage()

            topAppBar=binding.materialToolbar
            binding.chatFragment=this
            topAppBar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            loadAlertDialog.create(this,R.layout.alert_dialog_loading)

            sendLoadObserve()
            loadObserve()
            getMessageObserve()

            binding.chatRecycler.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
        }
    }

    fun sendMessage(message:String){
        if(message.isNotEmpty()){
            viewModel.sendMessage(message,mActivity.currentMember!!)
        }
    }
    private fun sendLoadObserve() {
        viewModel.sendLoad.observe(viewLifecycleOwner){
            if(it){
                binding.txtMessage.isEnabled=false
            }
            else{
                binding.txtMessage.isEnabled=true
                binding.txtMessage.text.clear()
            }
        }
    }
    private fun loadObserve() {
        viewModel.load.observe(viewLifecycleOwner){
            if(it){
                loadAlertDialog.open()
            }
            else loadAlertDialog.close()
        }
    }
    private fun getMessageObserve() {
        viewModel.messagelist.observe(viewLifecycleOwner){
            it?.let {
                adapter=ChatAdapter(it,mActivity.currentMember!!.uid)
                binding.chatRecycler.adapter=adapter
                //binding.chatRecycler.scrollToPosition(it.size)
            }
        }
    }
}