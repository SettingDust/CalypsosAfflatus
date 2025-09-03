package settingdust.calypsos_afflatus.adapter.impl

import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory

class AccessoriesAccessoryAdapter : AccessoryAdapter {
    override val modId = "accessories"
    override fun init() {
        AccessoriesAPI.registerAccessory(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, object : Accessory {
            override fun tick(stack: ItemStack, slot: SlotReference) {
                NightvisionGogglesAccessory.tick(stack, slot.entity())
            }

            override fun canEquip(stack: ItemStack, slot: SlotReference): Boolean {
                return true
            }
        })
    }
}