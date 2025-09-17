#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	color.rgb *= (1.0 - clamp(SmoothBlockLight * 1.3636 / 15.0, 0.0, 1.0));
	fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, 0.0, FogSkyEnd, FogSkyEnd, FogSkyEnd, FogColor);
}
