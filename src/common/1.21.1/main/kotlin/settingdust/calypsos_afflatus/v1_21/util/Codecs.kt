package settingdust.calypsos_afflatus.v1_21.util

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.util.ContainerType

object CalypsosAfflatusCodecs {
    @Suppress("EnumValuesSoftDeprecate")
    val NIGHTVISION_GOGGLES_MODE = StringRepresentable.fromEnum { NightvisionGogglesModeHandler.Mode.values() }
}

object CalypsosAfflatusStreamCodecs {
    val NIGHTVISION_GOGGLES_MODE = enumCodec<FriendlyByteBuf, NightvisionGogglesModeHandler.Mode>()
    val CONTAINER_TYPE_DATA: StreamCodec<FriendlyByteBuf, ContainerType.Data> =
        (ByteBufCodecs.STRING_UTF8 as StreamCodec<FriendlyByteBuf, String>).dispatch(
            { it.type },
            { key ->
                val serializer = ContainerType.ALL[key]!!.dataSerializer
                StreamCodec.of({ buf, data -> data.write(buf) }, serializer)
            })

    inline fun <B : FriendlyByteBuf, reified V : Enum<V>> enumCodec(): StreamCodec<B, V> {
        return object : StreamCodec<B, V> {
            override fun decode(buf: B): V {
                return buf.readEnum(V::class.java)
            }

            override fun encode(buf: B, value: V) {
                buf.writeEnum(value)
            }
        }
    }
}