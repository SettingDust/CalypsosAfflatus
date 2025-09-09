package settingdust.calypsos_afflatus.fabric.v1_20.util

import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import settingdust.calypsos_afflatus.util.ContainerType

object TrinketsContainerType {
    const val TRINKET_TYPE = "trinket"

    init {
        ContainerType.ALL[TRINKET_TYPE] =
            object : ContainerType<Trinket> {
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

    data class Trinket(val group: String, val slot: String) : ContainerType.Data {
        override val type = TRINKET_TYPE

        override fun write(buf: FriendlyByteBuf) {
            buf.writeUtf(group)
            buf.writeUtf(slot)
        }
    }
}