

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Peer implements Serializable {
	volatile HashMap<Integer, String> listpeers;
	volatile HashMap<Integer, String> battle;
	volatile List<String> result;
	String introIP;
	int portintro;
	int port;
	String name;
	Server server;
	Socket client;
//	Socket player;
	int flag = 0;
	String type;
	Message input, output;
	ObjectOutputStream out;
	ObjectInputStream in;
	BufferedReader inputuser;
	

	public Peer(String name, int port, String introIP, int portintro) throws IOException {
		this.name = name;
		this.port = port;
		this.introIP = introIP;
		this.portintro = portintro;
		listpeers = new HashMap<>();
		result = new ArrayList<>();
		executeserver();
		
	}
	
	public void executeserver() throws IOException {
		server = new Server(port, listpeers, this, result);
		server.start();
	}
	
	public void executeSocket()  {
			try {
				client = new Socket(introIP, portintro);
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
				System.out.println("write now to server");
				inputuser = new BufferedReader(new InputStreamReader(System.in));
				while((type = inputuser.readLine()) != null) {		
					
					out.writeObject(new Message(getName() + type, null));
					out.flush();
					
					if(type.equalsIgnoreCase("join")) {
						join();
					}
					if(type.equals("update")) {
						for(Map.Entry m : listpeers.entrySet()) {
							System.out.println("update from " + m.getValue());
							client = new Socket ("localhost", (int)m.getKey());
							out = new ObjectOutputStream(client.getOutputStream());
							in = new ObjectInputStream(client.getInputStream());
							out.writeObject(new Message("snyc", listpeers));
							Message msg = (Message) in.readObject();
							listpeers.putAll(msg.getMap());
						}
						showMap(listpeers);
					}
					if(type.equals("play")) {
						battle = listpeers;
						battle.remove(this.port);
						for(Map.Entry m : battle.entrySet()) {
							System.out.println("playing with player " + m.getValue());
							client = new Socket ("localhost", (int)m.getKey());
							out = new ObjectOutputStream(client.getOutputStream());
							in = new ObjectInputStream(client.getInputStream());
							out.reset();
							Scanner s = new Scanner(System.in);
							System.out.println("give number to duel");
							int num = s.nextInt();
							out.writeObject(new Game(this.name, num, null, 0));
							String re = ((Game)in.readObject()).result();
							System.out.println(re);
							result.add(re);
							out.writeObject(new Message(re, null));
							System.out.println("-----------------");
						}
			
					}
					if(type.equalsIgnoreCase("exit")) {
						int size = listpeers.size() - 1;
						if(result.size() != listpeers.size()) {
							System.out.println("sorry you cant quit because u need to play with other player");
						} else {
							HashMap<Integer, String> me = new HashMap<>();
							me.put(this.port, this.name);
							listpeers.remove(this.port);
							for(Map.Entry m : battle.entrySet()) {
								System.out.println("removing me from " + m.getKey());
								out.writeObject(new Message("quit", listpeers));
							}
							
							out.close();
							in.close();
							inputuser.close();
							client.close();
						}
						
					}
				}
			
		
			} catch (UnknownHostException e) {
				System.err.println("Unknown host: " + introIP + ".");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Connection error with" + introIP + ".");
				System.exit(1);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void join() {
		try {
			input = (Message) in.readObject();
			input.getMap().put(port, this.name);
			listpeers.putAll(input.getMap());
			output = new Message("my hasmap", listpeers);
			out.writeObject(output);
			out.flush();
			showMap(listpeers);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void showMap(HashMap<Integer, String> map) {
		System.out.println("list of player");
		for(Map.Entry m : map.entrySet()) {
			System.out.println(m.getKey() + " " +  m.getValue());
		}
	}
	public String getName() {
		String identity = name + " " + client.getInetAddress() + " :" + client.getLocalPort() + " ";
		return identity;
	}

}
