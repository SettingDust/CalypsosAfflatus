package settingdust.calypsos_afflatus.item.nightvision_goggles

import net.minecraft.ChatFormatting
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface NightvisionGogglesModeHandler {
    companion object : NightvisionGogglesModeHandler by ServiceLoaderUtil.findService<NightvisionGogglesModeHandler>()

    enum class Mode(
        val isEnabled: (ItemStack, LivingEntity) -> Boolean,
        val color: ChatFormatting
    ) : StringRepresentable {
        AUTO({ _, entity ->
            entity.level().updateSkyBrightness()
            val brightness = entity.level().getMaxLocalRawBrightness(entity.blockPosition())
            brightness < 8
        }, ChatFormatting.GOLD),
        ON({ _, _ -> true }, ChatFormatting.GREEN),
        OFF({ _, _ -> false }, ChatFormatting.RED);

        override fun getSerializedName() = name.lowercase()
    }

    var ItemStack.mode: Mode?
}

