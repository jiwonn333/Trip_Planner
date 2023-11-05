package com.example.letstravel.fragment.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.letstravel.databinding.FragmentTestFragment1Binding
import com.example.letstravel.fragment.common.BaseFragment

class TestFragment1 : BaseFragment() {

    private lateinit var viewModel: TestFragment1ViewModel
    private var binding: FragmentTestFragment1Binding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestFragment1Binding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TestFragment1ViewModel::class.java)

        // textview 클릭시 검색할 수 잇는 창으로 이동
        binding?.tvSearch?.setOnClickListener {
            replaceFragment(this@TestFragment1, TestFragment1Directions.actionNavigationTestToSearchTest())
        }
    }
}