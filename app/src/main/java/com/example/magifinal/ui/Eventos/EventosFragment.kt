package com.example.magifinal.ui.Eventos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.magifinal.Evento
import com.example.magifinal.databinding.FragmentEventosBinding
import com.example.magifinal.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventosFragment : Fragment() {


    private var _binding: FragmentEventosBinding? = null

    private lateinit var db_ref: DatabaseReference
    private lateinit var lista: MutableList<Evento>


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(EventosViewModel::class.java)

        _binding = FragmentEventosBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        var user = FirebaseAuth.getInstance().currentUser
        db_ref = FirebaseDatabase.getInstance().reference






        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}