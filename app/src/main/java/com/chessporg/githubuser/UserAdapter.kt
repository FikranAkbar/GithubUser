package com.chessporg.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chessporg.githubuser.databinding.ItemUserBinding

class UserAdapter(private val list: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(private val itemUserBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemUserBinding.root) {
        fun bind(user: User) {
            itemUserBinding.apply {
                tvName.text = user.name
                tvUsername.text = user.username
                tvAddress.text = user.location
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserAdapter.CustomViewHolder {
        val itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemUserBinding)
    }


    override fun onBindViewHolder(holder: UserAdapter.CustomViewHolder, position: Int) {
        val user = list[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = list.size
}