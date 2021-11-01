package com.chessporg.githubuser.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.data.model.UserResponse
import com.chessporg.githubuser.databinding.ItemUserBinding

class UserAdapter(private val listener: OnItemClickCallback) : RecyclerView.Adapter<UserAdapter.CustomViewHolder>() {

    private val users = ArrayList<UserResponse>()

    inner class CustomViewHolder(private val itemUserBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemUserBinding.root) {
        fun bind(holder: CustomViewHolder, user: UserResponse) {
            itemUserBinding.apply {
                tvUsername.text = user.username
                tvUserId.text = "${user.id}"

                Glide.with(holder.itemView.context)
                    .load(user.avatar_url)
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
        val user = users[position]
        holder.bind(holder, user)
    }

    override fun getItemCount(): Int = users.size

    fun submitList(list: List<UserResponse>) {
        users.clear()
        users.addAll(list)
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserResponse)
    }
}