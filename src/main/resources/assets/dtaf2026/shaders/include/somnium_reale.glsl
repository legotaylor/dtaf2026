#version 150

layout(std140) uniform SomniumReale {
	ivec4 lightData;
	vec4 smoothLightData;
	vec4 bloomAlpha;
};

#define Light lightData.x
#define SkyLight lightData.y
#define BlockLight lightData.z

#define SmoothLight smoothLightData.x
#define SmoothSkyLight smoothLightData.y
#define SmoothBlockLight smoothLightData.z

#define BloomAlpha bloomAlpha.x
#define SmoothBloomAlpha bloomAlpha.y
