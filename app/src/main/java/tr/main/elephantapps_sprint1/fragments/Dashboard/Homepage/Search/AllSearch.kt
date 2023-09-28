package tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.Search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tr.main.elephantapps_sprint1.Constants.Constans
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.activities.AddProduct
import tr.main.elephantapps_sprint1.adapter.BrandResultAdapter
import tr.main.elephantapps_sprint1.adapter.BrandsAdapter
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapter
import tr.main.elephantapps_sprint1.adapter.GarageResultAdapter
import tr.main.elephantapps_sprint1.adapter.ProductResultAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentAllSearchBinding
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Brand.Data
import tr.main.elephantapps_sprint1.model.response.Category.SubCategory
import tr.main.elephantapps_sprint1.model.response.Search.Brand
import tr.main.elephantapps_sprint1.model.response.Search.Category
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeSearchViewModel
import tr.main.elephantapps_sprint1.viewmodel.SearchBrandsViewModel

class AllSearch : Fragment() {

    private lateinit var binding : FragmentAllSearchBinding

    private var categoryResultAdapter: CategoryResultAdapter? = null
    private var brandResultAdapter: BrandResultAdapter? = null

    private val categoryInfoList = mutableListOf<Pair<String, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentAllSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchview = requireActivity().findViewById<SearchView>(R.id.search_view_homepage)

        searchview.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                if (searchText.isNullOrEmpty()){

                }else{
                    getSearchResult(searchText)
                }
                return false
            }

        })

    }

    private fun getSearchResult(searchText: String?) {
        val viewModel = ViewModelProvider(this)[HomeSearchViewModel::class.java]

        val searchModel = SearchModel(
            pagingParameters = PagingParameters(pageNumber = 1, pageSize = 50),
            searchText = searchText
        )
        if (searchText.isNullOrEmpty()){

        }else{
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getDataFromAPI(searchModel)

                withContext(Dispatchers.Main) {
                    viewModel.errorLiveData.observe(this@AllSearch) { message ->
                        Utils.showToast(requireContext(), message, Toast.LENGTH_SHORT, ToastType.Red)
                    }

                    viewModel.categories.observe(this@AllSearch) { categories ->
                        if (categories.isNullOrEmpty()){
                            binding.rvCategoryResult.visibility = View.GONE
                        }else{
                            binding.rvCategoryResult.visibility = View.VISIBLE

                            val categoryAdapter = CategoryResultAdapter(categories as ArrayList<Category>)
                            binding.rvCategoryResult.layoutManager = GridLayoutManager(requireContext(), 1)
                            binding.rvCategoryResult.adapter = categoryAdapter
                        }
                    }

                    viewModel.brands.observe(this@AllSearch) { brands ->
                        if (brands.isNullOrEmpty()){
                            binding.rvBrandResult.visibility = View.GONE
                        }else{
                            binding.rvBrandResult.visibility = View.VISIBLE

                            val brandAdapter = BrandResultAdapter(brands as ArrayList<Brand>)
                            binding.rvBrandResult.layoutManager = GridLayoutManager(requireContext(), 1)
                            binding.rvBrandResult.adapter = brandAdapter
                        }
                    }

                    viewModel.garages.observe(this@AllSearch) { garages ->
                        if (garages.isNullOrEmpty()){
                            binding.rvGarageResult.visibility = View.GONE
                        }else{
                            binding.rvGarageResult.visibility = View.VISIBLE
                            val garageAdapter = GarageResultAdapter(garages as ArrayList<Garage>)
                            binding.rvGarageResult.layoutManager = GridLayoutManager(requireContext(), 1)
                            binding.rvGarageResult.adapter = garageAdapter
                        }

                    }

                    viewModel.products.observe(this@AllSearch) { products ->
                        if (products.isNullOrEmpty()){
                            binding.rvProductResult.visibility = View.GONE
                        }else{
                            binding.rvProductResult.visibility = View.VISIBLE
                            val productAdapter = ProductResultAdapter(products as ArrayList<Product>)
                            binding.rvProductResult.layoutManager = GridLayoutManager(requireContext(), 3)
                            binding.rvProductResult.adapter = productAdapter
                        }

                    }
                }
            }
        }

    }

    /*
    private fun getCategories(){

        val viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)

        viewModel.errorLiveData.observe(viewLifecycleOwner){message->
            Utils.showToast(requireContext(),message,Toast.LENGTH_SHORT,ToastType.Red)
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            for (x in categories) {
                categoryInfoList.add(Pair(x.name, x.id))
            }
        }

        viewModel.subCategories.observe(viewLifecycleOwner){
            for (x in it){
                categoryInfoList.add(Pair(x.name, x.id))
            }
        }
        viewModel.subSubCategories.observe(viewLifecycleOwner){
            for (x in it){
                categoryInfoList.add(Pair(x.name, x.id))
            }
        }
        viewModel.subSubSubCategories.observe(viewLifecycleOwner){
            for (x in it){
                categoryInfoList.add(Pair(x.name, x.id))
            }
        }

        categoryResultAdapter = CategoryResultAdapter(categoryInfoList as ArrayList<Pair<String, Int>>,requireActivity())
        binding.rvCategoryResult.layoutManager = GridLayoutManager(requireContext(),1)
        binding.rvCategoryResult.adapter = categoryResultAdapter
    }

    private fun getBrands(){
        val viewModel = ViewModelProvider(this)[SearchBrandsViewModel::class.java]

        viewModel.getDataFromAPI(requireActivity())

        viewModel.errorLiveData.observe(viewLifecycleOwner){message->
            Utils.showToast(requireContext(),message,Toast.LENGTH_SHORT,ToastType.Red)
        }

        viewModel.brands.observe(viewLifecycleOwner) { brands ->
            brandResultAdapter = BrandResultAdapter(brands as ArrayList<Data>)
            binding.rvBrandResult.layoutManager = GridLayoutManager(requireContext(),1)
            binding.rvBrandResult.adapter = brandResultAdapter
        }
    }

    private fun getGarages(){
        val viewModel = ViewModelProvider(this)[HomeSearchViewModel::class.java]

        val searchModel = SearchModel(
            pagingParameters = PagingParameters(pageSize = 50, pageNumber = 1),
            searchText = "a"
        )

        viewModel.garages.observe(viewLifecycleOwner){garages->
            val garageAdapter = GarageResultAdapter(garages as ArrayList<Garage>)
            binding.rvGarageResult.layoutManager = GridLayoutManager(requireContext(),1)
            binding.rvGarageResult.adapter = garageAdapter
            println("garages : " + garages)
        }
    }

     */
}