package com.example.letstravel.fragment.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.R
import com.example.letstravel.databinding.FragmentSearchTestBinding
import com.example.letstravel.fragment.common.BaseFragment

class SearchTestFragment : BaseFragment() {

    private lateinit var viewModel: TestFragment1ViewModel
    private var binding: FragmentSearchTestBinding? = null

    // recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: TestRecyclerViewAdapter? = null


    private val itemList: ArrayList<TestItem> =
        object : ArrayList<TestItem>() {
            init {
                add(TestItem(R.drawable.ic_test_home))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchTestBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TestFragment1ViewModel::class.java)


        initRecyclerView()

        // back 키 누르면 이전화면
        binding?.ivBack?.setOnClickListener {
            removeFragment(SearchTestFragment@ this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecyclerView() {
        recyclerView = binding?.favoriteRecyclerview
        recyclerViewAdapter = TestRecyclerViewAdapter(requireContext(), itemList)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = recyclerViewAdapter
        recyclerViewAdapter!!.addItem(itemList)
    }
}