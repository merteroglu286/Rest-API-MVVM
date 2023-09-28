package tr.main.elephantapps_sprint1.fragments.Dashboard.Homepage.Search

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.BrandResultAdapter
import tr.main.elephantapps_sprint1.adapter.CategoryResultAdapter
import tr.main.elephantapps_sprint1.adapter.GarageResultAdapter
import tr.main.elephantapps_sprint1.adapter.HomeSearch.HomepageViewPagerAdapter
import tr.main.elephantapps_sprint1.adapter.HomeSearch.SearchResuktViewPager
import tr.main.elephantapps_sprint1.adapter.PopularSearchAdapter
import tr.main.elephantapps_sprint1.adapter.ProductResultAdapter
import tr.main.elephantapps_sprint1.databinding.FragmentHomepageSearchBinding
import tr.main.elephantapps_sprint1.enums.ToastType
import tr.main.elephantapps_sprint1.model.request.CategoryFilterModel
import tr.main.elephantapps_sprint1.model.request.PagingParameters
import tr.main.elephantapps_sprint1.model.request.SearchModel
import tr.main.elephantapps_sprint1.model.response.Home.PopularSearch
import tr.main.elephantapps_sprint1.model.response.Search.Brand
import tr.main.elephantapps_sprint1.model.response.Search.Garage
import tr.main.elephantapps_sprint1.model.response.Search.Product
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.CategoriesViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeSearchViewModel
import tr.main.elephantapps_sprint1.viewmodel.HomeViewModel


class HomepageSearchFragment : Fragment() {
    private val viewModel by viewModels<HomeSearchViewModel>()
    private var categoryResultAdapter: CategoryResultAdapter? = null
    private val categoryInfoList = mutableListOf<Pair<String, Int>>()

    private lateinit var binding: FragmentHomepageSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomepageSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        //getPopularSearch()
        //getCategories()

        setViewPager()



    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun setViewPager() {
        val viewPager2 = binding.viewPagerHomepageSearch
        val adapter = SearchResuktViewPager(parentFragmentManager, lifecycle)
        viewPager2.adapter = adapter
    }




    /*
        private fun getSearchResult(newText:String?){

            val viewModel = ViewModelProvider(this)[HomeSearchViewModel::class.java]

            val searchModel = SearchModel(
                pagingParameters = PagingParameters(1,50),
                searchText = "a"
            )

            viewModel.getDataFromAPI(searchModel)

            viewModel.errorLiveData.observe(this,Observer{message->
                Utils.showToast(requireContext(),message,Toast.LENGTH_SHORT,ToastType.Red)
            })
    /*
            viewModel.categories.observe(this){categories->
                val categoryAdapter = CategoryResultAdapter(categories as ArrayList<Category>)
                binding.rvCategoryResult.layoutManager = GridLayoutManager(requireContext(),1)
                binding.rvCategoryResult.adapter = categoryAdapter
                //println("categories : " + categories)
            }

     */

            viewModel.brands.observe(viewLifecycleOwner){brands->

                val brandAdapter = BrandResultAdapter(brands as ArrayList<Brand>)
                binding.rvBrandResult.layoutManager = GridLayoutManager(requireContext(),1)
                binding.rvBrandResult.adapter = brandAdapter

                //println("brands : " + brands)

            }

            viewModel.garages.observe(viewLifecycleOwner){garages->
                val garageAdapter = GarageResultAdapter(garages as ArrayList<Garage>)
                binding.rvGarageResult.layoutManager = GridLayoutManager(requireContext(),1)
                binding.rvGarageResult.adapter = garageAdapter
                //println("garages : " + garages)
            }

            viewModel.products.observe(viewLifecycleOwner){products->
                val productAdapter = ProductResultAdapter(products as ArrayList<Product>)
                binding.rvProductResult.layoutManager = GridLayoutManager(requireContext(),3)
                binding.rvProductResult.adapter = productAdapter
                //println("products : " + products)

            }


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

    private fun getCategories(){

        val viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        val model = CategoryFilterModel(
            false
        )
        viewModel.getDataFromAPI(model)

        viewModel.categories.observe(viewLifecycleOwner){
            for (x in it){
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
        binding.rvPopularSearch.layoutManager = GridLayoutManager(requireContext(),1)
        binding.rvPopularSearch.adapter = categoryResultAdapter
    }

     */
}