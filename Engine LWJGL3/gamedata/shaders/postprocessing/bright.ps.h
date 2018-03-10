#version 330

in vec2 texCoord;

out vec4 outColor;

uniform sampler2D tex;

void main(){
	vec4 color = texture(tex, texCoord);
	float bright = 1;//(color.r * 0.2126) + (color.g * 0.7152) + (color.b * 0.0722);
	outColor = color * bright;
}
