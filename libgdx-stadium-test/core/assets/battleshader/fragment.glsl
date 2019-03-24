#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture; // texture 0

uniform vec2 resolution; // our screen resolution, set from Java whenever the display is resized
varying LOWP vec4 vColor; //  "in" attributes from our vertex shader
varying vec2 vTexCoord;

const float RADIUS = 0.75; // RADIUS of our vignette, where 0.5 results in a circle fitting the screen
const float SOFTNESS = 0.45; // softness of our vignette, between 0.0 and 1.0
const vec3 SEPIA = vec3(1.2, 1.0, 0.8); // sepia colour, adjust to taste

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord); // sample our texture

// 1. VIGNETTE
	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5); // determine center position
	float len = length(position); // determine the vector length of the center position
	float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len); // use smoothstep to create a smooth vignette
	texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5); // apply the vignette with 50% opacity

// 2. GRAYSCALE
	// convert to grayscale using NTSC conversion weights
	float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));

// 3. SEPIA
    vec3 sepiaColor = vec3(gray) * SEPIA; // create our sepia tone from some constant value
    texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75); // again we'll use mix so that the sepia effect is at 75%
    gl_FragColor = texColor * vColor; // final colour, multiplied by vertex colour
}