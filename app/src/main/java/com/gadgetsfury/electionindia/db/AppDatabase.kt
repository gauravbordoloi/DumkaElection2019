package com.gadgetsfury.electionindia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gadgetsfury.electionindia.announcement.Announcement
import com.gadgetsfury.electionindia.candidate.Candidate
import com.gadgetsfury.electionindia.contacts.Contact
import com.gadgetsfury.electionindia.db.dao.*
import com.gadgetsfury.electionindia.events.ElectionEvent
import com.gadgetsfury.electionindia.feeds.Feed
import com.gadgetsfury.electionindia.poll.PollingStation
import com.gadgetsfury.electionindia.sveep.rohof.ROHoF
import com.gadgetsfury.electionindia.sveep.vfeed.VoterFeed
import com.gadgetsfury.electionindia.sveep.video.Playlist
import com.gadgetsfury.electionindia.sveep.video.Video
import com.gadgetsfury.electionindia.sveep.vod.VOD
import com.gadgetsfury.electionindia.util.StringListTypeConverter

@Database(entities = [ElectionEvent::class, PollingStation::class, Announcement::class, Candidate::class, Contact::class, Feed::class,
    ROHoF::class, VoterFeed::class, VOD::class, Playlist::class, Video::class], version = 1)
@TypeConverters(StringListTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun eventDao(): EventDao

    abstract fun pollStationDao(): PollStationDao

    abstract fun announcementDao(): AnnouncementDao

    abstract fun candidateDao(): CandidateDao

    abstract fun contactDao(): ContactDao

    abstract fun feedDao(): FeedDao

    abstract fun rohofDao(): ROHoFDao

    abstract fun voterFeedDao(): VoterFeedDao

    abstract fun vodDao(): VodDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun videoDao(): VideoDao

}