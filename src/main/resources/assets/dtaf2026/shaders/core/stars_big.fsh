#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	color.r += 0.25 * sin(GameTime + pos.x * 2.0);
	color.g += 0.25 * sin(GameTime + pos.y * 4.0);
	color.b += 0.25 * sin(GameTime + pos.z * 8.0);
	color.rgb = mix(color.rgb, vec3(color.r * 0.9 + color.g * 0.1, color.g * 0.9 + color.b * 0.1, color.b * 0.9 + color.r * 0.1), 0.3);
	fragColor = color;
}
