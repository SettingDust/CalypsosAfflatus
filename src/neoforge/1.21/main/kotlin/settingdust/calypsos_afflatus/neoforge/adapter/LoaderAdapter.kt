package settingdust.calypsos_afflatus.neoforge.adapter

import com.mojang.blaze3d.platform.InputConstants
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
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.fml.loading.LoadingModList
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

class LoaderAdapter : LoaderAdapter {
    override val isClient: Boolean
        get() = FMLLoader.getDist().isClient

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null

    override fun <T : Item> T.creativeTab(key: ResourceKey<CreativeModeTab>) {
        MOD_BUS.addListener<BuildCreativeModeTabContentsEvent> { event ->
            if (event.tabKey == key) event.accept(this)
        }
    }

    override fun onKeyPressedInScreen(key: KeyMapping, callback: (screen: Screen) -> Unit) {
        NeoForge.EVENT_BUS.addListener<ScreenEvent.KeyPressed.Post> { event ->
            if (key.isActiveAndMatches(InputConstants.getKey(event.keyCode, event.scanCode))) {
                callback(event.screen)
            }
        }
    }

    override fun onLivingEntityTick(callback: (entity: LivingEntity) -> Unit) {
        NeoForge.EVENT_BUS.addListener<EntityTickEvent.Post> { event ->
            if (event.entity !is LivingEntity) return@addListener
            callback(event.entity as LivingEntity)
        }
    }

    override fun onEquipmentChanged(callback: (entity: LivingEntity, slot: EquipmentSlot, from: ItemStack, to: ItemStack) -> Unit) {
        NeoForge.EVENT_BUS.addListener<LivingEquipmentChangeEvent> { event ->
            callback(event.entity, event.slot, event.from, event.to)
        }
    }

    override fun onItemStackedOnOther(callback: (player: Player, carried: ItemStack, target: ItemStack, slot: Slot, clickAction: ClickAction) -> Boolean) {
        NeoForge.EVENT_BUS.addListener<ItemStackedOnOtherEvent> { event ->
            if (callback(event.player, event.carriedItem, event.stackedOnItem, event.slot, event.clickAction)) {
                event.isCanceled = true
            }
        }
    }
}