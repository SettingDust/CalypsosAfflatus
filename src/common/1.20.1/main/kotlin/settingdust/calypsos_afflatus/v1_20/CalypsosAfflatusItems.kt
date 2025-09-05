package settingdust.calypsos_afflatus.v1_20

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusKeys
import settingdust.calypsos_afflatus.adapter.LoaderAdapter.Companion.creativeTab
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.util.AccessoryRenderer
import settingdust.calypsos_afflatus.v1_20.item.nightvision_goggles.NightvisionGogglesItem

class CalypsosAfflatusItems : CalypsosAfflatusItems {
    override fun registerItems(register: (ResourceLocation, Item) -> Unit) {
        register(CalypsosAfflatusKeys.NIGHTVISION_GOGGLES, NightvisionGogglesItem().apply {
            creativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES)
            AccessoryRenderer.registerRenderer(this, NightvisionGogglesAccessory)
        })
    }
}