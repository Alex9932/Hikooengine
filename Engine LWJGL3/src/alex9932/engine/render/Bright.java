package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import alex9932.utils.gl.Display;

public class Bright {
	private PPShader shader;
	private Image image;
	
	public Bright(Display display, int width, int height) {
		shader = new PPShader("gamedata/shaders/postprocessing/vertex.vs.h", "gamedata/shaders/postprocessing/bright.ps.h");
		image = new Image(display, width, height);
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		image.renderQuad();
		shader.stop();
	}
	
	public int getColor() {
		return image.getOutputTexture();
	}
}