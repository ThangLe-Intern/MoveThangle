package com.madison.move.ui.menu

import com.madison.move.data.model.ObjectResponse
import com.madison.move.data.model.DataUser
import com.madison.move.ui.base.BaseView
import com.madison.move.ui.base.BasePresenter

/**
 * Create by SonLe on 04/05/2023
 */
interface MainContract {
    interface View : BaseView {
        fun onSuccessGetToken(loginResponse: ObjectResponse<DataUser>)
        fun onSuccessLogout(logoutResponse: ObjectResponse<DataUser>)
        fun onError(error: String?)
    }

    interface Presenter : BasePresenter<View> {
        fun onGetTokenPresenter(email: String, password: String)
        fun logoutRequest(token: String)
    }
}