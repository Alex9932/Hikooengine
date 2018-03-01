package alex9932.engine.render;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import alex9932.engine.entity.Camera;
import alex9932.engine.entity.Entity;
import alex9932.engine.entity.EntityPlayer;
import alex9932.engine.utils.KeyListenerImpl;
import alex9932.engine.utils.Resource;
import alex9932.engine.utils.Scene;
import alex9932.utils.gl.Display;
import alex9932.vecmath.Matrix4f;

public class Renderer {
	public static final int MAX_POINT_LIGHTS = 40;
	
	private int POLYGON_MODE = GL11.GL_LINES;
	private MainShader shader;
	private GuiShader guiShader;
	private Camera camera;
	private TerrainRenderer terrainRenderer;
	
	private Matrix4f projGui = Matrix4f.createOrthoMatrix(0, 1280, 720, 0, -1, 10);
	
	public Renderer(Display display) {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//GL11.glEnable(GL11.GL_STENCIL_TEST);
		
		display.getEventSystem().addKeyListener(new KeyListenerImpl(){
			@Override
			public void keyPressed(int key) {
				if(key == GLFW.GLFW_KEY_F11){
					if(POLYGON_MODE == GL11.GL_LINE){
						System.out.println(false);
						POLYGON_MODE = GL11.GL_LINES;
						GL11.glEnable(GL11.GL_CULL_FACE);
						GL11.glCullFace(GL11.GL_BACK);
						
					}else{
						System.out.println(true);
						GL11.glEnable(GL11.GL_POLYGON_MODE);
						GL11.glDisable(GL11.GL_CULL_FACE);
						POLYGON_MODE = GL11.GL_LINE;
					}
				}
			}
		});
		shader = new MainShader(Resource.getShader("shader.vs.h"), Resource.getShader("shader.ps.h"));
		guiShader = new GuiShader();
		camera = new Camera(0, 0, 0, 70, display.getAspect());
		camera.setOffset(1.6f);
		
		terrainRenderer = new TerrainRenderer();
	}
	
	public void render(Scene scene, EntityPlayer player) {
		terrainRenderer.render(camera, scene);
		shader.start();
		shader.loadMatrix4f("proj", camera.getProjectionMatrix());
		shader.loadMatrix4f("view", camera.getViewMatrix());

		for (int i = 0; i < scene.getLights().size(); i++) {
			shader.loadPointLight("lights[" + i + "]", scene.getLights().get(i));
		}
		shader.loadInt("lightsCount", scene.getLights().size());
		shader.loadVector("camera_position", camera.getPosition());
		
		shader.loadDirLight("dirLight", scene.getDirLight());
		
		ArrayList<Entity> entitys = scene.getEntitys();

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, POLYGON_MODE);
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
		/**********************************************/
		guiShader.start();
		guiShader.loadProjection(projGui);
		
		guiShader.stop();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public int getPOLYGON_MODE() {
		return POLYGON_MODE;
	}

	public void destroy() {
		shader.destroy();
		terrainRenderer.destroy();
	}

	public GuiShader getGuiShader() {
		return guiShader;
	}
}