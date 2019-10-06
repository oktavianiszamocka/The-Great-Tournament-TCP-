
import java.io.Serializable;

public class Game implements Serializable {

	String start, other, winner;
	int p1, p2;
	
	Game(String start, int p1, String other, int p2){
		this.start = start;
		this.p1 = p1;
		this.other = other;
		this.p2 = p2;
		if(p1 != 0 && p2 != 0) {
			play();
			result();
		}
	}
	
	public void setp1(int i) {
		p1 = 1;
	}
	public void setp2(int i) {
		p2 = 1;
	}
	public void setother(String o) {
		other = o;
	}
	
	void play() {
		System.out.println("calculating");
		int re = p1 + p2;
		if ( re % 2 == 0) {
			winner = other;
		}
		else {
			winner = start;
		}
		
	}
	
	public String result() {
		return "the winner is " +  winner;
	}
	
}
