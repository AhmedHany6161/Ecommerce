import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.iti.team.ecommerce.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomNavigation: MeowBottomNavigation = findViewById(R.id.meowBottomNavigation)
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
                navController.navigate(R.id.store)
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
                    navController.navigate(R.id.store)
                }
                2->{
                    navController.popBackStack()
                    navController.navigate(R.id.categoriesFragment)
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