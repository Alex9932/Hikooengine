package alex9932.engine;

// Wrapper
public class Main {
	public static String gameClass = "alex9932.engine.Game";
	
	public static void main(String[] args) {
		try {
			
			// Print system information
			System.out.println("Hikooengine version 0.8b");
			
			System.out.println("[info] System information:");
			System.out.println("[info] Detected CPU:");
			System.out.println(" - " + System.getenv("PROCESSOR_IDENTIFIER"));
			System.out.println(" - Architecture: " + System.getenv("PROCESSOR_ARCHITECTURE"));
			System.out.println(" - Available processors (cores): " + System.getenv("NUMBER_OF_PROCESSORS"));
			System.out.println("");
			System.out.println("[info] Memory details:");
			System.out.println(" - Free memory: " + Runtime.getRuntime().freeMemory()/1000000 + " Mb");
			System.out.println(" - Maximum memory: " + Runtime.getRuntime().maxMemory()/1000000 + " Mb");
			System.out.println(" - Total memory available to JVM: " + Runtime.getRuntime().totalMemory()/1000000 + " Mb");
			System.out.println("");
			System.out.println("[info] Starting wrapper...");
			
			// Adding hooks
			new Thread(){
				public void run() {
					while (true) {
						try {sleep(5000);} catch (Exception e) {}
						float total = Runtime.getRuntime().totalMemory()/1000000F;
						float free = Runtime.getRuntime().freeMemory()/1000000F;
						float used = total - free;
						System.out.println("[wrapper] Memory used: " + used + " Mb, free: " + free + " Mb, total: " + total + " Mb.");
					}
				}
			}.start();

			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					System.out.println("[info] [wrapper] Shutting down...");
				}
			});
			
			// Getting game class
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-game")){
					gameClass = args[i + 1];
				}
			}

			System.out.println("[info] [wrapper] Loading game class: " + gameClass);

			// Loading class
			Class<?> clazz = Class.forName(gameClass);
			
			// Checking gameclass
			Class<?>[] array = clazz.getInterfaces();
			boolean isGame = false;
			for (int i = 0; i < array.length; i++) {
				if(array[i].getName().equals("alex9932.engine.IGame")){
					isGame = true;
					break;
				}
			}
			
			if(!isGame){
				// Exit with error
				System.out.println("[error] [wrapper] Selected \"gameclass\" is not a game!  " + clazz.getName());
				shutdown(0);
			}
			
			System.out.println("[info] [wrapper] Starting engine...");
			
			// Creating new instance
			clazz.newInstance();
			shutdown(0);
		} catch (Exception e) {
			System.out.println("[info] [wrapper] [ERROR] wrapper error: " + e);
			e.printStackTrace();
			System.out.println("[info] [wrapper] Shutting down...");
			new GuiError(e);
			try {Thread.sleep(100);} catch (Exception e2) {}
		}
	}

	public static void shutdown(int i) {
		System.exit(i);
	}
}