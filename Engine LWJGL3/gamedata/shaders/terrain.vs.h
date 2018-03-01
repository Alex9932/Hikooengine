#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 texCoord;

out vec2 textureCoord;
out vec3 pass_normal;
out vec3 pass_position;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main(){
	mat4 mvp = proj * view * model;
	gl_Position = mvp * vec4(pos, 1);
	textureCoord = texCoord;

	pass_position = (model * vec4(pos, 1)).xyz;
	pass_normal = normal;
}
