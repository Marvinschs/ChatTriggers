package com.chattriggers.ctjs.mixins;

import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ScreenShotHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

import static net.minecraft.util.ScreenShotHelper.saveScreenshot;

@Mixin(ScreenShotHelper.class)
public abstract class MixinScreenshotHelper {

    @Redirect(
        method = "saveScreenshot(Ljava/io/File;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/ScreenShotHelper;saveScreenshot(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;"
        )
    )
    private static IChatComponent ctjs_InjectSaveScreenshot(File dir, String name, int x, int y, Framebuffer framebuffer) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.ScreenshotTaken.triggerAll(name, event);
        if (event.isCancelled()) return saveScreenshot(dir, name, x, y, framebuffer);
        return null;
    }
}
