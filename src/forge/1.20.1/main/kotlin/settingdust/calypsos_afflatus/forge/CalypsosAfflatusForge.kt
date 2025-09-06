package settingdust.calypsos_afflatus.forge

import net.minecraft.client.KeyMapping
import net.minecraft.core.registries.Registries
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.RegisterEvent
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusKeyBindings
import settingdust.calypsos_afflatus.CalypsosAfflatusSoundEvents
import settingdust.calypsos_afflatus.adapter.Entrypoint
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(CalypsosAfflatus.ID)
object CalypsosAfflatusForge {
    init {
        requireNotNull(CalypsosAfflatus)
        Entrypoint.construct()
        MOD_BUS.apply {
            addListener<FMLCommonSetupEvent> {
                Entrypoint.init()
                requireNotNull(CalypsosAfflatusNetworking)
            }
            addListener<FMLClientSetupEvent> { Entrypoint.clientInit() }
            addListener<RegisterEvent> { event ->
                when (event.registryKey) {
                    Registries.ITEM -> CalypsosAfflatusItems.registerItems { id, value ->
                        event.register(Registries.ITEM, id) { value }
                    }

                    Registries.SOUND_EVENT -> CalypsosAfflatusSoundEvents.registerSoundEvents { id, value ->
                        event.register(Registries.SOUND_EVENT, id) { value(id) }
                    }
                }
            }
            addListener<RegisterKeyMappingsEvent> { event ->
                CalypsosAfflatusKeyBindings.registerKeyBindings { event.register(it) }
                (CalypsosAfflatusKeyBindings.ACCESSORY_MODE as KeyMapping).keyConflictContext = KeyConflictContext.GUI
            }
        }
    }
}