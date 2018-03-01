package alex9932.engine.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	public static int VERSION = 1;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	
	public Connection(String ipaddress, int port) throws Exception {
		this.socket = new Socket(InetAddress.getByName(ipaddress), port);
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public DataInputStream getIn() {
		return in;
	}
	
	public DataOutputStream getOut() {
		return out;
	}

	public void close() throws IOException {
		this.socket.close();
	}
}