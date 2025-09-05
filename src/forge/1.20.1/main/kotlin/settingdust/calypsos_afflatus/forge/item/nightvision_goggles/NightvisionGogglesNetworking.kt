package settingdust.calypsos_afflatus.forge.item.nightvision_goggles

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.forge.CalypsosAfflatusNetworking
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking
import java.util.function.Supplier

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    override fun c2sSwitchMode(slot: Int) {
        require(LoaderAdapter.isClient)
        CalypsosAfflatusNetworking.CHANNEL.sendToServer(C2SSwitchModePacket(slot))
    }
}

data class C2SSwitchModePacket(val slotIndex: Int) {
    companion object {
        fun decode(buf: FriendlyByteBuf): C2SSwitchModePacket {
            return C2SSwitchModePacket(buf.readVarInt())
        }

        fun handle(packet: C2SSwitchModePacket, context: Supplier<NetworkEvent.Context>) {
            context.get().enqueueWork {
                NightvisionGogglesNetworking.handleSwitchMode(packet.slotIndex, context.get().sender!!)
            }
        }
    }

    fun encode(buf: FriendlyByteBuf) {
        buf.writeVarInt(slotIndex)
    }
}