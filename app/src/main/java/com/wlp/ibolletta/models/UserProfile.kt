package com.wlp.ibolletta.model

import org.json.JSONObject

data class UserProfile constructor(   var id : String
                                      , var nickname : String
                                      , var email : String
                                      , var avatarname : String
                                      , var avatarcolor : String) : IModel


{
    override fun toRequest() : String {
        return toString()
    }

    override fun toString(): String {

        val jsonObject : JSONObject = JSONObject();


        jsonObject.put("nickname", nickname)
        jsonObject.put("email", email)
        jsonObject.put("avatarname", avatarname)
        jsonObject.put("avatarcolor", avatarcolor)

        return jsonObject.toString()

    }
}