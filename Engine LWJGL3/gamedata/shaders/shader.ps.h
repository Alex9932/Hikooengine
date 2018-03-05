#version 330 core

#define MAX_POINT_LIGHTS 40

in vec2 textureCoords;
in vec3 pass_normal;
in vec3 pass_position;
in vec3 reflectedVector;
in vec3 camera_position_pass;

in float visibility;

out vec4 color;

uniform sampler2D textureUnit;
uniform int lightsCount;

uniform vec3 skyColor;

uniform samplerCube cubemap;

struct BaseLight{
	vec3 color;
	float intensity;
};

struct Attenuation{
	float constant;
	float linear;
	float exponent;
};

struct PointLight{
	BaseLight base;
	Attenuation att;
	vec3 position;
};

struct DirectionalLight{
	BaseLight base;
	vec3 direction;
};

struct Material{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
    float specularPower;
};

uniform DirectionalLight dirLight;
uniform PointLight lights[MAX_POINT_LIGHTS];
uniform Material material;

vec4 calcLight(BaseLight base, vec3 worldPos, vec3 camPos, vec3 direction, vec3 normal) {
	float diffuseFactor = dot(-direction, normal);
	vec4 diffuseColor = vec4(0, 0, 0, 0);
	vec4 specularColor = vec4(0, 0, 0, 0);
	if(diffuseFactor > 0) {
		diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
		vec3 directionToCam = normalize(camPos - worldPos);
		vec3 reflectDirection = normalize(reflect(direction, normal));
		float specularFactor = dot(directionToCam, reflectDirection);
		specularFactor = pow(specularFactor, material.specularPower);
		if(specularFactor > 0) {
			specularColor = vec4(base.color, 1) * material.reflectance * specularFactor;
		}
	}
	return diffuseColor + specularColor * base.intensity * 0.1;
}

vec4 calcDirLight(DirectionalLight light, vec3 worldPos, vec3 normal, vec3 camPos) {
	return calcLight(light.base, worldPos, camPos, light.direction, normal);
}

vec4 calcPointLight(PointLight pointLight, vec3 worldPos, vec3 normal, vec3 camPos) {
	vec3 lightDirection = worldPos - pointLight.position;
	float distToPoint = length(lightDirection);
	if(distToPoint > 100){
		return vec4(0, 0, 0, 0);
	}
	lightDirection = normalize(lightDirection);
	vec4 color = calcLight(pointLight.base, worldPos, camPos, lightDirection, normal);
	float attenuation = pointLight.att.constant + pointLight.att.linear * distToPoint + pointLight.att.exponent * distToPoint * distToPoint;
	return color / attenuation;
}

void main(){
	vec4 textureColor = texture(textureUnit, textureCoords);

	if(textureColor.a < 0.5){
		discard;
	}

	//Textures and lights

	vec4 totalLight = material.ambient;
	vec4 color_textured = material.diffuse;

	color_textured += textureColor;

	//Light calculation
	totalLight += calcDirLight(dirLight, pass_position, pass_normal, camera_position_pass);
	for (int i = 0; i < lightsCount; ++i) {
		totalLight += calcPointLight(lights[i], pass_position, pass_normal, camera_position_pass);
	}

	vec4 reflectColor = texture(cubemap, reflectedVector);

	color = color_textured * totalLight;

	color = mix(color, reflectColor, 0.5);

	color.a = 1;

	color = mix(vec4(skyColor, 1), color, visibility);

	color = reflectColor;
}
