package com.aadipanchang.map.activities.dialogs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultItem(val name: String, val x: Float, val y: Float, val z: Float) : Parcelable {
  override fun toString() = name
}
