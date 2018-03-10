package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import alex9932.utils.gl.Display;
import alex9932.utils.gl.Vao;
import alex9932.utils.gl.Vbo;

public class PostProcessing {
	private static final float[] POSITIONS = {
		-1, 1, 0,
		-1, -1, 0,
		1, 1, 0,
		1, -1, 0};
	
	private static Vao vao;

	private static Contrast contrast;
	//private static VBlur vblur;
	//private static HBlur hblur;
	private static Bright bright;
	private static Combiner combiner;

	public static void init(Display display) {
		vao = new Vao();
		vao.put(new Vbo(0, 3, POSITIONS));
		
		//vblur = new VBlur(display, (int)display.getWidth(), (int)display.getHeight());
		//hblur = new HBlur(display, (int)display.getWidth(), (int)display.getHeight());
		bright = new Bright(display, (int)display.getWidth(), (int)display.getHeight());
		combiner = new Combiner(display, (int)display.getWidth(), (int)display.getHeight());
		contrast = new Contrast();
	}

	public static void doPostProcess(int colorMap, int depthMap) {
		start();
		
		bright.render(colorMap);
		//vblur.render(bright.getColor());
		//hblur.render(vblur.getColor());
		
		combiner.render(colorMap, bright.getColor());

		contrast.render(combiner.getColor(), 1f);
		//contrast.render(colorMap, 1f);
		
		end();
	}

	private static void start() {
		vao.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		vao.unbind();
	}
}