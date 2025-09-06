package settingdust.calypsos_afflatus.adapter

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface AccessoryIntegration {
    companion object : AccessoryIntegration {
        private val services by lazy {
            ServiceLoaderUtil
                .findServices<AccessoryIntegration>(required = false)
                .filter { LoaderAdapter.isModLoaded(it.modId) }
        }
        override val modId: String
            get() = throw UnsupportedOperationException()

        override fun init() {
            for (adapter in services) {
                adapter.init()
            }
        }

        override fun getEquipped(entity: LivingEntity, item: Item): ItemStack? {
            for (slot in EquipmentSlot.entries) {
                val stack = entity.getItemBySlot(slot)
                if (stack.`is`(item)) {
                    return stack
                }
            }
            for (adapter in services) {
                val equipped = adapter.getEquipped(entity, item)
                if (equipped != null) return equipped
            }
            return null
        }
    }

    val modId: String

    fun init()

    fun getEquipped(entity: LivingEntity, item: Item): ItemStack?
}