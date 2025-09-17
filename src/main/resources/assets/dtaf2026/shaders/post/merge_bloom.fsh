#version 150

#moj_import <dtaf2026:somnium_reale.glsl>

uniform sampler2D InSampler;
uniform sampler2D MergeSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
	vec3 baseColor = texture(InSampler, texCoord).rgb;
	vec3 mixColor = texture(MergeSampler, texCoord).rgb;
	fragColor = vec4(mix(mixColor, baseColor, mix(1.0, 0.5, SmoothBlockLight / 15.0) * SmoothBloomAlpha), 1.0);
}
