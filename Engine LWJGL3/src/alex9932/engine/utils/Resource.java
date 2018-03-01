package alex9932.engine.utils;

public class Resource {
	private static final String SHADERS = "gamedata/shaders/";
	private static final String TEXTURES = "gamedata/textures/";
	private static final String MODELS = "gamedata/models/";
	private static final String SOUNDS = "gamedata/sounds/";
	private static final String LEVELS = "gamedata/levels/";

	public static String getShader(String path) {
		return SHADERS + path;
	}

	public static String getTexture(String path) {
		return TEXTURES + path;
	}

	public static String getModel(String path) {
		return MODELS + path;
	}

	public static String getSound(String path) {
		return SOUNDS + path;
	}

	public static String getLevel(String path) {
		return LEVELS + path;
	}
	
	public static String getLevels() {
		return LEVELS;
	}
	
	public static String getModels() {
		return MODELS;
	}
	
	public static String getShaders() {
		return SHADERS;
	}
	
	public static String getSounds() {
		return SOUNDS;
	}
	
	public static String getTextures() {
		return TEXTURES;
	}
}