package com.dipanshu.videoplayerdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dipanshu.videoplayerdemo.databinding.ActivityMainBinding
import com.dipanshu.videoplayerdemo.ui.adapter.VideoPlayerAdapter
import com.dipanshu.videoplayerdemo.utils.Prefs
import com.dipanshu.videoplayerdemo.viewmodel.MainViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer

class MainActivity : AppCompatActivity(), VideoPlayerAdapter.BookmarkCallback  {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel = MainViewModel()
    private var videoPlayerAdapter = VideoPlayerAdapter()
    private lateinit var simpleExoplayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Prefs.init(applicationContext)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        simpleExoplayer.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        simpleExoplayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoplayer.run {
            stop()
            release()
        }
    }

    override fun onClickBookmarkIcon(postId: Int) {
        viewModel.onClickBookmarkIcon(postId).observe(this, Observer { postList ->
            postList?.let {
                videoPlayerAdapter.setData(it) }
        })
    }

    private fun initViews() {
        subscribeVideoPostData()
        initializePlayer()

        videoPlayerAdapter.apply {
            exoPlayer = simpleExoplayer
            bookmarkCallback = this@MainActivity
        }

        binding.adapter = videoPlayerAdapter
        PagerSnapHelper().attachToRecyclerView(binding.rvVideoPost)

        binding.rvVideoPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0) {
                    val firstVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = RecyclerView.NO_POSITION
                    if (lastVisibleItemPosition != firstVisibleItemPosition) {
                        videoPlayerAdapter.notifyItemChanged(firstVisibleItemPosition)
                    }
                }
            }
        })
        binding.rvVideoPost.scrollToPosition(0)
    }

    private fun initializePlayer() {
        val defaultRenderersFactory =
            DefaultRenderersFactory(this).setExtensionRendererMode(
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            )
        simpleExoplayer = SimpleExoPlayer.Builder(this, defaultRenderersFactory).build()
    }


    private fun subscribeVideoPostData() {
        viewModel.postsListLiveData.observe(this, Observer { postList ->
            postList?.let {
                videoPlayerAdapter.setData(it) }
        })
    }
}
