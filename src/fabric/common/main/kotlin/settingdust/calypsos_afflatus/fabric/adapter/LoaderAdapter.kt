package settingdust.calypsos_afflatus.fabric.adapter

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.fabric.util.ItemStackedOnOtherCallback
import settingdust.calypsos_afflatus.fabric.util.LivingTickCallback

class LoaderAdapter : LoaderAdapter {
    override val isClient = FabricLoader.getInstance().environmentType === EnvType.CLIENT

    override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)

    override fun <T : Item> T.creativeTab(key: ResourceKey<CreativeModeTab>) {
        ItemGroupEvents.modifyEntriesEvent(key).register { it.accept(this) }
    }

    override fun onKeyPressedInScreen(keyMapping: KeyMapping, callback: (screen: Screen) -> Unit) {
        ScreenEvents.AFTER_INIT.register { client, screen, scaledWidth, scaledHeight ->
            ScreenKeyboardEvents.afterKeyPress(screen).register { screen, key, scancode, modifiers ->
                if (keyMapping.matches(key, scancode)) callback(screen)
            }
        }
    }

    override fun onLivingEntityTick(callback: (entity: LivingEntity) -> Unit) {
        LivingTickCallback.EVENT.register(callback)
    }

    override fun onEquipmentChanged(callback: (entity: LivingEntity, slot: EquipmentSlot, from: ItemStack, to: ItemStack) -> Unit) {
        ServerEntityEvents.EQUIPMENT_CHANGE.register { livingEntity, equipmentSlot, previousStack, currentStack ->
            callback(livingEntity, equipmentSlot, previousStack, currentStack)
        }
    }

    override fun onItemStackedOnOther(callback: (player: Player, carriedItem: ItemStack, stackedOnItem: ItemStack, slot: Slot, clickAction: ClickAction) -> Boolean) {
        ItemStackedOnOtherCallback.EVENT.register { carriedItem, stackedOnItem, slot, clickAction, player ->
            callback(player, carriedItem, stackedOnItem, slot, clickAction)
        }
    }
}