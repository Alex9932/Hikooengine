package alex9932.engine.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import alex9932.utils.gl.Vao;
import alex9932.utils.gl.Vbo;

public class Mesh {
	private Vao vao;

	public Mesh(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		vao = new Vao();
		vao.put(new Vbo(0, 3, vertices));
		vao.put(new Vbo(1, 2, textureCoords));
		vao.put(new Vbo(2, 3, normals));
		vao.setIndices(indices);
	}
	
	public void render() {
		vao.bind();
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndices());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		vao.unbind();
	}
}