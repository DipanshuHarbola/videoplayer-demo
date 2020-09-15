package com.dipanshu.videoplayerdemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dipanshu.videoplayerdemo.data.Post
import com.dipanshu.videoplayerdemo.repository.PostRepository

class MainViewModel : ViewModel(){

    val postsListLiveData: LiveData<List<Post>>

    private val repository: PostRepository = PostRepository.getInstance()

    fun onClickBookmarkIcon(postId: Int): LiveData<List<Post>> {
        return repository.addUpdateBookmark(postId)
    }

    init {
        postsListLiveData = repository.fetchPostsList()
    }
}