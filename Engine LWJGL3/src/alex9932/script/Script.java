package alex9932.script;

import java.util.ArrayList;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import alex9932.engine.Event;
import alex9932.utils.IKeyListener;
import alex9932.utils.IMouseListener;

public class Script implements IKeyListener, IMouseListener, IEventHandler{
	private Invocable inv;
	private ArrayList<String> keyPrsdCallBacks = new ArrayList<String>();
	private ArrayList<String> keyRelCallBacks = new ArrayList<String>();
	private ArrayList<String> scrollCallBacks = new ArrayList<String>();
	private ArrayList<String> clickCallBacks = new ArrayList<String>();
	private ArrayList<String> moveCallBacks = new ArrayList<String>();
	private ArrayList<String> dragCallBacks = new ArrayList<String>();
	private ArrayList<String> onloadEventCallBacks = new ArrayList<String>();

	public Script(ScriptsEngine scriptsEngine, String src) throws Exception {
		ScriptEngine js = new ScriptEngineManager().getEngineByName("javascript");
		Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("stdout", System.out);
		bindings.put("script", scriptsEngine);
		js.eval(src);
		inv = (Invocable) js;
	}
	
	public void construct() throws Exception {
		inv.invokeFunction("construct");
	}
	
	public void destroy() throws Exception {
		inv.invokeFunction("destruct");
	}
	
	public void addKeyPrsdCallBack(String function) {
		keyPrsdCallBacks.add(function);
	}
	
	public void addKeyRelCallBack(String function) {
		keyRelCallBacks.add(function);
	}
	
	public void addMouseScrollCallBack(String function) {
		scrollCallBacks.add(function);
	}
	
	public void addMouseClickCallBack(String function) {
		clickCallBacks.add(function);
	}
	
	public void addMouseMoveCallBack(String function) {
		moveCallBacks.add(function);
	}
	
	public void addDragCallBack(String function) {
		dragCallBacks.add(function);
	}
	
	public void addOnloadEvent(String function) {
		onloadEventCallBacks.add(function);
	}
	
	public void invoke(String func) throws Exception {
		inv.invokeFunction(func);
	}
	
	public void invoke(String func, Object... args) throws Exception {
		inv.invokeFunction(func, args);
	}

	@Override
	public void buttonPressed(int button, double x, double y) {
		for (int i = 0; i < clickCallBacks.size(); i++) {
			try {
				invoke(clickCallBacks.get(i), x, y, button);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void buttonReleased(int button, double x, double y) {
	}

	@Override
	public void drag(int button, double x, double y) {
		for (int i = 0; i < dragCallBacks.size(); i++) {
			try {
				invoke(dragCallBacks.get(i), x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void move(double x, double y) {
		for (int i = 0; i < moveCallBacks.size(); i++) {
			try {
				invoke(moveCallBacks.get(i), x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void scroll(int scroll) {
		for (int i = 0; i < scrollCallBacks.size(); i++) {
			try {
				invoke(scrollCallBacks.get(i), scroll);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		for (int i = 0; i < keyRelCallBacks.size(); i++) {
			try {
				invoke(keyRelCallBacks.get(i), key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyReleased(int key) {
		for (int i = 0; i < keyPrsdCallBacks.size(); i++) {
			try {
				invoke(keyPrsdCallBacks.get(i), key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onLoadEvent(String level) {
		for (int i = 0; i < onloadEventCallBacks.size(); i++) {
			try {
				invoke(onloadEventCallBacks.get(i), level);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void startLoadLevelEvent(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endLoadLevelEvent(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startupEvent(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdownEvent(String level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(Event event) {
		// TODO Auto-generated method stub
		
	}
}