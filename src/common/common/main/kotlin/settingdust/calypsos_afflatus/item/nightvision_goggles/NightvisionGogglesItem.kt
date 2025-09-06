package settingdust.calypsos_afflatus.item.nightvision_goggles

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusKeyBindings
import settingdust.calypsos_afflatus.CalypsosAfflatusSoundEvents
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler.Companion.mode
import settingdust.calypsos_afflatus.mixin.AbstractContainerScreenAccessor

object NightvisionGogglesItem {
    val properties = Item.Properties().stacksTo(1).durability(1800)

    const val duration = 2 * 20
    const val amplifier = 0
    const val ambient = false
    const val visible = false
    const val shouIcon = true

    private var expanded = false

    init {
        LoaderAdapter.onKeyPressedInScreen(CalypsosAfflatusKeyBindings.ACCESSORY_MODE) { screen ->
            if (screen !is AbstractContainerScreen<*>) return@onKeyPressedInScreen
            val hoveredSlot = (screen as AbstractContainerScreenAccessor).hoveredSlot
            if (hoveredSlot == null
                || !hoveredSlot.hasItem()
                || hoveredSlot.item.item !== CalypsosAfflatusItems.NIGHTVISION_GOGGLES
            ) return@onKeyPressedInScreen
            Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(CalypsosAfflatusSoundEvents.UI_MODE_SWITCH, 1f, 1f))
            NightvisionGogglesNetworking.c2sSwitchMode(if (screen is CreativeModeInventoryScreen) hoveredSlot.containerSlot else hoveredSlot.index)
        }

        LoaderAdapter.onLivingEntityTick { entity ->
            val stack = entity.getItemBySlot(EquipmentSlot.HEAD)
            if (!stack.`is`(CalypsosAfflatusItems.NIGHTVISION_GOGGLES)) return@onLivingEntityTick
            NightvisionGogglesAccessory.tick(stack, entity)
        }
    }

    @Suppress("SimplifyBooleanWithConstants")
    fun MobEffectInstance?.isFromAccessory() =
        this != null
                && amplifier == NightvisionGogglesItem.amplifier
                && endsWithin(NightvisionGogglesItem.duration)
                && isVisible == NightvisionGogglesItem.visible
                && isAmbient == NightvisionGogglesItem.ambient
                && showIcon() == NightvisionGogglesItem.shouIcon

    fun MutableList<Component>.appendTooltip(stack: ItemStack) {
        if (stack.mode == null) stack.mode = NightvisionGogglesModeHandler.Mode.AUTO
        val spiderEye = Component.translatable("item.minecraft.spider_eye").withStyle { it.withColor(0xC85A54) }
        add(
            Component.translatable(
                "item.calypsos_afflatus.nightvision_goggles.tooltip.description",
                Component.translatable("effect.minecraft.night_vision").withStyle { it.withColor(0x658963) },
                spiderEye
            )
        )
        val modes = NightvisionGogglesModeHandler.Mode.entries
            .map { mode ->
                Component.translatable("item.calypsos_afflatus.nightvision_goggles.mode.${mode.name.lowercase()}")
                    .withStyle { style ->
                        if (stack.mode == mode) style.withColor(mode.color) else style.withColor(
                            ChatFormatting.GRAY
                        )
                    }
            }.toTypedArray()
        add(
            Component.translatable(
                "item.calypsos_afflatus.nightvision_goggles.tooltip.mode",
                *modes,
                CalypsosAfflatusKeyBindings.ACCESSORY_MODE.translatedKeyMessage
            )
        )
        if (LoaderAdapter.isClient && !Screen.hasShiftDown()) {
            if (expanded) {
                Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(CalypsosAfflatusSoundEvents.UI_COLLAPSE, 1f, 1f))
            }
            expanded = false
            add(Component.translatable("tooltip.calypsos_afflatus.expand"))
        } else {
            if (LoaderAdapter.isClient && !expanded) {
                Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(CalypsosAfflatusSoundEvents.UI_EXPAND, 1f, 1f))
                expanded = true
            }
            add(Component.translatable("item.calypsos_afflatus.nightvision_goggles.tooltip.expand.0"))
            add(
                Component.translatable(
                    "item.calypsos_afflatus.nightvision_goggles.tooltip.expand.1",
                    spiderEye,
                    Component.translatable("item.minecraft.fermented_spider_eye").withStyle { it.withColor(0xD4696F) },
                    Component.translatable("block.minecraft.glowstone").withStyle { it.withColor(0xF4A460) }
                )
            )
            add(Component.translatable("item.calypsos_afflatus.nightvision_goggles.tooltip.expand.2"))
            add(Component.translatable("item.calypsos_afflatus.nightvision_goggles.tooltip.expand.3"))
        }
    }
}