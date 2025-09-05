package settingdust.calypsos_afflatus.item

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.util.AccessoryRenderer
import settingdust.calypsos_afflatus.util.AccessoryRenderer.Companion.transformToModelPart
import settingdust.calypsos_afflatus.util.ServiceLoaderUtil

object NightvisionGogglesItem {
    val properties = Item.Properties().stacksTo(1).durability(1800)

    const val duration = 2 * 20
    const val amplifier = 0
    const val ambient = false
    const val visible = true

    @Suppress("SimplifyBooleanWithConstants")
    fun MobEffectInstance?.isFromAccessory() =
        this != null
                && amplifier == this@NightvisionGogglesItem.amplifier
                && endsWithin(duration)
                && isVisible == this@NightvisionGogglesItem.visible
                && isAmbient == this@NightvisionGogglesItem.ambient
}

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