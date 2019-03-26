#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture; // texture 0
uniform sampler2D u_lookup_texture;
uniform vec2 resolution; // our screen resolution, set from Java whenever the display is resized

uniform float u_time; // our screen resolution, set from Java whenever the display is resized

varying LOWP vec4 vColor; //  "in" attributes from our vertex shader
varying vec2 vTexCoord;

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	vec4 lookupColor = texture2D(u_lookup_texture, vTexCoord);

    if (lookupColor.r < mod(u_time, 1.1))
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    else
        gl_FragColor = texColor * vColor;
}

//    gl_FragColor = vec4(1.0 - u_fade, 0.0 - u_fade, 1.0 - u_fade, 1.0);
//	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5); // determine center position
