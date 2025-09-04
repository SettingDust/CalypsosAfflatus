package settingdust.calypsos_afflatus.v1_20.item

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun onUnequip(stack: ItemStack, owner: LivingEntity) {
        owner.removeEffect(MobEffects.NIGHT_VISION)
    }

    override fun tick(stack: ItemStack, owner: LivingEntity) {
        if (stack.damageValue >= stack.maxDamage) {
            owner.removeEffect(MobEffects.NIGHT_VISION)
            return
        }
        owner.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, -1, 0, false, false))
        stack.hurt(1, owner.random, owner as? ServerPlayer)
    }
}