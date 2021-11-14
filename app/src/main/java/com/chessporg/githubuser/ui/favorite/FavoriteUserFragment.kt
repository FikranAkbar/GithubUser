package com.chessporg.githubuser.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chessporg.githubuser.R
import com.chessporg.githubuser.databinding.FragmentFavoriteUserBinding

class FavoriteUserFragment : Fragment(R.layout.fragment_favorite_user) {
    private lateinit var binding: FragmentFavoriteUserBinding
    private val viewModel: FavoriteUserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFavoriteUserBinding.bind(view)
    }
}