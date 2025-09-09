package settingdust.calypsos_afflatus.fabric.v1_20.item.nightvision_goggles

import dev.emi.trinkets.TrinketSlot
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.Slot
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.fabric.v1_20.util.TrinketsContainerType
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking
import settingdust.calypsos_afflatus.util.ContainerType

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    override fun c2sSwitchMode(slot: Slot) {
        require(LoaderAdapter.isClient)
        val index =
            if (slot is CreativeModeInventoryScreen.SlotWrapper) {
                slot.containerSlot
            } else {
                slot.index
            }
        val type = if (slot is TrinketSlot) {
            TrinketsContainerType.TRINKET_TYPE
        } else {
            ContainerType.NORMAL
        }
        val data = if (slot is TrinketSlot) {
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
) : FabricPacket {
    companion object {
        val TYPE = PacketType.create(CalypsosAfflatus.id("switch_mode")) { buf ->
            C2SSwitchModePacket(buf.readVarInt(), buf.readUtf(), buf)
        }

        init {
            ServerPlayNetworking.registerGlobalReceiver(TYPE) { packet, player, _ ->
                NightvisionGogglesNetworking.handleSwitchMode(
                    packet.slotIndex,
                    ContainerType.ALL.getValue(packet.containerType),
                    player,
                    packet.data
                )
            }
        }
    }

    constructor(slotIndex: Int, containerType: String, buf: FriendlyByteBuf) : this(
        slotIndex,
        containerType,
        ContainerType.ALL[containerType]!!.dataSerializer(buf)
    )

    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(slotIndex)
        buf.writeUtf(containerType)
        data.write(buf)
    }

    override fun getType() = TYPE
}