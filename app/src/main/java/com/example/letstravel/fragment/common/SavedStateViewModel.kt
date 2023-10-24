package com.example.letstravel.fragment.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() {
    // test | 정수형 변수인 count 상태를 저장 후 불러오기
    var count = 0
        set(value) {
            state.set("count", value)
            field = value
        }

    init {
        state.get<Int>("count")?.run {
            count = this
        }
    }
}