package settingdust.calypsos_afflatus.adapter

import net.minecraft.client.KeyMapping
import net.minecraft.client.gui.screens.Screen
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface LoaderAdapter {
    companion object : LoaderAdapter by ServiceLoaderUtil.findService()

    val isClient: Boolean

    fun isModLoaded(modId: String): Boolean

    fun <T : Item> T.creativeTab(key: ResourceKey<CreativeModeTab>)

    fun onKeyPressedInScreen(key: KeyMapping, callback: (screen: Screen) -> Unit)

    fun onLivingEntityTick(callback: (entity: LivingEntity) -> Unit)

    fun onEquipmentChanged(callback: (entity: LivingEntity, slot: EquipmentSlot, from: ItemStack, to: ItemStack) -> Unit)
}