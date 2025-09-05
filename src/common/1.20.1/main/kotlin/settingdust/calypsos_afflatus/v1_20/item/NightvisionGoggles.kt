package settingdust.calypsos_afflatus.v1_20.item

import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem.isFromAccessory

class NightvisionGogglesItem : Item(NightvisionGogglesItem.properties) {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        components: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        components.add(Component.translatable("item.calypsos_mobs.furnace_sprite.desc"))
        if (level == null || !level.isClientSide || Screen.hasShiftDown()) {
            repeat(5) {
                components.add(Component.translatable("item.calypsos_mobs.furnace_sprite.desc$it"))
            }
        }
    }
}

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun onUnequip(stack: ItemStack, owner: LivingEntity) {
        val effect = owner.getEffect(MobEffects.NIGHT_VISION)
        if (!effect.isFromAccessory()) return

        owner.removeEffect(MobEffects.NIGHT_VISION)
    }

    override fun tick(stack: ItemStack, owner: LivingEntity) {
        if (stack.damageValue >= stack.maxDamage) {
            owner.removeEffect(MobEffects.NIGHT_VISION)
            return
        }
        owner.addEffect(
            MobEffectInstance(
                MobEffects.NIGHT_VISION,
                NightvisionGogglesItem.duration,
                NightvisionGogglesItem.amplifier,
                NightvisionGogglesItem.ambient,
                NightvisionGogglesItem.visible
            )
        )
        stack.hurt(1, owner.random, owner as? ServerPlayer)
    }
}