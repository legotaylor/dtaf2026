#version 150

// Values are automatically updated in UBORegistry/SomniumRealeUBO
layout(std140) uniform SomniumReale {
	ivec4 lightData;
	vec4 smoothLightData;
	vec4 bloomAlpha;
	vec4 time;
};

#define Light lightData.x // Light, max of SkyLight or BlockLight
#define SkyLight lightData.y // SkyLight (0-15)
#define BlockLight lightData.z // BlockLight (0-15)
#define PhotosensitiveMode lightData.w
// PhotosensitiveMode = 0: ALL EFFECTS
// PhotosensitiveMode = 1: REDUCED EFFECTS
// PhotosensitiveMode = 2: NO EFFECTS

#define SmoothLight smoothLightData.x // Smoothed version of Light
#define SmoothSkyLight smoothLightData.y // Smoothed version of SkyLight
#define SmoothBlockLight smoothLightData.z // Smoothed version of BlockLight

#define BloomAlpha bloomAlpha.x // Config:bloomAlpha
#define SmoothBloomAlpha bloomAlpha.y // Smoothed version of BloomAlpha

#define Time time.x // Same as Globals.GameTime
#define SmoothTime time.y // Smoothed version of Time
#define WorldTime time.z // Current world time (% 24000)
#define TickProgress time.w // Current tick progress

// Uses SmoothTime to cycle color between pink and blue, depending on PhotosensitiveMode; Used in <dtaf2026:rendertype_clouds.fsh>
vec4 getCloudsColor(vec4 color) {
	if (PhotosensitiveMode < 2) {
		float multi = PhotosensitiveMode > 0 ? 0.25 : 1.0;
		color.rgb = mix(color.rgb, mix(vec3(0.8392, 0.0078, 0.4392), vec3(0.0, 0.2196, 0.6588), (sin(SmoothTime * (24.0 * multi)) + 1.0) / 2.0), 0.24);
		color.a *= (0.2 + (((sin(SmoothTime * 2.0) * multi) + 1.0) / 2.0) * 0.1);
	}
	return color;
}
