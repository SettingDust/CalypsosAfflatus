package settingdust.calypsos_afflatus.fabric.util

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.fabric.util.ItemStackedOnOtherCallback.Callback

object ItemStackedOnOtherCallback {
    @JvmField
    val EVENT =
        EventFactory.createArrayBacked(Callback::class.java) { listeners ->
            Callback { carriedItem, stackedOnItem, slot, clickAction, player ->
                var result = false
                for (callback in listeners) {
                    if (callback.onItemStackedOnOther(
                            carriedItem,
                            stackedOnItem,
                            slot,
                            clickAction,
                            player
                        )
                    ) result = true
                }
                result
            }
        }

    fun interface Callback {
        fun onItemStackedOnOther(
            carriedItem: ItemStack,
            stackedOnItem: ItemStack,
            slot: Slot,
            clickAction: ClickAction,
            player: Player
        ): Boolean
    }
}