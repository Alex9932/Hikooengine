package alex9932.engine;

import alex9932.utils.gl.Vao;
import alex9932.utils.gl.Vbo;
import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Vector2f;
import alex9932.vecmath.Vector3f;

public class Terrain {
	private static final int VERTEX_COUNT = 512;

	public static final float SIZE = 0;
	
	private Matrix4f matrix = new Matrix4f();
	private Vao vao = new Vao();
	private float[][] heights;
	private int size;
	private float x, z;
	private TerrainTexture texture;
	
	public Terrain(TerrainTexture texture, float x, float z, int size, float[][] heightmap) {
		this.matrix.setIdentity();
		this.matrix.translate(x, 0, z);
		this.heights = heightmap;
		this.texture = texture;
		this.size = size;
		this.x = x;
		this.z = z;
		this.generateVAO();
	}
	
	private void generateVAO() {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * size;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * size;
				vertices[vertexPointer * 3 + 1] = getHeight(i, j);
				heights[j][i] = vertices[vertexPointer * 3 + 1];
				Vector3f normal = calcNormal(i, j);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = ((float) j / ((float) VERTEX_COUNT - 1));
				textureCoords[vertexPointer * 2 + 1] = ((float) i / ((float) VERTEX_COUNT - 1));
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		this.vao.put(new Vbo(0, 3, vertices));
		this.vao.put(new Vbo(1, 3, normals));
		this.vao.put(new Vbo(2, 2, textureCoords));
		this.vao.setIndices(indices);
	}

	private Vector3f calcNormal(int x, int z) {
		Vector3f normal = new Vector3f();
		normal.x = getHeight(x - 1, z) - getHeight(x + 1, z);
		normal.z = getHeight(x, z - 1) - getHeight(x, z + 1);
		normal.y = 10f;
		normal.normalize();
		return normal;
	}

	private float getHeight(int x, int z) {
		int a = x;
		int b = z;
		
		if(a < 0){
			a = 0;
		}
		if(a >= heights.length){
			a = heights.length - 1;
		}
		
		if(b < 0){
			b = 0;
		}
		if(b >= heights[a].length){
			b = heights[a].length - 1;
		}
		
		return heights[a][b];
	}
	
	public float getHeight(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = size / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;

		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(
				new Vector3f(0, heights[gridX][gridZ], 0),
				new Vector3f(1, heights[gridX + 1][gridZ], 0),
				new Vector3f(0, heights[gridX][gridZ + 1], 1),
				new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(
				new Vector3f(1, heights[gridX + 1][gridZ], 0),
				new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
				new Vector3f(0, heights[gridX][gridZ + 1], 1),
				new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	public Vao getVao() {
		return vao;
	}
	
	public float[][] getHeights() {
		return heights;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public Matrix4f getMatrix() {
		return matrix;
	}
	
	public TerrainTexture getTexture() {
		return texture;
	}
}