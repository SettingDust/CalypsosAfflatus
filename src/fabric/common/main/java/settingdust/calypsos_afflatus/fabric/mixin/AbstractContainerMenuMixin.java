package settingdust.calypsos_afflatus.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import settingdust.calypsos_afflatus.fabric.util.ItemStackedOnOtherCallback;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @WrapOperation(
            method = "doClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;tryItemClickBehaviourOverride(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/ClickAction;Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean calypsos_afflatus$checkForgeItemStackedEvent(
            AbstractContainerMenu instance,
            Player player,
            ClickAction action,
            Slot slot,
            ItemStack clickedItem,
            ItemStack carriedItem,
            Operation<Boolean> original) {
        var value = original.call(instance, player, action, slot, clickedItem, carriedItem);

        if (!value) {
            return ItemStackedOnOtherCallback.EVENT.invoker()
                    .onItemStackedOnOther(clickedItem, carriedItem, slot, action, player);
        }

        return true;
    }
}
