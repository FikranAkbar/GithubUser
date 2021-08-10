package com.chessporg.githubuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.databinding.ItemUserBinding

class UserAdapter(private val list: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.CustomViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

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
                    onItemClickCallback.onItemClicked(user)
                }
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
        holder.bind(holder, user)
    }

    override fun getItemCount(): Int = list.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}