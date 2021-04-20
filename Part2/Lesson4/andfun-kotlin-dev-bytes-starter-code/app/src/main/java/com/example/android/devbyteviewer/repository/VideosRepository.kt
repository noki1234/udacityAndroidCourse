/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.devbyteviewer.repository

import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException


/**
 * pdevbyte videos from the network and storing them on disk.
 */
class VideosRepository(private val database: VideosDatabase) {

    /** A playlist of videos that can be shown on the screen - obtaining from database LiveData<List<DatabaseVideos>>
     *  from DB and convert them into LiveData<List<Video>>
     */
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    } //from the DB

    /** Function for refresh/update offline cache - videos
     *  function gets via NetworkVideoContainer ( List<NetworkVideo>) networkvideos
     *  which converts to DatabaseVideos and put them all into Db
     * */
    suspend fun refreshVideos() {      //call method from coroutine
        //database is input/output operation on disk -> use Dispatcher.IO specific for input/output
        withContext(Dispatchers.IO) {
                try {
                    val playlist = Network.devbytes.getPlaylist().await() //getPlaylist() : Deferred<NetworkVideoContainer>
                    Timber.d(playlist.asDatabaseModel().toString())
                    database.videoDao.insertAll(*playlist.asDatabaseModel())
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                }
        }
    }
}

