#version 330

in vec2 texCoord;

out vec4 outColor;

uniform sampler2D tex0;
uniform sampler2D tex1;
uniform float contrast;

void main(){
	vec4 tex0Color = texture(tex0, texCoord);
	vec4 tex1Color = texture(tex1, texCoord);
	
	outColor = tex0Color + tex1Color;
}