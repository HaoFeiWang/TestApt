package com.whf.testapt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.whf.spi.SpiInterface
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val tag = MainActivity::class.java.simpleName;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_spi.setOnClickListener {
            Log.d(tag, "click tv spi")

            val loader: ServiceLoader<SpiInterface> = ServiceLoader.load(SpiInterface::class.java)
            for (serviceLoader in loader) {
                Log.d(tag, "ServiceLoader : ${serviceLoader.name} ")
            }
        }
    }


}
