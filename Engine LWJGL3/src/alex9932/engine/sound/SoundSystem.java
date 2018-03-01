package alex9932.engine.sound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

import alex9932.engine.render.ICamera;
import alex9932.vecmath.Vector3f;

public class SoundSystem {
	private static long device;
	private static long context;
	private static final HashMap<String, Buffer> soundBufferMap = new HashMap<String, Buffer>();
	
	private static Listener listener;
	
	public static void init() {
		device = ALC10.alcOpenDevice((ByteBuffer) null);
		if (device == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to open the default OpenAL device.");
		}
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		context = ALC10.alcCreateContext(device, (IntBuffer) null);
		if (context == MemoryUtil.NULL) {
			throw new IllegalStateException("Failed to create OpenAL context.");
		}
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		listener = new Listener();
	}
	
	public static Buffer getSoundBuffer(String path){
		if(soundBufferMap.get(path) == null){
			try {
				Buffer buffer = new Buffer(path);
				soundBufferMap.put(path, buffer);
				return buffer;
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to load sound from: " + path + "!");
			}
		}else{
			return soundBufferMap.get(path);
		}	
	}
	
	public static Source createSource(Buffer buffer, float x, float y, float z) {
		return new Source(buffer, x, y, z);
	}
	
	public static Listener getListener() {
		return listener;
	}
	
	public static void updateListenerPosition(ICamera camera) {
		Vector3f at = new Vector3f();
		Vector3f up = new Vector3f(0, 1, 0);
		
		at.x = (float)-Math.sin(Math.toRadians(-camera.getAnglex()));
		at.y = (float)-Math.tan(Math.toRadians(camera.getAngley()));
		at.z = (float)-Math.cos(Math.toRadians(-camera.getAnglex()));
		at.normalize();

		listener.setPosition(camera.getPosition());
		listener.setOrientation(at, up);
	}
	
	public static void cleanUp() {
		ALC.destroy();
	}

	public static void setAttenuationModel(int atten) {
		AL10.alDistanceModel(atten);
	}
}