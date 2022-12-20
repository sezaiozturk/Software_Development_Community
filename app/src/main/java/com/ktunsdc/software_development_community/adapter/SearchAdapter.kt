package com.ktunsdc.software_development_community.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ktunsdc.software_development_community.R
import com.ktunsdc.software_development_community.databinding.RecyclerRowMemberBinding
import com.ktunsdc.software_development_community.model.Member
import com.ktunsdc.software_development_community.util.Singleton
import com.ktunsdc.software_development_community.view.SearchFragmentDirections
import kotlinx.android.synthetic.main.recycler_row_member.view.*
import java.util.ArrayList

class SearchAdapter(val memberList:ArrayList<Member>,var currentLevel:String):RecyclerView.Adapter<SearchAdapter.MemberViewHolder>(),ClickListener,RootOptionsClickListener{
    class MemberViewHolder(val binding:RecyclerRowMemberBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=DataBindingUtil.inflate<RecyclerRowMemberBinding>(inflater,R.layout.recycler_row_member,parent,false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.binding.member= memberList[position]
        holder.binding.listener=this
        holder.binding.optionsListener=this
       /* holder.itemView.setOnClickListener {
            val action=SearchFragmentDirections.actionSearchFragment2ToProfileFragment2(1,memberList.get(position).userName)
            Navigation.findNavController(it).navigate(action)
        }*/

    }

    override fun getItemCount(): Int {
        return memberList.size
    }
    fun updateMember(list:ArrayList<Member>){
        memberList.clear()
        memberList.addAll(list)
        notifyDataSetChanged()
    }
    fun updateLevel(level: String){
        currentLevel=level
    }

    override fun onMemberClicked(v: View) {
        val key=v.goneText.text.toString()
        val uid=key.subSequence(0,28).toString()
        val action=SearchFragmentDirections.actionSearchFragmentToProfileFragment2(uid,1)
        Navigation.findNavController(v).navigate(action)
    }


    override fun onRootClickListener(v: View): Boolean {
        val key=v.goneText.text.toString()
        val uid=key.subSequence(0,28).toString()
        val level=key.subSequence(29,30).toString()
        val popupMenu=PopupMenu(v.context,v)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true)
        }
        popupMenu.setOnMenuItemClickListener { item->
            when(item.itemId){
                R.id.status-> {
                    var status=v.imgStatus.tag as Boolean
                    status=!status
                    Singleton.db.collection("Members").document(uid).update("status",status).addOnSuccessListener {
                        if(status){
                            v.imgStatus.setImageResource(R.mipmap.check)
                            v.imgStatus.tag = true
                        }
                        else{
                            v.imgStatus.setImageResource(R.mipmap.clock)
                            v.imgStatus.tag=false
                        }
                    }
                    true
                }
                R.id.levelTwo->{
                    changeLevel(uid,"2")
                    v.txtLevel.text="Başkan"
                    true}
                R.id.levelOne->{
                    changeLevel(uid,"1")
                    v.txtLevel.text="Yönetici"
                    true}
                R.id.levelZero->{
                    changeLevel(uid,"0")
                    v.txtLevel.text="Üye"
                    true}
                R.id.delete->{
                    //There is a user delete codes!!!
                    true
                }
                R.id.cancel->{
                    true
                }
                else-> false
            }
        }
        popupMenu.inflate(R.menu.root_options_menu)
        if(currentLevel.toInt()>0&&currentLevel.toInt()>level.toInt()){
            if (currentLevel.toInt()<2){
                popupMenu.menu.findItem(R.id.levelTwo).isVisible=false
            }
            popupMenu.show()
        }
        return super.onRootClickListener(v)
    }
    fun changeLevel(uid:String,level:String){
        Singleton.db.collection("Members").document(uid).update("level",level)
    }
}