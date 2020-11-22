package com.wlp.ibolletta.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.wlp.ibolletta.IBollettaActivity
import com.wlp.ibolletta.R
import com.wlp.ibolletta.domain.AuthObj
import com.wlp.ibolletta.model.*
import com.wlp.ibolletta.models.Bolletta
import com.wlp.ibolletta.models.IModelMock
import com.wlp.ibolletta.util.*

object BollettaService {

    /**********************************
     * saveBolletta
     */

    fun saveBolletta(context: Context, bolletta: Bolletta, complete: (Boolean, String) -> Unit) {

        var uri: String = URI_SAVE_BOLLETTA

        var mapHeader: MutableMap<String, String> = mutableMapOf();
        mapHeader.put("Authentication", "${AuthObj.token}")

        val baseStringPostRequest: BaseStringPostRequest = BaseStringPostRequest(
            uri,
            bolletta,
            "application/json; charset=utf-8",
            Response.Listener<String> { response -> complete(true, response) },
            Response.ErrorListener { error ->
                try {
                    complete(false, error.message!!)
                } catch (e: Exception) {
                    complete(false, context.getString(R.string.msg_save_bolletta))
                }
            },
            mapHeader
        )

        baseStringPostRequest.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        Volley.newRequestQueue(context).add(baseStringPostRequest)
    }

    /**********************************
     * updateBolletta
     */

    fun updateBolletta(context: Context, bolletta: Bolletta, complete: (Boolean, String) -> Unit) {

        var uri: String = "$URI_UPDATE_BOLLETTA/${bolletta.id}"

        var mapHeader: MutableMap<String, String> = mutableMapOf();
        mapHeader.put("Authentication", "${AuthObj.token}")

        val baseStringPostQueryRequest: BaseStringPostQueryRequest = BaseStringPostQueryRequest(
            uri,
            bolletta,
            "application/json; charset=utf-8",
            Response.Listener<String> { response -> complete(true, response) },
            Response.ErrorListener { error ->
                try {
                    complete(false, error.message!!)
                } catch (e: Exception) {
                    complete(false, context.getString(R.string.msg_update_bolletta))
                }
            },
            mapHeader, null
        )

        baseStringPostQueryRequest.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        Volley.newRequestQueue(context).add(baseStringPostQueryRequest)
    }

    /**********************************
     * findBolletta
     */

    fun deleteBolletta(context: Context, idBolletta : String, complete: (Boolean, String) -> Unit) {

        var uri: String = "$URI_DELELE_BOLLETTA/$idBolletta"

        var mapHeader: MutableMap<String, String> = mutableMapOf();
        mapHeader.put("Authentication", "${AuthObj.token}")

        val baseStringQueryRequest: BaseStringQueryRequest = BaseStringQueryRequest(
            uri,
            Bolletta(),"application/json; charset=utf-8",
            Response.Listener<String> { response -> complete(true, response) },
            Response.ErrorListener { error ->
                try {
                    complete(false, error.message!!)
                } catch (e: Exception) {
                    complete(false, context.getString(R.string.msg_delete_bolletta))
                }
            },mapHeader , null
        )

        baseStringQueryRequest.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        Volley.newRequestQueue(context).add(baseStringQueryRequest)

    }


    /**********************************
     * findBolletta
     */

    fun findBolletta(context: Context, idBolletta: String, complete: (Boolean, String) -> Unit) {

        var uri: String = "$URI_FIND_BOLLETTA/$idBolletta"

        var mapHeader: MutableMap<String, String> = mutableMapOf();
        mapHeader.put("Authentication", "${AuthObj.token}")

        val baseStringQueryRequest: BaseStringQueryRequest = BaseStringQueryRequest(
            uri,
            Bolletta(),"application/json; charset=utf-8",
            Response.Listener<String> { response -> complete(true, response) },
            Response.ErrorListener { error ->
                try {
                    complete(false, error.message!!)
                } catch (e: Exception) {
                    complete(false, context.getString(R.string.msg_find_bolletta))
                }
            },
            mapHeader , null
        )

        baseStringQueryRequest.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        Volley.newRequestQueue(context).add(baseStringQueryRequest)

    }

    /**********************************
     * findBollette
     */

    fun findBollette(context: Context, email: String, complete: (Boolean, String) -> Unit) {

        var uri: String = "$URI_FIND_BOLLETTE/$email"

        var mapHeader: MutableMap<String, String> = mutableMapOf();
        mapHeader.put("Authentication", "${AuthObj.token}")

        val baseStringQueryRequest: BaseStringQueryRequest = BaseStringQueryRequest(
            uri,
            IModelMock(),
            "application/json; charset=utf-8",
            Response.Listener<String> { response -> complete(true, response) },
            Response.ErrorListener { error ->
                try {
                    complete(false, error.message!!)
                } catch (e: Exception) {
                    (context as IBollettaActivity).manageSpinner(View.INVISIBLE, true)
                    ToastCustom.show(
                        context as Activity,
                        context.getString(R.string.session_exparied)
                    )
                    LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(Intent(BROADCAST_LOGOUT))
                }
            },
            mapHeader , null
        )

        baseStringQueryRequest.setRetryPolicy(
            DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )

        Volley.newRequestQueue(context).add(baseStringQueryRequest)

    }

}