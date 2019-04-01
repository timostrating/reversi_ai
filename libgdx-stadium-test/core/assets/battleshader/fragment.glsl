#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

uniform sampler2D u_texture; // texture 0
uniform sampler2D u_lookup_texture;
uniform vec2 u_resolution;  // window x and y resolution

uniform float u_time;
uniform float u_fade;
uniform vec4 u_maincolor;

varying vec2 vTexCoord;

void main() {
	vec4 texColor = texture2D(u_texture, vTexCoord);
	vec4 lookupColor = texture2D(u_lookup_texture, vTexCoord);

    if (lookupColor.r < mod(u_time, 1.1))
        gl_FragColor = u_maincolor - u_fade;
    else
        gl_FragColor = texColor - u_fade;
}

//    gl_FragColor = vec4(1.0 - u_fade, 0.0 - u_fade, 1.0 - u_fade, 1.0);
//	vec2 position = (gl_FragCoord.xy / resolution.xy) - vec2(0.5); // determine center position
