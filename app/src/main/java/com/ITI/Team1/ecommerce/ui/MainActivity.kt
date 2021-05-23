package com.ITI.Team1.ecommerce.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.ITI.Team1.ecommerce.R
import com.etebarian.meowbottomnavigation.MeowBottomNavigation


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation: MeowBottomNavigation = findViewById(R.id.meowBottomNavigation)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.store))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.widgets))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.person))
        bottomNavigation.show(1)
        bottomNavigation.setOnClickMenuListener { model: MeowBottomNavigation.Model? ->
            when(model?.id){
                1->{
                    navController.popBackStack()
                    navController.navigate(R.id.store)
                }
                2->{

                }
                3->{
                    navController.popBackStack()
                    navController.navigate(R.id.loginFragment)

                }
                else->{

                }
            }
        }
    }
}