package net.noliaware.yumi.feature_categories.presentation.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi.R
import net.noliaware.yumi.commun.ACCOUNT_DATA
import net.noliaware.yumi.commun.util.getSerializableExtraCompat
import net.noliaware.yumi.feature_login.domain.model.AccountData

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intent.getSerializableExtraCompat<AccountData>(ACCOUNT_DATA)?.let { accountData ->
            supportFragmentManager.beginTransaction().run {
                replace(R.id.main_fragment_container, HomeFragment.newInstance(accountData))
                commit()
            }
        }
    }
}