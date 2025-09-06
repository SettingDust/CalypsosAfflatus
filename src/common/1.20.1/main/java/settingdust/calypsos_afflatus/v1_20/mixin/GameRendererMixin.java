package settingdust.calypsos_afflatus.v1_20.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.calypsos_afflatus.CalypsosAfflatusItems;
import settingdust.calypsos_afflatus.adapter.AccessoryIntegration;
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesItem;
import settingdust.calypsos_afflatus.item.nightvision_goggles.NightvisionGogglesModeHandler;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getNightVisionScale", at = @At("RETURN"))
    private static float calypsos_afflatus$getNightVisionScale(float original, LivingEntity entity, float nanoTime) {
        var effect = entity.getEffect(MobEffects.NIGHT_VISION);
        var equipped = AccessoryIntegration.Companion.getEquipped(
                entity,
                CalypsosAfflatusItems.Companion.getNIGHTVISION_GOGGLES());
        if (equipped == null
                || !NightvisionGogglesItem.INSTANCE.isFromAccessory(effect)
                || !NightvisionGogglesModeHandler.Companion.getMode(equipped).isEnabled().invoke(equipped, entity)) {
            return original;
        }
        return 1.0f;
    }
}
