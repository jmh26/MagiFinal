package com.example.magifinal.Pedidos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.IDN

@Parcelize
data class Pedido(
    var id: String? = null,
    var imagen: String? = null,
    var clienteID: String? = null,
    var cartaID: String? = null,
    var estado: String? = null,
    var nombre: String? = null,
    var precio: String? = null,
    var estado_noti:Int? = null,
    var user_notificacion:String? = null




): Parcelable
