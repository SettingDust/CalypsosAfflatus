package settingdust.calypsos_afflatus.forge.adapter

import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.forge.item.NightvisionGogglesCurios
import top.theillusivec4.curios.api.CuriosApi

class CuriosAccessoryAdapter : AccessoryAdapter {
    override val modId = CuriosApi.MODID
    override fun init() {
        CuriosApi.registerCurio(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, NightvisionGogglesCurios)
    }
}