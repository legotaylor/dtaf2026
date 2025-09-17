#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	color *= 2.0;
	color.r += 0.25 * sin(GameTime + pos.x * 2.0);
	color.g += 0.25 * sin(GameTime + pos.y * 4.0);
	color.b += 0.25 * sin(GameTime + pos.z * 8.0);
	color.rgb = mix(color.rgb, vec3(color.r * 0.9 + color.g * 0.1, color.g * 0.9 + color.b * 0.1, color.b * 0.9 + color.r * 0.1), 0.3);
	color *= mix(1.0, (1.0 - clamp(SmoothBlockLight * 1.3636 / 15.0, 0.0, 1.0)), 0.6);
	fragColor = color;
}
