package settingdust.calypsos_afflatus.adapter

import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface AccessoryAdapter {
    companion object {
        private val services by lazy { ServiceLoaderUtil.findServices<AccessoryAdapter>(required = false) }

        fun init() {
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