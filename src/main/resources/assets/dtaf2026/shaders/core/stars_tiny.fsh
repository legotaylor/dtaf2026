#version 150

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

in vec3 pos;

out vec4 fragColor;

void main() {
	vec4 color = ColorModulator;
	color.a += sin((GameTime * 4800.0) + pos.x + pos.z);
	fragColor = color;
}
