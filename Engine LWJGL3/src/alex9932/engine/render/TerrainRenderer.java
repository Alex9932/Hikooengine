package alex9932.engine.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import alex9932.engine.Terrain;
import alex9932.engine.utils.Resource;
import alex9932.engine.utils.Scene;
import alex9932.utils.gl.Shader;
import alex9932.vecmath.Quaternion;
import alex9932.vecmath.Vector3f;

public class TerrainRenderer extends Shader{
	Material material = new Material(new Quaternion(0.0f, 0.0f, 0.0f, 1), new Quaternion(0.0f, 0.0f, 0.0f, 1), new Quaternion(0, 0, 0, 1), 1, 0, 1);
	public TerrainRenderer() {
		super(Resource.getShader("terrain.vs.h"), Resource.getShader("terrain.ps.h"));
	}
	
	public void render(ICamera camera, Scene scene, Fog fog) {
		this.start();
		this.loadMatrix4f("proj", camera.getProjectionMatrix());
		this.loadMatrix4f("view", camera.getViewMatrix());

		this.loadInt("texture0", 0);
		this.loadInt("texture1", 1);
		this.loadInt("texture2", 2);
		this.loadInt("texture3", 3);
		this.loadInt("blendmap", 4);
		this.loadInt("normalmap0", 5);
		this.loadInt("normalmap1", 6);
		this.loadInt("normalmap2", 7);
		this.loadInt("normalmap3", 8);
		
		this.loadVector("camera_position", camera.getPosition());

		loadDirLight("dirLight", scene.getDirLight());
		for (int i = 0; i < scene.getLights().size(); i++) {
			loadPointLight("lights[" + i + "]", scene.getLights().get(i));
		}
		loadInt("lightsCount", scene.getLights().size());
		loadMaterial("material", material);
		
		loadFog(fog.getColor(), fog.getGradient(), fog.getDensity());
		
		ArrayList<Terrain> terrains = scene.getTerrains();
		for (int i = 0; i < terrains.size(); i++) {
			Terrain terrain = terrains.get(i);
			this.loadMatrix4f("model", terrain.getMatrix());
			terrain.getVao().bind();

			terrain.getTexture().texture0.connectToAndBind(0);
			terrain.getTexture().texture1.connectToAndBind(1);
			terrain.getTexture().texture2.connectToAndBind(2);
			terrain.getTexture().texture3.connectToAndBind(3);
			terrain.getTexture().blendmap.connectToAndBind(4);
			terrain.getTexture().normalmap0.connectToAndBind(5);
			terrain.getTexture().normalmap1.connectToAndBind(6);
			terrain.getTexture().normalmap2.connectToAndBind(7);
			terrain.getTexture().normalmap3.connectToAndBind(8);
			
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getVao().getIndices());
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			terrain.getVao().unbind();
		}
		
		this.stop();
	}

	@Override
	public void bindAttribs() {
		this.bindAttribute(0, "pos");
		this.bindAttribute(1, "normal");
		this.bindAttribute(2, "texCoord");
	}

	@Override
	public void bindUniformLocations() {
		this.createUniformLocation("proj");
		this.createUniformLocation("view");
		this.createUniformLocation("model");
		this.createUniformLocation("texture0");
		this.createUniformLocation("texture1");
		this.createUniformLocation("texture2");
		this.createUniformLocation("texture3");
		this.createUniformLocation("blendmap");
		this.createUniformLocation("normalmap0");
		this.createUniformLocation("normalmap1");
		this.createUniformLocation("normalmap2");
		this.createUniformLocation("normalmap3");

		this.createUniformLocation("skyColor");
		this.createUniformLocation("density");
		this.createUniformLocation("gradient");
		
		this.createUniformLocation("camera_position");
		
		this.createUniformLocation("lightsCount");
		createDirLightUniform("dirLight");
		for (int i = 0; i < Renderer.MAX_POINT_LIGHTS; i++) {
			createPointLightUniform("lights[" + i + "]");
		}
		createMaterialUniform("material");
	}

	public void loadFog(Vector3f color, float gradient, float density) {
		this.loadFloat("density", density);
		this.loadFloat("gradient", gradient);
		this.loadVector("skyColor", color);
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