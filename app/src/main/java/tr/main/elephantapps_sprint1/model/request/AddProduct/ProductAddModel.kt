package tr.main.elephantapps_sprint1.model.request.AddProduct

import android.os.Parcel
import android.os.Parcelable

data class ProductAddModel(
    var brandId: Int? = null,
    var categoryId: Int? = null,
    var code: String? = null,
    var description: String? = null,
    var height: Int? = null,
    var isOpenToOffer: Boolean? = false,
    var length: Int? = null,
    var price: Int? = null,
    var productStatus: String? = null,
    var requestId: Int? = null,
    var stock: Int? = null,
    var title: String? = null,
    var weight: Int? = null,
    var width: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(brandId)
        parcel.writeValue(categoryId)
        parcel.writeString(code)
        parcel.writeString(description)
        parcel.writeValue(height)
        parcel.writeValue(isOpenToOffer)
        parcel.writeValue(length)
        parcel.writeValue(price)
        parcel.writeString(productStatus)
        parcel.writeValue(requestId)
        parcel.writeValue(stock)
        parcel.writeString(title)
        parcel.writeValue(weight)
        parcel.writeValue(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductAddModel> {
        override fun createFromParcel(parcel: Parcel): ProductAddModel {
            return ProductAddModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductAddModel?> {
            return arrayOfNulls(size)
        }
    }
}
