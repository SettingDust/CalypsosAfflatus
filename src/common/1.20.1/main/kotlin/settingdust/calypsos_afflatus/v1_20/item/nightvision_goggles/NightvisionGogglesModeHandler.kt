package settingdust.calypsos_afflatus.v1_20.item.nightvision_goggles

import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler

class NightvisionGogglesModeHandler : NightvisionGogglesModeHandler {
    companion object {
        private const val MODE_TAG_KEY = "${CalypsosAfflatus.ID}:mode"
    }

    override var ItemStack.mode: NightvisionGogglesModeHandler.Mode?
        get() {
            if (!orCreateTag.contains(MODE_TAG_KEY)) return null
            return NightvisionGogglesModeHandler.Mode.entries[orCreateTag.getByte(MODE_TAG_KEY).toInt()]
        }
        set(value) {
            if (value != null)
                orCreateTag.putByte(MODE_TAG_KEY, value.ordinal.toByte())
        }
}

