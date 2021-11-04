package com.getswipe.textmatic

import android.app.Application
import com.getswipe.textmatic.data.ForwardingRuleDatabaseProvider
import com.vaibhavpandey.katora.Container
import com.vaibhavpandey.katora.contracts.ImmutableContainer
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        container = Container().apply {
            install(MainProvider(this@MainApplication))
            install(ForwardingRuleDatabaseProvider())
        }
    }

    companion object {

        private var container: Container? = null

        val CONTAINER: ImmutableContainer get() = container!!

        init {
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
        }
    }
}
