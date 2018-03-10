package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import alex9932.utils.gl.Display;

public class HBlur {
	private PPShader shader;
	private Image image;
	
	public HBlur(Display display, int width, int height) {
		shader = new PPShader("gamedata/shaders/postprocessing/horizontalBlur.vs.h", "gamedata/shaders/postprocessing/blur.ps.h");
		shader.createUniformLocation("targetWidth");
		image = new Image(display, width, height);
	}
	
	public void render(int texture) {
		shader.start();
		shader.loadFloat("targetWidth", image.getWidth() / 4);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		image.renderQuad();
		shader.stop();
	}
	
	public int getColor() {
		return image.getOutputTexture();
	}
}