package settingdust.calypsos_afflatus.adapter

import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
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

        override fun isEquipped(entity: LivingEntity, item: Item): Boolean {
            for (slot in EquipmentSlot.entries) {
                if (entity.getItemBySlot(slot).`is`(item)) {
                    return true
                }
            }
            for (adapter in services) {
                if (adapter.isEquipped(entity, item)) return true
            }
            return false
        }
    }

    val modId: String

    fun init()

    fun isEquipped(entity: LivingEntity, item: Item): Boolean
}