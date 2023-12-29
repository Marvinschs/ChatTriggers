package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class MixinGuiContainer {

    @Shadow private Slot theSlot;

    @Inject(
        method = "drawSlot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_injectDrawSlot(Slot slotIn, CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();

        GlStateManager.pushMatrix();
        TriggerType.RenderSlot.triggerAll(slotIn, this, event);
        GlStateManager.popMatrix();

        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerForegroundLayer(II)V",
            shift = At.Shift.AFTER
        )
    )
    private void ctjs_injectDrawScreen_PreRenderItem(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.theSlot == null) return;
        GlStateManager.pushMatrix();
        TriggerType.PreItemRender.triggerAll(mouseX, mouseY, this.theSlot, this);
        GlStateManager.popMatrix();
    }

    @Inject(
        method = "drawScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGradientRect(IIIIII)V"
        ),
        cancellable = true
    )
    private void ctjs_InjectDrawScreen_RenderSlotHighlight(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.theSlot == null) return;
        CancellableEvent event = new CancellableEvent();

        GlStateManager.pushMatrix();
        TriggerType.RenderSlotHighlight.triggerAll(mouseX, mouseY, this.theSlot, this, event);
        GlStateManager.popMatrix();

        if (event.isCanceled()) ci.cancel();
    }
}
