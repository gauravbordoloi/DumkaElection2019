package com.gadgetsfury.electionindia.sveep.video

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gadgetsfury.electionindia.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_video_player.textViewTitle

class VideoPlayerActivity : AppCompatActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var video: Video

    private var youTubePlayer: YouTubePlayer? = null
    private var isFullScreen = false

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?, p2: Boolean) {
        this.youTubePlayer = youTubePlayer
        youTubePlayer!!.loadVideo(video.videoId)
        youTubePlayer.setOnFullscreenListener { b1 -> isFullScreen = b1 }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
        Log.e("TAG", p1.toString())
        Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        video = intent.getParcelableExtra("video")!!

        textViewTitle.text = video.title

        val frag = supportFragmentManager.findFragmentById(R.id.youtubeFragment) as YouTubePlayerSupportFragment
        frag.initialize("AIzaSyB2EZjsHMLKxDQS3161daOfIK16s82NY4I", this)

    }

    override fun onBackPressed() {
        if (isFullScreen && youTubePlayer != null) {
            youTubePlayer!!.setFullscreen(false)
            isFullScreen = false
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (youTubePlayer != null)
            youTubePlayer!!.release()
        super.onDestroy()
    }

}
