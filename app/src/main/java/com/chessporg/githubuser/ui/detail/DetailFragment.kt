package com.chessporg.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.FragmentDetailBinding
import kotlinx.coroutines.flow.collect

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        binding.apply {
            ivBackButton.setOnClickListener {
                viewModel.onBackClick()
            }

            val userData = viewModel.user!!

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
                viewModel.onShareClick(userData)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.detailUserEvent.collect { event ->
                when(event) {
                    DetailViewModel.DetailUserEvent.NavigateBackToHome -> {
                        findNavController().popBackStack()
                    }
                    is DetailViewModel.DetailUserEvent.ShareGithubUserData -> {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Github Username: ${event.userData.username}")
                        startActivity(Intent.createChooser(shareIntent, "Share Github Username to"))
                    }
                }
            }
        }
    }
}