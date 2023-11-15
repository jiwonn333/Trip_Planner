package com.example.letstravel.fragment.test.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.BuildConfig
import com.example.letstravel.R
import com.example.letstravel.api.RetrofitApiManager
import com.example.letstravel.api.RetrofitInterface
import com.example.letstravel.api.text_search_model.PlacesTextSearchResponse
import com.example.letstravel.databinding.FragmentSearchTestBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.fragment.test.TestFragment1ViewModel
import com.example.letstravel.fragment.test.TestItem
import com.example.letstravel.fragment.test.TestRecyclerViewAdapter
import com.example.letstravel.util.CLog
import retrofit2.Response

class SearchTestFragment : BaseFragment() {

    private lateinit var viewModel: TestFragment1ViewModel
    private var binding: FragmentSearchTestBinding? = null
    private var preText: String = ""

    // recyclerview
    private var recyclerView: RecyclerView? = null
    private var testRecyclerViewAdapter: TestRecyclerViewAdapter? = null
    private var searchRecyclerViewAdapter: SearchRecyclerViewAdapter? = null
    private val itemList: ArrayList<TestItem> =
        object : ArrayList<TestItem>() {
            init {
                add(TestItem(R.drawable.ic_test_home))
            }
        }

    private var searchList = ArrayList<SearchItem>()

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


        initTestRecyclerView()
        initSearchRecyclerView()

        // back 키 누르면 이전화면
        binding?.ivBack?.setOnClickListener {
            removeFragment(SearchTestFragment@ this)
        }

        binding?.etSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                preText = s.toString()
            }

            // 텍스트가 변경될 때마다 호출
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                CLog.e("변경됨")

                if (s.toString() == "") {
                    binding?.searchRecyclerview?.visibility = View.GONE
                    binding?.favoriteRecyclerview?.visibility = View.VISIBLE
                    binding?.clSaveStar?.visibility = View.VISIBLE
                } else {
                    binding?.searchRecyclerview?.visibility = View.VISIBLE
                    binding?.favoriteRecyclerview?.visibility = View.GONE
                    binding?.clSaveStar?.visibility = View.GONE

                    CLog.e("s : $s")
                    requestService(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun requestService(text: String) {
        RetrofitApiManager.getInstance().requestFindPlaceFromText(
            text,
            "ko",
            BuildConfig.MAPS_API_KEY,
            object : RetrofitInterface {
                override fun onResponse(response: Response<*>?) {
                    CLog.d("response : $response")

                    if (response != null) {
                        if (response.isSuccessful) {
                            CLog.d("response is Successful!!")
                            val placesTextSearchResponse =
                                response.body() as PlacesTextSearchResponse

                            var status = placesTextSearchResponse.status
                            when (status) {
                                "OK" -> {

                                    var result = placesTextSearchResponse.results
                                    var size = placesTextSearchResponse.results.size - 1

                                    for (i: Int in 0..size) {
                                        var ele = result[i].name
                                        searchList.add(SearchItem(ele))
                                    }
                                    CLog.e("searchList : $searchList")
                                    searchRecyclerViewAdapter?.addItem(searchList)
                                }
                                "ZERO_RESULTS" -> {
                                    CLog.e("대기")
                                }
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable?) {
                    CLog.e("Throwable : $t")
                }

            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initTestRecyclerView() {
        recyclerView = binding?.favoriteRecyclerview
        testRecyclerViewAdapter = TestRecyclerViewAdapter(requireContext(), itemList)
        val linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = testRecyclerViewAdapter
        testRecyclerViewAdapter!!.addItem(itemList)
    }

    private fun initSearchRecyclerView() {
        recyclerView = binding?.searchRecyclerview
        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = searchRecyclerViewAdapter
    }
}