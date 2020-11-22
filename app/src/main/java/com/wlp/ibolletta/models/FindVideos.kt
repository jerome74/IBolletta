package com.wlp.ibolletta.models

import com.wlp.ibolletta.model.IModel
import org.json.JSONObject

data class FindVideos(var research : String

) : IModel {
    override fun toRequest(): String {
        return toString()
    }

    override fun toString(): String {

        val jsonObject: JSONObject = JSONObject();


        jsonObject.put("research", research)

        return jsonObject.toString()

    }
}