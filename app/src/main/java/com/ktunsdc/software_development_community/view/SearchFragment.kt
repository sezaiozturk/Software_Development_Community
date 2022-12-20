package com.ktunsdc.software_development_community.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.adapter.SearchAdapter
import com.ktunsdc.software_development_community.databinding.FragmentSearchBinding
import com.ktunsdc.software_development_community.util.CustomAlertDialog
import com.ktunsdc.software_development_community.util.FormControl
import com.ktunsdc.software_development_community.viewmodel.SearchViewModel


class SearchFragment : Fragment() {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private var adapter=SearchAdapter(arrayListOf(),"0")
    private var loadingAlertDialog=CustomAlertDialog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_search,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this)[SearchViewModel::class.java]

        binding.recyclerMember.layoutManager=LinearLayoutManager(context)
        binding.recyclerMember.adapter=adapter
        viewModel.getMembers()
        viewModel.getCurrentLevel()
        memberListObserve()
        loadObserve()
        levelObserve()
        swipeRefresh()
        binding.searchFragment=this
        loadingAlertDialog.create(this,R.layout.alert_dialog_loading)
        

    }

     fun memberListObserve(){
         viewModel.memberList.observe(viewLifecycleOwner){list->
             list?.let {
                 adapter.updateMember(it)
             }

         }
     }
    fun loadObserve(){
        viewModel.load.observe(viewLifecycleOwner){
            if(it) loadingAlertDialog.open()
            else loadingAlertDialog.close()
        }
    }
    fun levelObserve(){
        viewModel.currentlevel.observe(viewLifecycleOwner){
            adapter.updateLevel(it)
        }
    }
    fun swipeRefresh(){
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getMembers()
            binding.refreshLayout.isRefreshing = false
        }
    }
    fun filter(filter:String){
        viewModel.getFilterMembers(filter)
    }
}