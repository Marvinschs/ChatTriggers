package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.entity.Particle;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EffectRenderer.class)
public abstract class MixinEffectRenderer {

    @Inject(
        method = "spawnEffectParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/particle/EffectRenderer;addEffect(Lnet/minecraft/client/particle/EntityFX;)V"
        ),
        cancellable = true
    )
    private void ctjs_injectSpawnEffectParticle(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int[] parameters, CallbackInfoReturnable<EntityFX> cir, @Local EntityFX entityfx) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.SpawnParticle.triggerAll(new Particle(entityfx), EnumParticleTypes.getParticleFromId(particleId), event);
        if (event.isCancelled()) cir.setReturnValue(null);
    }
}
