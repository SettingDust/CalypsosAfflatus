package settingdust.calypsos_afflatus.v1_20.item.nightvision_goggles

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode

class NightvisionGogglesItem : Item(NightvisionGogglesItem.properties), Equipable {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        components: MutableList<Component>,
        isAdvanced: TooltipFlag
    ) {
        if (stack.mode == null) stack.mode = NightvisionGogglesModeHandler.Mode.AUTO
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
            NightvisionGogglesModeHandler.Mode.entries[
                stack.mode?.ordinal?.let { (it + 1) % NightvisionGogglesModeHandler.Mode.entries.size } ?: 0
            ]
        return InteractionResultHolder.success(stack)
    }

    override fun getEquipmentSlot() = EquipmentSlot.HEAD
}