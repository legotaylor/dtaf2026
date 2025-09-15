/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris.mixin;

import dev.dannytaylor.dtaf2026.iris.IrisMain;
import dev.dannytaylor.dtaf2026.common.utils.UnsafeEnumHelper;
import net.irisshaders.iris.pipeline.programs.ShaderKey;
import net.irisshaders.iris.pipeline.programs.SomniumRealeShader;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Map;

@Mixin(value = ShaderKey.class, remap = false, priority = 100)
public abstract class ShaderKeyMixin {
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void dtaf2026$addCustomShader(CallbackInfo ci) {
		IrisMain.registerShaderKeys();
		SomniumRealeShader.getShaderKeys().forEach((id, shaderKey) -> {
			try {
				System.out.println("Registering custom shader key with id: " + id);
				ShaderKey[] oldValues = ShaderKey.values();
				System.out.println(id + ": program: " + ProgramId.valueOf(shaderKey.program()));
				ShaderKey customShaderKey = UnsafeEnumHelper.createEnum(ShaderKey.class, id, oldValues.length, Map.of("program", ProgramId.valueOf(shaderKey.program()), "alphaTest", shaderKey.alphaTest(), "vertexFormat", shaderKey.vertexFormat(), "fogMode", shaderKey.fogMode(), "lightingModel", shaderKey.lightingModel().lightingModel));
				SomniumRealeShader.registerCustomKey(id, customShaderKey);
				ShaderKey[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
				newValues[newValues.length - 1] = customShaderKey;
				UnsafeEnumHelper.UNSAFE.putObject(ShaderKey.class, UnsafeEnumHelper.UNSAFE.staticFieldOffset(Arrays.stream(ShaderKey.class.getDeclaredFields()).filter(f -> f.getType().equals(ShaderKey[].class)).findFirst().orElseThrow()), newValues);
				if (shaderKey.pipeline() != null) IrisPipelinesAccessor.dtaf2026$invokeAssignToMain(shaderKey.pipeline(), (p) -> customShaderKey);
			} catch (Exception error) {
				error.printStackTrace();
			}
		});
	}
}
