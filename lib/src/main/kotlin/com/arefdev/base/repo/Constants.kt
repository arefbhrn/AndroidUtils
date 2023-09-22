package com.arefdev.base.repo

/**
 * Updated on 25/06/2022
 *
 * @author <a href="https://github.com/arefbhrn">Aref Bahreini</a>
 */
object Constants {

    const val BASE_URL_DOMAIN = "something.com"
    const val BASE_URL = "https://$BASE_URL_DOMAIN"
    const val API_BASE_URL = "$BASE_URL/VOD/"

    object URLs {

        const val API_TIME = "http://worldtimeapi.org/api/timezone/{timezone}"
        fun API_TIME(timezone: String) = API_TIME.replace("{timezone}", timezone)
    }
}
