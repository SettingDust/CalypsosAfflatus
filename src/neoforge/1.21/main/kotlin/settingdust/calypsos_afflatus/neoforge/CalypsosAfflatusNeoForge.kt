package settingdust.calypsos_afflatus.neoforge

import net.minecraft.client.KeyMapping
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.settings.KeyConflictContext
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.registries.RegisterEvent
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusKeyBindings
import settingdust.calypsos_afflatus.CalypsosAfflatusSoundEvents
import settingdust.calypsos_afflatus.adapter.Entrypoint
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking
import settingdust.calypsos_afflatus.neoforge.item.nightvision_goggles.C2SSwitchModePacket
import settingdust.calypsos_afflatus.util.ContainerType
import settingdust.calypsos_afflatus.v1_21.CalypsosAfflatusDataComponents
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(CalypsosAfflatus.ID)
object CalypsosAfflatusNeoForge {
    init {
        requireNotNull(CalypsosAfflatus)
        Entrypoint.construct()
        MOD_BUS.apply {
            addListener<FMLCommonSetupEvent> {
                Entrypoint.init()
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

                    Registries.DATA_COMPONENT_TYPE -> CalypsosAfflatusDataComponents.registerDataComponents { id, value ->
                        event.register(Registries.DATA_COMPONENT_TYPE, id) { value }
                    }
                }
            }
            addListener<RegisterKeyMappingsEvent> { event ->
                CalypsosAfflatusKeyBindings.registerKeyBindings { event.register(it) }
                (CalypsosAfflatusKeyBindings.ACCESSORY_MODE as KeyMapping).keyConflictContext = KeyConflictContext.GUI
            }
            MOD_BUS.addListener<RegisterPayloadHandlersEvent> { event ->
                val registrar = event.registrar("1")
                registrar.playToServer(C2SSwitchModePacket.TYPE, C2SSwitchModePacket.STREAM_CODEC) { packet, context ->
                    NightvisionGogglesNetworking.handleSwitchMode(
                        packet.slotIndex,
                        ContainerType.ALL.getValue(packet.containerType),
                        context.player() as ServerPlayer,
                        packet.data
                    )
                }
            }
        }
    }
}