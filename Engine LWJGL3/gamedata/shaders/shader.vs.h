#version 330 core

in vec3 pos;
in vec2 tex;
in vec3 normal;

out vec2 textureCoords;
out vec3 pass_normal;
out vec3 pass_position;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main(){
	mat4 mvp = proj * view * model;
	gl_Position = mvp * vec4(pos, 1);
	pass_position = (model * vec4(pos, 1)).xyz;

	textureCoords = tex;
	pass_normal = normal;
}
