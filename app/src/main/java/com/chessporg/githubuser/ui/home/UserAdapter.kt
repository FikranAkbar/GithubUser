package com.chessporg.githubuser.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.ItemUserBinding

class UserAdapter(private val list: ArrayList<User>, private val listener: OnItemClickCallback) : RecyclerView.Adapter<UserAdapter.CustomViewHolder>() {
    inner class CustomViewHolder(private val itemUserBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemUserBinding.root) {
        fun bind(holder: CustomViewHolder, user: User) {
            itemUserBinding.apply {
                tvName.text = user.name
                tvUsername.text = user.username
                tvAddress.text = user.location

                Glide.with(holder.itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(50, 50))
                    .into(ivUserImage)

                civItem.setOnClickListener {
                    listener.onItemClicked(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(itemUserBinding)
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val user = list[position]
        holder.bind(holder, user)
    }

    override fun getItemCount(): Int = list.size


    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}