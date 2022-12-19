package com.xtianxian.utils

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import okhttp3.HttpUrl
import java.util.*

fun Context.battleKnight(
    callback: (KnightGonzo) -> Unit,
    isNull: Boolean
) {
    var isDone = false
    AppLinkData
        .fetchDeferredAppLinkData(
            this
        ) { appLinkData: AppLinkData? ->

            callback.invoke(
                KnightGonzo(
                    data =
                    if (isNull) "null"
                    else "myapp://test1/test2/test3/test4/test5"
                )
            )

            //if (!isDone) {
            //    isDone = true
            //    callback.invoke(
            //        KnightGonzo(
            //            data = appLinkData?.targetUri.toString()
            //        )
            //    )
            //}
        }
}

fun Context.battleBear(
    callback: (BearGonzo) -> Unit
) {
    callback.invoke(
        BearGonzo(
            data = AdvertisingIdClient
                .getAdvertisingIdInfo(this)
                .id.toString()
        )
    )
}

fun Context.battleDragon(
    callback: (DragonGonzo) -> Unit
) {
    callback.invoke(
        DragonGonzo(
            data = Settings.Global
                .getString(
                    contentResolver,
                    Settings.Global.ADB_ENABLED
                ) != "1"
        )
    )
}

fun Context.battleReptiles(
    callback: (ReptilesGonzo) -> Unit,
    isNull: Boolean
) {
    var isDone = false
    val appsFlyerLib = AppsFlyerLib.getInstance()
    val conversionListener = object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(convData1: MutableMap<String, Any>?) {
            Log.e("AppsFlyerConversion", "Success")

            var convData: MutableMap<String, Any>? = null
            if (!isNull) {
                convData = mutableMapOf()
                convData["af_status"] = "Non-organic"
                convData["media_source"] = "testSource"
                convData["campaign"] = "test1_test2_test3_test4_test5"
                convData["adset"] = "testAdset"
                convData["adset_id"] = "testAdsetId"
                convData["campaign_id"] = "testCampaignId"
                convData["orig_cost"] = "1.22"
                convData["af_siteid"] = "testSiteID"
                convData["adgroup"] = "testAdgroup"
            }

            callback.invoke(
                ReptilesGonzo(
                    data = listOf(
                        convData?.get("media_source").toString(),
                        convData?.get("campaign").toString(),
                        convData?.get("adset_id").toString(),
                        convData?.get("campaign_id").toString(),
                        convData?.get("adset").toString(),
                        convData?.get("adgroup").toString(),
                        convData?.get("orig_cost").toString(),
                        convData?.get("af_siteid").toString(),
                        appsFlyerLib.getAppsFlyerUID(
                            this@battleReptiles
                        ).toString()
                    )
                )
            )

            //if (!isDone) {
            //    isDone = true
            //    callback.invoke(
            //        ReptilesGonzo(
            //            data = listOf(
            //                convData?.get("media_source").toString(),
            //                convData?.get("campaign").toString(),
            //                convData?.get("adset_id").toString(),
            //                convData?.get("campaign_id").toString(),
            //                convData?.get("adset").toString(),
            //                convData?.get("adgroup").toString(),
            //                convData?.get("orig_cost").toString(),
            //                convData?.get("af_siteid").toString(),
            //                appsFlyerLib.getAppsFlyerUID(
            //                    this@battleReptiles
            //                ).toString()
            //            )
            //        )
            //    )
            //}
        }
        override fun onConversionDataFail(p0: String?) {
            Log.e("AppsFlyerConversion", "Fail")
            if (!isDone) {
                isDone = true
                callback.invoke(
                    ReptilesGonzo(
                        data = List(9) { "null" }
                    )
                )
            }
        }
        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) { }
        override fun onAttributionFailure(p0: String?) { }
    }
    appsFlyerLib.init(
        getStr("punch"),
        conversionListener,
        this
    )
    appsFlyerLib.start(this)
}

fun Context.getStr(name: String): String {
    return getString(resources.getIdentifier(name, "string", packageName))
}

fun Context.getWayDirection(
    knightGonzo: KnightGonzo,
    reptilesGonzo: ReptilesGonzo,
    bearGonzo: BearGonzo
): List<String> {
    fun HttpUrl.Builder.addQuery(
        key: String, value: String
    ): HttpUrl.Builder {
        addQueryParameter(
            getStr(key),
            value
        )
        return this
    }

    val source =
        if (!knightGonzo.data.isNullOrEmpty) "deeplink"
        else reptilesGonzo.data[0]

    val tag = when {
        reptilesGonzo.data[1].isNullOrEmpty && knightGonzo.data.isNullOrEmpty -> {
            "organic"
        }
        !knightGonzo.data.isNullOrEmpty -> {
            knightGonzo.data
                    .replace("myapp://", "")
                    .substringBefore("/")
        }
        !reptilesGonzo.data[1].isNullOrEmpty -> {
            reptilesGonzo.data[1].substringBefore("_")
        }
        else -> "null"
    }

    val link = HttpUrl.Builder()
        .scheme(getStr("minimize"))
        .host(getStr("offender"))
        .addPathSegment(getStr("mood"))
        .addQuery("clinic", getStr("cooperation"))
        .addQuery("wheat", source)
        .addQuery("preparation", bearGonzo.data)
        .addQuery("gravel", knightGonzo.data)
        .addQuery("flawed", reptilesGonzo.data[1])
        .addQuery("accept", reptilesGonzo.data[2])
        .addQuery("principle", reptilesGonzo.data[3])
        .addQuery("abnormal", reptilesGonzo.data[4])
        .addQuery("ton", reptilesGonzo.data[5])
        .addQuery("looting", reptilesGonzo.data[6])
        .addQuery("concrete", reptilesGonzo.data[7])
        .addQuery("minority", reptilesGonzo.data[8])
        .addQuery("bind", TimeZone.getDefault().id)
        .build()
        .toString()

    return listOf(
        tag,
        link,
    )
}

val String.isNullOrEmpty: Boolean
    get() = isEmpty() || this == "null"

val String.fix: String
    get() = replace("wv", "")