package com.wlp.ibolletta.models

import com.wlp.ibolletta.model.IModel
import org.json.JSONObject

data class DownloadVideo(var idv : String , var type : String = "mp3") : IModel {
    override fun toRequest(): String {
        return toString()
    }

    override fun toString(): String {

        val jsonObject: JSONObject = JSONObject();


        jsonObject.put("idv", idv)

        return jsonObject.toString()

    }
}