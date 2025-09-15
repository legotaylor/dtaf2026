/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class IrisMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {
		MixinExtrasBootstrap.init();
	}
	@Override
	public String getRefMapperConfig() {
		return null;
	}
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return IrisMain.isIrisInstalled();
	}
	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}
	@Override
	public List<String> getMixins() {
		return null;
	}
	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
