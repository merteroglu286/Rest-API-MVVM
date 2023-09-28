package tr.main.elephantapps_sprint1.model.request.GetProduct

import android.os.Parcel
import android.os.Parcelable

data class Comment(
    val commentDate: String,
    val commentType: String,
    val id: Int,
    val score: Int,
    val text: String,
    val userName: String
) : Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(commentDate)
        dest.writeString(commentType)
        dest.writeInt(id)
        dest.writeInt(score)
        dest.writeString(text)
        dest.writeString(userName)
    }

    // Parcel'dan veriyi okuma işlemi
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comment> {
        override fun createFromParcel(parcel: Parcel): Comment {
            return Comment(parcel)
        }

        override fun newArray(size: Int): Array<Comment?> {
            return arrayOfNulls(size)
        }
    }
}
