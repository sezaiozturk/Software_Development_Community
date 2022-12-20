package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.PostAdapter
import com.ktunsdc.software_development_community.databinding.FragmentCommentBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.viewmodel.CommentViewModel
import java.text.SimpleDateFormat

class CommentFragment : Fragment() {

    private lateinit var topAppBar:MaterialToolbar
    private lateinit var binding:FragmentCommentBinding
    private lateinit var adapter: PostAdapter
    private lateinit var viewModel: CommentViewModel
    private var postId:String?=null
    private var loadAlertDialog=CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_comment,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).binding.bottomNavigationView.visibility=View.GONE

        topAppBar=binding.materialToolbar
        binding.fragment=this
        loadAlertDialog.create(this,R.layout.alert_dialog_loading)
        viewModel=ViewModelProvider(this)[CommentViewModel::class.java]

        adapter=PostAdapter(arrayListOf(),"1")
        binding.recyclerComment.layoutManager=LinearLayoutManager(context)
        binding.recyclerComment.adapter=adapter
        topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        arguments?.let {

            postId=CommentFragmentArgs.fromBundle(it).postId
            viewModel.getComment(postId!!)
        }
        commentObserve()
        loadObserve()
        sendLoadObserve()
    }

    private fun loadObserve() {
        viewModel.load.observe(viewLifecycleOwner){
            if(it){
                loadAlertDialog.open()
            }
            else loadAlertDialog.close()
        }
    }
    private fun sendLoadObserve() {
        viewModel.sendLoad.observe(viewLifecycleOwner){
            if(it){
                binding.txtComment.isEnabled=false
            }
            else{
                binding.txtComment.isEnabled=true
                binding.txtComment.text.clear()
            }
        }
    }

    private fun commentObserve(){
        viewModel.comment.observe(viewLifecycleOwner){
            it.let {
                adapter.updateList(it)
            }
        }
    }
    fun sendComment(comment:String){
        viewModel.sendComment(comment,postId!!,(activity as MainActivity).currentMember!!)
    }
}