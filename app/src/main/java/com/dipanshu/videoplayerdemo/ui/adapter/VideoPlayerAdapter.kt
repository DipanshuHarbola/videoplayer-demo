package com.dipanshu.videoplayerdemo.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dipanshu.videoplayerdemo.R
import com.dipanshu.videoplayerdemo.data.Post
import com.dipanshu.videoplayerdemo.databinding.ItemPostBinding
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class VideoPlayerAdapter :
    RecyclerView.Adapter<VideoPlayerAdapter.VideoPlayerViewHolder>() {

    lateinit var exoPlayer: SimpleExoPlayer
    lateinit var bookmarkCallback: BookmarkCallback

    private var videoPostList = mutableListOf<Post>()

    private var lastIndex = RecyclerView.NO_POSITION

    private var lastHolder: VideoPlayerViewHolder? = null

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemPostBinding.inflate(layoutInflater, parent, false)
        return VideoPlayerViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
        if (lastHolder == holder) return
        lastHolder?.setExoPlayer(null)
        lastHolder = holder
        lastIndex = position

        val mediaSource = buildMediaSource(Uri.parse(videoPostList[position].videoUrl), holder.itemView.context)
        exoPlayer.prepare(mediaSource, false, false)
        exoPlayer.seekTo(0, 0)
        holder.setExoPlayer(exoPlayer)
        exoPlayer.playWhenReady = true
        holder.bind(videoPostList[position])
    }

    private fun buildMediaSource(uri: Uri, context: Context): MediaSource {
        val dataSourceFactory =
            DefaultDataSourceFactory(context, context.getString(R.string.app_name))
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    override fun getItemCount(): Int {
        return videoPostList.size
    }

    override fun onViewDetachedFromWindow(holder: VideoPlayerViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewAttachedToWindow(holder: VideoPlayerViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.bind(videoPostList[holder.adapterPosition])
        if (lastIndex == RecyclerView.NO_POSITION)
            notifyItemChanged(0)
    }

    fun setData(data: List<Post>) {
        videoPostList.clear()
        videoPostList.addAll(data)
    }

    inner class VideoPlayerViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            binding.run {
                post = item
                executePendingBindings()
            }

            /*
            *  It's not the correct or only way to do it, but it fulfill our purpose.
            * */
            var currentPost = item
            binding.ivBookmark.setOnClickListener {
                currentPost = if (currentPost.isBookmarked) {
                    binding.ivBookmark.setImageResource(R.drawable.ic_non_bookmarked)
                    currentPost.copy(isBookmarked = false)

                } else {
                    binding.ivBookmark.setImageResource(R.drawable.ic_bookmarked)
                    currentPost.copy(isBookmarked = true)
                }

                bookmarkCallback.onClickBookmarkIcon(currentPost.postId)

                //notifyItemChanged(adapterPosition)
            }
        }

        fun setExoPlayer(exoPlayer: SimpleExoPlayer?) {
            binding.playerView.player = exoPlayer
        }

        fun onViewDetachedFromWindow() {
            binding.playerView.player = null
        }
    }

    // callback interface
    interface BookmarkCallback {
        fun onClickBookmarkIcon(postId: Int)
    }

}