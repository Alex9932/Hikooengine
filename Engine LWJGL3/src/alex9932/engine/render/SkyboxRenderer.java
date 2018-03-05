package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import alex9932.utils.gl.Shader;
import alex9932.utils.gl.Texture;
import alex9932.utils.gl.Vao;
import alex9932.utils.gl.Vbo;
import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Vector3f;

public class SkyboxRenderer extends Shader{
	private static final float SIZE = 500f;
	private static final float[] VERTICES = {
		-SIZE, SIZE, -SIZE,
		-SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		
		-SIZE, -SIZE, SIZE,
		-SIZE, -SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,
		-SIZE, SIZE, SIZE,
		-SIZE, -SIZE, SIZE,
		
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		
		-SIZE, -SIZE, SIZE,
		-SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		SIZE, -SIZE, SIZE,
		-SIZE, -SIZE, SIZE,
		
		-SIZE, SIZE, -SIZE,
		SIZE, SIZE, -SIZE,
		SIZE, SIZE, SIZE,
		SIZE, SIZE, SIZE,
		-SIZE, SIZE, SIZE,
		-SIZE, SIZE, -SIZE,
		
		-SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		SIZE, -SIZE, -SIZE,
		SIZE, -SIZE, -SIZE,
		-SIZE, -SIZE, SIZE,
		SIZE, -SIZE, SIZE
	};
	
	private Vao vao;
	private Matrix4f model = new Matrix4f();
	private Texture skyboxTextureDay;
	private Texture skyboxTextureNight;
	
	public SkyboxRenderer() {
		super("gamedata/shaders/skybox.vs.h", "gamedata/shaders/skybox.ps.h");
		vao = new Vao();
		vao.put(new Vbo(0, 3, VERTICES));

		skyboxTextureDay = new Texture(new String[]{
				"./gamedata/textures/skybox/day/posX.png", "./gamedata/textures/skybox/day/negX.png",
				"./gamedata/textures/skybox/day/posY.png", "./gamedata/textures/skybox/day/negY.png",
				"./gamedata/textures/skybox/day/posZ.png", "./gamedata/textures/skybox/day/negZ.png"});
		skyboxTextureNight = new Texture(new String[]{
				"./gamedata/textures/skybox/night/posX.png", "./gamedata/textures/skybox/night/negX.png",
				"./gamedata/textures/skybox/night/posY.png", "./gamedata/textures/skybox/night/negY.png",
				"./gamedata/textures/skybox/night/posZ.png", "./gamedata/textures/skybox/night/negZ.png"});
	}
	
	public void render(ICamera camera, Vector3f fogColor, float time) {
		start();
		vao.bind();
		model.setIdentity();
		model.translate(camera.getPosition());
		this.loadMatrix4f("proj", camera.getProjectionMatrix());
		this.loadMatrix4f("view", camera.getViewMatrix());
		this.loadMatrix4f("model", model);
		this.loadVector("fogColor", fogColor);
		this.loadInt("cubeMap_day", 0);
		this.loadInt("cubeMap_night", 1);
		this.loadFloat("time", time);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skyboxTextureDay.getId());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skyboxTextureNight.getId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, VERTICES.length / 3);
		GL20.glDisableVertexAttribArray(0);
		vao.unbind();
		stop();
	}

	@Override
	public void bindAttribs() {
		this.bindAttribute(0, "position");
	}

	@Override
	public void bindUniformLocations() {
		this.createUniformLocation("proj");
		this.createUniformLocation("view");
		this.createUniformLocation("model");
		this.createUniformLocation("cubeMap_day");
		this.createUniformLocation("cubeMap_night");
		this.createUniformLocation("time");
		this.createUniformLocation("fogColor");
	}
}