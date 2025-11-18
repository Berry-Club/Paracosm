#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
uniform vec2 InSize;

const float Pi = 3.1415926535;
const float PincushionAmount = 0.02;
const float CurvatureAmount = 0.02;
const float ScanlineAmount = 0.8;
const float ScanlineScale = 1.0;
const float ScanlineHeight = 1.0;
const float ScanlineBrightScale = 1.0;
const float ScanlineBrightOffset = 0.0;
const float ScanlineOffset = 0.0;
const vec3 Floor = vec3(0.05, 0.05, 0.05);
const vec3 Power = vec3(0.8, 0.8, 0.8);

const float distortionStrength = 1.0;

out vec4 fragColor;

void main() {
	// Apply distortion to texture coordinates
	vec2 centeredCoord = texCoord - vec2(0.5);
	float distance = length(centeredCoord);
	float distortionFactor = 1.0 + distortionStrength * pow(distance, 2.0);
	vec2 distortedCoord = centeredCoord * distortionFactor + vec2(0.5);

	// Add border by discarding fragments outside the central region
	if (distortedCoord.x < 0 || distortedCoord.x > 1 || distortedCoord.y < 0 || distortedCoord.y > 1) {
		discard;
	}

	// Sample the texture with the distorted coordinates
	vec4 InTexel = texture(DiffuseSampler, distortedCoord);

	// Apply scanline effect
	float InnerSine = distortedCoord.y * InSize.y * ScanlineScale * 0.25;
	float ScanBrightMod = sin(InnerSine * Pi + ScanlineOffset * InSize.y * 0.25);
	float ScanBrightness = mix(1.0, (pow(ScanBrightMod * ScanBrightMod, ScanlineHeight) * ScanlineBrightScale + 1.0) * 0.5, ScanlineAmount);
	vec3 ScanlineTexel = InTexel.rgb * ScanBrightness;

	// Color compression
	ScanlineTexel = Floor + (vec3(1.0) - Floor) * ScanlineTexel;

	// Gamma correction
	ScanlineTexel.rgb = pow(ScanlineTexel.rgb, Power);

	// Output the final color
	fragColor = vec4(ScanlineTexel.rgb, 1.0);
}
