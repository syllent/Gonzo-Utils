package com.xtianxian.utils

import android.net.Uri
import android.webkit.ValueCallback

interface GonzoCallback {
    fun onDataSave(data: String?)
    fun onFileChoose(callback: ValueCallback<Array<Uri?>>?)
    fun onEmpty()
}