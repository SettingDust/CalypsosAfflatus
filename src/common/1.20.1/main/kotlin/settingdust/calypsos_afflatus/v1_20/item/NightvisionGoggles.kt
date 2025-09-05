package settingdust.calypsos_afflatus.v1_20.item

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.NightvisionGogglesAdapter
import settingdust.calypsos_afflatus.item.NightvisionGogglesAdapter.Companion.mode
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem.isFromAccessory

class NightvisionGogglesAdapter : NightvisionGogglesAdapter {
    companion object {
        private const val MODE_TAG_KEY = "${CalypsosAfflatus.ID}:mode"
    }

    override var ItemStack.mode: NightvisionGogglesAdapter.Mode?
        get() {
            if (!orCreateTag.contains(MODE_TAG_KEY)) return null
            return NightvisionGogglesAdapter.Mode.entries[orCreateTag.getByte(MODE_TAG_KEY).toInt()]
        }
        set(value) {
            if (value != null)
                orCreateTag.putByte(MODE_TAG_KEY, value.ordinal.toByte())
        }
}

class NightvisionGogglesItem : Item(NightvisionGogglesItem.properties), Equipable {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        components: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        if (stack.mode == null) stack.mode = NightvisionGogglesAdapter.Mode.AUTO
        components.add(
            Component.translatable(
                "tooltip.calypsos_afflatus.nightvision_goggles.mode",
                Component.translatable("tooltip.calypsos_afflatus.nightvision_goggles.mode.${stack.mode!!.name.lowercase()}")
            )
        )
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        stack.mode =
            NightvisionGogglesAdapter.Mode.entries[
                stack.mode?.ordinal?.let { (it + 1) % NightvisionGogglesAdapter.Mode.entries.size } ?: 0
            ]
        return InteractionResultHolder.success(stack)
    }

    override fun getEquipmentSlot() = EquipmentSlot.HEAD
}

class NightvisionGogglesAccessory : NightvisionGogglesAccessory {
    override fun onUnequip(stack: ItemStack, owner: LivingEntity) {
        val effect = owner.getEffect(MobEffects.NIGHT_VISION)
        if (!effect.isFromAccessory()) return

        owner.removeEffect(MobEffects.NIGHT_VISION)
    }

    override fun tick(stack: ItemStack, owner: LivingEntity) {
        if (stack.mode == null) stack.mode = NightvisionGogglesAdapter.Mode.AUTO
        if (stack.damageValue >= stack.maxDamage && !stack.mode!!.isEnabled(stack, owner)) {
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
                NightvisionGogglesItem.visible
            )
        )
        stack.hurt(1, owner.random, owner as? ServerPlayer)
    }
}