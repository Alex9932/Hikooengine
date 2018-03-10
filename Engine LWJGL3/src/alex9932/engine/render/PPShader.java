package alex9932.engine.render;

import alex9932.utils.gl.Shader;

public class PPShader extends Shader{

	public PPShader(String vert, String frag) {
		super(vert, frag);
	}

	@Override
	public void bindAttribs() {
		this.bindAttribute(0, "position");
	}

	@Override
	public void bindUniformLocations() {
		
	}
}