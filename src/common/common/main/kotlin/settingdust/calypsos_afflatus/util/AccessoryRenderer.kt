package settingdust.calypsos_afflatus.util

import com.mojang.blaze3d.vertex.PoseStack
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import settingdust.calypsos_afflatus.mixin.ModelPartAccessor
import kotlin.math.max
import kotlin.math.min

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

        fun PoseStack.transformToModelPart(
            part: ModelPart,
            xPercent: Double? = null,
            yPercent: Double? = null,
            zPercent: Double? = null
        ) {
            part.translateAndRotate(this)

            val (min, max) = part.aabb()

            scale(1 / 16f, 1 / 16f, 1 / 16f)

            translate(
                Mth.lerp((-(xPercent ?: 0.0) + 1.0) / 2.0, min.x, max.x) ,
                Mth.lerp((-(yPercent ?: 0.0) + 1.0) / 2.0, min.y, max.y) ,
                Mth.lerp((-(zPercent ?: 0.0) + 1.0) / 2.0, min.z, max.z)
            )

            scale(16f, 16f, 16f)
        }

        fun PoseStack.transformToFace(part: ModelPart, side: Side) {
            val n = side.direction.normal
            transformToModelPart(part, n.x.toDouble(), n.y.toDouble(), n.z.toDouble())
        }

        private fun ModelPart.aabb(): Pair<Vec3, Vec3> {
            var min = Vec3(0.0, 0.0, 0.0)
            var max = Vec3(0.0, 0.0, 0.0)

            if (this.javaClass.simpleName.contains("EMFModelPart")) {
                val parts = buildList {
                    add(this@aabb)
                    addAll((this@aabb as ModelPartAccessor).children.values)
                }

                for (mp in parts) {
                    for (cube in (mp as ModelPartAccessor).cubes) {
                        val ox = mp.x.toDouble()
                        val oy = mp.y.toDouble()
                        val oz = mp.z.toDouble()

                        val cxMin = min(cube.minX + ox, cube.maxX + ox)
                        val cyMin = min(cube.minY + oy, cube.maxY + oy)
                        val czMin = min(cube.minZ + oz, cube.maxZ + oz)

                        val cxMax = max(cube.minX + ox, cube.maxX + ox)
                        val cyMax = max(cube.minY + oy, cube.maxY + oy)
                        val czMax = max(cube.minZ + oz, cube.maxZ + oz)

                        min = Vec3(min(min.x, cxMin), min(min.y, cyMin), min(min.z, czMin))
                        max = Vec3(max(max.x, cxMax), max(max.y, cyMax), max(max.z, czMax))
                    }
                }
            } else {
                for (cube in (this as ModelPartAccessor).cubes) {
                    min = Vec3(
                        min(min.x, min(cube.minX, cube.maxX).toDouble()),
                        min(min.y, min(cube.minY, cube.maxY).toDouble()),
                        min(min.z, min(cube.minZ, cube.maxZ).toDouble())
                    )
                    max = Vec3(
                        max(max.x, max(cube.minX, cube.maxX).toDouble()),
                        max(max.y, max(cube.minY, cube.maxY).toDouble()),
                        max(max.z, max(cube.minZ, cube.maxZ).toDouble())
                    )
                }
            }

            return min to max
        }
    }


    fun render(stack: ItemStack, owner: LivingEntity, poseStack: PoseStack, buffer: MultiBufferSource, light: Int)
}