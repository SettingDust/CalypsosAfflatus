package settingdust.calypsos_afflatus.v1_20.adapter

import net.minecraft.resources.ResourceLocation
import settingdust.calypsos_afflatus.adapter.MinecraftAdapter

class MinecraftAdapter : MinecraftAdapter {
    override fun id(namespace: String, path: String) = ResourceLocation(namespace, path)
}