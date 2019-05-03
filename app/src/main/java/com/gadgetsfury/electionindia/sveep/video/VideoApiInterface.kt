package com.gadgetsfury.electionindia.sveep.video

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiInterface {

    @GET("playlists?part=snippet&channelId=${VideoApiClient.CHANNEL_ID}&key=${VideoApiClient.YOUTUBE_API}&maxResults=50&fields=items")
    fun getPlaylist(): Call<String>

    @GET("playlistItems?part=snippet&channelId=${VideoApiClient.CHANNEL_ID}&key=${VideoApiClient.YOUTUBE_API}&maxResults=50&fields=items")
    fun getPlaylistVideos(@Query("playlistId") playlistId: String): Call<String>

}