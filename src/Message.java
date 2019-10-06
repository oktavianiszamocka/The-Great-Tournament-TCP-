
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
	
	String str;
	HashMap<Integer, String> list;
	
	public Message(String str, HashMap<Integer, String> list) {
		this.str = str;
		this.list = list;
	}
	
	public String getMessage(){
		return str;
	}
	
	public HashMap<Integer, String> getMap() {
		return list;
	}
	
	public void setMap(HashMap<Integer, String> a) {
		list = a;
	}
	
	public void setMessage(String msg) {
		str = msg;
	}
	
}
