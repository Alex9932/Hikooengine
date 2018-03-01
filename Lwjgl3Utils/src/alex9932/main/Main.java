package alex9932.main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.opengl.GL11;

import alex9932.utils.NVGUtils;
import alex9932.utils.gl.Display;
import alex9932.utils.gl.Shader;
import alex9932.utils.gl.Texture;
import alex9932.vecmath.Matrix4f;

public class Main {
	static NVGColor color;
	static float anglex, angley;
	
	public static void main(String[] args) {
		Display display = new Display(1280, 720, "Game");
		//Timer timer = new Timer(1000);
		
		color = NVGUtils.color(0, 1, 1, 1);
		
		Texture texture = new Texture("gamedata/textures/default.png");

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		NVGUtils.registerFont("gamedata/textures/fonts/arial.ttf", "arial");
		NVGUtils.registerFont("gamedata/textures/fonts/gui.ttf", "gui");
		
		Shader shader = new Shader("gamedata/shaders/shader.vs", "gamedata/shaders/shader.ps") {
			
			@Override
			public void bindAttribs() {
				this.bindAttribute(0, "pos");
			}
			
			@Override
			public void bindUniformLocations() {
				this.createUniformLocation("proj");
				this.createUniformLocation("view");
				this.createUniformLocation("model");
			}
		};
		
		Matrix4f proj = Matrix4f.createProjMatrix(70f, 0.01f, 1000f, display.getAspect());
		
		display.getEventSystem().setGrabbed(true);
		while (!display.isCloseRequested()) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
			
			//float delta = timer.getElapsedTime();
			
			if(display.getEventSystem().isKeyDown(GLFW.GLFW_KEY_UP)){
				angley -= 100f;
			}
			if(display.getEventSystem().isKeyDown(GLFW.GLFW_KEY_DOWN)){
				angley += 100f;
			}
			if(display.getEventSystem().isKeyDown(GLFW.GLFW_KEY_LEFT)){
				anglex -= 100f;
			}
			if(display.getEventSystem().isKeyDown(GLFW.GLFW_KEY_RIGHT)){
				anglex += 100f;
			}
			
			GL11.glColor3f(1, 1, 1);

			GL11.glLoadIdentity();
			GL11.glOrtho(-1, 1, -1, 1, -1, 1);

			anglex -= display.getEventSystem().getMouseDX() * 0.1f;
			angley -= display.getEventSystem().getMouseDY() * 0.1f;
			
			Matrix4f view = Matrix4f.createViewMatrix(0, 0, 2, anglex, angley);
			Matrix4f model = Matrix4f.createModelMatrix(0, 0, 0, 0, 0, 1);
			
			shader.start();
			shader.loadMatrix4f("proj", proj);
			shader.loadMatrix4f("view", view);
			shader.loadMatrix4f("model", model);
			texture.bind();
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(-0.5f, -0.5f);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(0.5f, -0.5f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(0.5f, 0.5f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(-0.5f, 0.5f);
			GL11.glEnd();
			shader.stop();
			
			long vg = NVGUtils.getVg();
			NanoVG.nvgBeginFrame(vg, 1280, 720, 1);

			NanoVG.nvgFillColor(vg, color);
			NVGUtils.drawGlowString("NanoVG!", "arial", 100, 40, 40, (float)(Math.sin(System.nanoTime() * 0.00000001f) + 1f) * 10f);
			NVGUtils.drawGlowString("More fonts!", "gui", 100, 100, 45, (float)(Math.sin(System.nanoTime() * 0.00000001f) + 1f) * 10f);
			
			NanoVG.nvgEndFrame(vg);

			display.update();
		}
		NVGUtils.destroy();
		
		display.destroy();
	}
}