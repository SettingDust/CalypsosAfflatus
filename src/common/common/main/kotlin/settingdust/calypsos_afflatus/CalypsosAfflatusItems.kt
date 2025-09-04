package settingdust.calypsos_afflatus

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.adapter.LoaderAdapter.Companion.creativeTab
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem
import settingdust.calypsos_afflatus.util.AccessoryRenderer
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface CalypsosAfflatusItems {
    companion object : CalypsosAfflatusItems {
        val NIGHTVISION_GOGGLES by lazy {
            BuiltInRegistries.ITEM.get(CalypsosAfflatusKeys.NIGHTVISION_GOGGLES)
        }

        private val implementations = ServiceLoaderUtil.findServices<CalypsosAfflatusItems>()

        override fun registerItems(register: (ResourceLocation, Item) -> Unit) {
            register(CalypsosAfflatusKeys.NIGHTVISION_GOGGLES, NightvisionGogglesItem().apply {
                creativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES)
                AccessoryRenderer.registerRenderer(this, NightvisionGogglesAccessory)
            })

            for (implementation in implementations) {
                implementation.registerItems(register)
            }

            AccessoryAdapter.init()
        }
    }

    fun registerItems(register: (ResourceLocation, Item) -> Unit)
}