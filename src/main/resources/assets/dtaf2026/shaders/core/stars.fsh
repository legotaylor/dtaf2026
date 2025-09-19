#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	float time = PhotosensitiveMode < 2 ? SmoothTime : 0.5;
	float multi = PhotosensitiveMode > 0 ? 0.25 : 1.0;
	if (PhotosensitiveMode < 2) color.a += cos((time * (2400.0 * multi)) + pos.x + pos.z);
	color.r += 0.25 * (sin(time + pos.x * 8.0) * multi);
	color.g += 0.25 * (sin(time + pos.y * 2.0) * multi);
	color.b += 0.25 * (sin(time + pos.z * 4.0) * multi);
	color.rgb = mix(color.rgb, vec3(color.r * 0.9 + color.g * 0.1, color.g * 0.9 + color.b * 0.1, color.b * 0.9 + color.r * 0.1), 0.3);
	color *= mix(1.0, (1.0 - clamp(SmoothBlockLight * 1.3636 / 15.0, 0.0, 1.0)), PhotosensitiveMode < 2 ? (PhotosensitiveMode == 1 ? 0.2 : 0.8) : 0.0);
	fragColor = color;
}
