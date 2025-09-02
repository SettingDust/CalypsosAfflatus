package settingdust.calypsos_afflatus.adapter

import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface AccessoryAdapter {
    companion object : Entrypoint {
        override fun construct() {
            for (adapter in ServiceLoaderUtil.findServices<AccessoryAdapter>(required = false)) {
                if (LoaderAdapter.isModLoaded(adapter.modId)) {
                    adapter.init()
                }
            }
        }
    }

    val modId: String

    fun init()
}