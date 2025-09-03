package settingdust.calypsos_afflatus.v1_20.item

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun tick(stack: ItemStack, owner: LivingEntity) {
        if (stack.damageValue >= stack.maxDamage) return
        if (owner.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 2))) {
            stack.hurt(1, owner.random, owner as? ServerPlayer)
        }
    }
}