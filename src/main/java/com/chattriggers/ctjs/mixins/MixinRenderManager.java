package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public abstract class MixinRenderManager {

    @Inject(
        method = "doRenderEntity",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectDoRenderEntity_RenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderEntity.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(entity),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks,
            event
        );

        if (event.isCancelled()) cir.setReturnValue(false);
    }

    @Inject(
        method = "doRenderEntity",
        at = @At(
            value = "RETURN",
            ordinal = 1
        )
    )
    private void ctjs_InjectDoRenderEntity_PostRenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.PostRenderEntity.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(entity),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks
        );
    }
}
