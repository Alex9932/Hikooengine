package alex9932.engine.render;

import org.lwjgl.opengl.GL11;

import alex9932.utils.gl.Display;
import alex9932.utils.gl.Fbo;

public class Image {
	private Fbo fbo;

	protected Image(Display display, int width, int height) {
		this.fbo = new Fbo(display, width, height);
	}

	protected Image() {
	}

	protected void renderQuad() {
		if (fbo != null) {
			fbo.bind();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbind();
		}
	}

	protected int getOutputTexture() {
		return fbo.getRenderTexture();
	}

	public float getWidth() {
		return fbo.getWidth();
	}

	public float getHeight() {
		return fbo.getHeight();
	}
}