package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.PostAdapter
import com.ktunsdc.software_development_community.databinding.FragmentPostBinding
import com.ktunsdc.software_development_community.viewmodel.ChatViewModel
import com.ktunsdc.software_development_community.viewmodel.MainViewModel
import com.ktunsdc.software_development_community.viewmodel.PostViewModel


class PostFragment : Fragment() {
    private lateinit var binding:FragmentPostBinding
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var adapter:PostAdapter
    private lateinit var viewModel: PostViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_post,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).binding.bottomNavigationView.visibility=View.VISIBLE
        adapter= PostAdapter(arrayListOf(),"0")
        binding.recyclerPost.adapter=adapter
        binding.recyclerPost.layoutManager=LinearLayoutManager(context)

        topAppBar=binding.materialToolbar

        viewModel=ViewModelProvider(this)[PostViewModel::class.java]
        mainViewModel=ViewModelProvider(activity as MainActivity)[MainViewModel::class.java]
        viewModel.getPost()
        postObserve()
        currentMemberInfoObserve()
        swipeRefresh()
        topAppBarClickListener()
    }

    private fun postObserve(){
        viewModel.post.observe(viewLifecycleOwner){
            it.let {
                adapter.updateList(it)
            }
        }
    }
    private fun currentMemberInfoObserve(){
        mainViewModel.currentMember.observe(viewLifecycleOwner){
            it?.let {
                if(it.level=="0"){
                    topAppBar.menu.findItem(R.id.photo_share).isVisible=false
                }
            }
        }
    }

    private fun swipeRefresh(){
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getPost()
            binding.refreshLayout.isRefreshing = false
        }
    }
    private fun topAppBarClickListener(){
        topAppBar.setOnMenuItemClickListener {item->
            if(item.itemId==R.id.photo_share){
                val action=PostFragmentDirections.actionPostFragmentToShareFragment2(0)
                this.findNavController().navigate(action)
            }
            true
        }
    }
}