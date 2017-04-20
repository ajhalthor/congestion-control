import java.util.Scanner;

class Packet{
	int number;
	ArrayList<Node> path;

	Packet(int number){
		Scanner scanner = new Scanner(System.in);
		this.number = number;
	}

	void init(){
		path = new ArrayList<Node>();
	}
}