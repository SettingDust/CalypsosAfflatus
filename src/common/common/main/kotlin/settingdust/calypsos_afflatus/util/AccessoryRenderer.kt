package settingdust.calypsos_afflatus.util

import com.mojang.blaze3d.vertex.PoseStack
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

interface AccessoryRenderer {
    companion object : AccessoryRenderer {
        val RENDERERS: MutableMap<Item, AccessoryRenderer> = Reference2ReferenceOpenHashMap()

        fun registerRenderer(item: Item, renderer: AccessoryRenderer) {
            RENDERERS[item] = renderer
        }

        override fun render(
            stack: ItemStack,
            owner: LivingEntity,
            poseStack: PoseStack,
            buffer: MultiBufferSource,
            light: Int
        ) {
            RENDERERS[stack.item]?.render(stack, owner, poseStack, buffer, light)
        }
    }

    fun render(stack: ItemStack, owner: LivingEntity, poseStack: PoseStack, buffer: MultiBufferSource, light: Int)
}