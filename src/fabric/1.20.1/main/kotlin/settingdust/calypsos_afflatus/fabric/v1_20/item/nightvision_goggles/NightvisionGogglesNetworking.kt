package settingdust.calypsos_afflatus.fabric.v1_20.item.nightvision_goggles

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    override fun c2sSwitchMode(slot: Int) {
        require(LoaderAdapter.isClient)
        ClientPlayNetworking.send(C2SSwitchModePacket(slot))
    }
}

data class C2SSwitchModePacket(val slotIndex: Int) : FabricPacket {
    companion object {
        val TYPE = PacketType.create(
            CalypsosAfflatus.id("switch_mode"),
        ) { buf -> C2SSwitchModePacket(buf.readVarInt()) }

        init {
            ServerPlayNetworking.registerGlobalReceiver(TYPE) { packet, player, sender ->
                NightvisionGogglesNetworking.handleSwitchMode(packet.slotIndex, player)
            }
        }
    }

    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(slotIndex)
    }

    override fun getType() = TYPE
}