package settingdust.calypsos_afflatus.item

import com.mojang.blaze3d.vertex.PoseStack
import io.wispforest.accessories.api.Accessory
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.AccessoryRenderer
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

class NightvisionGogglesItem : Item(Properties().stacksTo(1).durability(1800))

interface NightvisionGogglesAccessory {
    companion object :
        NightvisionGogglesAccessory by ServiceLoaderUtil.findService<NightvisionGogglesAccessory>(),
        AccessoryRenderer {

        override fun render(
            stack: ItemStack,
            owner: LivingEntity,
            poseStack: PoseStack,
            buffer: MultiBufferSource,
            light: Int
        ) {
            Minecraft.getInstance().itemRenderer.renderStatic(
                owner,
                stack,
                ItemDisplayContext.HEAD,
                false,
                poseStack,
                buffer,
                owner.level(),
                light,
                OverlayTexture.NO_OVERLAY,
                0
            )
        }
    }

    object Accessories : Accessory {
        override fun tick(stack: ItemStack, slot: SlotReference) {
            NightvisionGogglesAccessory.tick(stack, slot.entity())
        }
    }

    fun tick(stack: ItemStack, owner: LivingEntity)
}