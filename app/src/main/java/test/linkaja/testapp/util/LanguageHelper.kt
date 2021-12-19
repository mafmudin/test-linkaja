package test.linkaja.testapp.util

import java.util.*

class LanguageHelper {
    companion object{
        fun getLangByLocale(locale: Locale): String{
            return when (locale.language) {
                "in" -> {
                    "id"
                }
                "en" -> {
                    "en-US"
                }
                else -> {
                    "en-US"
                }
            }
        }
    }
}