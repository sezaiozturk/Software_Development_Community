package com.ktunsdc.software_development_community.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.RecyclerRowMessageMeBinding
import com.ktunsdc.software_development_community.databinding.RecyclerRowMessageTheyBinding
import com.ktunsdc.software_development_community.model.Message
import com.ktunsdc.software_development_community.util.FirebaseFunctions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val list:ArrayList<Message>,val uid:String):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MessageMeViewHolder(val binding:RecyclerRowMessageMeBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.message=message
        }
    }

    class MessageTheyViewHolder(val binding: RecyclerRowMessageTheyBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(message: Message){
            binding.message=message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        if(viewType==0){
            val binding= DataBindingUtil.inflate<RecyclerRowMessageMeBinding>(layoutInflater,
                R.layout.recycler_row_message_me,parent,false)
            return MessageMeViewHolder(binding)
        }
        else{
            val binding= DataBindingUtil.inflate<RecyclerRowMessageTheyBinding>(layoutInflater,
                R.layout.recycler_row_message_they,parent,false)
            return MessageTheyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is MessageMeViewHolder ->{
                holder.bind(list[position])
                holder.binding.hour=FirebaseFunctions().toHour(list[position].firebaseDate)
            }
            is MessageTheyViewHolder ->{
                holder.bind(list[position])
                holder.binding.hour=FirebaseFunctions().toHour(list[position].firebaseDate)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].memberUid==uid) 0
        else 1
    }
}