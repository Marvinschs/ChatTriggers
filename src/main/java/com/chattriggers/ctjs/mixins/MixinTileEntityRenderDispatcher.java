package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public class MixinTileEntityRenderDispatcher {

    @Inject(
        method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectRenderTileEntityAt_Pre(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderTileEntity.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity(tileEntityIn),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks,
            event
        );
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V",
        at = @At("TAIL")
    )
    private void ctjs_InjectRenderTileEntityAt_Post(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        TriggerType.PostRenderTileEntity.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity(tileEntityIn),
            new Vector3f((float) x, (float) y, (float) z),
            partialTicks
        );
    }
}
