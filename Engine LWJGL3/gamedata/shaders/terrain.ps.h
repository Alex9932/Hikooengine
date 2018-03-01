#version 330 core

#define MAX_POINT_LIGHTS 40
#define TILING 150

in vec2 textureCoord;
in vec3 pass_normal;
in vec3 pass_position;

in float visibility;

out vec4 color;

uniform sampler2D texture0;
uniform sampler2D texture1;
uniform sampler2D texture2;
uniform sampler2D texture3;
uniform sampler2D blendmap;
uniform sampler2D normalmap0;
uniform sampler2D normalmap1;
uniform sampler2D normalmap2;
uniform sampler2D normalmap3;
uniform int lightsCount;
uniform vec3 camera_position;

uniform vec3 skyColor;

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
	return diffuseColor + specularColor;
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
	//Textures
	vec4 blend = texture(blendmap, textureCoord);
	vec4 texture0Color = texture(texture0, textureCoord * TILING) * ( 1 - (blend.r + blend.g + blend.b));
	vec4 texture1Color = texture(texture1, textureCoord * TILING) * blend.r;
	vec4 texture2Color = texture(texture2, textureCoord * TILING) * blend.g;
	vec4 texture3Color = texture(texture3, textureCoord * TILING) * blend.b;
	vec4 normalmap0Color = (texture(normalmap0, textureCoord * TILING) * 2 - 1) * ( 1 - (blend.r + blend.g + blend.b));
	vec4 normalmap1Color = (texture(normalmap1, textureCoord * TILING) * 2 - 1) * blend.r;
	vec4 normalmap2Color = (texture(normalmap2, textureCoord * TILING) * 2 - 1) * blend.g;
	vec4 normalmap3Color = (texture(normalmap3, textureCoord * TILING) * 2 - 1) * blend.b;

	//Normals
	vec4 normal_color = normalmap0Color + normalmap1Color + normalmap2Color + normalmap3Color;
	vec3 normal = normalize(pass_normal + normal_color.xyz);
	normal = pass_normal;

	//Textures and lights
	vec4 textureColor = texture0Color + texture1Color + texture2Color + texture3Color;

	vec4 totalLight = material.ambient;
	vec4 color_textured = material.diffuse;

	color_textured += textureColor;

	//Light calculation
	totalLight += calcDirLight(dirLight, pass_position, pass_normal, camera_position);
	for (int i = 0; i < lightsCount; ++i) {
		totalLight += calcPointLight(lights[i], pass_position, pass_normal, camera_position);
	}

	color = color_textured * totalLight;

	color = mix(vec4(skyColor, 1), color, visibility);
}
