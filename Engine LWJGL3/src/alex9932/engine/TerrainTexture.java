package alex9932.engine;

import alex9932.utils.gl.texture.Texture;

public class TerrainTexture {
	public Texture texture0;
	public Texture texture1;
	public Texture texture2;
	public Texture texture3;
	public Texture blendmap;
	public Texture normalmap0;
	public Texture normalmap1;
	public Texture normalmap2;
	public Texture normalmap3;
	
	public TerrainTexture(Texture texture0, Texture texture1, Texture texture2, Texture texture3, Texture blendmap, Texture normalmap0, Texture normalmap1, Texture normalmap2, Texture normalmap3) {
		this.texture0 = texture0;
		this.texture1 = texture1;
		this.texture2 = texture2;
		this.texture3 = texture3;
		this.blendmap = blendmap;
		this.normalmap0 = normalmap0;
		this.normalmap1 = normalmap1;
		this.normalmap2 = normalmap2;
		this.normalmap3 = normalmap3;
	}
	
	public Texture getTexture0() {
		return texture0;
	}
	
	public Texture getTexture1() {
		return texture1;
	}
	
	public Texture getTexture2() {
		return texture2;
	}
	
	public Texture getTexture3() {
		return texture3;
	}
	
	public Texture getBlendmap() {
		return blendmap;
	}
	
	public Texture getNormalmap0() {
		return normalmap0;
	}
	
	public Texture getNormalmap1() {
		return normalmap1;
	}
	
	public Texture getNormalmap2() {
		return normalmap2;
	}
	
	public Texture getNormalmap3() {
		return normalmap3;
	}
}