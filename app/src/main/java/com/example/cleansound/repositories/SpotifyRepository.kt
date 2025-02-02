package com.example.cleansound.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.cleansound.local.data.AppDatabase
import com.example.cleansound.model.playlists.Playlists
import com.example.cleansound.model.search.ItemsItem
import com.example.cleansound.model.track.SingleTrack
import com.example.cleansound.model.tracks.PlaylistTracks
import com.example.cleansound.remotemediator.TracksRemoteMediator
import com.example.cleansound.spotify.SpotifyService
import kotlinx.coroutines.flow.Flow

class SpotifyRepository(private val spotifyService: SpotifyService, private val appDatabase: AppDatabase) {

    suspend fun getFeaturedPlaylists(): Result<Playlists?> {
        return try {
            val response = spotifyService.getFeaturedPlaylist()
            if (response.isSuccessful) {
                Result.success(response.body()?.playlists ?: throw Exception("No playlists found"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchTracks(query: String) : Result<List<ItemsItem?>>{
        return try{
            val response = spotifyService.searchTracks(query)
            if (response.isSuccessful) {
                Result.success(response.body()?.tracks?.items ?: throw Exception("No Tracks found"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTrack(trackId: String?) : Result<SingleTrack> {
        return try {
            val response = spotifyService.getTrack(trackId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: throw Exception("No Tracks found"))
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e : Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFeaturedTracks(playlistId: String?) : Result<PlaylistTracks?> {
        return try {
            val response = spotifyService.getPlaylistTracks(playlistId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
        } catch (e :Exception) {
            Result.failure(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTracksForPlaylist(): Flow<PagingData<com.example.cleansound.local.model.Track>> {
        println("It can be fetched")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = TracksRemoteMediator(spotifyService, appDatabase),
            pagingSourceFactory = { appDatabase.trackDao().getAllTracks() }
            ).flow
    }


}