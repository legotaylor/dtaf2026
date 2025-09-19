#version 150

// Values are automatically updated in UBORegistry/SomniumRealeUBO
layout(std140) uniform SomniumReale {
	vec4 lightData;
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

float Multiplier = PhotosensitiveMode < 2 ? (PhotosensitiveMode > 0 ? 0.125 : 1.0) : 0.0; // Returns effects multiplier based on PhotosensitiveMode 0=1.0, 1=0.125, 2=0.0

// Uses SmoothTime to cycle color between pink and blue, depending on PhotosensitiveMode; Used in <dtaf2026:rendertype_clouds.fsh>
vec4 getCloudsColor(vec4 color) {
	if (PhotosensitiveMode < 2) {
		float multi = PhotosensitiveMode > 0 ? 0.25 : 1.0;
		color.rgb = mix(color.rgb, mix(vec3(0.8392, 0.0078, 0.4392), vec3(0.0, 0.2196, 0.6588), (sin(SmoothTime * (24.0 * multi)) + 1.0) / 2.0), 0.24);
		color.a *= (0.2 + (((sin(SmoothTime * 2.0) * multi) + 1.0) / 2.0) * 0.1);
	}
	return color;
}

// Uses SmoothBlockLight to dim stars when in lit areas, depending on PhotosensitiveMode; Used in <dtaf2026:stars.fsh>, <dtaf2026:stars_tiny.fsh>, <dtaf2026:stars_big.fsh>
vec4 getDimColor(vec4 color, float level) {
	color *= mix(1.0, (1.0 - clamp(SmoothBlockLight * 1.3636, 0.0, 1.0)), PhotosensitiveMode < 2 ? (PhotosensitiveMode == 1 ? level * 0.1923076923 : level) : 0.0);
	return color;
}

// Uses SmoothTime to twinkle stars, depending on PhotosensitiveMode; Used in <dtaf2026:stars.fsh>, <dtaf2026:stars_tiny.fsh>
vec4 twinkleStars(vec4 color, float level, vec3 pos) {
	color.a += sin((SmoothTime * (level * Multiplier)) + pos.x + pos.z);
	return color;
}
