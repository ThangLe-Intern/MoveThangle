package com.madison.move.ui.offlinechannel

import com.madison.move.data.model.*
import com.madison.move.data.model.comment.CommentResponse
import com.madison.move.data.model.comment.DataComment
import com.madison.move.data.model.comment.SendComment
import com.madison.move.data.model.videodetail.DataVideoDetail
import com.madison.move.ui.base.BasePresenter
import com.madison.move.ui.base.BaseView

interface CommentContract {

    interface CommentContract : BaseView {
        fun onSuccessGetVideoDetail(objectResponse: ObjectResponse<DataVideoDetail>)
        fun onSuccessGetCommentVideo(objectResponse: ObjectResponse<List<DataComment>>)
        fun onError(errorMessage: String)
        fun onSuccessSendCommentVideo(objectResponse: ObjectResponse<CommentResponse>)
        fun onSuccessSendReplyComment(objectResponse: ObjectResponse<CommentResponse>)

        fun onSuccessCallLikeComment(objectResponse: LikeResponse)
        fun onSuccessCallDiskLikeComment(objectResponse: DiskLikeResponse)

        fun onSuccessPostView(objectResponse: ObjectResponse<PostViewResponse>)

    }

    interface CommentPresenter : BasePresenter<CommentContract> {
        fun getVideoDetail(id: Int)
        fun getCommentVideo(token: String, id: Int)
        fun sendCommentVideo(token: String, idVideo: Int, content: SendComment)
        fun sendReplyComment(token: String, idComment: Int, content: SendComment)
        fun postView(
            token: String, idVideo: Int,
            time: PostView
        )

        fun callLikeComment(token: String, idComment: Int)
        fun callDiskLikeComment(token: String, idComment: Int)
    }

}