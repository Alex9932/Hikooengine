package alex9932.engine;

import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL11;

import alex9932.engine.entity.EntityPlayer;
import alex9932.engine.render.Renderer;
import alex9932.engine.sound.SoundSystem;
import alex9932.engine.utils.Scene;
import alex9932.script.ScriptsEngine;
import alex9932.utils.Timer;
import alex9932.utils.gl.Display;

public class Engine {
	private Display display;
	private Renderer renderer;
	private Timer timer;
	private Scene scene;
	private IGame game;
	private EntityPlayer player;
	private ScriptsEngine scriptEngine;
	
	public Engine() throws Exception{
		display = new Display(1280, 720, "Hikooengine");
		renderer = new Renderer(display);
		timer = new Timer(1000);
		scene = new Scene();
		player = new EntityPlayer(0, 0, 0);
		scene.addEntity(player);
		scriptEngine = new ScriptsEngine(this, renderer.getGuiShader());
		SoundSystem.init();
		SoundSystem.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
	}
	
	public void setGame(IGame game){
		this.game = game;
	}

	public void start() throws Exception {
		game.startup();
		while (!display.isCloseRequested()) {
			
			timer.updateTimer();
			
			for (int i = 0; i < timer.elapsedTicks; ++i){
				game.loop();
				scene.update();
			}
			SoundSystem.updateListenerPosition(renderer.getCamera());
			
			renderer.render(scene, player);
			
			display.update();
		}
		game.shutdown();
		display.destroy();
		scriptEngine.destroy();
		SoundSystem.cleanUp();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public Timer getTimer() {
		return timer;
	}
	
	public EntityPlayer getPlayer() {
		return player;
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void restartRenderer() {
		renderer.destroy();
		renderer = null;
		renderer = new Renderer(display);
	}

	public void setScene(Scene scene) {
		this.scene = scene;
		scene.addEntity(player);
	}
}