package settingdust.calypsos_afflatus.item.nightvision_goggles

import net.minecraft.server.level.ServerPlayer
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface NightvisionGogglesNetworking {
    companion object : NightvisionGogglesNetworking by ServiceLoaderUtil.findService<NightvisionGogglesNetworking>() {
        fun handleSwitchMode(slotIndex: Int, sender: ServerPlayer) {
            val slot = sender.containerMenu.getSlot(slotIndex)
            val stack = slot.item
            if (stack.item === CalypsosAfflatusItems.NIGHTVISION_GOGGLES) {
                stack.mode =
                    NightvisionGogglesModeHandler.Mode.entries[
                        stack.mode?.ordinal?.let { (it - 1 + NightvisionGogglesModeHandler.Mode.entries.size) % NightvisionGogglesModeHandler.Mode.entries.size } ?: (NightvisionGogglesModeHandler.Mode.entries.size - 1)
                    ]
                slot.setChanged()
            }
        }
    }

    fun c2sSwitchMode(slot: Int)
}