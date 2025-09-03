package settingdust.calypsos_afflatus.forge.adapter

import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

class CuriosAccessoryAdapter : AccessoryAdapter {
    override val modId = CuriosApi.MODID
    override fun init() {
        CuriosApi.registerCurio(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, object : ICurioItem {
            override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
                NightvisionGogglesAccessory.tick(stack, slotContext.entity)
            }
        })
    }
}