package alex9932.engine.render;

import alex9932.utils.gl.Shader;

public class MainShader extends Shader{

	public MainShader(String vs, String ps) {
		super(vs, ps);
	}

	@Override
	public void bindAttribs() {
		this.bindAttribute(0, "pos");
		this.bindAttribute(1, "tex");
		this.bindAttribute(2, "normal");
	}

	@Override
	public void bindUniformLocations() {
		this.createUniformLocation("proj");
		this.createUniformLocation("view");
		this.createUniformLocation("model");

		this.createUniformLocation("lightsCount");
		this.createUniformLocation("camera_position");
		createDirLightUniform("dirLight");
		for (int i = 0; i < Renderer.MAX_POINT_LIGHTS; i++) {
			createPointLightUniform("lights[" + i + "]");
		}
		createMaterialUniform("material");
	}

	public void createPointLightUniform(String uniformName) {
		createUniformLocation(uniformName + ".base.color");
		createUniformLocation(uniformName + ".position");
		createUniformLocation(uniformName + ".base.intensity");
		createUniformLocation(uniformName + ".att.constant");
		createUniformLocation(uniformName + ".att.linear");
		createUniformLocation(uniformName + ".att.exponent");
	}

	public void createDirLightUniform(String uniformName) {
		createUniformLocation(uniformName + ".base.color");
		createUniformLocation(uniformName + ".base.intensity");
		createUniformLocation(uniformName + ".direction");
	}
	
	public void createMaterialUniform(String uniformName) {
		createUniformLocation(uniformName + ".ambient");
		createUniformLocation(uniformName + ".diffuse");
		createUniformLocation(uniformName + ".specular");
		createUniformLocation(uniformName + ".hasTexture");
		createUniformLocation(uniformName + ".reflectance");
		createUniformLocation(uniformName + ".specularPower");
	}
	
	public void loadMaterial(String uniformName, Material material) {
		this.loadVector(uniformName + ".ambient", material.getAmbient());
		this.loadVector(uniformName + ".diffuse", material.getDiffuse());
		this.loadVector(uniformName + ".specular", material.getSpecular());
		this.loadInt(uniformName + ".hasTexture", material.getHasTexture());
		this.loadFloat(uniformName + ".reflectance", material.getReflectance());
		this.loadFloat(uniformName + ".specularPower", material.getSpecularPower());
	}

	public void loadPointLight(String uniformName, PointLight light) {
		this.loadVector(uniformName + ".base.color", light.getColor());
		this.loadVector(uniformName + ".position", light.getPosition());
		this.loadFloat(uniformName + ".base.intensity", light.getIntensity());
		this.loadFloat(uniformName + ".att.constant", light.getConstant());
		this.loadFloat(uniformName + ".att.linear", light.getLinear());
		this.loadFloat(uniformName + ".att.exponent", light.getExponent());
	}

	public void loadDirLight(String uniformName, DirLight light) {
		this.loadVector(uniformName + ".base.color", light.getColor());
		this.loadFloat(uniformName + ".base.intensity", light.getIntensity());
		this.loadVector(uniformName + ".direction", light.getDirection());
	}
}