package alex9932.engine;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import alex9932.engine.entity.Entity;
import alex9932.engine.render.Material;
import alex9932.engine.render.Mesh;
import alex9932.engine.sound.Buffer;
import alex9932.engine.sound.SoundSystem;
import alex9932.engine.sound.Source;
import alex9932.engine.utils.OBJLoader;
import alex9932.engine.utils.Resource;
import alex9932.engine.utils.Scene;
import alex9932.utils.gl.texture.Texture;
import alex9932.vecmath.Quaternion;

public class LevelLoader {
	private static final int HEADER = 0x00;
	private static final int BODY = 0x01;
	private static final int SEPARATOR = 0x02;
	private static final int EOF = 0xFF;
	
	private String level;
	private String resourcesSRC = "";
	private String allspawnSRC = "";
	private HashMap<String, Mesh> models = new HashMap<String, Mesh>();
	private HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private HashMap<String, Buffer> sounds = new HashMap<String, Buffer>();
	private HashMap<String, TerrainModel> terrains = new HashMap<String, TerrainModel>();
	private JSONObject resources;
	private JSONObject allspawn;
	private Scene scene;
	
	public LevelLoader(String level) {
		this.level = level;
		//reading files
		System.out.println("Prepairing to load level...");
		readResources();
		readAllspawn();
		//load models
		System.out.println("Loading models...");
		loadModels();
		//load textures
		System.out.println("Loading textures...");
		loadTextures();
		loadTerrains();
		//load sounds
		System.out.println("Loading sounds...");
		loadSounds();
	}

