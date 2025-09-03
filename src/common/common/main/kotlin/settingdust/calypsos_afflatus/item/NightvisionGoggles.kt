package settingdust.calypsos_afflatus.item

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

class NightvisionGogglesItem : Item(Properties().stacksTo(1).durability(1800)) {
}

interface NightvisionGogglesAccessory {
    companion object : NightvisionGogglesAccessory by ServiceLoaderUtil.findService<NightvisionGogglesAccessory>()

    fun tick(stack: ItemStack, owner: LivingEntity)
}