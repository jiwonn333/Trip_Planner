package com.example.letstravel.fragment.transport.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letstravel.BuildConfig
import com.example.letstravel.api.RetrofitApiManager
import com.example.letstravel.api.RetrofitInterface
import com.example.letstravel.api.text_search_model.PlacesTextSearchResponse
import com.example.letstravel.databinding.FragmentTransportDetailBinding
import com.example.letstravel.fragment.common.BaseFragment
import com.example.letstravel.fragment.test.search.SearchItem
import com.example.letstravel.fragment.test.search.SearchRecyclerViewAdapter
import com.example.letstravel.fragment.transport.TransportViewModel
import com.example.letstravel.util.CLog
import retrofit2.Response

class TransportDetailFragment : BaseFragment() {
    private var binding: FragmentTransportDetailBinding? = null
    private var searchList = ArrayList<SearchItem>()
    private var recyclerView: RecyclerView? = null
    private var searchRecyclerViewAdapter: SearchRecyclerViewAdapter? = null
    private var preText: String = ""
    private lateinit var transportViewModel: TransportViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransportDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initSearchRecyclerView()

        // back 키 누르면 이전화면
        binding?.ivBack?.setOnClickListener {
            removeFragment(this)
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
                } else {
                    binding?.searchRecyclerview?.visibility = View.VISIBLE

                    CLog.e("s : $s")
                    requestService(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun initViewModel() {
        transportViewModel =
            ViewModelProvider(requireActivity()).get(TransportViewModel::class.java)
        CLog.e("1) getAddress : " + transportViewModel.getAddress().value)

        transportViewModel.getAddress().observe(viewLifecycleOwner, Observer { address ->
            if (address == "") {
                if (transportViewModel.getCheckNum().value == 1) {
                    binding?.etSearch?.hint = "출발지 입력"
                } else {
                    binding?.etSearch?.hint = "도착지 입력"
                }
            } else {
                binding?.etSearch?.text = Editable.Factory.getInstance().newEditable(address)
            }
        })
    }


    private fun initSearchRecyclerView() {
        recyclerView = binding?.searchRecyclerview
        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(requireContext())
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.adapter = searchRecyclerViewAdapter
    }

    private fun requestService(text: String) {
        RetrofitApiManager.getInstance().requestFindPlaceFromText(
            text,
            "ko",
            BuildConfig.MAPS_API_KEY,
            object : RetrofitInterface {
                @SuppressLint("NotifyDataSetChanged")
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
                                    if (searchList.size > 0) {
                                        searchList.clear()
                                    }
                                    for (i: Int in 0..size) {
                                        var ele = result[i].name
                                        searchList.add(SearchItem(ele))
                                    }
                                    CLog.e("searchList : $searchList")
                                    searchRecyclerViewAdapter?.addItem(searchList)
                                    searchRecyclerViewAdapter?.notifyDataSetChanged()
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


}