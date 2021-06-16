package com.iti.team.ecommerce.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.ui.shop.ShopFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigation: MeowBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigation = findViewById(R.id.meowBottomNavigation)
        val navController = navHostFragment.navController

        splashSetup(navController,bottomNavigation)
        navigationSetup(navController,bottomNavigation)
    }
    private fun splashSetup(navController: NavController, bottomNavigation: MeowBottomNavigation){
        bottomNavigation.isGone = true
        CoroutineScope(Dispatchers.Default).launch{
            delay(3000)
            CoroutineScope(Dispatchers.Main).launch{
                bottomNavigation.isGone = false
                navController.popBackStack()
                navController.navigate(R.id.shopFragment)
            }
        }
    }


    private fun navigationSetup(navController: NavController, bottomNavigation: MeowBottomNavigation){
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.store))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.widgets))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.person))
        bottomNavigation.show(1)
        bottomNavigation.setOnClickMenuListener { model: MeowBottomNavigation.Model? ->
            when(model?.id){
                1->{
                    navController.popBackStack()
                    navController.navigate(R.id.shopFragment)
                }
                2->{
                    navController.popBackStack()
                    navController.navigate(R.id.addressFragment2)
                }
                3->{
                    navController.popBackStack()
                    navController.navigate(R.id.profileFragment)

                }
                else->{

                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val navHostFragment = supportFragmentManager.fragments.first()
                as? NavHostFragment

        navHostFragment?.let {
            val childFragments =
                navHostFragment.childFragmentManager.fragments

            childFragments.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)

                if (fragment is ShopFragment) {
                    val page = supportFragmentManager
                        .findFragmentByTag(
                            "android:switcher:${R.id.shopFragment}:${fragment.arguments}"
                        )

                    page?.let {
                        page.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

}