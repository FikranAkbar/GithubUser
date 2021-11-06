package com.chessporg.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.R
import com.chessporg.githubuser.databinding.FragmentDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect

class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val EXTRA_USERNAME = "extra_username"
    }

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        val mBundle = Bundle()
        val username = viewModel.user!!
        mBundle.putString(EXTRA_USERNAME, username)
        viewModel.getUserDetail(username)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity() as AppCompatActivity, mBundle)
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.apply {
            ivBackButton.setOnClickListener {
                viewModel.onBackClick()
            }
            ivShare.setOnClickListener {
                viewModel.onShareClick(viewModel.user!!)
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
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Github Username: ${event.username}")
                        startActivity(Intent.createChooser(shareIntent, "Share Github Username to"))
                    }
                    is DetailViewModel.DetailUserEvent.Error -> {
                        showLoading(false)
                    }
                    is DetailViewModel.DetailUserEvent.Loading -> {
                        showLoading(true)
                    }
                    is DetailViewModel.DetailUserEvent.Success -> {
                        binding.apply {
                            event.data.apply {
                                tvName.text = name
                                tvUsername.text = username
                                tvUserId.text = "$id"
                                tvFollowerFollowing.text = getString(R.string.follower_1_s_following_2_s, followers.toString(), following.toString())
                                tvRepository.text = getString(R.string.repository_1s, public_repos.toString())
                                tvCompanyLocation.text = getString(R.string.company_location, company, location)

                                Glide.with(this@DetailFragment)
                                    .load(avatar_url)
                                    .apply(RequestOptions().override(200,200))
                                    .into(civProfilPicture)
                            }

                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(bool: Boolean) {
        when (bool) {
            true -> {
                binding.shimmerLayout.visibility = View.VISIBLE
            }
            else -> {
                binding.shimmerLayout.visibility = View.GONE
            }
        }
    }
}