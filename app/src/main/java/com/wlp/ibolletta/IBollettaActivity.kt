package com.wlp.ibolletta

import android.app.AlertDialog
import android.content.*
import android.content.DialogInterface.OnClickListener
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.text.Html
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.wlp.ibolletta.activities.KeepAndFlipImgeActivity
import com.wlp.ibolletta.activities.LoginActivity
import com.wlp.ibolletta.activities.SigninActivity
import com.wlp.ibolletta.adapters.BollettaListAdapter
import com.wlp.ibolletta.domain.AuthObj
import com.wlp.ibolletta.model.UserObj
import com.wlp.ibolletta.models.Bolletta
import com.wlp.ibolletta.services.BollettaService
import com.wlp.ibolletta.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class IBollettaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var outputFileUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar
            , R.string.navigation_drawer_open
            , R.string.navigation_drawer_close)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener { true }


        LocalBroadcastManager.getInstance(this).registerReceiver(
            logoutReceiver, IntentFilter(
                BROADCAST_LOGOUT
            )
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            deleteBollettaReceiver, IntentFilter(
                BROADCAST_DELETE_BOLLETTA
            )
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateBollettaReceiver, IntentFilter(
                BROADCAST_UPDATE_BOLLETTA
            )
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            saveBollettaReceiver, IntentFilter(
                BROADCAST_SAVE_BOLLETTA
            )
        )


        LocalBroadcastManager.getInstance(this).registerReceiver(
            findBolletteReceiver, IntentFilter(
                BROADCAST_FIND_BOLLETTE
            )
        )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            findBolletteNotifyListReceiver, IntentFilter(
                BROADCAST_FIND_BOLLETTE_NOTIFY_LIST
            )
        )

        notifyEventuallyLogin()

        manageSpinner(View.INVISIBLE , true)
    }

    fun onLoginBntClicked(view : MenuItem) {

        if (!AuthObj.isLoggIn) {
            val intentLogin: Intent = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        } else {


            var nav_view = findViewById<NavigationView>(R.id.nav_view)
            nav_view.menu.findItem(R.id.loginItem).setTitle(getString(R.string.login));
            nav_view.menu.findItem(R.id.loginItem).setIcon(android.R.drawable.ic_secure)
            emailTxt.text = ""
            userImg.setImageResource(R.mipmap.profiledefault)

            UserObj.reset()
            AuthObj.reset()
        }
    }

    fun onSigninBntClicked(view : MenuItem) {

        if (!AuthObj.isLoggIn) {
            val intentSignin: Intent = Intent(this, SigninActivity::class.java)
            startActivity(intentSignin)
        } else {


            var nav_view = findViewById<NavigationView>(R.id.nav_view)
            nav_view.menu.findItem(R.id.loginItem).setTitle(getString(R.string.login));
            nav_view.menu.findItem(R.id.loginItem).setIcon(android.R.drawable.ic_secure)
            emailTxt.text = ""
            userImg.setImageResource(R.mipmap.profiledefault)

            UserObj.reset()
            AuthObj.reset()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun onPickPhotoClicked(view: View){

        if (!AuthObj.isLoggIn) {
            ToastCustom.show(this@IBollettaActivity, getString(R.string.do_login))
            return
        }

        val builder = AlertDialog.Builder(this@IBollettaActivity)
        val dialogView = layoutInflater.inflate(R.layout.section_dialog_custom, null)
        val alertDialog = builder.setView(dialogView)

       alertDialog.setNeutralButton( getString(R.string.select_section_text), { dialog: DialogInterface?, which: Int -> doOnPickPhotoClicked()  }).show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun doOnPickPhotoClicked(){


        val sdImageMainDirectory = File.createTempFile(getString(R.string.img_prefix),getString(R.string.png_suffix),getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
        outputFileUri = Uri.fromFile(sdImageMainDirectory)

        // Camera.
        val cameraIntents = mutableListOf<Intent>()
        val captureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)

        listCam.forEach {
            val strPkgName = it.activityInfo.packageName
            val localIntent = Intent(captureIntent);
            localIntent.setComponent(ComponentName(strPkgName, it.activityInfo.name))
            localIntent.setPackage(packageName)
            localIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            cameraIntents.add(localIntent)
        }

        //Filesystem
        val galleryIntent = Intent();
        galleryIntent.setType(getString(R.string.img_type));
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of filesystem options.
        val chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.select_source))

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray() as Array<out Parcelable>)

        startActivityForResult(chooserIntent, CAMERA_REQUEST_CODE);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                var isCamera = (data == null || data.action.isNullOrEmpty())
                if (!isCamera)
                    isCamera = data!!.action.equals(MediaStore.ACTION_IMAGE_CAPTURE)

                var selectedImageUri = outputFileUri

                if (isCamera) {
                    if (selectedImageUri == null) {
                        ToastCustom.show(this@IBollettaActivity, getString(R.string.no_image))
                        return;
                    }
                    nextStep(selectedImageUri.toString());

                } else {
                    selectedImageUri = data!!.data!!;
                    if (selectedImageUri == null) {
                        ToastCustom.show(this@IBollettaActivity, getString(R.string.no_image))
                        return;
                    }
                    nextStep(selectedImageUri.toString());
                }
            }
        }
    }
    private fun nextStep(file : String) {
        val localIntent = Intent(this@IBollettaActivity, KeepAndFlipImgeActivity::class.java)
        localIntent.putExtra(EXTRA_FILE, file);
        startActivity(localIntent);
    }

    val deleteBollettaReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {

            val idBolletta = intent!!.getStringExtra("ID")

            BollettaService.deleteBolletta(context!!
                ,idBolletta!!
            ) { esito: Boolean, messaggio: String ->
                if(esito) {
                    try{

                        var bolletta : Bolletta? = null

                        bollette.stream().filter { it.id == idBolletta }.forEach {
                            bolletta = it
                        }

                        bollette.remove(bolletta)

                        bolletteListView.adapter!!.notifyDataSetChanged()

                        ToastCustom.show(this@IBollettaActivity,getString(R.string.bolletta_delete))

                    }catch(e : Exception){
                        ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  e.message))
                    }

                } else {
                    ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  messaggio))
                }
            }

        }
    }

    val updateBollettaReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context?, intent: Intent?) {

            val bolletta = intent!!.getParcelableExtra<Bolletta>("BOLLETTA")

            BollettaService.updateBolletta(context!!
                ,bolletta!!
            ) { esito: Boolean, messaggio: String ->
                if(esito) {
                    try{

                        bollette.stream().filter { it.id == bolletta.id }.forEach {
                            it.cc = bolletta.cc
                            it.importo = bolletta.importo
                            it.numero = bolletta.numero
                            it.scadenza = bolletta.scadenza
                            it.owner = bolletta.owner
                            it.td = bolletta.td
                        }

                        bolletteListView.adapter!!.notifyDataSetChanged()

                        ToastCustom.show(this@IBollettaActivity,getString(R.string.bolletta_updated))

                    }catch(e : Exception){
                        ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  e.message))
                    }

                } else {
                    ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  messaggio))
                }
            }

        }
    }

    val saveBollettaReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val bolletta = intent!!.getParcelableExtra<Bolletta>("BOLLETTA")

            BollettaService.saveBolletta(context!!
                ,bolletta!!
            ) { esito: Boolean, messaggio: String ->
                if(esito) {
                    try{

                        bollette.add(bolletta)
                        bolletteListView.adapter!!.notifyDataSetChanged()

                        ToastCustom.show(this@IBollettaActivity,getString(R.string.bolletta_saved))

                    }catch(e : Exception){
                        ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  e.message))
                    }

                } else {
                    ToastCustom.show(this@IBollettaActivity,getString(R.string.generic_error,  messaggio))
                }
            }

        }
    }

    val findBolletteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            manageSpinner(View.VISIBLE , false)

            onLogin()

            runOnUiThread {
                BollettaService.findBollette(this@IBollettaActivity
                    , intent!!.getStringExtra("EMAIL")!!,
                    { esito: Boolean, messaggio: String ->
                        if (esito) {
                            try {

                                if (messaggio.length > 0 && !messaggio.equals("[]")) {

                                    bollette.clear()

                                    val responseJson: JSONArray = JSONArray(messaggio)

                                    var i = 0


                                    for ( i in 0 until responseJson.length()) {

                                        val id = responseJson.getJSONObject(i).getString("id")
                                        val cc = responseJson.getJSONObject(i).getString("cc")
                                        val importo = responseJson.getJSONObject(i).getString("importo")
                                        val scadenza = responseJson.getJSONObject(i).getString("scadenza")
                                        val numero = responseJson.getJSONObject(i).getString("numero")
                                        val owner = responseJson.getJSONObject(i).getString("owner")
                                        val td = responseJson.getJSONObject(i).getString("td")

                                        val bolletta: Bolletta = Bolletta(id, cc, importo, scadenza, numero, owner, td)

                                        bollette.add(bolletta)
                                    }
                                }

                                manageSpinner(View.INVISIBLE , true)
                                LocalBroadcastManager.getInstance(this@IBollettaActivity).sendBroadcast(Intent(BROADCAST_FIND_BOLLETTE_NOTIFY_LIST))

                                ToastCustom.show(this@IBollettaActivity,getString(R.string.bollette_found_successfully))

                            } catch (e: Exception) {
                                ToastCustom.show(this@IBollettaActivity,getString(R.string.find_bollette_error, e.message))
                                manageSpinner(View.INVISIBLE , true)
                            }

                        } else {
                            ToastCustom.show(this@IBollettaActivity,getString(R.string.find_bollette_failed, messaggio))
                            manageSpinner(View.INVISIBLE , true)
                        }
                    })
            }
        }
    }

    lateinit var bollettaListAdapter: BollettaListAdapter
    var bollette = mutableListOf<Bolletta>()

    val findBolletteNotifyListReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?)
        {
            bollettaListAdapter = BollettaListAdapter(this@IBollettaActivity, bollette)
            bolletteListView.adapter = bollettaListAdapter

            val linearLayout: GridLayoutManager = GridLayoutManager(context, 1)
            bolletteListView.layoutManager = linearLayout
        }
    }

    fun manageSpinner(visibility : Int, enable: Boolean)
    {
        camera_img.isEnabled = enable
        bollette_list_pb.visibility = visibility
    }

    fun notifyEventuallyLogin() {

        if (AuthObj.isLoggIn)
        {

            var nav_view = findViewById<NavigationView>(R.id.nav_view)

            nav_view.menu.findItem(R.id.loginItem).setTitle(getString(R.string.logout));
            nav_view.menu.findItem(R.id.loginItem).setIcon(android.R.drawable.ic_partial_secure)


            var email = nav_view.getHeaderView(0).findViewById<TextView>(R.id.emailTxt)
            var image = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.userImg)

            email.text = UserObj.userProfile?.nickname

            val identifier = resources.getIdentifier(UserObj.userProfile?.avatarname,"mipmap",packageName)
            val bitmap_1 = BitmapFactory.decodeResource(resources, identifier)
            val rounded_1 = RoundedBitmapDrawableFactory.create(resources,bitmap_1);

            rounded_1.cornerRadius = 15f;
            rounded_1.isCircular = true;

            image.setImageDrawable(rounded_1);

            val findBolletteIntent = Intent(BROADCAST_FIND_BOLLETTE)
            findBolletteIntent.putExtra("EMAIL" , UserObj.userProfile?.email)
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(findBolletteIntent))
        }
    }

    fun onLogin() {

        if (AuthObj.isLoggIn && emailTxt != null && userImg != null) {
            emailTxt.text = " ${UserObj.userProfile?.nickname} "
            var nav_view = findViewById<NavigationView>(R.id.nav_view)
            nav_view.menu.findItem(R.id.loginItem).setTitle(getString(R.string.logout));
            nav_view.menu.findItem(R.id.loginItem).setIcon(android.R.drawable.ic_partial_secure)

            val identifier =
                resources.getIdentifier(UserObj.userProfile?.avatarname, "mipmap", packageName)
            val bitmap_1 = BitmapFactory.decodeResource(resources, identifier)
            val rounded_1 = RoundedBitmapDrawableFactory.create(resources, bitmap_1);

            rounded_1.cornerRadius = 15f;
            rounded_1.isCircular = true;

            userImg.setImageDrawable(rounded_1);

            drawer_layout.closeDrawer(GravityCompat.START, false);
        }
    }


    val logoutReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?)
        {
            var nav_view = findViewById<NavigationView>(R.id.nav_view)
            nav_view.menu.findItem(R.id.loginItem).setTitle(getString(R.string.login));
            nav_view.menu.findItem(R.id.loginItem).setIcon(android.R.drawable.ic_secure)
            emailTxt.text = ""
            userImg.setImageResource(R.mipmap.profiledefault)

            UserObj.reset()
            AuthObj.reset()
        }
    }
}


