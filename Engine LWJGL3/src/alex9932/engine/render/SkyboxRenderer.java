package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import alex9932.engine.utils.Resource;
import alex9932.utils.gl.Shader;
import alex9932.utils.gl.Vao;
import alex9932.utils.gl.Vbo;
import alex9932.utils.gl.texture.Texture;
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
				Resource.getTexture("skybox/day/posX.png"), Resource.getTexture("skybox/day/negX.png"),
				Resource.getTexture("skybox/day/posY.png"), Resource.getTexture("skybox/day/negY.png"),
				Resource.getTexture("skybox/day/posZ.png"), Resource.getTexture("skybox/day/negZ.png")});
		skyboxTextureNight = new Texture(new String[]{
				Resource.getTexture("skybox/night/posX.png"), Resource.getTexture("skybox/night/negX.png"),
				Resource.getTexture("skybox/night/posY.png"), Resource.getTexture("skybox/night/negY.png"),
				Resource.getTexture("skybox/night/posZ.png"), Resource.getTexture("skybox/night/negZ.png")});
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
		skyboxTextureDay.bindAsCubeMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		skyboxTextureNight.bindAsCubeMap();
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