package tr.main.elephantapps_sprint1.model.request

import android.os.Parcel
import android.os.Parcelable
import tr.main.elephantapps_sprint1.enums.OrderByProduct

data class SearchModel(
    val approvalStatus: String? = null,
    var brandId: Int? = null,
    val campaignId: Int? = null,
    var categoryId: Int? = null,
    val garageId: Int? = null,
    val hasCampaign: Boolean? = null,
    var maxPrice: Int? = null,
    var minPrice: Int? = null,
    val orderBy: OrderByProduct? = OrderByProduct.NewToOld,
    val pagingParameters: PagingParameters? = null,
    var productStatus: String? = null,
    val saleStatus: String? = null,
    val searchText: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readSerializable() as? OrderByProduct,
        parcel.readParcelable(PagingParameters::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(approvalStatus)
        parcel.writeValue(brandId)
        parcel.writeValue(campaignId)
        parcel.writeValue(categoryId)
        parcel.writeValue(garageId)
        parcel.writeValue(hasCampaign)
        parcel.writeValue(maxPrice)
        parcel.writeValue(minPrice)
        parcel.writeSerializable(orderBy)
        parcel.writeParcelable(pagingParameters, flags)
        parcel.writeString(productStatus)
        parcel.writeString(saleStatus)
        parcel.writeString(searchText)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchModel> {
        override fun createFromParcel(parcel: Parcel): SearchModel {
            return SearchModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchModel?> {
            return arrayOfNulls(size)
        }
    }
}