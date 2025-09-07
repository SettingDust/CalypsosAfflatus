package settingdust.calypsos_afflatus.forge.item.nightvision_goggles

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.inventory.Slot
import net.minecraftforge.network.NetworkEvent
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.forge.CalypsosAfflatusNetworking
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking
import java.util.function.Supplier

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    override fun c2sSwitchMode(slot: Slot) {
        require(LoaderAdapter.isClient)
        val index =
            if (slot is CreativeModeInventoryScreen.SlotWrapper) {
                slot.containerSlot
            } else {
                slot.index
            }
        CalypsosAfflatusNetworking.CHANNEL.sendToServer(
            C2SSwitchModePacket(
                index,
                NightvisionGogglesNetworking.ContainerType.NORMAL,
                NightvisionGogglesNetworking.ContainerType.Data.Normal
            )
        )
    }
}

data class C2SSwitchModePacket(
    val slotIndex: Int,
    val containerType: String,
    val data: NightvisionGogglesNetworking.ContainerType.Data
) {
    companion object {
        fun decode(buf: FriendlyByteBuf): C2SSwitchModePacket {
            return C2SSwitchModePacket(buf.readVarInt(), buf.readUtf(), buf)
        }

        fun handle(packet: C2SSwitchModePacket, context: Supplier<NetworkEvent.Context>) {
            context.get().enqueueWork {
                NightvisionGogglesNetworking.handleSwitchMode(
                    packet.slotIndex,
                    NightvisionGogglesNetworking.ContainerType.ALL.getValue(packet.containerType),
                    context.get().sender!!,
                    packet.data
                )
            }
        }
    }

    constructor(slotIndex: Int, containerType: String, buf: FriendlyByteBuf) : this(
        slotIndex,
        containerType,
        NightvisionGogglesNetworking.ContainerType.ALL[containerType]!!.dataSerializer(buf)
    )

    fun encode(buf: FriendlyByteBuf) {
        buf.writeVarInt(slotIndex)
        buf.writeUtf(containerType)
    }
}