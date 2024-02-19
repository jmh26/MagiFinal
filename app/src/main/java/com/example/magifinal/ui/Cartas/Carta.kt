package com.example.magifinal.ui.Cartas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Carta(
    var id: String? = null,
    var nombre: String? = null,
    var categoria: String? = null,
    var precio: String? = null,
    var stock: String? = "0",
    var imagen: String? = null
): Parcelable

