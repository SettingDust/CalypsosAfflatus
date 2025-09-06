package settingdust.calypsos_afflatus.fabric.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import settingdust.calypsos_afflatus.fabric.util.LivingTickCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void calypsos_afflatus$tick(CallbackInfo ci) {
        LivingTickCallback.EVENT.invoker().onLivingTick((LivingEntity) (Object) this);
    }
}
