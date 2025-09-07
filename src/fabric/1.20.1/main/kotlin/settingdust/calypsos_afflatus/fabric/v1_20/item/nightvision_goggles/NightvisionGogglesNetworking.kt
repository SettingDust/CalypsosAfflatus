package settingdust.calypsos_afflatus.fabric.v1_20.item.nightvision_goggles

import dev.emi.trinkets.TrinketSlot
import dev.emi.trinkets.api.TrinketsApi
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.FabricPacket
import net.fabricmc.fabric.api.networking.v1.PacketType
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.Slot
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesNetworking

class NightvisionGogglesNetworking : NightvisionGogglesNetworking {
    companion object {
        const val TRINKET_TYPE = "trinket"

        init {
            NightvisionGogglesNetworking.ContainerType.ALL[TRINKET_TYPE] =
                object : NightvisionGogglesNetworking.ContainerType<Trinket> {
                    override val dataSerializer: (FriendlyByteBuf) -> Trinket = { Trinket(it.readUtf(), it.readUtf()) }

                    override fun getItem(
                        slotIndex: Int,
                        sender: ServerPlayer,
                        data: Trinket
                    ) =
                        TrinketsApi.getTrinketComponent(sender).orElseThrow()
                            .inventory
                            .getValue(data.group)
                            .getValue(data.slot)
                            .getItem(slotIndex)
                }
        }
    }

    data class Trinket(val group: String, val slot: String) : NightvisionGogglesNetworking.ContainerType.Data {
        override fun write(buf: FriendlyByteBuf) {
            buf.writeUtf(group)
            buf.writeUtf(slot)
        }
    }

    override fun c2sSwitchMode(slot: Slot) {
        require(LoaderAdapter.isClient)
        val index =
            if (slot is CreativeModeInventoryScreen.SlotWrapper) {
                slot.containerSlot
            } else {
                slot.index
            }
        val type = if (slot is TrinketSlot) {
            TRINKET_TYPE
        } else {
            NightvisionGogglesNetworking.ContainerType.NORMAL
        }
        val data = if (slot is TrinketSlot) {
            Trinket(slot.type.group, slot.type.name)
        } else {
            NightvisionGogglesNetworking.ContainerType.Data.Normal
        }
        ClientPlayNetworking.send(C2SSwitchModePacket(index, type, data))
    }
}

data class C2SSwitchModePacket(
    val slotIndex: Int,
    val containerType: String,
    val data: NightvisionGogglesNetworking.ContainerType.Data
) : FabricPacket {
    companion object {
        val TYPE = PacketType.create(CalypsosAfflatus.id("switch_mode")) { buf ->
            C2SSwitchModePacket(buf.readVarInt(), buf.readUtf(), buf)
        }

        init {
            ServerPlayNetworking.registerGlobalReceiver(TYPE) { packet, player, _ ->
                NightvisionGogglesNetworking.handleSwitchMode(
                    packet.slotIndex,
                    NightvisionGogglesNetworking.ContainerType.ALL.getValue(packet.containerType),
                    player,
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

    override fun write(buf: FriendlyByteBuf) {
        buf.writeVarInt(slotIndex)
        buf.writeUtf(containerType)
        data.write(buf)
    }

    override fun getType() = TYPE
}