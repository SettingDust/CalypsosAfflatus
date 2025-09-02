package settingdust.calypsos_afflatus

import io.wispforest.accessories.Accessories
import settingdust.calypsos_afflatus.adapter.Entrypoint
import settingdust.calypsos_afflatus.adapter.LoaderAdapter

class CalypsosAfflatusCompats : Entrypoint {
    companion object {
        val curios by lazy { LoaderAdapter.isModLoaded("curios") }
        val trinkets by lazy { LoaderAdapter.isModLoaded("trinkets") }
        val accessories by lazy { LoaderAdapter.isModLoaded(Accessories) }
    }
}