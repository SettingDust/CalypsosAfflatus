package settingdust.calypsos_afflatus.forge.adapter

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.adapter.LoaderAdapter
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory
import top.theillusivec4.curios.api.CuriosApi
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.client.CuriosRendererRegistry
import top.theillusivec4.curios.api.client.ICurioRenderer
import top.theillusivec4.curios.api.type.capability.ICurioItem

class CuriosAccessoryAdapter : AccessoryAdapter {
    object Renderer : ICurioRenderer {
        override fun <T : LivingEntity, M : EntityModel<T>> render(
            stack: ItemStack,
            slotContext: SlotContext,
            poseStack: PoseStack,
            renderLayerParent: RenderLayerParent<T, M>,
            multiBufferSource: MultiBufferSource,
            light: Int,
            limbSwing: Float,
            limbSwingAmount: Float,
            partialTicks: Float,
            ageInTicks: Float,
            netHeadYaw: Float,
            headPitch: Float
        ) {
            settingdust.calypsos_afflatus.util.AccessoryRenderer.render(
                stack,
                slotContext.entity(),
                poseStack,
                multiBufferSource,
                light
            )
        }
    }

    override val modId = CuriosApi.MODID
    override fun init() {
        CuriosApi.registerCurio(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, object : ICurioItem {
            override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
                NightvisionGogglesAccessory.tick(stack, slotContext.entity)
            }
        })

        if (LoaderAdapter.isClient) {
            CuriosRendererRegistry.register(CalypsosAfflatusItems.NIGHTVISION_GOGGLES) { Renderer }
        }
    }
}