package com.wlp.ibolletta.network

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.wlp.ibolletta.model.IModel

class VolleyMultipartRequest constructor( url : String
                                            , val model: IModel
                                            , val contentType: String
                                            , val listener : Response.Listener<NetworkResponse>
                                            , errorListener : Response.ErrorListener
                                            , val mapHeaders : MutableMap<String, String>?) :

    Request<NetworkResponse>(
    Method.POST, url, errorListener) {



    override fun parseNetworkResponse(response: NetworkResponse?): Response<NetworkResponse> {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    override fun deliverResponse(response: NetworkResponse?) {
        listener?.onResponse(response)
    }

    override fun getBody(): ByteArray {
        if (model == null)
            return super.getBody();
        else
            return model.toRequest().toByteArray();
    }

    override fun getBodyContentType(): String {
        return contentType
    }

    override fun getHeaders(): MutableMap<String, String> {
        if (mapHeaders == null)
            return super.getHeaders()
        else
            return mapHeaders!!
    }

}