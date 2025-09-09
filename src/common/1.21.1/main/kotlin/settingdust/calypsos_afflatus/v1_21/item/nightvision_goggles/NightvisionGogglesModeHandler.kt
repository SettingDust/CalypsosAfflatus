package settingdust.calypsos_afflatus.v1_21.item.nightvision_goggles

import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.v1_21.CalypsosAfflatusDataComponents

class NightvisionGogglesModeHandler : NightvisionGogglesModeHandler {
    override var ItemStack.mode: NightvisionGogglesModeHandler.Mode?
        get() {
            val mode = get(CalypsosAfflatusDataComponents.MODE) ?: return null
            return mode
        }
        set(value) {
            if (value != null) {
                set(CalypsosAfflatusDataComponents.MODE, value)
            }
        }
}

