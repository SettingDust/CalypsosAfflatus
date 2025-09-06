package settingdust.calypsos_afflatus.fabric.util

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.LivingEntity
import settingdust.calypsos_afflatus.fabric.util.LivingTickCallback.Callback

object LivingTickCallback {
    @JvmField
    val EVENT =
        EventFactory.createArrayBacked(Callback::class.java) { listeners ->
            Callback {
                for (callback in listeners) {
                    callback.onLivingTick(it)
                }
            }
        }

    fun interface Callback {
        fun onLivingTick(entity: LivingEntity)
    }
}