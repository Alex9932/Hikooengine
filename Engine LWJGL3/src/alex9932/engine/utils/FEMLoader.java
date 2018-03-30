package alex9932.engine.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import alex9932.utils.Mesh;

public class FEMLoader {
	private static final int HEADER = 0x00;
	private static final int VERTICES = 0x01;
	private static final int NORMALS = 0x02;
	private static final int TEXTURE_COORDS = 0x03;
	private static final int INDICES = 0x04;
	//private static final int SEPARATOR = 0x05;
	private static final int EOB = 0xFE;
	private static final int EOF = 0xFF;
	private Mesh mesh;
		
	public FEMLoader(String file) {
		System.out.println("Loading model: " + file + ": ");
		try{
			DataInputStream filein = new DataInputStream(new FileInputStream(new File(file)));

			// Read header
			int header = filein.read();
			if (header != HEADER) {
				System.err.println("  - Invalid header!");
			}
			
			char[] head = new char[3];
			head[0] = (char)filein.read();
			head[1] = (char)filein.read();
			head[2] = (char)filein.read();
			if(head[0] != 'F' || head[1] != 'E' || head[2] != 'M'){
				System.out.println("  - Invalid header!");
				filein.close();
				return;
			}

			ArrayList<Float> vertices = new ArrayList<Float>();
			ArrayList<Float> normals = new ArrayList<Float>();
			ArrayList<Float> textureCoords = new ArrayList<Float>();
			ArrayList<Integer> indices = new ArrayList<Integer>();
			
			System.out.println("  - Reading geometry...");
			
			// Read vertices
			int data = filein.read();
			if(data == VERTICES){
				while (true) {
					vertices.add(filein.readFloat());
					int sep = filein.read();
					if(sep == EOB){
						break;
					}
				}
			}else{
				System.out.println("  - Invalid vertices!");
			}
				
			// Read normals
			data = filein.read();
			if(data == NORMALS){
				while (true) {
					normals.add(filein.readFloat());
					int sep = filein.read();
					if(sep == EOB){
						break;
					}
				}
			}else{
				System.out.println("  - Invalid normals!");
			}
				
			// Read texture coordinates
			data = filein.read();
			if(data == TEXTURE_COORDS){
				while (true) {
					textureCoords.add(filein.readFloat());
					int sep = filein.read();
					if(sep == EOB){
						break;
					}
				}
			}else{
				System.out.println("  - Invalid texture coordinaes!");
			}

			// Read indices
			data = filein.read();
			if(data == INDICES){
				while (true) {
					indices.add(filein.readInt());
					int sep = filein.read();
					if(sep == EOF){
						break;
					}
				}
			}else{
				System.out.println("  - Invalid indices!");
			}
	
			filein.close();
			
			// Convert data
			System.out.println("  - Converting data...");

			float[] vertices_array = new float[vertices.size()];
			float[] normals_array = new float[normals.size()];
			float[] textureCoords_array = new float[textureCoords.size()];
			int[] indices_array = new int[indices.size()];
			for (int i = 0; i < vertices_array.length; i++) {
				vertices_array[i] = vertices.get(i);
			}
			for (int i = 0; i < normals_array.length; i++) {
				normals_array[i] = normals.get(i);
			}
			for (int i = 0; i < textureCoords_array.length; i++) {
				textureCoords_array[i] = textureCoords.get(i);
			}
			for (int i = 0; i < indices_array.length; i++) {
				indices_array[i] = indices.get(i);
			}

			System.out.println("  - Creating mesh...");
			mesh = new Mesh(vertices_array, textureCoords_array, normals_array, indices_array);
			System.out.println("  - Done!");
		}catch(Exception e) {
			System.out.println("  - Failed!");
		}
	}

	public Mesh getMesh() {
		return mesh;
	}
}