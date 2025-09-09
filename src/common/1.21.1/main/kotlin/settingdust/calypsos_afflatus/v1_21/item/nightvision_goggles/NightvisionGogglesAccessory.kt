package settingdust.calypsos_afflatus.v1_21.item.nightvision_goggles

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun tick(stack: ItemStack, owner: LivingEntity) {
        val player = owner as? ServerPlayer ?: return
        if (stack.mode == null) stack.mode = NightvisionGogglesModeHandler.Mode.AUTO
        if (stack.damageValue >= stack.maxDamage - 1 || !stack.mode!!.isEnabled(stack, owner)) {
            return
        }
        owner.addEffect(
            MobEffectInstance(
                MobEffects.NIGHT_VISION,
                NightvisionGogglesItem.duration,
                NightvisionGogglesItem.amplifier,
                NightvisionGogglesItem.ambient,
                NightvisionGogglesItem.visible,
                NightvisionGogglesItem.shouIcon
            )
        )
        stack.hurtAndBreak(1, player.serverLevel(), player) {}
    }
}