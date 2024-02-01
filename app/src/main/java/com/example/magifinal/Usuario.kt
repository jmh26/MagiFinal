package com.example.magifinal

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize

data class Usuario(
    var id: String? = null,
    var correo: String? = null,
    var contrasena: String? = null,
    var tipo: String? ="cliente"

): Parcelable
