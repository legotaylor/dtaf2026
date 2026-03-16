#version 150

uniform sampler2D InSampler;

in vec2 texCoord;
in vec2 oneTexel;

layout(std140) uniform PanoramaConfig {
	float Radius;
};

out vec4 fragColor;

float gaussian(float x, float sigma) {
	return exp(-(x * x) / (2.0 * sigma * sigma));
}

void main() {
	vec4 color = texture(InSampler, texCoord);

	vec2 center = vec2(0.5);
	vec2 dir = texCoord - center;
	float dist = length(dir);
	float edge = pow(smoothstep(0.35, 0.75, dist), 1.4);

	float shift = 0.003 * edge;
	vec2 radial = normalize(dir) * shift;

	float r = texture(InSampler, texCoord + radial).r;
	float g = color.g;
	float b = texture(InSampler, texCoord - radial).b;
	vec4 chroma = vec4(r, g, b, color.a);

	vec4 blurred = vec4(0.0);
	float weightSum = 0.0;

	for (float a = -Radius; a <= Radius; a++) {
		float weight = gaussian(a, (Radius / 2));
		blurred += texture(InSampler, texCoord + vec2(a, 0.0) * oneTexel) * weight;
		blurred += texture(InSampler, texCoord + vec2(0.0, a) * oneTexel) * weight;
		weightSum += weight * 2.0;
	}

	blurred /= weightSum;

	fragColor = vec4(mix(chroma, blurred, edge).rgb * (1.0 - edge * 0.15), color.a);
}
