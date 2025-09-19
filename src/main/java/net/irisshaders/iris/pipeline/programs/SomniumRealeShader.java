/*
    dtaf2026
    Contributor(s): dannytaylor
    Github: https://github.com/legotaylor/dtaf2026
    Licence: GNU LGPLv3
*/

package net.irisshaders.iris.pipeline.programs; // We have to be in this package to be able to use ShaderKey$LightingModel.

import dev.dannytaylor.dtaf2026.iris.SomniumRealeProgramId;
import dev.dannytaylor.dtaf2026.iris.SomniumRealeShaderKey;
import net.irisshaders.iris.shaderpack.loading.ProgramId;

import java.util.HashMap;
import java.util.Map;

public class SomniumRealeShader {
	private static final Map<String, SomniumRealeProgramId> programIds = new HashMap<>();
	private static final Map<String, SomniumRealeShaderKey> shaderKeys = new HashMap<>();
	private static final Map<String, ProgramId> registeredProgramIds = new HashMap<>();
	private static final Map<String, ShaderKey> registeredShaderKeys = new HashMap<>();

	public static void registerProgram(String id, SomniumRealeProgramId programId) {
		programIds.put(id, programId);
	}

	public static Map<String, SomniumRealeProgramId> getProgramIds() {
		return programIds;
	}

	public static void registerKey(String id, SomniumRealeShaderKey key) {
		shaderKeys.put(id, key);
	}

	public static Map<String, SomniumRealeShaderKey> getShaderKeys() {
		return shaderKeys;
	}

	public static void registerCustomProgram(String id, ProgramId programId) {
		registeredProgramIds.put(id, programId);
	}

	public static Map<String, ProgramId> getCustomProgramIds() {
		return registeredProgramIds;
	}

	public static void registerCustomKey(String id, ShaderKey key) {
		registeredShaderKeys.put(id, key);
	}

	public static Map<String, ShaderKey> getCustomShaderKeys() {
		return registeredShaderKeys;
	}

	public enum LightingModel {
		FULLBRIGHT(ShaderKey.LightingModel.FULLBRIGHT),
		LIGHTMAP(ShaderKey.LightingModel.LIGHTMAP),
		DIFFUSE(ShaderKey.LightingModel.DIFFUSE),
		DIFFUSE_LM(ShaderKey.LightingModel.DIFFUSE_LM);

		public final ShaderKey.LightingModel lightingModel;

		LightingModel(ShaderKey.LightingModel lightingModel) {
			this.lightingModel = lightingModel;
		}
	}
}
