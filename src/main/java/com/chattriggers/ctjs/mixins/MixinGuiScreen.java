package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow private int eventButton;

    @Inject(
        method = "handleKeyboardInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;keyTyped(CI)V"
        ),
        cancellable = true
    )
    private void ctjs_InjectHandleKeyboardInput(CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.GuiKey.triggerAll(Keyboard.getEventCharacter(), Keyboard.getEventKey(), this, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"
        ),
        cancellable = true
    )
    private void ctjs_InjectHandleMouseInput_MouseClicked(CallbackInfo ci, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int button) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.GuiMouseClick.triggerAll(x, y, button, this, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseReleased(III)V"
        ),
        cancellable = true
    )
    private void ctjs_InjectHandleMouseInput_MouseRelease(CallbackInfo ci, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int button) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.GuiMouseRelease.triggerAll(x, y, button, this, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "handleMouseInput",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;mouseClickMove(IIIJ)V"
        ),
        cancellable = true
    )
    private void ctjs_InjectHandleMouseInput_MouseClickMove(CallbackInfo ci, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int button) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.GuiMouseDrag.triggerAll(x, y, button, this, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "handleComponentClick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectHandleComponentClick(IChatComponent component, CallbackInfoReturnable<Boolean> cir) {
        if (component == null) return;
        CancellableEvent event = new CancellableEvent();
        TriggerType.ChatComponentClicked.triggerAll(new TextComponent(component), event);
        if (event.isCancelled()) cir.setReturnValue(false);
    }

    @Inject(
        method = "handleComponentHover",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectHandleComponentHover(IChatComponent component, int x, int y, CallbackInfo ci) {
        if (component == null) return;
        CancellableEvent event = new CancellableEvent();
        TriggerType.ChatComponentHovered.triggerAll(new TextComponent(component), x, y, event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "renderToolTip",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/EntityPlayer;Z)Ljava/util/List;"
        ),
        cancellable = true
    )
    private void ctjs_InjectRenderTooltip(ItemStack stack, int x, int y, CallbackInfo ci, @Local List<String> list) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.Tooltip.triggerAll(list, new Item(stack), event);
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(
        method = "drawDefaultBackground",
        at = @At("HEAD"),
        cancellable = true
    )
    private void ctjs_InjectDrawDefaultBackground(CallbackInfo ci) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.GuiDrawBackground.triggerAll(this, event);
        if (event.isCancelled()) ci.cancel();
    }
}
