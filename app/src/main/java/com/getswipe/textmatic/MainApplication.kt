package com.getswipe.textmatic

import android.app.Application
import com.getswipe.textmatic.data.ForwardingRuleDatabaseProvider
import com.vaibhavpandey.katora.Container
import com.vaibhavpandey.katora.contracts.ImmutableContainer

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        _container = Container()
        _container!!.install {
            ForwardingRuleDatabaseProvider()
        }
    }

    companion object {

        @Suppress("ObjectPropertyName")
        private var _container: Container? = null

        val container: ImmutableContainer get() = _container!!
    }
}
