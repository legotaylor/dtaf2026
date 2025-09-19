#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	// We disable tiny stars on PhotosensitiveMode 2 as they unintentionally flicker when the player moves.
	fragColor = getStarsColor(PhotosensitiveMode < 2 ? twinkleStars(ColorModulator, 4800.0, pos) : vec4(0.0), 0.975);
}
