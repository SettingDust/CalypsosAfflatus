package settingdust.calypsos_afflatus

import org.apache.logging.log4j.LogManager
import settingdust.calypsos_afflatus.adapter.MinecraftAdapter
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

object CalypsosAfflatus {
    const val ID = "calypsos_afflatus"

    val LOGGER = LogManager.getLogger()

    init {
        ServiceLoaderUtil.defaultLogger = LOGGER
    }

    fun id(path: String) = MinecraftAdapter.id(ID, path)
}