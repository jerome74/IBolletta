package com.wlp.ibolletta.adapters


import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wlp.ibolletta.R
import com.wlp.ibolletta.models.Bolletta
import com.wlp.ibolletta.util.BROADCAST_DELETE_BOLLETTA
import com.wlp.ibolletta.util.BROADCAST_UPDATE_BOLLETTA
import kotlinx.android.synthetic.main.activity_ocr.*
import kotlinx.android.synthetic.main.bolletta_item_list.*


class BollettaListAdapter(val context : Context, val bollette : List<Bolletta>) :  RecyclerView.Adapter<BollettaListAdapter.Holder>() {

    inner class Holder (itemView: View?) : RecyclerView.ViewHolder(itemView!!)
    {

        val layoutInflater = context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ) as LayoutInflater

        fun bindProduct(bolletta: Bolletta, context: Context)
        {
            val cc_item_value     = itemView?.findViewById<TextView>(R.id.cc_item_value)
            val numero_item_value       = itemView?.findViewById<TextView>(R.id.numero_item_value)
            val owner_item_value       = itemView?.findViewById<TextView>(R.id.owner_item_value)
            val import_item_value       = itemView?.findViewById<TextView>(R.id.import_item_value)
            val scad_item_value       = itemView?.findViewById<TextView>(R.id.scad_item_value)
            val td_item_value       = itemView?.findViewById<TextView>(R.id.td_item_value)
            val bolletta_item_img       = itemView?.findViewById<ImageView>(R.id.bolletta_item_img)



            cc_item_value?.text   = bolletta.cc
            numero_item_value?.text  = bolletta.numero
            owner_item_value.text   = bolletta.owner
            import_item_value?.text   = bolletta.importo
            scad_item_value?.text  = bolletta.scadenza
            td_item_value.text   = bolletta.td

            setEventClick(bolletta_item_img,bolletta,layoutInflater)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bolletta_item_list ,parent,false)


        val reservationReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {}}

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return bollette.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindProduct(bollette[position], context)
        val item = bollette[position]
    }

    fun setEventClick(img : ImageView, bolletta: Bolletta, layoutInflater : LayoutInflater) {

        img?.setOnClickListener({


            val builder = AlertDialog.Builder(context)
            val dialogView = layoutInflater.inflate(R.layout.bolletta_dialog, null)

            dialogView.findViewById<TextView>(R.id.cc_value_dialog).text = bolletta.cc
            dialogView.findViewById<TextView>(R.id.import_value_dialog).text = bolletta.importo
            dialogView.findViewById<TextView>(R.id.scad_value_dialog).text = bolletta.scadenza
            dialogView.findViewById<TextView>(R.id.number_value_dialog).text = bolletta.numero
            dialogView.findViewById<TextView>(R.id.owner_value_dialog).text = bolletta.owner
            dialogView.findViewById<TextView>(R.id.td_value_dialog).text = bolletta.td


            builder.setView(dialogView)

                .setPositiveButton(context.getString(R.string.cambia_dati), { dialog: DialogInterface?, which: Int ->

                    val updateBollettaIntent = Intent(BROADCAST_UPDATE_BOLLETTA)

                    val cc = dialogView.findViewById<TextView>(R.id.cc_value_dialog)
                    val importo = dialogView.findViewById<TextView>(R.id.import_value_dialog)
                    val scadenza = dialogView.findViewById<TextView>(R.id.scad_value_dialog)
                    val numero = dialogView.findViewById<TextView>(R.id.number_value_dialog)
                    val owner = dialogView.findViewById<TextView>(R.id.owner_value_dialog)
                    val td = dialogView.findViewById<TextView>(R.id.td_value_dialog)

                    val updateBolletta = Bolletta(bolletta.id,cc.text.toString()
                        ,importo.text.toString()
                        ,scadenza.text.toString()
                        ,numero.text.toString()
                        ,owner.text.toString()
                        ,td.text.toString())

                    updateBollettaIntent.putExtra("BOLLETTA", updateBolletta)

                     LocalBroadcastManager.getInstance(context).sendBroadcast(updateBollettaIntent)
                })
                .setNeutralButton(context.getString(R.string.elimina), { dialog: DialogInterface?, which: Int ->

                    val deleteIntent = Intent(BROADCAST_DELETE_BOLLETTA)

                    deleteIntent.putExtra("ID", bolletta.id)

                    LocalBroadcastManager.getInstance(context).sendBroadcast(deleteIntent)

                })
                .setNegativeButton(context.getString(R.string.cancel), { dialog: DialogInterface?, which: Int ->

                }).create().show()
        })

    }
}