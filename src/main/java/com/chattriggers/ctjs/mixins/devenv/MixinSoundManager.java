package com.chattriggers.ctjs.mixins.devenv;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.audio.SoundManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundManager.class)
public class MixinSoundManager {

    @WrapWithCondition(
        method = "playSound",
        at = @At(
            value = "INVOKE",
            target = "Lorg/apache/logging/log4j/Logger;warn(Lorg/apache/logging/log4j/Marker;Ljava/lang/String;[Ljava/lang/Object;)V",
            ordinal = 0
        )
    )
    private boolean ctjs_WrapSoundWarn(Logger instance, Marker marker, String s, Object[] objects) {
        //Ignore unknown Sound Events
        return false;
    }
}
