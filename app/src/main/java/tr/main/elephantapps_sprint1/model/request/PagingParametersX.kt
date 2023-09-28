package tr.main.elephantapps_sprint1.model.request

import android.os.Parcel
import android.os.Parcelable

data class PagingParametersX(
    val maxPageSize: Int? = 0,
    val pageNumber: Int? = 0,
    val pageSize: Int? = 0,
    val pageSizeProp: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(maxPageSize)
        parcel.writeValue(pageNumber)
        parcel.writeValue(pageSize)
        parcel.writeValue(pageSizeProp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PagingParametersX> {
        override fun createFromParcel(parcel: Parcel): PagingParametersX {
            return PagingParametersX(parcel)
        }

        override fun newArray(size: Int): Array<PagingParametersX?> {
            return arrayOfNulls(size)
        }
    }
}

