#version 150

// Author: Lezzlebit
// License: CC0-1.0
// https://modrinth.com/resourcepack/gradient-clouds

#moj_import <minecraft:fog.glsl>
#moj_import <dtaf2026:somnium_reale.glsl>

in float vertexDistance;
in vec4 vertexColor;
in vec3 Pos;
in vec3 PlayerPos;

out vec4 fragColor;

void main() {
	vec4 color = getCloudsColor(vertexColor); // dtaf2026 uses <dtaf2026:somnium_reale.glsl>$getCloudsColor(vertexColor) instead of vertexColor
	color = mix(color, FogColor, 0.5);
	float fadeDirection = (PlayerPos.y+5)/10;
	float fadeIntensity = abs(PlayerPos.y/40);
	float topFadeHeight = 1 - ((20-Pos.y) / 20);
	float bottomFadeHeight = 1 - (Pos.y / 20);
	float fadeHeight = mix(topFadeHeight, bottomFadeHeight, clamp(fadeDirection, 0, 1));
	color.a *= mix(1.0, fadeHeight, clamp(fadeIntensity, 0, 1));
	color.a *= 1.0f - linear_fog_value(vertexDistance, 0, FogCloudsEnd);
	fragColor = color;
}
