package com.wlp.ibolletta.models

import android.os.Parcel
import android.os.Parcelable
import com.wlp.ibolletta.model.IModel
import org.json.JSONObject

data class Bolletta constructor(   var id : String = ""
                                      , var cc : String = ""
                                      , var importo : String = ""
                                      , var scadenza : String = ""
                                      , var numero : String = ""
                                      , var owner : String = ""
                                      , var td : String = "") : IModel, Parcelable


{

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toRequest() : String {
        return toString()
    }

    override fun toString(): String {

        val jsonObject : JSONObject = JSONObject();

        jsonObject.put("id", id)
        jsonObject.put("cc", cc)
        jsonObject.put("importo", importo)
        jsonObject.put("scadenza", scadenza)
        jsonObject.put("numero", numero)
        jsonObject.put("owner", owner)
        jsonObject.put("td", td)

        return jsonObject.toString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(cc)
        parcel.writeString(importo)
        parcel.writeString(scadenza)
        parcel.writeString(numero)
        parcel.writeString(owner)
        parcel.writeString(td)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bolletta> {
        override fun createFromParcel(parcel: Parcel): Bolletta {
            return Bolletta(parcel)
        }

        override fun newArray(size: Int): Array<Bolletta?> {
            return arrayOfNulls(size)
        }
    }
}