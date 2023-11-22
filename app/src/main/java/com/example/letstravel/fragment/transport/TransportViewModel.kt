package com.example.letstravel.fragment.transport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// sharedviewmodel 가정
class TransportViewModel : ViewModel() {
    private var _address = MutableLiveData<String>()
    private var _checkNum: MutableLiveData<Int> = MutableLiveData()
    fun getAddress(): MutableLiveData<String> = _address
    fun updateAddress(address: String) {
        _address.value = address
    }

    fun getCheckNum(): MutableLiveData<Int> = _checkNum
    fun checkNum(num: Int) {
        _checkNum.value = num
    }
}