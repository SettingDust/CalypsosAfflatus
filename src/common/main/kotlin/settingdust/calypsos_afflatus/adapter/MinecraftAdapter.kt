package settingdust.calypsos_afflatus.adapter

import net.minecraft.resources.ResourceLocation
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

interface MinecraftAdapter {
    companion object : MinecraftAdapter by ServiceLoaderUtil.findService()

    fun id(namespace: String, path: String): ResourceLocation
}