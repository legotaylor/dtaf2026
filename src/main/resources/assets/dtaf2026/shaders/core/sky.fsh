#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec3 pos;

out vec4 fragColor;

void main() {
	fragColor = apply_fog(ColorModulator, sphericalVertexDistance, cylindricalVertexDistance, 0.0, FogSkyEnd, FogSkyEnd, FogSkyEnd, FogColor);
}
