package com.xtianxian.utils

sealed interface Gonzo
class KnightGonzo(val data: String) : Gonzo
class ReptilesGonzo(val data: List<String>) : Gonzo
class BearGonzo(val data: String) : Gonzo
class DragonGonzo(val data: Boolean) : Gonzo