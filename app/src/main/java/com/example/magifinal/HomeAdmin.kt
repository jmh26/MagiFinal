package com.example.magifinal

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.magifinal.Pedidos.Pedido
import com.example.magifinal.Pedidos.PedidosFragment
import com.example.magifinal.databinding.ActivityHomeAdminBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.atomic.AtomicInteger
import java.util.jar.Manifest

class HomeAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var db_ref: DatabaseReference
    private lateinit var androidId: String
    private lateinit var generador: AtomicInteger

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("DarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }

        super.onCreate(savedInstanceState)

        crearCanalNotificaciones()
        androidId = Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID)
        db_ref = FirebaseDatabase.getInstance().reference
        generador = AtomicInteger(0)
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        db_ref.child("Tienda").child("reservas_carta")
            .addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val pojo_pedido = snapshot.getValue(Pedido::class.java)
                    if (pojo_pedido!!.clienteID == auth.currentUser?.uid) {
                        generarNotificacion(
                            generador.incrementAndGet(),
                            pojo_pedido,
                            "Pedido aceptado",
                            "El pedido de ${pojo_pedido.nombre} por ${pojo_pedido.precio}€ está preparado",
                            PedidosFragment::class.java
                        )
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val pojo_pedido = snapshot.getValue(Pedido::class.java)
                    if (pojo_pedido!!.clienteID == auth.currentUser?.uid) {
                        generarNotificacion(
                            generador.incrementAndGet(),
                            pojo_pedido,
                            "Pedido aceptado",
                            "El pedido de ${pojo_pedido.nombre} por ${pojo_pedido.precio}€ está preparado",
                            PedidosFragment::class.java
                        )
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val pojo_pedido = snapshot.getValue(Pedido::class.java)
                    if (pojo_pedido!!.clienteID == auth.currentUser?.uid) {
                        generarNotificacion(
                            generador.incrementAndGet(),
                            pojo_pedido,
                            "Pedido eliminado",
                            "El pedido de ${pojo_pedido.nombre} por ${pojo_pedido.precio}€ ha sido eliminado",
                            PedidosFragment::class.java
                        )
                    }

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



                window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_fragment_home_admin)



        navView.setupWithNavController(navController)
    }


    private fun generarNotificacion(id_noti: Int, pojo: Parcelable, contenido: String, titulo: String, destino: Class<*>){
        val actividad = Intent(applicationContext,destino)
        actividad.putExtra("magic",pojo)

        var id = "test_channel"


        val pendingIntent = PendingIntent.getActivity(this,0,actividad, PendingIntent.FLAG_MUTABLE)
        val notificacion = NotificationCompat.Builder(this,id).
        setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titulo)
            .setContentText(contenido)
            .setSubText("sistema de informacion")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(this)){
            if(ActivityCompat.checkSelfPermission(applicationContext,android.Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){

            }
            notify(id_noti,notificacion)
        }
    }

    private fun crearCanalNotificaciones(){
        val nombre = "canal basico"
        val id = "test_channel"
        val descripcion = "Notificacion basica"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id,nombre,importancia).apply {
            this.description = descripcion
        }

        val nm: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }




}

