package com.madison.move.data.source

import androidx.lifecycle.MutableLiveData
import com.madison.move.data.model.Video
import com.madison.move.data.model.carousel.CarouselResponse
import com.madison.move.data.model.category.CategoryResponse
import retrofit2.Call


interface MoveDataSource {
    interface LoadVideosCallback {
        fun onVideosLoaded(videos: List<Video?>?)
        fun onDataNotAvailable()
        fun onError()
    }

    fun getVideos(callback: LoadVideosCallback?)

    fun saveVideos(videos: List<Video?>?)

    fun testFun(): Int

    fun getCarousel(): Call<CarouselResponse>?
    fun setCarousel(): MutableLiveData<CarouselResponse>

    fun getCategory(): Call<CategoryResponse>?

}