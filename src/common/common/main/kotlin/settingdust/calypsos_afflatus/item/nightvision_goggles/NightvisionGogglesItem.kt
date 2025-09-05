package settingdust.calypsos_afflatus.item.nightvision_goggles

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.CalypsosAfflatusKeyBindings
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.mixin.AbstractContainerScreenAccessor

object NightvisionGogglesItem {
    val properties = Item.Properties().stacksTo(1).durability(1800)

    const val duration = 2 * 20
    const val amplifier = 0
    const val ambient = false
    const val visible = true

    @Suppress("SimplifyBooleanWithConstants")
    fun MobEffectInstance?.isFromAccessory() =
        this != null
                && amplifier == NightvisionGogglesItem.amplifier
                && endsWithin(NightvisionGogglesItem.duration)
                && isVisible == NightvisionGogglesItem.visible
                && isAmbient == NightvisionGogglesItem.ambient

    init {
        LoaderAdapter.onKeyPressedInScreen(CalypsosAfflatusKeyBindings.ACCESSORY_MODE) { screen ->
            if (screen !is AbstractContainerScreen<*>) return@onKeyPressedInScreen
            val hoveredSlot = (screen as AbstractContainerScreenAccessor).hoveredSlot
            if (hoveredSlot == null
                || !hoveredSlot.hasItem()
                || hoveredSlot.item.item !== CalypsosAfflatusItems.NIGHTVISION_GOGGLES
            ) return@onKeyPressedInScreen
            NightvisionGogglesNetworking.c2sSwitchMode(if (screen is CreativeModeInventoryScreen) hoveredSlot.containerSlot else hoveredSlot.index)
        }
    }
}