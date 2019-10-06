
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Clienthandler implements Runnable {
	Socket clientsocket;
	ObjectInputStream in;
	ObjectOutputStream out;
	Message input,output;
	Game game;
	volatile HashMap<Integer, String> listpeers;
	volatile Peer peer;
	volatile List<String> result;

	
	public Clienthandler(Socket clientsocket, HashMap<Integer,String> listpeers, Peer peer, List<String> result) {
		this.clientsocket = clientsocket;
		this.listpeers = listpeers;
  		this.peer = peer;
  		this.result = result;
		System.out.println("---new socket coming");
		
	}

	@Override
	public void run() {
		try {
			boolean play = false;
			in = new ObjectInputStream(clientsocket.getInputStream());
			out = new ObjectOutputStream(clientsocket.getOutputStream());
			Object ob;
			while((ob = (Object) in.readObject()) != null) {
				
				if(ob instanceof Message) {
					input = (Message) ob;
				} 
				if(ob instanceof Game) {
					game = (Game) ob;
					play = true;
				}
				
				if(input !=null) {
					if(input.getMessage().contains("join")) {
						input.setMessage("the list of player in tournament");
						listpeers.put(peer.port, peer.name);
						input.setMap(listpeers);
						out.writeObject(input);
						out.flush();
					}
				
					if(input.getMessage().contains("my hasmap")) {
						listpeers.putAll(input.getMap());
						showMap(listpeers);
						}
					
					if(input.getMessage().contains("snyc")) {
						listpeers.putAll(input.getMap());
						out.writeObject(new Message("update complete", listpeers));
						showMap(listpeers);
					}
					System.out.println(input.getMessage());
					
					if(input.getMessage().contains("the winner is")) {
						result.add(input.getMessage());
					}
					
					if(input.getMessage().contains("quit")){
						System.out.println("a player quit");
						listpeers = new HashMap(input.getMap());
					}
				}
				
				if(game != null && play == true) {
					System.out.println("you r going to play with " + game.start);
					Scanner sc = new Scanner(System.in);
					System.out.println("give number to play,if result not appear type the number again");
					int p2 = sc.nextInt();
					Game send = new Game (game.start, game.p1, peer.name, p2);
					out.writeObject(send);
					out.flush();
					play = false;
				}
				
			}
		
			System.out.println("a socket is disconnected");
			in.close();
			out.close();
			clientsocket.close();
		} catch (IOException e) {
			System.out.println("an error happens during Input/Output process");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public void showMap(HashMap<Integer, String> map) {
		for(Map.Entry m : map.entrySet()) {
			System.out.println(m.getKey() + " " +  m.getValue());
		}
	}

}
