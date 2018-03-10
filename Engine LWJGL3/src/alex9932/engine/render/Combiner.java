package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import alex9932.utils.gl.Display;

public class Combiner {
	private PPShader shader;
	private Image image;
	
	public Combiner(Display display, int width, int height) {
		shader = new PPShader("gamedata/shaders/postprocessing/vertex.vs.h", "gamedata/shaders/postprocessing/combine.ps.h");
		image = new Image(display, width, height);
		shader.createUniformLocation("tex0");
		shader.createUniformLocation("tex1");
	}
	
	public void render(int texture, int texture2) {
		shader.start();
		shader.loadInt("tex0", 0);
		shader.loadInt("tex1", 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture2);
		image.renderQuad();
		shader.stop();
	}
	
	public int getColor() {
		return image.getOutputTexture();
	}
}