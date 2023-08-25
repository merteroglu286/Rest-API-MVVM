package tr.main.elephantapps_sprint1.model.request.AddProduct
import android.os.Parcel
import android.os.Parcelable

data class AdditionalInfoModel(
    var categoryName: String? = null,
    var brandName: String? = null,
    var cargoSize: String? = null,
    var price: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryName)
        parcel.writeString(brandName)
        parcel.writeString(cargoSize)
        parcel.writeValue(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdditionalInfoModel> {
        override fun createFromParcel(parcel: Parcel): AdditionalInfoModel {
            return AdditionalInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<AdditionalInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}

