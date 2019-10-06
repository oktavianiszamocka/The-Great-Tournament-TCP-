
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;


public class Server extends Thread{
	volatile List<String> result;
	ServerSocket server;
	Socket client;
	int portserver;
	Clienthandler ch;
	volatile HashMap<Integer, String> listpeers;
	volatile Peer peer;
	
	
  	public Server(int portserver, HashMap<Integer, String> listpeers, Peer peer, List<String> result) throws IOException {
  		this.portserver = portserver;
  		this.listpeers = listpeers;
  		this.peer = peer;
  		this.result = result;
		server = new ServerSocket(portserver);
	}
  	
  	public void run(){
		System.out.println("waiting for client");
		try {
		
			while(true) {
				client = server.accept();
				ch = new Clienthandler(client, listpeers, peer, result);
				new Thread(ch).start();		
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}


}

