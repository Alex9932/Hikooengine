package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Contrast {
	private PPShader shader;
	private Image image;
	
	public Contrast() {
		shader = new PPShader("gamedata/shaders/postprocessing/vertex.vs.h", "gamedata/shaders/postprocessing/contrast.ps.h");
		shader.createUniformLocation("contrast");
		image = new Image();
	}
	
	public void render(int texture, float contrast) {
		shader.start();
		shader.loadFloat("contrast", contrast);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		image.renderQuad();
		shader.stop();
	}
}