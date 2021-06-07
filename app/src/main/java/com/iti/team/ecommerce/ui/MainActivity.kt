package com.iti.team.ecommerce.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.iti.team.ecommerce.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigation: MeowBottomNavigation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        generateFBKey()
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

    override fun onBackPressed() {
        super.onBackPressed()
        bottomNavigation.isGone = false
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
                    navController.navigate(R.id.categoriesFragment)
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

    fun generateFBKey(){
        try {
            val info = packageManager.getPackageInfo(
                "com.iti.team.ecommerce",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

}