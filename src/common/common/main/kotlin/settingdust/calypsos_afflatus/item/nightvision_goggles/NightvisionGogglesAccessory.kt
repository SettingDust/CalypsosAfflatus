package settingdust.calypsos_afflatus.item.nightvision_goggles

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.AccessoryRenderer
import settingdust.calypsos_afflatus.util.AccessoryRenderer.Companion.transformToModelPart
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

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
            val entityRenderer =
                Minecraft.getInstance().entityRenderDispatcher.getRenderer(owner) as? LivingEntityRenderer<*, *>
                    ?: return
            val entityModel = entityRenderer.model as? HumanoidModel<*> ?: return
            poseStack.pushPose()
            poseStack.transformToModelPart(entityModel.head)
            Minecraft.getInstance().itemRenderer.renderStatic(
                owner,
                stack,
                ItemDisplayContext.FIXED,
                false,
                poseStack,
                buffer,
                owner.level(),
                light,
                OverlayTexture.NO_OVERLAY,
                0
            )
            poseStack.popPose()
        }
    }

    fun onUnequip(stack: ItemStack, owner: LivingEntity)

    fun tick(stack: ItemStack, owner: LivingEntity)
}