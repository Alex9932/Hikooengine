package alex9932.engine;

public class TerrainModel {
	private TerrainTexture texture;
	private float[][] heightmap;
	
	public TerrainModel(TerrainTexture texture, float[][] heightmap) {
		this.texture = texture;
		this.heightmap = heightmap;
	}
	
	public float[][] getHeightmap() {
		return heightmap;
	}
	
	public TerrainTexture getTexture() {
		return texture;
	}
}