	private void readAllspawn() {
		try {
			allspawnSRC = readFile("allspawn.als");
			allspawn = new JSONObject(allspawnSRC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readResources() {
		try {
			resourcesSRC = readFile("resources.jsn");
			resources = new JSONObject(resourcesSRC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadScene() {
		scene = new Scene();
		
		//Entity
		JSONArray entitys = allspawn.getJSONArray("entitys");
		for (int i = 0; i < entitys.length(); i++) {
			JSONObject entityCRG = entitys.getJSONObject(i);
			JSONObject material = entityCRG.getJSONObject("material");
			JSONArray materialColor = material.getJSONArray("color");
			Entity entity = new Entity(
				(float)entityCRG.getDouble("x"),
				(float)entityCRG.getDouble("y"),
				(float)entityCRG.getDouble("z"),
				models.get(entityCRG.getString("model"))
			);
			entity.setTexture(textures.get(entityCRG.getString("texture")));
			entity.setMaterial(new Material(
				new Quaternion(
					(float)materialColor.getDouble(0),
					(float)materialColor.getDouble(1),
					(float)materialColor.getDouble(2),
					(float)materialColor.getDouble(3)
				),
				new Quaternion(0, 0, 0, 1),
				new Quaternion(0, 0, 0, 1), 1,
				(float)material.getDouble("reflectance"),
				(float)material.getDouble("specular"))
			);
			scene.addEntity(entity);
		}
		
		//Terrain
		JSONArray terrains = allspawn.getJSONArray("terrains");
		for (int i = 0; i < terrains.length(); i++) {
			JSONObject trn = terrains.getJSONObject(i);
			TerrainModel model = this.terrains.get(trn.getString("terrain"));
			Terrain terrain = new Terrain(model.getTexture(), (float)trn.getDouble("x"), (float)trn.getDouble("z"), 200, model.getHeightmap());
			scene.addTerrain(terrain);
		}
		
		//Sounds
		JSONArray sounds = allspawn.getJSONArray("sounds");
		for (int i = 0; i < sounds.length(); i++) {
			JSONObject snd = sounds.getJSONObject(i);
			Source src = SoundSystem.createSource(
				this.sounds.get(snd.getString("sound")),
				(float)snd.getDouble("x"),
				(float)snd.getDouble("y"),
				(float)snd.getDouble("z")
			);
			src.setVolume((float)snd.getDouble("volume"));
			scene.addSoundSource(src);
		}
		
	}

	private void loadModels() {
		JSONArray array = resources.getJSONArray("models");
		for (int i = 0; i < array.length(); i++) {
			JSONObject mod = array.getJSONObject(i);
			try {
				models.put(mod.getString("name"), OBJLoader.loadMesh(Resource.getModel(mod.getString("file"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadTextures() {
		JSONArray array = resources.getJSONArray("textures");
		for (int i = 0; i < array.length(); i++) {
			JSONObject tex = array.getJSONObject(i);
			textures.put(tex.getString("name"), new Texture(Resource.getTexture(tex.getString("file"))));
		}
	}
	
	private void loadSounds() {
		JSONArray array = resources.getJSONArray("sounds");
		for (int i = 0; i < array.length(); i++) {
			JSONObject snd = array.getJSONObject(i);
			sounds.put(snd.getString("name"), SoundSystem.getSoundBuffer(Resource.getSound(snd.getString("file"))));
		}
	}
	
	private void loadTerrains() {
		JSONArray array = resources.getJSONArray("terrains");
		for (int i = 0; i < array.length(); i++) {
			JSONObject terrain = array.getJSONObject(i);
			JSONObject texturePack = terrain.getJSONObject("texture_pack");
			TerrainTexture terrainTexture = new TerrainTexture(
				new Texture(Resource.getTexture(texturePack.getJSONArray("textures").getString(0)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("textures").getString(1)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("textures").getString(2)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("textures").getString(3)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getString("blend")), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("normal_maps").getString(0)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("normal_maps").getString(1)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("normal_maps").getString(2)), Texture.REPEAT),
				new Texture(Resource.getTexture(texturePack.getJSONArray("normal_maps").getString(3)), Texture.REPEAT)
			);
			TerrainModel model = null;
			try {
				model = new TerrainModel(terrainTexture, loadHM(terrain.getString("file")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			terrains.put(terrain.getString("name"), model);
		}
	}
	
	private float[][] loadHM(String file) throws Exception {
		String path = "";
		if(file.startsWith("$")){
			path = Resource.getLevels() + level + "/";
		}else{
			path = Resource.getModels() + "/";
		}
		String filename = file.substring(1, file.length());
		path += filename;
		System.out.println(path);
		
		DataInputStream filein = new DataInputStream(new FileInputStream(new File(path)));
		
		//Read header
		int header = filein.read();
		if(header != HEADER){
			System.err.println("Invalid header!");
		}
		
		int w = (int)filein.readFloat();
		filein.read();
		int d = (int)filein.readFloat();
		filein.read();
		int size = filein.readInt();
		for (int i = 0; i < size; i++) {
			filein.read();
		}
		int data = filein.read();
		if(data != BODY){
			System.out.println("Invalid data block!");
		}
		
		//Read data
		ArrayList<Float> heights = new ArrayList<Float>();
		
		while (true) {
			heights.add(filein.readFloat());
			int sep = filein.read();
			if(sep == SEPARATOR){
			}else if(sep == EOF){
				break;
			}else{
				break;
			}
		}
		
		filein.close();
		
		float[][] hm = new float[w][d];
		
		int k = 0;
		for (int i = 0; i < hm.length; i++) {
			for (int j = 0; j < hm[i].length; j++) {
				hm[i][j] = heights.get(k);
				k++;
			}
		}
		heights.clear();
		
		return hm;
	}

	private String readFile(String file) throws Exception{
		FileInputStream in = new FileInputStream(Resource.getLevel(this.level + "/" + file));
		System.out.println("Reading: " + file);
		String src = "";
		int data;
		while ((data = in.read()) != -1) {
			src += (char)data;
		}
		in.close();
		return src;
	}
	
	public Scene getScene() {
		//creating scene
		System.out.println("Dene loading!");
		loadScene();
		//Cleaning
		models.clear();
		terrains.clear();
		textures.clear();
		sounds.clear();
		return scene;
	}
}