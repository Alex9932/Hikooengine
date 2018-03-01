#version 330 core

in vec3 pos;
in vec3 normal;
in vec2 texCoord;

out vec2 textureCoord;
out vec3 pass_normal;
out vec3 pass_position;

out float visibility;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;
uniform float density;
uniform float gradient;

void main(){
	mat4 mvp = proj * view * model;

	vec4 worldPos = model * vec4(pos, 1);
	vec3 positionRelativeToCam = (view * worldPos).xyz;

	gl_Position = mvp * vec4(pos, 1);
	textureCoord = texCoord;

	pass_position = (model * vec4(pos, 1)).xyz;
	pass_normal = normal;

	float distance = length(positionRelativeToCam);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
