package settingdust.calypsos_afflatus

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import settingdust.calypsos_afflatus.adapter.AccessoryIntegration
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface CalypsosAfflatusItems {
    companion object : CalypsosAfflatusItems {
        val NIGHTVISION_GOGGLES by lazy { BuiltInRegistries.ITEM.get(CalypsosAfflatusKeys.NIGHTVISION_GOGGLES) }

        private val implementations = ServiceLoaderUtil.findServices<CalypsosAfflatusItems>()

        override fun registerItems(register: (ResourceLocation, Item) -> Unit) {
            for (implementation in implementations) {
                implementation.registerItems(register)
            }

            AccessoryIntegration.init()
        }
    }

    fun registerItems(register: (ResourceLocation, Item) -> Unit)
}