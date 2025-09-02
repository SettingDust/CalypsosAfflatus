package settingdust.calypsos_afflatus.forge.item

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

object NightvisionGogglesCurios : ICurioItem {
    override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
        if (stack.damageValue >= stack.maxDamage) return
        if (slotContext.entity.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 2))) {
            stack.hurt(1, slotContext.entity.random, slotContext.entity as? ServerPlayer)
        }
    }
}