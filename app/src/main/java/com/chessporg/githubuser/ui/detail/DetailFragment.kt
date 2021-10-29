package com.chessporg.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding
    private lateinit var userData: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        setDetailScreen()
    }

    private fun setDetailScreen() {
        binding.apply {
            ivBackButton.setOnClickListener {

            }

            tvUserName.text = userData.username
            tvRealName.text = userData.name

            Glide.with(requireActivity())
                .load(userData.avatar)
                .apply(RequestOptions().override(200, 200))
                .into(civProfilPicture)

            tvFollowerFollowing.text = getString(R.string.follower_1_s_following_2_s, userData.followers, userData.following)
            tvCompany.text = userData.company
            tvAddress.text = userData.location
            tvRepository.text = userData.repository

            btnShare.setOnClickListener {

            }
        }
    }
}