package com.example.my_heaven.view.main.detail.make

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.my_heaven.util.preference.PreferenceManager

class MakeViewModel : BaseObservable(){

    // 메인화면 컨트롤 플래그
    private var recentNoticeSeq = 0
    private var isNoticeNotRead = false
    private var isNoticePaused = false

    fun setNoticePaused(noticePaused: Boolean) {
        isNoticePaused = noticePaused
    }



}