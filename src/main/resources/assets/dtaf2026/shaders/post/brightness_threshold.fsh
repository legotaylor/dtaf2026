#version 150

uniform sampler2D InSampler;

in vec2 texCoord;

layout(std140) uniform BrightnessThresholdConfig {
	float ThresholdBrightness;
};

out vec4 fragColor;

void main(){
	fragColor = dot(vec3(0.2126, 0.7152, 0.0722), texture(InSampler, texCoord).rgb) > ThresholdBrightness ? vec4(1.0) : vec4(0.0);
}
