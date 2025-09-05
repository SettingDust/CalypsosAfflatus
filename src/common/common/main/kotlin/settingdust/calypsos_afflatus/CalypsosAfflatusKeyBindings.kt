package settingdust.calypsos_afflatus

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import org.lwjgl.glfw.GLFW

object CalypsosAfflatusKeyBindings {
    val ACCESSORY_MODE = KeyMapping(
        "key.calypsos_afflatus.accessory_mode",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_P,
        "key.categories.calypsos_afflatus"
    )

    fun registerKeyBindings(register: (KeyMapping) -> Unit) {
        register(ACCESSORY_MODE)
    }
}