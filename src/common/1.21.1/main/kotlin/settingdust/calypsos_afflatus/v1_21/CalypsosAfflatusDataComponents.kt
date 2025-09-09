package settingdust.calypsos_afflatus.v1_21

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import settingdust.calypsos_afflatus.CalypsosAfflatusKeys
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.v1_21.util.CalypsosAfflatusCodecs
import settingdust.calypsos_afflatus.v1_21.util.CalypsosAfflatusStreamCodecs

object CalypsosAfflatusDataComponents {
    val MODE by lazy { BuiltInRegistries.DATA_COMPONENT_TYPE.get(CalypsosAfflatusKeys.MODE) as DataComponentType<NightvisionGogglesModeHandler.Mode> }

    fun registerDataComponents(register: (ResourceLocation, DataComponentType<*>) -> Unit) {
        register(
            CalypsosAfflatusKeys.MODE,
            DataComponentType.builder<NightvisionGogglesModeHandler.Mode>()
                .networkSynchronized(CalypsosAfflatusStreamCodecs.NIGHTVISION_GOGGLES_MODE)
                .persistent(CalypsosAfflatusCodecs.NIGHTVISION_GOGGLES_MODE)
                .build()
        )
    }
}