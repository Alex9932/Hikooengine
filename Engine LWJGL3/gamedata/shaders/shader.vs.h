#version 330 core

in vec3 pos;
in vec2 tex;
in vec3 normal;

out vec2 textureCoords;
out vec3 pass_normal;
out vec3 pass_position;
out vec3 reflectedVector;
out vec3 camera_position_pass;

out float visibility;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 model;
uniform float density;
uniform float gradient;
uniform vec3 camera_position;

void main(){
	camera_position_pass = camera_position;

	mat4 mvp = proj * view * model;

	vec4 worldPos = model * vec4(pos, 1);
	vec3 positionRelativeToCam = (view * worldPos).xyz;

	gl_Position = mvp * vec4(pos, 1);
	pass_position = (model * vec4(pos, 1)).xyz;

	textureCoords = tex;
	pass_normal = normal;


	vec3 viewVector = normalize(worldPos.xyz - camera_position_pass);
	reflectedVector = reflect(viewVector, normalize(pass_normal));


	float distance = length(positionRelativeToCam);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
