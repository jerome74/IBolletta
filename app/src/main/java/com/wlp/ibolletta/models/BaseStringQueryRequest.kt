package com.wlp.ibolletta.model

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class BaseStringQueryRequest constructor(
    uri: String
    , val model: IModel
    , val contentType: String
    , responseOK: Response.Listener<String>
    , responseError: Response.ErrorListener
    , val mapHeaders: MutableMap<String, String>?
    , val mapParams: MutableMap<String, String>?
) :
    StringRequest(
        Method.GET
        , uri
        , responseOK
        , responseError
    ) {

    override fun getParams(): MutableMap<String, String> {
        if(mapParams.isNullOrEmpty())
            return super.getParams()
        else
            return mapParams!!
    }

    override fun getBody(): ByteArray {
        return super.getBody()
    }

    override fun getBodyContentType(): String {
        return contentType
    }

    override fun getHeaders(): MutableMap<String, String> {
        if (mapHeaders.isNullOrEmpty())
            return super.getHeaders()
        else
            return mapHeaders!!
    }
}