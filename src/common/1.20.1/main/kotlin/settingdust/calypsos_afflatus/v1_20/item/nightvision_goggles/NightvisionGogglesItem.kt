package settingdust.calypsos_afflatus.v1_20.item.nightvision_goggles

import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesAccessory
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem.appendTooltip
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode

class NightvisionGogglesItem : Item(NightvisionGogglesItem.properties), Equipable {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        components: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        components.appendTooltip(stack)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        stack.mode =
            NightvisionGogglesModeHandler.Mode.entries[
                stack.mode?.ordinal?.let { (it + 1) % NightvisionGogglesModeHandler.Mode.entries.size } ?: 0
            ]
        return InteractionResultHolder.success(stack)
    }

    override fun getEquipmentSlot() = EquipmentSlot.HEAD

    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        if (entity !is ServerPlayer) return
        val inventory = entity.inventory
        if (!(slotId >= inventory.items.size && slotId < inventory.items.size + inventory.armor.size)) return
        NightvisionGogglesAccessory.tick(stack, entity)
    }
}