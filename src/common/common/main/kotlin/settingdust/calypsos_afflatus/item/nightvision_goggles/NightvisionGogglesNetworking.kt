package settingdust.calypsos_afflatus.item.nightvision_goggles

import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface NightvisionGogglesNetworking {
    companion object : NightvisionGogglesNetworking by ServiceLoaderUtil.findService<NightvisionGogglesNetworking>() {
        fun handleSwitchMode(
            slotIndex: Int,
            containerType: ContainerType<*>,
            sender: ServerPlayer,
            data: ContainerType.Data
        ) {
            val stack = (containerType as ContainerType<ContainerType.Data>).getItem(slotIndex, sender, data)
            if (stack.item === CalypsosAfflatusItems.NIGHTVISION_GOGGLES) {
                stack.mode =
                    NightvisionGogglesModeHandler.Mode.entries[
                        stack.mode?.ordinal?.let { (it - 1 + NightvisionGogglesModeHandler.Mode.entries.size) % NightvisionGogglesModeHandler.Mode.entries.size }
                            ?: (NightvisionGogglesModeHandler.Mode.entries.size - 1)
                    ]
            }
        }
    }

    fun c2sSwitchMode(slot: Slot)

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
}