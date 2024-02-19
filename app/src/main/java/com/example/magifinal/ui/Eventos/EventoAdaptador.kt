package com.example.magifinal.ui.Eventos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magifinal.R
import com.example.magifinal.Utilidades
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EventoAdaptador(private var listaEventos: MutableList<Evento>, private var suscripcion:MutableList<Suscripcion>?=null) : RecyclerView.Adapter<EventoAdaptador.EventoViewHolder>(),
    Filterable {

        private lateinit var contexto: Context

        class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nombre = itemView.findViewById<TextView>(R.id.item_nombreEvento)
            val fecha = itemView.findViewById<TextView>(R.id.item_fecha)
            val precio = itemView.findViewById<TextView>(R.id.item_precioEvento)
            val aforoMax = itemView.findViewById<TextView>(R.id.item_aforoMaximo)
            val aforoActual = itemView.findViewById<TextView>(R.id.item_aforoActual)
            val imagen = itemView.findViewById<ImageView>(R.id.item_imagenEvento)
            val suscribir = itemView.findViewById<ImageView>(R.id.item_apuntarseEvento)
            val eliminar = itemView.findViewById<ImageView>(R.id.item_eliminarEvento)
            val estado = itemView.findViewById<TextView>(R.id.item_estadoEvento)

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): EventoAdaptador.EventoViewHolder {
            contexto = parent.context
            val itemView = LayoutInflater.from(contexto).inflate(R.layout.item_evento, parent, false)
            return EventoAdaptador.EventoViewHolder(itemView)
        }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val eventoActual = listaEventos[position]
        holder.nombre.text = eventoActual.nombre
        holder.fecha.text = eventoActual.fecha
        holder.precio.text = eventoActual.precio + "€"
        holder.aforoMax.text = eventoActual.aforo_max.toString()
        holder.estado.text = eventoActual.estado
        holder.aforoActual.text = eventoActual.aforo_actual.toString()

        val url: String? = when (eventoActual.imagen) {
            "" -> null
            else -> eventoActual.imagen
        }

        Glide.with(contexto).load(url).apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion).into(holder.imagen)

        var sharedPreferences = contexto.getSharedPreferences("login", Context.MODE_PRIVATE)
        var esAdmin = sharedPreferences.getBoolean("esAdmin", false)

        if (esAdmin) {
            holder.suscribir.visibility = View.GONE
            holder.eliminar.visibility = View.VISIBLE

            holder.eliminar.setOnClickListener {
                try {
                    val db_ref = FirebaseDatabase.getInstance().reference
                    val st_ref = FirebaseStorage.getInstance().reference
                    listaEventos.remove(eventoActual)

                    st_ref.child("Eventos").child("Fotos").child(eventoActual.id!!).delete()
                    db_ref.child("Tienda").child("reservas_eventos").child(eventoActual.id!!).removeValue()


                    Toast.makeText(contexto, "Evento eliminado", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(contexto, "Error al eliminar el evento", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            holder.eliminar.visibility = View.GONE
            holder.suscribir.visibility = View.VISIBLE

            holder.suscribir.setOnClickListener {
                val db_ref = FirebaseDatabase.getInstance().reference
                val sharedPreferences = contexto.getSharedPreferences("login", Context.MODE_PRIVATE)
                val androidId = sharedPreferences.getString("androidId", null)
                val eventoID = eventoActual.id ?: "" // Obtener el ID del evento actual
                val suscripcion = Suscripcion(eventoID, androidId)
                db_ref.child("Tienda").child("reservas_eventos").child(eventoID).child(androidId!!).setValue(suscripcion)
                Toast.makeText(contexto, "Te has suscrito al evento", Toast.LENGTH_SHORT).show()
            }



        }


    }

    override fun getItemCount(): Int = listaEventos.size

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }



}