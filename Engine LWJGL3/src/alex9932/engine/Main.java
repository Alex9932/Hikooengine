package alex9932.engine;

public class Main {
	public static String gameClass = "alex9932.engine.Game";
	
	public static void main(String[] args) {
		try {
			System.out.println("Foxcub engine version 0.8b");
			
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
			
			new Thread(){
				public void run() {
					while (true) {
						try {sleep(5000);} catch (Exception e) {}
						float total = Runtime.getRuntime().maxMemory()/1000000F;
						float free = Runtime.getRuntime().freeMemory()/1000000F;
						float used = total - free;
						System.out.println("[wrapper] Memory used: " + used + " Mb, free: " + free + " Mb, total: " + total + " Mb.");
					}
				}
			}.start();
			
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-game")){
					gameClass = args[i + 1];
				}
			}

			System.out.println("[info] [wrapper] Loading game class: " + gameClass);
			System.out.println("[info] [wrapper] Starting engine...");

			Runtime.getRuntime().addShutdownHook(new Thread(){
				@Override
				public void run() {
					System.out.println("[info] [wrapper] Shutting down...");
				}
			});
			
			Class<?> clazz = Class.forName(gameClass);
			clazz.newInstance();
			shutdown(0);
		} catch (Exception e) {
			System.out.println("[info] [wrapper] [ERROR] Engine error: " + e);
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