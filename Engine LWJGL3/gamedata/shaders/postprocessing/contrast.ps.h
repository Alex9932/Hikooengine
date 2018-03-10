#version 330

in vec2 texCoord;

out vec4 outColor;

uniform sampler2D tex;
uniform float contrast;

void main(){
	outColor = texture(tex, texCoord) * contrast;
}
