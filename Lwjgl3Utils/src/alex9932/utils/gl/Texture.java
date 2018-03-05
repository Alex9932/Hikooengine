package alex9932.utils.gl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class Texture {
	public static final int REPEAT = GL11.GL_REPEAT;
	public static final int CLAMP_TO_EDGE = GL12.GL_CLAMP_TO_EDGE;
	private int id;
	private int width;
	private int height;

	public Texture(int tex){
		this.id = tex;
	}

	public Texture(String path){
		this(path, GL12.GL_CLAMP_TO_EDGE);
	}
	
	public Texture(String path, int glMode) {
		System.out.print("[Texture] Loading texture: " + path + " ...  ");
		ByteBuffer data = decodeTextureFile(path);
		loadToGL(width, height, data, glMode);
		System.out.println("OK!");
	}
	
	public Texture(String[] path){
		System.out.print("[Texture] Loading textures: " + path[0] + " ...  ");
		loadCubeMap(path);
		System.out.println("OK!");
	}

	public int getId() {
		return this.id;
	}

	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}

	public void bindAsCubeMap() {
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.id);
	}

	public void connectTo(int i) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
	}

	public void connectToAndBind(int i) {
		connectTo(i);
		bind();
	}

	private void loadToGL(int width, int height, ByteBuffer buffer, int glMode) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.id = GL11.glGenTextures();
		this.bind();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, glMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, glMode);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.4f);
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);

		for (int i = 0; i < textureFiles.length; i++) {
			ByteBuffer data = decodeTextureFile(textureFiles[i]);
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
		}
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		return texID;
	}

	private ByteBuffer decodeTextureFile(String path) {

		try {
			BufferedImage image = ImageIO.read(new File(path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			
			byte[] pixels = new byte[width * height * 4];
			int i = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int color = image.getRGB(x, y);
					pixels[(i * 4) + 0] = (byte)((color >> 16) & 0xFF);
					pixels[(i * 4) + 1] = (byte)((color >> 8) & 0xFF);
					pixels[(i * 4) + 2] = (byte)(color & 0xFF);
					pixels[(i * 4) + 3] = (byte)((color >> 24) & 0xFF);
					i++;
				}
			}

			ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length);
			buffer.put(pixels);
			buffer.flip();
			return buffer;
		} catch (IOException e) {
			System.out.println("FAILED!");
			e.printStackTrace();
		}
		return null;
	}
}