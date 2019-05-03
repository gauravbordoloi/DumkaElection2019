package com.gadgetsfury.electionindia.networking

import com.gadgetsfury.electionindia.announcement.Announcement
import com.gadgetsfury.electionindia.candidate.Candidate
import com.gadgetsfury.electionindia.contacts.Contact
import com.gadgetsfury.electionindia.events.ElectionEvent
import com.gadgetsfury.electionindia.feeds.Feed
import com.gadgetsfury.electionindia.poll.PollingStation
import com.gadgetsfury.electionindia.settings.ServerResponse
import com.gadgetsfury.electionindia.sveep.rohof.ROHoF
import com.gadgetsfury.electionindia.sveep.vfeed.VoterFeed
import com.gadgetsfury.electionindia.sveep.vod.VOD
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("polling-stations/get")
    fun getPolls(@Query("constitution") constitution: String?, @Query("block") block: String?): Call<List<PollingStation>>

    @GET("announcements/get")
    fun getAnnouncements(): Call<List<Announcement>>

    @GET("news/get")
    fun getFeeds(): Call<List<Feed>>

    @GET("contacts/get")
    fun getContacts(): Call<List<Contact>>

    @GET("candidates/get")
    fun getCandidates(): Call<List<Candidate>>

    @GET("polling-stations/georadius/{lat}/{lng}/{radius}")
    fun getPollsNearbyMe(@Path("lat") lat: Double, @Path("lng") lng: Double, @Path("radius") radius: Int): Call<List<PollingStation>>

    @POST("feedbacks/post")
    fun postFeedback(@Body body: HashMap<String, String>): Call<ServerResponse>

    @GET("events/get")
    fun getEvents(): Call<List<ElectionEvent>>

    @GET("voters/vod")
    fun getVods(): Call<List<VOD>>

    @GET("voters/vod/{id}")
    fun getVodById(@Path("id") id: Int): Call<VOD>

    @POST("voters/vod") //name, image, content
    fun participateInVOD(@Body body: HashMap<String, String>): Call<ServerResponse>

    @GET("rohof")
    fun getRoHof(@Query("limit") limit: String?, @Query("offset") offset: String?): Call<List<ROHoF>>

    @GET("votersfeed")
    fun getVotersFeeds(@Query("limit") limit: String?, @Query("offset") offset: String?): Call<List<VoterFeed>>

    @GET("votersfeed/{id}")
    fun getVotersFeedById(@Path("id") id: Int): Call<VoterFeed>

    @POST("votersfeed") //name, image, content
    fun postVotersFeed(@Body body: HashMap<String, String>): Call<ServerResponse>

    @POST("events/interested")
    fun setInterested(@Body body: HashMap<String, Int>): Call<ServerResponse>

}