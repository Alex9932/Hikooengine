package alex9932.engine.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
	public static int VERSION = 1;
	private static boolean running = true;
	private static ArrayList<Handler> clients = new ArrayList<Handler>();
	
	public static void main(String[] args) throws Exception {
		System.out.println("[Server] Starting up...");
		ServerSocket server = new ServerSocket(1337);
		
		Thread console = new Thread(){
			@Override
			public void run() {
				DataInputStream in = new DataInputStream(System.in);
				while (running) {
					try {
						@SuppressWarnings("deprecation")
						String command = in.readLine();
						if(command.equals("stop")){
							System.out.println("Stopping server...");
							for (int i = 0; i < clients.size(); i++) {
								clients.get(i).disconnect("Server stopped!");
							}
							running = false;
							System.exit(0);
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		console.start();
		
		while (running) {
			clients.add(new Handler(server.accept()));
		}
		
		server.close();
	}

	public static void delete(Handler handler) {
		clients.remove(handler);
	}
	
	public static ArrayList<Handler> getClients() {
		return clients;
	}
	
	public static boolean isConnected(Handler handler, String name) {
		for (Handler client : clients) {
			if(!client.equals(handler) && client.getNickname().equals(name)) {
				return true;
			}
		}
		return false;
	}
}