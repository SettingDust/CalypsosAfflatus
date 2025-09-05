package settingdust.calypsos_afflatus.item.nightvision_goggles

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface NightvisionGogglesModeHandler {
    companion object : NightvisionGogglesModeHandler by ServiceLoaderUtil.findService<NightvisionGogglesModeHandler>()

    enum class Mode(val isEnabled: (ItemStack, LivingEntity) -> Boolean) {
        AUTO({ _, entity ->
            val brightness = entity.level().getRawBrightness(entity.blockPosition(), 0)
            brightness >= 8
        }),
        ON({ _, _ -> true }),
        OFF({ _, _ -> false });
    }

    var ItemStack.mode: Mode?
}

