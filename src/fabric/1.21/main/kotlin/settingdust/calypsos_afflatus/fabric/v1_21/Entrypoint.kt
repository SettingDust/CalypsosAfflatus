package settingdust.calypsos_afflatus.fabric.v1_21

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import settingdust.calypsos_afflatus.adapter.Entrypoint
import settingdust.calypsos_afflatus.v1_21.CalypsosAfflatusDataComponents

class Entrypoint : Entrypoint {
    override fun init() {
        CalypsosAfflatusDataComponents.registerDataComponents { id, type ->
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, type)
        }
    }
}