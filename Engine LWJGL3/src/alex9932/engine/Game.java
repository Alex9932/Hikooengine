package alex9932.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

import alex9932.engine.render.DirLight;
import alex9932.engine.render.PointLight;
import alex9932.utils.NVGUtils;
import alex9932.vecmath.Vector3f;

// IGame.java implementation
public class Game implements IGame{
	private Engine engine;
	private PointLight light;
	private PointLight light1;
	private PointLight light0;
	private Vector3f forward = new Vector3f();

	public Game() throws Exception {
		engine = new Engine();
		engine.setGame(this);
		engine.start();
	}

	@Override
	public void startup() {
		try {
			engine.loadLevel("test");
			
			DirLight dirLight = new DirLight(new Vector3f(1, 1, 1), new Vector3f(-1, -1, -1), 0.5f);
			engine.getScene().setDirLight(dirLight);
			
			light = new PointLight(new Vector3f(0, 1, 0), new Vector3f(0, 0.5f, 1), 5f, 1, 1, 1);
			light1 = new PointLight(new Vector3f(0, 1, 0), new Vector3f(1, 0.5f, 0), 3f, 1, 0, 1);
			light0 = new PointLight(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), 5f, 1, 0, 1);
			engine.getScene().addLight(light);
			engine.getScene().addLight(light1);
			//engine.getScene().addLight(light0);
			engine.getDisplay().getEventSystem().setGrabbed(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		NVGUtils.registerFont("gamedata/textures/fonts/gui.ttf", "font");
	}

	@Override
	public void loop() {
		engine.getPlayer().setRotation(engine.getPlayer().getRotation() - engine.getDisplay().getEventSystem().getMouseDX() * 0.03f);
		engine.getPlayer().setRotationY(engine.getPlayer().getRotationY() - engine.getDisplay().getEventSystem().getMouseDY() * 0.03f);

		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_UP)){
			engine.getPlayer().setRotationY(engine.getPlayer().getRotationY() - 0.1f);
		}
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_DOWN)){
			engine.getPlayer().setRotationY(engine.getPlayer().getRotationY() + 0.1f);
		}
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_LEFT)){
			engine.getPlayer().setRotation(engine.getPlayer().getRotation() - 0.1f);
		}
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_RIGHT)){
			engine.getPlayer().setRotation(engine.getPlayer().getRotation() + 0.1f);
		}
		
		forward.x = (float)-Math.sin(Math.toRadians(-engine.getRenderer().getCamera().getAnglex()));
		forward.z = (float)-Math.cos(Math.toRadians(-engine.getRenderer().getCamera().getAnglex()));
		forward.normalize();
		
		float speed = 0.01f;
		float coof = 1;

		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)){
			speed = 0.1f;
		}
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_LEFT_ALT)){
			speed = 0.003f;
		}
		
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_SPACE)){
			engine.getPlayer().jump();
		}
		
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_W)){
			engine.getPlayer().applayImpulse(new Vector3f(forward.x * speed, forward.y * speed, forward.z * speed));
		}
		
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_S)){
			engine.getPlayer().applayImpulse(new Vector3f(-forward.x * speed, -forward.y * speed, -forward.z * speed));
		}
		
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_A)){
			Vector3f tmp = Vector3f.cross(new Vector3f(0, 1, 0), forward);
			engine.getPlayer().applayImpulse(new Vector3f(-tmp.x * speed * coof, -tmp.y * speed * coof, -tmp.z * speed * coof));
		}
		
		if(engine.getDisplay().getEventSystem().isKeyDown(GLFW.GLFW_KEY_D)){
			Vector3f tmp = Vector3f.cross(forward, new Vector3f(0, 1, 0));
			engine.getPlayer().applayImpulse(new Vector3f(-tmp.x * speed * coof, -tmp.y * speed * coof, -tmp.z * speed * coof));
		}
		
		light.setPosition(new Vector3f(((float)Math.sin(System.nanoTime()*0.000000001f)*3) + 5, 1, 4));
		light1.setPosition(new Vector3f(((float)Math.sin(System.nanoTime()*0.000000001f + 3)*3) + 5, 1, 4));
		light0.setPosition(new Vector3f(engine.getRenderer().getCamera().getX(), engine.getRenderer().getCamera().getY() + 1.6f, engine.getRenderer().getCamera().getZ()));

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		NanoVG.nvgBeginFrame(NVGUtils.getVg(), 1280, 720, 1);

		NVGUtils.drawGlowString("Hello, world!", "font", 10, 25, 25, 10);
		
		NanoVG.nvgEndFrame(NVGUtils.getVg());
	}

	@Override
	public void shutdown() {
		
	}
}