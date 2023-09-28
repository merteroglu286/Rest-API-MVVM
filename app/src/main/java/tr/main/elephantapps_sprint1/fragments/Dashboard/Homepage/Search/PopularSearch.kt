package tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapter
import tr.main.elephantapps_sprint1.adapter.PopularSearchAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentPopularSearchBinding
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.response.Home.PopularSearch
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeViewModel


class PopularSearch : Fragment() {

    private lateinit var binding : FragmentPopularSearchBinding
    private var categoryResultAdapter: CategoryResultAdapter? = null
    private val categoryInfoList = mutableListOf<Pair<String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPopularSearchBinding.inflate(layoutInflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPopularSearch()

    }

    private fun getPopularSearch(){
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.getDataFromAPI()

        viewModel.popularSearch.observe(viewLifecycleOwner , Observer { data->
            val categoryAdapter = PopularSearchAdapter(data as ArrayList<PopularSearch>)
            binding.rvPopularSearch.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            binding.rvPopularSearch.adapter = categoryAdapter
        })
    }



}