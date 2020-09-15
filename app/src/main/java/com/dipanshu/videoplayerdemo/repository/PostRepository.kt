package com.dipanshu.videoplayerdemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dipanshu.videoplayerdemo.data.Post
import com.dipanshu.videoplayerdemo.utils.Constants.Companion.BOOKMARKED_ID
import com.dipanshu.videoplayerdemo.utils.Prefs

class PostRepository {

    private fun postsList(): MutableList<Post> {
        val posts = mutableListOf<Post>()
        posts.add(
            Post(
                1,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/AwhvNXEqkvJy4NRr04fBgS2cKMz8GHcO.mp4",
                false
            )
        )
        posts.add(
            Post(
                2,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/ka1BZcmRg6QTIC5ZHOUXtlxzORCsBhrw.mp4",
                false
            )
        )
        posts.add(
            Post(
                3,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/awx7j6XFpockk6Z7J0z5qS3r7bWbzaks.mp4",
                false
            )
        )
        posts.add(
            Post(
                4,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/DBTtF8N4qzgRrR1dg2wTTZtJUzVyuYaN.mp4 ",
                false
            )
        )
        posts.add(
            Post(
                5,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/jRf8cFJuPzlj44ODSKdvfP8QpSfPUN6G.mp4",
                false
            )
        )
        posts.add(
            Post(
                6,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/GJHLQLc4GATFp8wTS0iQ1WojNbIBG2gR.mp4",
                false
            )
        )
        posts.add(
            Post(
                7,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/LVfhTmgINNnoKQX5jSfjFAG0KChuuQGv.mp4",
                false
            )
        )
        posts.add(
            Post(
                8,
                "https://cdn.trell.co/videos/transformed/h_640,w_640/videos/orig/cc3Pzf05wMQzijHzwoOFdYyE8o54mU5z.mp4",
                false
            )
        )
        return posts
    }

    fun fetchPostsList(): LiveData<List<Post>> {
        val resultPostListLiveData = MutableLiveData<List<Post>>()
        val resultPostList = mutableListOf<Post>()
        val bookmarkedIds = Prefs.getPrefsString(BOOKMARKED_ID)
        val bookmarkedIdsList = bookmarkedIds?.toMutableList()

        if (bookmarkedIdsList.isNullOrEmpty()) {
            resultPostListLiveData.value = postsList().toList()
        } else {
            for (post in postsList()) {
                val updatedPostValue = post.copy(isBookmarked = bookmarkedIdsList.any { post.postId.toString() == it.toString() })
                resultPostList.add(updatedPostValue)
            }
            resultPostListLiveData.value = resultPostList.toList()
        }

        return resultPostListLiveData
    }

    fun addUpdateBookmark(postId: Int): LiveData<List<Post>> {
        val bookmarkedIds = Prefs.getPrefsString(BOOKMARKED_ID)
        if (bookmarkedIds.isNullOrEmpty()) {
            Prefs.setPrefs(BOOKMARKED_ID, postId.toString())
        }
        else if (bookmarkedIds.contains(postId.toString())) {
            val postIdIndex = bookmarkedIds.indexOf(postId.toString())
            Prefs.setPrefs(BOOKMARKED_ID, bookmarkedIds.substring(0, postIdIndex) + bookmarkedIds.substring(postIdIndex + 1))
        }
        else {
            Prefs.setPrefs(BOOKMARKED_ID, "$bookmarkedIds$postId")
        }
        return fetchPostsList()
    }

    companion object {
        @Volatile
        private var INSTANCE: PostRepository? = null

        fun getInstance(): PostRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: PostRepository()
                        .also { INSTANCE = it }
            }
    }

}