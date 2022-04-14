package net.noliaware.yumi.presentation.controllers

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.CATEGORIES_DATA
import net.noliaware.yumi.commun.CONNECT_DATA
import net.noliaware.yumi.commun.util.withArgs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().run {
            replace(
                R.id.main_fragment_container,
                HomeFragment().withArgs(CONNECT_DATA to intent.getSerializableExtra(CONNECT_DATA))
            )
            commit()
        }
    }

    /*
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars())
    }
     */
}