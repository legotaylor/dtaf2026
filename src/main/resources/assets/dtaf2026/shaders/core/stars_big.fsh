#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

out vec4 fragColor;

void main() {
	fragColor = getStarsColor(ColorModulator * 2.0, 0.325);
}
