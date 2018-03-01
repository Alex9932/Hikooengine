package alex9932.engine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class TerrainGenerator {
	private static final int HEADER = 0x00;
	private static final int BODY = 0x01;
	private static final int SEPARATOR = 0x02;
	private static final int EOF = 0xFF;
	
	public static void main(String[] args) throws Exception{
		int WIDTH = 512;
		int DEPTH = 512;
		
		//Generating heights
		float[][] heights = new float[WIDTH][DEPTH];
		HeightsGenerator generator = new HeightsGenerator();
		for (int i = 0; i < heights.length; i++) {
			for (int j = 0; j < heights[i].length; j++) {
				heights[i][j] = generator.generateHeight(i, j);
			}
		}
		
		float[] array = new float[WIDTH * DEPTH];
		
		int k = 0;
		for (int i = 0; i < heights.length; i++) {
			for (int j = 0; j < heights[i].length; j++) {
				array[k] = heights[i][j];
				k++;
			}
		}
		
		//Write to file
		DataOutputStream out = new DataOutputStream(new FileOutputStream(new File("terrain")));
		
		//Write header
		out.write(HEADER);
		out.writeFloat(WIDTH);
		out.write(SEPARATOR);
		out.writeFloat(DEPTH);
		out.write(SEPARATOR);
		out.writeInt(26);
		out.writeChars("Hello, world!");

		//Write data
		out.write(BODY);
		
		for (int i = 0; i < array.length; i++) {
			out.writeFloat(array[i]);
			out.write(SEPARATOR);
		}
		
		//EOF
		out.write(EOF);
		out.writeChars("LOL End of file!");
		
		out.close();
	}
}