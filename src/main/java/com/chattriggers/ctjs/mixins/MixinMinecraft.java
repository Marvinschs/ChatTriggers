package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Final public File mcDataDir;

    @Shadow public int displayWidth;

    @Shadow public int displayHeight;

    @Shadow private Framebuffer framebufferMc;

    @Inject(
        method = "dispatchKeypresses",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiIngame;getChatGUI()Lnet/minecraft/client/gui/GuiNewChat;",
            ordinal = 0,
            shift = At.Shift.BY,
            by = -2
        ),
        cancellable = true
    )
    private void ctjs_InjectDispatchKeypresses(CallbackInfo ci) {
        IChatComponent chatComponent = ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc);
        if (chatComponent != null) new TextComponent(chatComponent).chat();
        ci.cancel();
    }

    @Inject(
        method = "displayGuiScreen",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiScreen;onGuiClosed()V"
        )
    )
    private void ctjs_InjectDisplayGuiScreen(GuiScreen guiScreenIn, CallbackInfo ci) {
        TriggerType.GuiClosed.triggerAll(Minecraft.getMinecraft().currentScreen);
    }

    @Inject(
        method = "startGame",
        at = @At("TAIL")
    )
    private void ctjs_InjectStartGame(CallbackInfo ci) {
        TriggerType.GameLoad.triggerAll();
    }
}
