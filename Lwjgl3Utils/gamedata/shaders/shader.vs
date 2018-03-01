#version 330 core

in vec3 pos;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;

void main(){
	mat4 mvp = proj * view * model;
	gl_Position = mvp * vec4(pos, 1);
}