package com.mfa.pengaduanmasyarakat.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Complaint(
    var id: Int? = null,
    var complaint: String? = null,
    var category: String? = null,
    var user_id: Int? = null,
    var created_at: String? = null
): Parcelable