package com.chessporg.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    companion object {
        const val DATA = "USER_DATA"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var userData: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<User>(DATA)?.also { userData = it }

        setDetailScreen()
    }

    private fun setDetailScreen() {
        binding.apply {
            ivBackButton.setOnClickListener {
                finish()
            }

            tvUserName.text = userData.username
            tvUsername.text = userData.username
            tvRealName.text = userData.name

            Glide.with(this@DetailActivity)
                .load(userData.avatar)
                .apply(RequestOptions().override(200,200))
                .into(civProfilPicture)

            tvFollowerFollowing.text = getString(R.string.follower_1_s_following_2_s, userData.followers, userData.following)
            tvCompany.text = userData.company
            tvAddress.text = userData.location
            tvRepository.text = userData.repository

            btnShare.setOnClickListener {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Github Username: ${userData.username}")
                startActivity(Intent.createChooser(shareIntent, "Share Github Username to"))
            }
        }
    }
}