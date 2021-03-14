package com.mateuszkrawczuk.tvprogramsearcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.mateuszkrawczuk.tvprogramsearcher.BuildConfig
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // needed for osmandroid
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        setContent {
            MainLayout()
        }
    }
}

@Composable
fun MainLayout() {}