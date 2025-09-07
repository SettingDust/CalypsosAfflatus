package settingdust.calypsos_afflatus.fabric

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusSoundEvents
import settingdust.calypsos_afflatus.adapter.Entrypoint

object Entrypoint {
    init {
        requireNotNull(CalypsosAfflatus)
        Entrypoint.construct()
    }

    fun init() {
        CalypsosAfflatusItems.registerItems { id, item -> Registry.register(BuiltInRegistries.ITEM, id, item) }
        CalypsosAfflatusSoundEvents.registerSoundEvents { id, factory ->
            Registry.register(BuiltInRegistries.SOUND_EVENT, id, factory(id))
        }
        Entrypoint.init()
    }

    fun clientInit() {
        Entrypoint.clientInit()
    }
}
