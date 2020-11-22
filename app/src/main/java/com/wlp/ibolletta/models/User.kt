package com.wlp.ibolletta.model

import org.json.JSONObject

data class User constructor(var username: String, var password : String) : IModel
{
    override fun toRequest() : String {
        return toString()
    }

    override fun toString(): String {

        val jsonObject : JSONObject = JSONObject();

        jsonObject.put("username", username)
        jsonObject.put("password", password)

        return jsonObject.toString()

    }
}