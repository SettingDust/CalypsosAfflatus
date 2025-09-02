package settingdust.calypsos_afflatus.forge.adapter

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent
import net.minecraftforge.fml.loading.FMLLoader
import net.minecraftforge.fml.loading.LoadingModList
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import thedarkcolour.kotlinforforge.forge.MOD_BUS

class LoaderAdapter : LoaderAdapter {
    override val isClient: Boolean
        get() = FMLLoader.getDist().isClient

    override fun isModLoaded(modId: String) = LoadingModList.get().getModFileById(modId) != null

    override fun <T : Item> T.creativeTab(key: ResourceKey<CreativeModeTab>) {
        MOD_BUS.addListener<BuildCreativeModeTabContentsEvent> { event ->
            if (event.tabKey == key) event.accept(this)
        }
    }
}