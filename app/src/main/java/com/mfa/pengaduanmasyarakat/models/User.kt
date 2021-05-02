package com.mfa.pengaduanmasyarakat.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
  var id: Int? = null,
  var name: String? = null,
  var email: String? = null,
  var nik: String? = null,
  var rukun_tetangga: String? = null,
  var rukun_warga: String? = null,
  var profile: String? = null,
  var api_token: String? = null
) : Parcelable