package alex9932.engine.render;

import alex9932.engine.utils.Resource;
import alex9932.utils.gl.Shader;
import alex9932.vecmath.Matrix4f;

public class GuiShader extends Shader {
	public GuiShader() {
		super(Resource.getShader("gui.vs"), Resource.getShader("gui.ps"));
	}

	@Override
	public void bindAttribs() {
		this.bindAttribute(0, "pos");
	}

	@Override
	public void bindUniformLocations() {
		this.createUniformLocation("proj");
	}

	public void loadProjection(Matrix4f matrix) {
		this.loadMatrix4f("proj", matrix);
	}
}