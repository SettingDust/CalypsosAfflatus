package settingdust.calypsos_afflatus

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

object CalypsosAfflatusSoundEvents {
    val UI_MODE_SWITCH by lazy { BuiltInRegistries.SOUND_EVENT.get(CalypsosAfflatusKeys.UI_MODE_SWITCH)!! }
    val UI_EXPAND by lazy { BuiltInRegistries.SOUND_EVENT.get(CalypsosAfflatusKeys.UI_EXPAND)!! }
    val UI_COLLAPSE by lazy { BuiltInRegistries.SOUND_EVENT.get(CalypsosAfflatusKeys.UI_COLLAPSE)!! }

    fun registerSoundEvents(register: (ResourceLocation, (ResourceLocation) -> SoundEvent) -> Unit) {
        register(CalypsosAfflatusKeys.UI_MODE_SWITCH) { SoundEvent.createFixedRangeEvent(it, 0f) }
        register(CalypsosAfflatusKeys.UI_EXPAND) { SoundEvent.createFixedRangeEvent(it, 0f) }
        register(CalypsosAfflatusKeys.UI_COLLAPSE) { SoundEvent.createFixedRangeEvent(it, 0f) }
    }
}