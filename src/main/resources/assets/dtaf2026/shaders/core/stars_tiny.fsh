#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	color *= 2.0;
	color.a += sin((GameTime * 4800.0) + pos.x + pos.z);
	color *= (1.0 - clamp(SmoothBlockLight * 1.3636 / 15.0, 0.0, 1.0));
	fragColor = color;
}
