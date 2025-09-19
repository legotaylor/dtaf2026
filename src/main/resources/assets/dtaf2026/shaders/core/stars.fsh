#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = twinkleStars(ColorModulator, 2400.0, pos);
	float time = PhotosensitiveMode < 2 ? SmoothTime : 0.5;
	color.r += 0.25 * (sin(time + pos.x * 8.0) * Multiplier);
	color.g += 0.25 * (sin(time + pos.y * 2.0) * Multiplier);
	color.b += 0.25 * (sin(time + pos.z * 4.0) * Multiplier);
	color.rgb = mix(color.rgb, vec3(color.r * 0.9 + color.g * 0.1, color.g * 0.9 + color.b * 0.1, color.b * 0.9 + color.r * 0.1), 0.3);
	fragColor = getDimColor(color, 0.65);
}
