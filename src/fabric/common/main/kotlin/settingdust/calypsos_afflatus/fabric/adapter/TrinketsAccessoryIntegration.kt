package settingdust.calypsos_afflatus.fabric.adapter

import com.mojang.blaze3d.vertex.PoseStack
import dev.emi.trinkets.TrinketsMain
import dev.emi.trinkets.api.SlotReference
import dev.emi.trinkets.api.Trinket
import dev.emi.trinkets.api.TrinketsApi
import dev.emi.trinkets.api.client.TrinketRenderer
import dev.emi.trinkets.api.client.TrinketRendererRegistry
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import settingdust.calypsos_afflatus.CalypsosAfflatusItems
import settingdust.calypsos_afflatus.adapter.AccessoryIntegration
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesAccessory
import kotlin.jvm.optionals.getOrNull

class TrinketsAccessoryIntegration : AccessoryIntegration {
    object Renderer : TrinketRenderer {
        override fun render(
            stack: ItemStack,
            slotReference: SlotReference,
            contextModel: EntityModel<out LivingEntity>,
            matrices: PoseStack,
            vertexConsumers: MultiBufferSource,
            light: Int,
            entity: LivingEntity,
            limbAngle: Float,
            limbDistance: Float,
            tickDelta: Float,
            animationProgress: Float,
            headYaw: Float,
            headPitch: Float
        ) {
            NightvisionGogglesAccessory.render(stack, entity, matrices, vertexConsumers, light)
        }

    }

    override val modId = TrinketsMain.MOD_ID

    override fun init() {
        TrinketsApi.registerTrinket(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, object : Trinket {
            override fun tick(stack: ItemStack, slot: SlotReference, owner: LivingEntity) {
                NightvisionGogglesAccessory.tick(stack, owner)
            }
        })

        TrinketRendererRegistry.registerRenderer(CalypsosAfflatusItems.NIGHTVISION_GOGGLES, Renderer)
    }

    override fun getEquipped(entity: LivingEntity, item: Item): ItemStack? =
        TrinketsApi.getTrinketComponent(entity).getOrNull()?.getEquipped(item)?.firstOrNull()?.b
}