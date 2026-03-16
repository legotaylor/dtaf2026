#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

float lum(vec3 color) {
	return dot(color, vec3(0.299, 0.587, 0.114));
}

float maxC(vec3 color) {
	return max(max(color.r, color.g), color.b);
}

float minC(vec3 color) {
	return min(min(color.r, color.g), color.b);
}

vec3 vibrance(vec3 color, float strength) {
	float lum = lum(color);
	float max = maxC(color);
	float min = minC(color);
	float sat = max - min;
	float factor = (1.0 - sat) * strength;
	vec3 grayscale = vec3(lum);
	return mix(grayscale, color, 1.0 + factor);
}

vec3 lumGradient(vec3 color) {
	float lum = lum(color);
	float dist = abs(lum - 0.35);
	float falloff = 1.0 - smoothstep(0.0, 0.6, dist);
	float boost = mix(1.0, 1.35, falloff);
	return clamp(color * boost, 0.0, 1.0);
}

vec3 contrast(vec3 color) {
	float lum = lum(color);
	float over = smoothstep(0.5, 1.0, lum);
	float under = 1.0 - smoothstep(0.0, 0.5, lum);
	vec3 bright = mix(color, vec3(1.0), over * 0.08);
	vec3 dark = mix(color, vec3(0.0), under * 0.08);
	return mix(dark, bright, over);
}

vec3 decreaseOverbright(vec3 color) {
	float lum = lum(color);
	float max = maxC(color);
	float min = minC(color);
	float sat = max - min;
	float lumFactor = smoothstep(0.65, 0.95, lum);
	float satFactor = 1.0 - smoothstep(0.0, 0.35, sat);
	float whiteFactor = lumFactor * satFactor;
	return color * mix(1.0, 0.95, whiteFactor);
}

void main() {
	vec4 texColor = texture(Sampler0, texCoord0);

	#ifdef ALPHA_CUTOUT
	if (texColor.a < ALPHA_CUTOUT) {
		discard;
	}
	#endif

	vec4 color = texColor * vertexColor * ColorModulator;
	color.rgb *= 0.5;
	color.rgb = vibrance(color.rgb, 0.5);

	float vertexMax = max(max(vertexColor.r, vertexColor.g), vertexColor.b);
	if (vertexMax < 0.9) color.rgb *= vec3(0.95, 0.97, 1.0);
	if (vertexMax < 0.5) color.rgb *= vec3(0.9);
	if (vertexMax < 0.25) color.rgb *= vec3(0.8);

	color.rgb = lumGradient(color.rgb);
	color.rgb = contrast(color.rgb);
	color.rgb = decreaseOverbright(color.rgb);
	color.rgb = mix(vec3(dot(color.rgb, vec3(0.3, 0.5, 0.1))), color.rgb, 1.0);

	fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
