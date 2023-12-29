package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {

    @Inject(
        method = "attackEntity",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectAttackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();
        com.chattriggers.ctjs.minecraft.wrappers.entity.Entity entity = new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(targetEntity);
        TriggerType.AttackEntity.triggerAll(entity, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "clickBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectClickBlock(BlockPos loc, EnumFacing face, CallbackInfoReturnable<Boolean> cir) {
        if (!ClientListener.INSTANCE.onHitBlock(loc, face)) cir.setReturnValue(false);
    }

    @Inject(
        method = "onPlayerDestroyBlock",
        at = @At("HEAD")
    )
    private void ctjs_InjectOnPlayerDestroyBlock(BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.BlockBreak.triggerAll(
            World.getBlockAt(pos.getX(), pos.getY(), pos.getZ())
                .withFace(BlockFace.Companion.fromMCEnumFacing(side))
        );
    }
}
