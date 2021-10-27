package com.chessporg.githubuser.ui.home

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chessporg.githubuser.ui.detail.DetailActivity
import com.chessporg.githubuser.R
import com.chessporg.githubuser.data.model.User
import com.chessporg.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object;

    private lateinit var binding: ActivityMainBinding

    //region DataUser
    private lateinit var dataName: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var userList: ArrayList<User>
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        setHomeScreen()
    }

    private fun setHomeScreen() {
        binding.rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            val userAdapter = UserAdapter(userList)
            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.DATA, user)
                    startActivity(intent)
                }
            })
            adapter = userAdapter
        }
    }

    private fun getData() {
        dataName = resources.getStringArray(R.array.name)
        dataUsername = resources.getStringArray(R.array.username)
        dataLocation = resources.getStringArray(R.array.location)
        dataRepository = resources.getStringArray(R.array.repository)
        dataCompany = resources.getStringArray(R.array.company)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)

        userList = arrayListOf()
        for(i in dataName.indices) {
            val user = User(
                name = dataName[i],
                username = dataUsername[i],
                location = dataLocation[i],
                repository = dataRepository[i],
                company = dataCompany[i],
                followers = dataFollowers[i],
                following = dataFollowing[i],
                avatar = dataAvatar.getResourceId(i, -1),
            )
            userList.add(user)
        }
    }
}