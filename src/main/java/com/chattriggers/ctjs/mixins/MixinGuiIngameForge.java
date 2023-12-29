package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge extends GuiIngame {

    public MixinGuiIngameForge(Minecraft mcIn) {
        super(mcIn);
    }

    @Inject(
        method = "renderTitle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GlStateManager;pushMatrix()V",
            ordinal = 0
        ),
        cancellable = true
    )
    private void ctjs_injectRenderTitle(int width, int height, float partialTicks, CallbackInfo ci) {
        if (this.displayedTitle.isEmpty() || this.displayedSubTitle.isEmpty()) return;
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderTitle.triggerAll(this.displayedTitle, this.displayedSubTitle, event);
        if (event.isCancelled()) ci.cancel();
    }
}
