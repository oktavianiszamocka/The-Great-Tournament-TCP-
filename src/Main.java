
import java.io.IOException;
import java.util.Scanner;

public class Main {
	static Scanner scanner;
	static Peer peer;
	static Peer first = null;
//name : Oktaviani Szamocka s17874

	public static void main(String[] args) throws IOException  {
		System.out.println("name : Oktaviani Szamocka s17874");
		initializePeer();
	}
	
	public static void initializePeer() throws IOException {
		
		scanner = new Scanner(System.in);
		System.out.println("please enter ur name");
		String name = scanner.next();
		System.out.println("please enter the port number of peer server");
		int port = scanner.nextInt();
		System.out.println("please enter the introducing IP (if local just write localhost)");
		String host = scanner.next();
		System.out.println("please enter the introducing port number");
		int introport = scanner.nextInt();
		
		peer = new Peer(name, port, host, introport);
		if(port == introport) {
			first = peer;
		}
		
		if(first == null) {
			connecttoIntroducingPeer();
		}
	}
	
	public static void connecttoIntroducingPeer() {
		peer.executeSocket();
		
	}
	
	
}
