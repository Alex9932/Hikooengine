package alex9932.engine.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import alex9932.vecmath.Vector3f;

public class Handler implements Runnable{
	private Thread thread;
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private String nickname = "null";
	private boolean running = true;
	private Vector3f position = new Vector3f();
	
	public Handler(Socket socket) {
		this.socket = socket;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	@Override
	public void run() {
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			
			this.out.write(Net.REQUEST_VERSION);
			this.out.flush();
			System.out.println("Requesting version...");
			if(this.in.read() != Server.VERSION) {
				out.write(Net.RESPONSE_ERROR);
				out.write("Version check failed!".getBytes());
				//throw new Exception("Version check failed!");
			}else{
				out.write(Net.RESPONSE_SUCCESS);
			}
			
			Thread.sleep(100);

			System.out.println("Requesting name...");
			this.out.write(Net.REQUEST_NICKNAME);
			this.out.flush();
			byte[] string = new byte[128];
			this.in.read(string);
			this.nickname = new String(string, StandardCharsets.UTF_8).trim();
			System.out.println(this.nickname + " joined the game!");
			
			if(Server.isConnected(this, this.nickname)) {
				out.write(Net.RESPONSE_ERROR);
				out.write((this.nickname + " already playing!").getBytes());
				throw new Exception(this.nickname + " already playing!");
			}else{
				out.write(Net.RESPONSE_SUCCESS);
			}
			
			this.out.write(Net.REQUEST_COORDS);
			
			float[] vector = new float[3];
			vector[0] = this.in.readFloat();
			vector[1] = this.in.readFloat();
			vector[2] = this.in.readFloat();
			this.position  = Vector3f.parse(vector);
			
			System.out.println("Loading player at: " + position);
			
			while (running) {
				int data = this.in.read();
				if(data == Net.REQUEST_COORDS){
					float[] pos = new float[3];
					pos[0] = this.in.readFloat();
					pos[1] = this.in.readFloat();
					pos[2] = this.in.readFloat();
					this.position  = Vector3f.parse(pos);
					System.out.println(position);
				}else if(data == Net.DISCONNECT_BY_CLIENT){
					System.out.println(this.nickname + " left the game!");
					Server.delete(this);
					socket.close();
					running = false;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(this.nickname + " left the game!");
			Server.delete(this);
		}
	}

	public InputStream getIn() {
		return in;
	}
	
	public OutputStream getOut() {
		return out;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void disconnect(String reason) {
		Server.delete(this);
		try {
			out.write(Net.DISCONNECT_BY_SERVER);
			out.writeUTF(reason);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}