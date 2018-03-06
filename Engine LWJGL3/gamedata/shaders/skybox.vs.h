#version 400

in vec3 position;

out vec3 textureCoords;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main(void){
	gl_Position = proj * view * model * vec4(position, 1.0);
	textureCoords = position;
}
