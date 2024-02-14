package com.example.magifinal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Evento(
    var id: String? = null,
    var nombre: String? = null,
    var fecha: String? = null,
    var precio: String? = null,
    var imagen: String? = null
): Parcelable

