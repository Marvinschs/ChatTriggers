package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame {

    @Inject(
        method = "renderScoreboard",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectRenderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderScoreboard.triggerAll(event);
        if (event.isCancelled()) ci.cancel();
    }
}
