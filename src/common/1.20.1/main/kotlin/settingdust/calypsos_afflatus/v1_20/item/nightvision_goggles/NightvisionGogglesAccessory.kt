package settingdust.calypsos_afflatus.v1_20.item.nightvision_goggles

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem.isFromAccessory
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun onUnequip(stack: ItemStack, owner: LivingEntity) {
        val effect = owner.getEffect(MobEffects.NIGHT_VISION)
        if (!effect.isFromAccessory()) return

        owner.removeEffect(MobEffects.NIGHT_VISION)
    }

    override fun tick(stack: ItemStack, owner: LivingEntity) {
        if (stack.mode == null) stack.mode = NightvisionGogglesModeHandler.Mode.AUTO
        if (stack.damageValue >= stack.maxDamage || !stack.mode!!.isEnabled(stack, owner)) {
            val effect = owner.getEffect(MobEffects.NIGHT_VISION)
            if (effect.isFromAccessory()) owner.removeEffect(MobEffects.NIGHT_VISION)
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
        stack.hurt(1, owner.random, owner as? ServerPlayer)
    }
}