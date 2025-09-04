package settingdust.calypsos_afflatus.adapter.impl

import com.mojang.blaze3d.vertex.PoseStack
import io.wispforest.accessories.api.AccessoriesAPI
import io.wispforest.accessories.api.client.AccessoriesRendererRegistry
import io.wispforest.accessories.api.client.AccessoryRenderer
import io.wispforest.accessories.api.slot.SlotReference
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryAdapter
import settingdust.calypsos_afflatus.item.NightvisionGogglesAccessory

class AccessoriesAccessoryAdapter : AccessoryAdapter {
    object Renderer : AccessoryRenderer {
        override fun <M : LivingEntity> render(
            stack: ItemStack,
            reference: SlotReference,
            matrices: PoseStack,
            model: EntityModel<M>,
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
                reference.entity(),
                matrices,
                multiBufferSource,
                light
            )
        }
    }

    override val modId = "accessories"
    override fun init() {
        AccessoriesAPI.registerAccessory(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, NightvisionGogglesAccessory.Accessories)

        AccessoriesRendererRegistry.registerRenderer(CalypsosAfflatusItems.NIGHTVISION_GOGGLES) { Renderer }
    }
}