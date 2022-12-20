package com.ktunsdc.software_development_community.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.RecyclerRowCommentBinding
import com.ktunsdc.software_development_community.databinding.RecyclerRowEmptyBinding
import com.ktunsdc.software_development_community.databinding.RecyclerRowInfoBinding
import com.ktunsdc.software_development_community.databinding.RecyclerRowPostBinding
import com.ktunsdc.software_development_community.model.Comment
import com.ktunsdc.software_development_community.model.Post
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import com.ktunsdc.software_development_community.view.PostFragmentDirections
import kotlinx.android.synthetic.main.recycler_row_post.view.*


class PostAdapter(val list:ArrayList<Any>, val page:String):RecyclerView.Adapter<RecyclerView.ViewHolder>(),ClickListener{
    class PostHolderView(val binding: RecyclerRowPostBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(post:Post){
            binding.post=post
            binding.time=FirebaseFunctions().differentTime(post.firebaseDate.seconds)
        }
    }
    class CommentHolderView(val binding:RecyclerRowCommentBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(comment:Comment){
            binding.comment=comment
            binding.time=FirebaseFunctions().differentTime(comment.firebaseDate.seconds)
        }
    }
    class InfoHolderView(val binding:RecyclerRowInfoBinding):RecyclerView.ViewHolder(binding.root)
    class EmptyHolderView(val binding:RecyclerRowEmptyBinding):RecyclerView.ViewHolder(binding.root)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        if(viewType==0){
            val binding=DataBindingUtil.inflate<RecyclerRowPostBinding>(layoutInflater,R.layout.recycler_row_post,parent,false)
            return PostHolderView(binding)
        }
        else if(viewType==1){
            val binding=DataBindingUtil.inflate<RecyclerRowCommentBinding>(layoutInflater,R.layout.recycler_row_comment,parent,false)
            return CommentHolderView(binding)
        }
        else if(viewType==2){
            val binding=DataBindingUtil.inflate<RecyclerRowInfoBinding>(layoutInflater,R.layout.recycler_row_info,parent,false)
            return InfoHolderView(binding)
        }
        else{
            val binding=DataBindingUtil.inflate<RecyclerRowEmptyBinding>(layoutInflater,R.layout.recycler_row_empty,parent,false)
            return EmptyHolderView(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PostHolderView->{
                holder.bind(list[position] as Post)
                holder.binding.listener=this
            }
            is CommentHolderView->{
                holder.bind(list[position] as Comment)
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if(list[position] is Post){
            return 0
        }
        else if(list[position] is Comment){
            return 1
        }
        else if(list[position] == "commentLine")
        {
            return 2
        }
        else return 3
    }

    fun updateList(list: ArrayList<Any>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun onMemberClicked(v: View) {
        if(page=="0"){
            val action=PostFragmentDirections.actionPostFragmentToCommentFragment(v.postPhoto.tag as String)
            Navigation.findNavController(v).navigate(action)
        }

    }

}