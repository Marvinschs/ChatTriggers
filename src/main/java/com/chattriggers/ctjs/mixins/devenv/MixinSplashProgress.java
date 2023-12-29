package com.chattriggers.ctjs.mixins.devenv;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraftforge.fml.client.SplashProgress;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.PrintStream;

@Mixin(SplashProgress.class)
public abstract class MixinSplashProgress {

    @WrapWithCondition(
        method = "start",
        at = @At(
            value = "INVOKE",
            target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"
        ),
        remap = false
    )
    private static boolean ctjs_removeCrashReportInLog(PrintStream instance, String x) {
        LogManager.getLogger("CTJS").info("Removed Crash Report from Forge!");
        return false;
    }
}
