package alex9932.utils.gl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

public class Vbo {
	private int id;
	private int buffer;
	private int coordSize;
	private FloatBuffer data;

	public Vbo(int id, int coordSize, float[] data) {
		this.id = id;
		this.coordSize = coordSize;
		this.data = convertData(data);
	}

	private FloatBuffer convertData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void create() {
		this.buffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.buffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.data, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(this.id, this.coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void cleanUp(){
		GL15.glDeleteBuffers(this.buffer);
	}

	public void destroy() {
		GL15.glDeleteBuffers(this.id);
	}
}