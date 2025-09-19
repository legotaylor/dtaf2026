#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	if (PhotosensitiveMode < 2) color.a += cos((SmoothTime * (PhotosensitiveMode == 1 ? 600.0 : 2400.0)) + pos.x + pos.z);
	else {
		// We disable tiny stars on PhotosensitiveMode 2 as they unintentionally flicker when the player moves.
		color = vec4(0.0);
	}
	fragColor = color;
}
