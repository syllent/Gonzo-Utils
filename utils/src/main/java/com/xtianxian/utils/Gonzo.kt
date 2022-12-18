package com.xtianxian.utils

sealed class Gonzo<D: Any>(val data: D)
class KnightGonzo(data: String) : Gonzo<String>(data = data)
class ReptilesGonzo(data: List<String>) : Gonzo<List<String>>(data = data)
class BearGonzo(data: String) : Gonzo<String>(data = data)
class DragonGonzo(data: Boolean) : Gonzo<Boolean>(data = data)