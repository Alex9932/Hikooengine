package alex9932.engine.net;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import alex9932.vecmath.Vector3f;

public class Net {
	public static final int NULL = 0x00;
	public static final int REQUEST_VERSION = 0x01;
	public static final int REQUEST_NICKNAME = 0x02;
	public static final int REQUEST_COORDS = 0x03;
	public static final int RESPONSE_ERROR = 0x04;
	public static final int RESPONSE_SUCCESS = 0x05;
	public static final int DISCONNECT_BY_SERVER = 0x06;
	public static final int DISCONNECT_BY_CLIENT = 0x07;
	
	private static Vector3f position = new Vector3f(1.564f, 5.4486f, 7.454f);
	private static Connection connection;
	
	public static void main(String[] args) throws Exception {
		String name = "Alex9932";
		System.out.println("Connecting...");
		connection = new Connection("127.0.0.1", 1337);
		while (true) {
			int command = connection.getIn().read();
			if(command == Net.REQUEST_VERSION){
				System.out.println("Server ==> Client : VERSION_REQUEST");
				connection.getOut().write(Connection.VERSION);
				connection.getOut().flush();
			}else if(command == Net.REQUEST_NICKNAME){
				System.out.println("Server ==> Client : NICKNAME_REQUEST");
				connection.getOut().write(name.getBytes());
				connection.getOut().flush();
			}else if(command == Net.REQUEST_COORDS){
				connection.getOut().writeFloat(position.x);
				connection.getOut().writeFloat(position.y);
				connection.getOut().writeFloat(position.z);
			}else if(command == Net.RESPONSE_ERROR){
				byte[] array = new byte[1024];
				connection.getIn().read(array);
				System.out.println(new String(array, StandardCharsets.UTF_8).trim());
			}else if(command == Net.DISCONNECT_BY_SERVER){
				System.out.println(connection.getIn().readUTF());
				break;
			}
		}
		connection.close();
	}
	
	public static void sendCoords(Vector3f position) throws IOException {
		Net.position = position;
		connection.getOut().write(Net.REQUEST_COORDS);
		connection.getOut().writeFloat(position.x);
		connection.getOut().writeFloat(position.y);
		connection.getOut().writeFloat(position.z);
	}

	public static void disconnect() throws IOException {
		connection.getOut().write(Net.DISCONNECT_BY_CLIENT);
		connection.close();
	}
}