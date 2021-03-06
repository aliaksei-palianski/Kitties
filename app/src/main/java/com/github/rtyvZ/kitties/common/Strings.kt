package com.github.rtyvZ.kitties.common

object Strings {

    const val BASE_URL: String = "https://api.thecatapi.com/v1/"

    object Crypt {
        const val AES_ALGORITHM = "AES"
        const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        const val SHA_256_ALGORITHM = "SHA-256"

    }

    object PreferenceConst {
        const val AUTH_USER_PREFERENCE = "prefs.for.auth.user"
    }

    object Charsets{
        const val UTF_8 = "UTF-8"
    }

    object IntentConsts {
        const val EXTRA_SEND_UPLOAD = "send.data"
        const val INTENT_TYPE_IMAGE = "image/jpg"
        const val SEND_NO_CONNECTIVITY_INTENT_ACTION = "no.connection.action"
        const val SEND_NO_CONNECTIVITY_KEY = "no_connectivity_message"
        const val DOWNLOAD_IMAGE_KEY = "com.github.rtyvZ.kitties.DOWNLOAD_IMAGE_KEY"
        const val DESCRIPTION_BREEDS = "details_breed"
    }

    object Notification {
        const val CHANNEL_ID = "com.github.com.rtyvZ.vasili.kitties"
        const val CHANNEL_NAME = "com.github.com.rtyvZ.vasili.kitties_NAME"
        const val CHANNEL_DESCRIPTION = "channel_for_response"
    }

    object Const {
        const val AUTHORITY = "com.github.rtyvZ.kitties.fileprovider"
        const val DB_NAME = "CatDB"
    }
}

