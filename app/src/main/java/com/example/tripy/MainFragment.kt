package com.example.tripy

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.tripy.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainFragment : Fragment() {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var drawer:DrawerLayout
    private lateinit var actionBar:ActionBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentMainBinding
    private lateinit var icon:ImageView
    private lateinit var menu:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)


    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater!!.inflate(R.menu.drawer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()


        return binding.root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                // navigate to settings screen
                firebaseAuth.signOut()
                findNavController(binding.root).navigate(R.id.action_mainFragment_to_loginFragment)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

