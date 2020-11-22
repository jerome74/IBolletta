package com.wlp.ibolletta.util


/**
 * BROADCAST
 */

const val BROADCAST_SAVE_BOLLETTA = "BROADCAST_9997"
const val BROADCAST_UPDATE_BOLLETTA = "BROADCAST_9996"
const val BROADCAST_DELETE_BOLLETTA = "BROADCAST_9995"
const val BROADCAST_LOGOUT = "BROADCAST_9994"
const val BROADCAST_FIND_BOLLETTE = "BROADCAST_9993"
const val BROADCAST_FIND_BOLLETTE_NOTIFY_LIST = "BROADCAST_9992"

const val REQUEST_WRITE_PERMISSION = 9999

const val PAYLOAD_VOICE = "PAYLOAD_VOICE_9999"
const val EXTRA_FILE = "EXTRA_FILE_9999"

const val LANGUAGE_IT = "it-IT"
const val LANGUAGE_EN = "en-EN"

/**
 * INTENT
 */

const val INTENT_TITLE = "INTENT_9998"
const val INTENT_FOLDER = "INTENT_9997"

/**
 * REQUEST_CODE
 */


const val CAMERA_REQUEST_CODE = 1234

/**
 * URI
 */

//######################BASE_URI####################################
const val BASE_URL : String = "https://850bb42b-25b0-4be3-b909-a729ef490e3d.mock.pstmn.io"
//const val BASE_URL : String = "https://ibolletta-1601218389825.azurewebsites.net"
//-------------------------------------------------------------------


const val URI_LOGIN : String = "$BASE_URL/login"
const val URI_SIGNIN : String = "$BASE_URL/api/ibolletta/signin"
const val URI_FIND_BY_EMAIL : String = "$BASE_URL/api/ibolletta/profile"
const val URI_SAVE_BOLLETTA : String = "$BASE_URL/api/ibolletta/savebolletta"
const val URI_DELELE_BOLLETTA : String = "$BASE_URL/api/ibolletta/deletebolletta"
const val URI_UPDATE_BOLLETTA : String = "$BASE_URL/api/ibolletta/updatebolletta"
const val URI_FIND_BOLLETTE : String = "$BASE_URL/api/ibolletta/findbollette"
const val URI_FIND_BOLLETTA : String = "$BASE_URL/api/ibolletta/findbolletta"



