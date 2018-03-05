#version 400

in vec3 textureCoords;

out vec4 out_Color;

uniform samplerCube cubeMap_day;
uniform samplerCube cubeMap_night;
uniform vec3 fogColor;
uniform float time;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

void main(void){
    vec4 dayColor = texture(cubeMap_day, textureCoords);
    vec4 nightColor = texture(cubeMap_night, textureCoords);

    float toMix = (sin(tan(cos(time))) + 1.0) / 2.0;

    out_Color = mix(dayColor, nightColor, toMix);

    float factor = (textureCoords.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0, 1);
    out_Color = mix(vec4(fogColor, 1), out_Color, factor);

}
