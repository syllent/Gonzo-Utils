package com.xtianxian.utils

import android.content.Context
import android.provider.Settings
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import io.reactivex.rxjava3.core.Single
import okhttp3.HttpUrl
import java.util.*

inline fun<reified G: Gonzo<*>> Context.gonzoBattle(): Single<G> {
    with (G::class.java) {
        return Single.create { emitter ->
            when {
                isAssignableFrom(KnightGonzo::class.java) -> {
                    AppLinkData
                        .fetchDeferredAppLinkData(
                            this@gonzoBattle
                        ) { appLinkData: AppLinkData? ->
                            emitter.onSuccess(
                                KnightGonzo(
                                    data = appLinkData?.targetUri.toString()
                                ) as G
                            )
                        }
                }
                isAssignableFrom(ReptilesGonzo::class.java) -> {
                    val appsFlyerLib = AppsFlyerLib.getInstance()
                    val conversionListener = object : AppsFlyerConversionListener {
                        override fun onConversionDataSuccess(convData: MutableMap<String, Any>?) {
                            emitter.onSuccess(
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
                                            this@gonzoBattle
                                        ).toString()
                                    )
                                ) as G
                            )
                        }
                        override fun onConversionDataFail(p0: String?) {
                            emitter.onSuccess(
                                ReptilesGonzo(
                                    data = List(9) { "null" }
                                ) as G
                            )
                        }
                        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) { }
                        override fun onAttributionFailure(p0: String?) { }
                    }
                    appsFlyerLib.init(
                        getStr("punch"),
                        conversionListener,
                        this@gonzoBattle
                    )
                    appsFlyerLib.start(this@gonzoBattle)
                }
                isAssignableFrom(BearGonzo::class.java) -> {
                    emitter.onSuccess(
                        BearGonzo(
                            data = AdvertisingIdClient
                                .getAdvertisingIdInfo(this@gonzoBattle)
                                .id.toString()
                        ) as G
                    )
                }
                isAssignableFrom(DragonGonzo::class.java) -> {
                    emitter.onSuccess(
                        DragonGonzo(
                            data = Settings.Global
                                .getString(
                                    contentResolver,
                                    Settings.Global.ADB_ENABLED
                                ) != "1"
                        ) as G
                    )
                }
                else -> emitter.onError(Throwable("Invalid type"))
            }
        }
    }
}

fun Context.getStr(name: String): String {
    return getString(resources.getIdentifier(name, "null", packageName))
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
        .addQuery("clinic", "cooperation")
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