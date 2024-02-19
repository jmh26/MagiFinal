package com.example.magifinal.ui.Eventos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Suscripcion(
    val id: String? = null,
    val eventoID: String? = null,
    val userID: String? = null,
): Parcelable
