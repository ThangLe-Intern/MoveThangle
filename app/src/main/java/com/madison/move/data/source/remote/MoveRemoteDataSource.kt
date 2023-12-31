package com.madison.move.data.source.remote

import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.data.model.comment.SendComment
import com.madison.move.data.model.videosuggestion.DataVideoSuggestion
import com.madison.move.data.model.videosuggestion.VideoSuggestion
import com.madison.move.data.source.MoveDataSource
import com.madison.move.data.source.remote.model.MoveResponse
import com.madison.move.data.source.remote.services.MoveApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoveRemoteDataSource private constructor(private val moveApi: MoveApi) : MoveDataSource {
    override fun getVideos(callback: MoveDataSource.LoadVideosCallback?) {
        moveApi.getMovies()?.enqueue(object : Callback<MoveResponse?> {
            override fun onResponse(
                call: Call<MoveResponse?>, response: Response<MoveResponse?>
            ) {
                val movies: List<Video?>? =
                    if (response.body() != null) response.body()!!.videos else null
                if (movies != null && movies.isNotEmpty()) {
                    callback?.onVideosLoaded(movies)
                } else {
                    callback?.onDataNotAvailable()
                }
            }

            override fun onFailure(call: Call<MoveResponse?>, t: Throwable) {
                callback?.onError()
            }
        })
    }


    override fun getCarousel(): Call<ObjectResponse<List<DataVideoSuggestion>>> {
        return moveApi.getCarousel()
    }


    override fun getCategory(): Call<ObjectResponse<List<DataCategory>>> {
        return moveApi.getCategory()
    }

    override fun getVideoSuggestion(): Call<ObjectResponse<VideoSuggestion>> {
        return moveApi.getVideoSuggestion()
    }

    override fun getVideoSuggestionForUser(token: String): Call<ObjectResponse<VideoSuggestion>> {
        return moveApi.getVideoSuggestionForUser(token)
    }

    override fun getCommentVideo(token: String, id: Int): Call<ObjectResponse<List<DataComment>>>? {
        return moveApi.getCommentVideo(token, id)
    }

    override fun sendComment(
        token: String, videoId: Int, content: SendComment
    ): Call<ObjectResponse<CommentResponse>>? {
        return moveApi.sendComment(token, videoId, content)

    }

    override fun sendReply(
        token: String, commentId: Int, content: SendComment
    ): Call<ObjectResponse<CommentResponse>>? {
        return moveApi.sendReply(token, commentId, content)
    }

    override fun callLikeComment(
        token: String,
        commentId: Int
    ): Call<LikeResponse>? {
        return moveApi.callLikeComment(token, commentId)
    }

    override fun callDiskLikeComment(
        token: String,
        commentId: Int
    ): Call<DiskLikeResponse>? {
        return moveApi.callDiskLikeComment(token, commentId)
    }

    override fun getFaq(): Call<ObjectResponse<List<DataFAQ>>>? {
        return moveApi.getFaq()
    }

    override fun getGuidelines(): Call<ObjectResponse<List<DataGuidelines>>>? {
        return moveApi.getGuidelines()
    }

    override fun postView(
        token: String, videoId: Int,
        time: PostView
    ): Call<ObjectResponse<PostViewResponse>>? {
        return moveApi.postViewVideo(token, videoId, time)
    }

    override fun getTokenLogin(email: String, password: String): Call<ObjectResponse<DataUser>> {
        return moveApi.loginApi(email, password)
    }

    override fun logOutUser(token: String): Call<ObjectResponse<DataUser>> {
        return moveApi.logoutRequest(token)
    }

    override fun getUserProfile(token: String): Call<ObjectResponse<DataUser>> {
        return moveApi.getUserProfile(token)
    }

    override fun getCountryData(): Call<ObjectResponse<List<DataCountry>>>? {
        return moveApi.getCountry()
    }

    override fun getStateData(countryID: Int): Call<ObjectResponse<List<DataState>>> {
        return moveApi.getStates(countryID)
    }


    override fun updateProfileUser(
        token: String, profileRequest: ProfileRequest
    ): Call<ObjectResponse<DataUser>>? {
        return moveApi.updateProfile(token, profileRequest)
    }

    override fun getVideoDetail(id: Int): Call<ObjectResponse<DataVideoDetail>>? {
        return moveApi.showVideoDetail(id)
    }

    override fun saveVideos(videos: List<Video?>?) {

    }

    companion object {
        private var instance: MoveRemoteDataSource? = null
        fun getInstance(movieApi: MoveApi): MoveRemoteDataSource {
            if (instance == null) {
                instance = MoveRemoteDataSource(movieApi)
            }
            return instance!!
        }
    }
}