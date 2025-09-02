package settingdust.calypsos_afflatus.forge

import net.minecraft.core.registries.Registries
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.RegisterEvent
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.Entrypoint
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(CalypsosAfflatus.ID)
object CalypsosAfflatusForge {
    init {
        requireNotNull(CalypsosAfflatus)
        Entrypoint.construct()
        MOD_BUS.apply {
            addListener<FMLCommonSetupEvent> { Entrypoint.init() }
            addListener<FMLClientSetupEvent> { Entrypoint.clientInit() }
            addListener { event: RegisterEvent ->
                when (event.registryKey) {
                    Registries.ITEM -> CalypsosAfflatusItems.registerItems { id, value ->
                        event.register(Registries.ITEM, id) { value }
                    }
                }
            }
        }
    }
}