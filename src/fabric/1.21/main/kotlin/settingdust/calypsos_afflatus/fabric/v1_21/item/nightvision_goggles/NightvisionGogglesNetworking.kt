package settingdust.calypsos_afflatus.fabric.v1_21.item.nightvision_goggles

import dev.emi.trinkets.CreativeTrinketSlot
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.inventory.Slot
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.fabric.v1_21.util.TrinketsContainerType
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking
import settingdust.calypsos_afflatus.util.ContainerType
import settingdust.calypsos_afflatus.v1_21.util.CalypsosAfflatusStreamCodecs

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    override fun c2sSwitchMode(slot: Slot) {
        require(LoaderAdapter.isClient)
        requireNotNull(TrinketsContainerType)
        val index =
            if (slot is CreativeModeInventoryScreen.SlotWrapper) {
                slot.containerSlot
            } else {
                slot.index
            }
        val type = if (slot is CreativeTrinketSlot) {
            TrinketsContainerType.TRINKET_TYPE
        } else {
            ContainerType.NORMAL
        }
        val data = if (slot is CreativeTrinketSlot) {
            TrinketsContainerType.Trinket(slot.type.group, slot.type.name)
        } else {
            ContainerType.Data.Normal
        }
        ClientPlayNetworking.send(C2SSwitchModePacket(index, type, data))
    }
}

data class C2SSwitchModePacket(
    val slotIndex: Int,
    val containerType: String,
    val data: ContainerType.Data
) : CustomPacketPayload {
    companion object {
        val TYPE = CustomPacketPayload.Type<C2SSwitchModePacket>(CalypsosAfflatus.id("switch_mode"))
        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            C2SSwitchModePacket::slotIndex,
            ByteBufCodecs.STRING_UTF8,
            C2SSwitchModePacket::containerType,
            CalypsosAfflatusStreamCodecs.CONTAINER_TYPE_DATA,
            C2SSwitchModePacket::data,
            ::C2SSwitchModePacket
        )

        init {
            PayloadTypeRegistry.playC2S().register(TYPE, STREAM_CODEC)
            ServerPlayNetworking.registerGlobalReceiver(TYPE) { packet, context ->
                NightvisionGogglesNetworking.handleSwitchMode(
                    packet.slotIndex,
                    ContainerType.ALL.getValue(packet.containerType),
                    context.player(),
                    packet.data
                )
            }
        }
    }

    override fun type() = TYPE
}