package alex9932.engine;

import org.lwjgl.openal.AL11;

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
	private EventSystem event;
	
	public Engine() throws Exception{
		display = new Display(1280, 720, "Hikooengine");
		renderer = new Renderer(display);
		timer = new Timer(1000);
		scene = new Scene();
		player = new EntityPlayer(0, 0, 0);
		scene.addEntity(player);
		event = new EventSystem();
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
			
			SoundSystem.updateListenerPosition(renderer.getCamera());
			
			renderer.render(scene, player);

			for (int i = 0; i < timer.elapsedTicks; ++i){
				game.loop();
				scene.update();
				renderer.tickTime();
			}
			
			display.update();
		}
		game.shutdown();
		display.destroy();
		scriptEngine.destroy();
		SoundSystem.cleanUp();
	}
	
	public void loadLevel(String level) {
		event.sendSignal(Event.START_LOAD_LEVEL);
		LevelLoader loader = new LevelLoader("test");
		event.sendSignal(Event.ON_LOAD_EVENT);
		scene = loader.getScene();
		event.sendSignal(Event.END_LOAD_LEVEL);
		scene.addEntity(player);
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

	public EventSystem getEventSystem() {
		return event;
	}
}