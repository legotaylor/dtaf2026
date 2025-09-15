/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package dev.dannytaylor.dtaf2026.iris.mixin;

import dev.dannytaylor.dtaf2026.iris.IrisMain;
import dev.dannytaylor.dtaf2026.common.utils.UnsafeEnumHelper;
import net.irisshaders.iris.pipeline.programs.SomniumRealeShader;
import net.irisshaders.iris.shaderpack.loading.ProgramId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = ProgramId.class, remap = false, priority = 100)
public abstract class ProgramIdMixin {
	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void dtaf2026$addCustomProgram(CallbackInfo ci) {
		IrisMain.registerPrograms();
		SomniumRealeShader.getProgramIds().forEach((id, programId) -> {
			try {
				System.out.println("Registering custom program with id: " + id);
				ProgramId[] oldValues = ProgramId.values();
				ProgramId fallback = ProgramId.Basic;
				try {
					if (programId.fallback() != null) fallback = programId.fallback();
				} catch (IllegalArgumentException error) {
					System.out.println("Using fallback programId for program with id '" + id  + "': " + error.getLocalizedMessage());
				}

				Map<String, Object> options = new HashMap<>();
				options.putAll(Map.of("group", programId.group(), "sourceName", programId.sourceName(), "fallback", fallback));
				if (programId.defaultBlendOverride() != null) options.put("defaultBlendOverride", programId.defaultBlendOverride());

				ProgramId customProgramId = UnsafeEnumHelper.createEnum(ProgramId.class, id, oldValues.length, options);
				SomniumRealeShader.registerCustomProgram(id, customProgramId);
				ProgramId[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
				newValues[newValues.length - 1] = customProgramId;
				UnsafeEnumHelper.UNSAFE.putObject(ProgramId.class, UnsafeEnumHelper.UNSAFE.staticFieldOffset(Arrays.stream(ProgramId.class.getDeclaredFields()).filter(f -> f.getType().equals(ProgramId[].class)).findFirst().orElseThrow()), newValues);
			} catch (Exception error) {
				error.printStackTrace();
			}
		});
	}
}
