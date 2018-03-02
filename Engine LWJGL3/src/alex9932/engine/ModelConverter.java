package alex9932.engine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import alex9932.vecmath.Vector2f;
import alex9932.vecmath.Vector3f;

public class ModelConverter {
	private static final int HEADER = 0x00;
	private static final int VERTICES = 0x01;
	private static final int NORMALS = 0x02;
	private static final int TEXTURE_COORDS = 0x03;
	private static final int INDICES = 0x04;
	private static final int SEPARATOR = 0x05;
	private static final int EOB = 0xFE;
	private static final int EOF = 0xFF;
	
	private static float[] posArr;
	private static float[] textCoordArr;
	private static float[] normArr;
	private static int[] indicesArr;
	
	public static void main(String[] args) throws Exception {
		String FILE_IN = "gamedata/models/bunny.obj";
		String FILE_OUT = "model.fem.gzip";
		
		System.out.println("Reading original obj file...");
		loadMesh(FILE_IN);
	
		DataOutputStream out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(new File(FILE_OUT))));
		
		System.out.println("Writing header...");
		
		// Header
		out.write(HEADER);
		out.write((int)'F');
		out.write((int)'E');
		out.write((int)'M');

		System.out.println("Writing vertices...");
		// Vertices block
		out.write(VERTICES);
		for (int i = 0; i < posArr.length; i++) {
			out.writeFloat(posArr[i]);
			if(i < posArr.length-1){
				out.write(SEPARATOR);
			}else{
				out.write(EOB);
			}
		}

		System.out.println("Writing normals...");
		// Normals block
		out.write(NORMALS);
		for (int i = 0; i < normArr.length; i++) {
			out.writeFloat(normArr[i]);
			if(i < normArr.length-1){
				out.write(SEPARATOR);
			}else{
				out.write(EOB);
			}
		}

		System.out.println("Writing texture coords...");
		// Texture coords block
		out.write(TEXTURE_COORDS);
		for (int i = 0; i < textCoordArr.length; i++) {
			out.writeFloat(textCoordArr[i]);
			if(i < textCoordArr.length-1){
				out.write(SEPARATOR);
			}else{
				out.write(EOB);
			}
		}

		System.out.println("Writing indices...");
		// Indices block
		out.write(INDICES);
		for (int i = 0; i < indicesArr.length; i++) {
			out.writeFloat(indicesArr[i]);
			if(i < indicesArr.length-1){
				out.write(SEPARATOR);
			}else{
				out.write(EOF);
			}
		}
		
		out.close();
		System.out.println("Done!");
	}
	
	public static void loadMesh(String fileName) throws Exception {
		List<String> lines = readAllLines(fileName);

		List<Vector3f> vertices = new ArrayList<>();
		List<Vector2f> textures = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Face> faces = new ArrayList<>();

		for (String line : lines) {
			String[] tokens = line.split("\\s+");
			switch (tokens[0]) {
			case "v":
				// Geometric vertex
				Vector3f vec3f = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				vertices.add(vec3f);
				break;
			case "vt":
				// Texture coordinate
				Vector2f vec2f = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
				textures.add(vec2f);
				break;
			case "vn":
				// Vertex normal
				Vector3f vec3fNorm = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
				normals.add(vec3fNorm);
				break;
			case "f":
				Face face = new Face(tokens[1], tokens[2], tokens[3]);
				faces.add(face);
				break;
			default:
				// Ignore other lines
				break;
			}
		}
		reorderLists(vertices, textures, normals, faces);
	}

	private static void reorderLists(List<Vector3f> posList, List<Vector2f> textCoordList, List<Vector3f> normList, List<Face> facesList) {
		List<Integer> indices = new ArrayList<Integer>();
		// Create position array in the order it has been declared
		posArr = new float[posList.size() * 3];
		int i = 0;
		for (Vector3f pos : posList) {
			posArr[i * 3] = pos.x;
			posArr[i * 3 + 1] = pos.y;
			posArr[i * 3 + 2] = pos.z;
			i++;
		}
		textCoordArr = new float[posList.size() * 2];
		normArr = new float[posList.size() * 3];

		for (Face face : facesList) {
			IdxGroup[] faceVertexIndices = face.getFaceVertexIndices();
			for (IdxGroup indValue : faceVertexIndices) {
				processFaceVertex(indValue, textCoordList, normList, indices, textCoordArr, normArr);
			}
		}
		indicesArr = new int[indices.size()];
		indicesArr = indices.stream().mapToInt((Integer v) -> v).toArray();
	}

	private static void processFaceVertex(IdxGroup indices, List<Vector2f> textCoordList, List<Vector3f> normList, List<Integer> indicesList, float[] texCoordArr, float[] normArr) {

		// Set index for vertex coordinates
		int posIndex = indices.idxPos;
		indicesList.add(posIndex);

		// Reorder texture coordinates
		if (indices.idxTextCoord >= 0) {
			Vector2f textCoord = textCoordList.get(indices.idxTextCoord);
			texCoordArr[posIndex * 2] = textCoord.x;
			texCoordArr[posIndex * 2 + 1] = 1 - textCoord.y;
		}
		if (indices.idxVecNormal >= 0) {
			// Reorder vectornormals
			Vector3f vecNorm = normList.get(indices.idxVecNormal);
			normArr[posIndex * 3] = vecNorm.x;
			normArr[posIndex * 3 + 1] = vecNorm.y;
			normArr[posIndex * 3 + 2] = vecNorm.z;
		}
	}
	
	public static List<String> readAllLines(String fileName) throws Exception {
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		}
		return list;
	}

	protected static class Face {
		/**
		 * List of idxGroup groups for a face triangle (3 vertices per face).
		 */
		private IdxGroup[] idxGroups = new IdxGroup[3];

		public Face(String v1, String v2, String v3) {
			idxGroups = new IdxGroup[3];
			// Parse the lines
			idxGroups[0] = parseLine(v1);
			idxGroups[1] = parseLine(v2);
			idxGroups[2] = parseLine(v3);
		}

		private IdxGroup parseLine(String line) {
			IdxGroup idxGroup = new IdxGroup();
			String[] lineTokens = line.split("/");
			int length = lineTokens.length;
			idxGroup.idxPos = Integer.parseInt(lineTokens[0]) - 1;
			if (length > 1) {
				// It can be empty if the obj does not define text coords
				String textCoord = lineTokens[1];
				idxGroup.idxTextCoord = textCoord.length() > 0 ? Integer.parseInt(textCoord) - 1 : IdxGroup.NO_VALUE;
				if (length > 2) {
					idxGroup.idxVecNormal = Integer.parseInt(lineTokens[2]) - 1;
				}
			}
			return idxGroup;
		}

		public IdxGroup[] getFaceVertexIndices() {
			return idxGroups;
		}
	}

	protected static class IdxGroup {
		public static final int NO_VALUE = -1;
		public int idxPos;
		public int idxTextCoord;
		public int idxVecNormal;
		public IdxGroup() {
			idxPos = NO_VALUE;
			idxTextCoord = NO_VALUE;
			idxVecNormal = NO_VALUE;
		}
	}
}