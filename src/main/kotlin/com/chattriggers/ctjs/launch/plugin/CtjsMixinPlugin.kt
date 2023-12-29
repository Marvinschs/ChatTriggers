package com.chattriggers.ctjs.launch.plugin

import com.chattriggers.ctjs.engine.module.ModuleManager
import com.llamalad7.mixinextras.MixinExtrasBootstrap
import org.apache.logging.log4j.LogManager
import org.spongepowered.asm.lib.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class CtjsMixinPlugin: IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {
        if (System.getProperty("ctjs.debug").toBoolean()) LogManager.getLogger("CTJS").info("Earliest point to load!")
        MixinExtrasBootstrap.init()

        ModuleManager.setup()
        ModuleManager.asmPass()
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        if (System.getProperty("ctjs.debug").toBoolean()) {
            LogManager.getLogger("CTJS").info("ShouldApplyCall: $mixinClassName; To Class: $targetClassName")
        }
        if (!System.getProperty("ctjs.debug").toBoolean() && mixinClassName?.contains("devenv") == true)
            return false
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {
    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
    }
}