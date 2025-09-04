package settingdust.calypsos_afflatus.adapter

import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface AccessoryAdapter {
    class Entrypoint : settingdust.calypsos_afflatus.adapter.Entrypoint {
        private val services by lazy { ServiceLoaderUtil.findServices<AccessoryAdapter>(required = false) }

        override fun construct() {
            for (adapter in services) {
                if (LoaderAdapter.isModLoaded(adapter.modId)) {
                    adapter.init()
                }
            }
        }
    }

    val modId: String

    fun init()
}