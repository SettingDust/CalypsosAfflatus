package settingdust.calypsos_afflatus.fabric.v1_21.util

import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import settingdust.calypsos_afflatus.util.ContainerType

object TrinketsContainerType {
    const val TRINKET_TYPE = "trinket"

    init {
        ContainerType.ALL[TRINKET_TYPE] =
            object : ContainerType<Trinket> {
                override val dataSerializer: (FriendlyByteBuf) -> Trinket = { Trinket.STREAM_CODEC.decode(it) }

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
        companion object {
            val STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                Trinket::group,
                ByteBufCodecs.STRING_UTF8,
                Trinket::slot,
                ::Trinket
            )
        }

        override fun write(buf: FriendlyByteBuf) {
            STREAM_CODEC.encode(buf, this)
        }
    }
}