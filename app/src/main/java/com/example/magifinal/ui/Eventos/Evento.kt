package com.example.magifinal.ui.Eventos

import android.os.Parcelable
import com.example.magifinal.Estado
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Evento(
    var id: String? = null,
    var nombre: String? = null,
    var fecha: String? = null,
    var precio: String? = null,
    var aforo_actual: String? = null,
    var estado: String? = null,
    var aforo_max: String? = null,
    var imagen: String? = null
): Parcelable

