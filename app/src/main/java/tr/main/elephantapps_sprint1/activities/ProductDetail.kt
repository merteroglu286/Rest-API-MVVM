package tr.main.elephantapps_sprint1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.adapter.GarageProductsByProductDetailAdapter
import tr.main.elephantapps_sprint1.adapter.ProductPhotoViewPagerAdapter
import tr.main.elephantapps_sprint1.adapter.SimilarProductAdapter
import tr.main.elephantapps_sprint1.databinding.ActivityProductDetailBinding
import tr.main.elephantapps_sprint1.model.request.GetProduct.GarageProduct
import tr.main.elephantapps_sprint1.model.request.GetProduct.SimularProduct
import tr.main.elephantapps_sprint1.util.Utils
import tr.main.elephantapps_sprint1.viewmodel.ProductDetailViewModel

class ProductDetail : BaseActivity() {

    private lateinit var binding : ActivityProductDetailBinding
    private lateinit var productPhoto : String
    private lateinit var productName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("productId",0)
        val garageId = intent.getIntExtra("garageId",0)
        getProductData(id)

        binding.ivBack.setOnClickListener{
            onBackPressed()
            overridePendingTransition(R.anim.activity_left_to_right, R.anim.activity_right_to_left)
        }

        binding.rlGarage.setOnClickListener {
            val intent = Intent(this@ProductDetail,GarageProfile::class.java)
            intent.putExtra("productId",id)
            intent.putExtra("garageId",garageId)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
        }
    }


    private fun getProductData(id : Int){
        val viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)

        viewModel.getDataFromAPI(id)

        viewModel.successLiveData.observe(this, Observer { success ->
            if (success == false){
                viewModel.errorLiveData.observe(this, Observer { error ->
                    Toast.makeText(this@ProductDetail, "Hata: $error", Toast.LENGTH_LONG).show()

                })
            }
        })

        viewModel.data.observe(this, Observer { data ->
            binding.tvProductStatus.text = data.productStatus
            binding.tvProductDescription.text = data.description
            binding.txtTitle.text = data.title
            binding.txtPrice.text = data.price.toString()
            productName = data.title
        })

        viewModel.garageLiveData.observe(this, Observer { garage ->
            binding.tvGarageName.text = garage.userName
            binding.followerCount.text = garage.followerCount.toString()
            binding.productAdvertCount.text = garage.productCount.toString()
            binding.productSoldCount.text = garage.soldProducts.toString()
            binding.txtScore.text = garage.score.toString()
        })

        viewModel.productPhotoUrls.observe(this, Observer {photoList ->
            val viewpager = binding.vpProductDetail
            val adapter = ProductPhotoViewPagerAdapter(photoList,this@ProductDetail)
            viewpager.adapter = adapter
            binding.indicator.setViewPager(viewpager)
            binding.indicator.dataSetObserver?.let { adapter.registerDataSetObserver(it) }

            productPhoto = photoList[0]
        })

        viewModel.similarProducts.observe(this, Observer {similarProductList ->
            val productAdapter = SimilarProductAdapter(similarProductList as ArrayList<SimularProduct>,this@ProductDetail)
            binding.rvSimilarProducts.layoutManager = GridLayoutManager(this@ProductDetail,3)
            binding.rvSimilarProducts.adapter = productAdapter
        })

        viewModel.garageProducts.observe(this, Observer {garageProducts ->
            val productAdapter = GarageProductsByProductDetailAdapter(garageProducts as ArrayList<GarageProduct>,this@ProductDetail)
            binding.rvOtherProductsOfGarage.layoutManager = GridLayoutManager(this@ProductDetail,3)
            binding.rvOtherProductsOfGarage.adapter = productAdapter
        })

        viewModel.brandLiveData.observe(this, Observer { brand ->
            binding.txtBrandName.text = brand.name

        })

        viewModel.commentLiveData.observe(this, Observer {commentList->

            if(commentList.isNullOrEmpty()){
                binding.llComments.visibility = View.GONE
            }else{
                binding.tvCommentUserName.text = commentList[0].userName
                binding.tvComment.text = commentList[0].text
                binding.tvDate.text = commentList[0].commentDate
                binding.commentSize.text = commentList.size.toString()

                binding.btnSeeComments.setOnClickListener{
                    val intent = Intent(this@ProductDetail,CommentsScreen::class.java)
                    intent.putParcelableArrayListExtra("commentList", ArrayList(commentList))
                    intent.putExtra("productPhoto",productPhoto)
                    intent.putExtra("productName",productName)
                    intent.putExtra("productId",id)
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit)
                }
            }

        })

    }
}