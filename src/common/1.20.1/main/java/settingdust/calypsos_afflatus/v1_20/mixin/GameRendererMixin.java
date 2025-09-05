package settingdust.calypsos_afflatus.v1_20.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.calypsos_afflatus.CalypsosAfflatusItems;
import settingdust.calypsos_afflatus.adapter.AccessoryIntegration;
import settingdust.calypsos_afflatus.item.NightvisionGogglesItem;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getNightVisionScale", at = @At("RETURN"))
    private static float getNightVisionScale(float original, LivingEntity entity, float nanoTime) {
        var effect = entity.getEffect(MobEffects.NIGHT_VISION);
        if (!AccessoryIntegration.Companion.isEquipped(entity, CalypsosAfflatusItems.Companion.getNIGHTVISION_GOGGLES())
                || !NightvisionGogglesItem.INSTANCE.isFromAccessory(effect)) {
            return original;
        }
        return 1.0f;
    }
}
