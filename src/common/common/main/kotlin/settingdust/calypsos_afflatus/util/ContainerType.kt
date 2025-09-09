package settingdust.calypsos_afflatus.util

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

interface ContainerType<T : ContainerType.Data> {
    companion object {
        val ALL = Object2ReferenceOpenHashMap<String, ContainerType<*>>()

        val NORMAL = "normal"

        init {
            ALL[NORMAL] = object : ContainerType<Data.Normal> {
                override val dataSerializer: (FriendlyByteBuf) -> Data.Normal = { Data.Normal }

                override fun getItem(slotIndex: Int, sender: ServerPlayer, data: Data.Normal) =
                    sender.containerMenu.getSlot(slotIndex).item
            }
        }
    }

    val dataSerializer: (FriendlyByteBuf) -> T

    fun getItem(slotIndex: Int, sender: ServerPlayer, data: T): ItemStack

    interface Data {
        data object Normal : Data {
            override fun write(buf: FriendlyByteBuf) {
            }
        }

        fun write(buf: FriendlyByteBuf)
    }
}