package alex9932.engine.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import alex9932.engine.entity.Camera;
import alex9932.engine.entity.Entity;
import alex9932.engine.entity.EntityPlayer;
import alex9932.engine.utils.Resource;
import alex9932.engine.utils.Scene;
import alex9932.utils.gl.Display;
import alex9932.utils.gl.texture.Texture;
import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Vector3f;

public class Renderer {
	public static final int MAX_POINT_LIGHTS = 40;
	
	private MainShader shader;
	private GuiShader guiShader;
	private Camera camera;
	private TerrainRenderer terrainRenderer;
	private SkyboxRenderer skyboxRenderer;
	
	private Fog fog;
	
	private Matrix4f projGui = Matrix4f.createOrthoMatrix(0, 1280, 720, 0, -1, 10);

	private Texture cubemap;
	
	public float time;

	private Fbo frameBuffer;
	
	public Renderer(Display display) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(GL11.GL_STENCIL_TEST);
		
		this.fog = new Fog(new Vector3f(0.3f, 0.6f, 0.8f), 0.007f, 1.5f);
		
		PostProcessing.init(display);
		
		shader = new MainShader(Resource.getShader("shader.vs.h"), Resource.getShader("shader.ps.h"));
		guiShader = new GuiShader();
		camera = new Camera(0, 0, 0, 70, display.getAspect());
		camera.setOffset(1.6f);
		
		cubemap = new Texture(new String[]{
				Resource.getTexture("skybox/day/posX.png"), Resource.getTexture("skybox/day/negX.png"),
				Resource.getTexture("skybox/day/posY.png"), Resource.getTexture("skybox/day/negY.png"),
				Resource.getTexture("skybox/day/posZ.png"), Resource.getTexture("skybox/day/negZ.png")});
		
		terrainRenderer = new TerrainRenderer();
		skyboxRenderer = new SkyboxRenderer();
		
		frameBuffer = new Fbo(display, (int)display.getWidth(), (int)display.getHeight());
	}
	
	public void render(Scene scene, EntityPlayer player) {
		frameBuffer.bind();
		
		GL11.glClearColor(fog.getColor().x, fog.getColor().y, fog.getColor().z, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		skyboxRenderer.render(camera, fog.getColor(), 2.5f);
		
		terrainRenderer.render(camera, scene, fog);
		shader.start();
		shader.loadMatrix4f("proj", camera.getProjectionMatrix());
		shader.loadMatrix4f("view", camera.getViewMatrix());
		shader.loadFog(fog.getColor(), fog.getGradient(), fog.getDensity());

		for (int i = 0; i < scene.getLights().size(); i++) {
			shader.loadPointLight("lights[" + i + "]", scene.getLights().get(i));
		}
		shader.loadInt("lightsCount", scene.getLights().size());
		shader.loadVector("camera_position", camera.getPosition());
		
		shader.loadDirLight("dirLight", scene.getDirLight());
		
		shader.loadInt("cubemap", 1);
		
		ArrayList<Entity> entitys = scene.getEntitys();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		cubemap.bindAsCubeMap();
		
		for (int i = 0; i < entitys.size(); i++) {
			Mesh mesh = entitys.get(i).getMesh();
			if(mesh != null){
				shader.loadMatrix4f("model", entitys.get(i).getModelMatrix());
				shader.loadMaterial("material", entitys.get(i).getMaterial());
				if(entitys.get(i).getTexture() != null){
					entitys.get(i).getTexture().connectToAndBind(0);
				}else{
					GL13.glActiveTexture(GL13.GL_TEXTURE0);
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				}
				if(entitys.get(i).isTransparent()){
					GL11.glDisable(GL11.GL_CULL_FACE);
				}
				mesh.render();
				if(entitys.get(i).isTransparent()){
					GL11.glEnable(GL11.GL_CULL_FACE);
				}
			}
		}
		
		camera.moveTo(player);
		camera.update();
		
		shader.stop();
		
		frameBuffer.unbind();
		/**********************************************/

		PostProcessing.doPostProcess(frameBuffer.getRenderTexture(), frameBuffer.getDepthTexture());
		
		guiShader.start();
		guiShader.loadProjection(projGui);
		
		guiShader.stop();
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void destroy() {
		shader.destroy();
		terrainRenderer.destroy();
	}

	public GuiShader getGuiShader() {
		return guiShader;
	}

	public void setQubeMap(Texture cubeMap) {
		this.cubemap = cubeMap;
	}
	
	public void tickTime() {
		this.time += 0.001f;
	}
}