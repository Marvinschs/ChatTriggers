package com.chattriggers.ctjs.mixins.devenv;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.io.PrintStream;

@Mixin(Main.class)
public abstract class MixinMain {

    @WrapWithCondition(
        method = "main",
        at = @At(
            value = "INVOKE",
            target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"
        ),
        remap = false
    )
    private static boolean ctjs_suppressIgnoredArguments(PrintStream instance, String x) {
        return false;
    }
}
