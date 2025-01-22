package com.insightface.inspireface_demo.utils

import android.text.TextUtils


fun listIsEmp(list: List<*>?): Boolean {
    return null == list || list.isEmpty()
}

/**
 * 是否包含规定类型设备
 */
fun isContain(dev: String, datas: Array<String>): Boolean {
    if (TextUtils.isEmpty(dev)) return false

    for (i in datas) {
        if (dev.contains(i)) {
            return true
        }
    }
    return false
}

fun getListSize(list: List<*>?): Int {
    if (listIsEmp(list)) return 0
    return list!!.size
}


fun isInListRange(list: List<*>?, pos: Int): Boolean {
    if (listIsEmp(list)) return false
    if (pos >= 0 && pos < list!!.size) return true
    return false
}

fun <T> getListIndex(list: List<T>?, pos: Int): T? {
    if (isInListRange(list, pos)) {
        return list!![pos]
    }
    return null
}


/**
 * 判断 Pos 是否在列表区间内
 */
fun isListRang(list: List<*>?, pos: Int): Boolean {
    if (listIsEmp(list)) return false
    if (pos >= 0 && pos < list!!.size) return true
    return false
}

