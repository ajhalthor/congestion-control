import java.util.Scanner;

class Node{
    int number, inLines,outLines;
    String name;
    ArrayList<Packet> buffer;
    ArrayList<Node> outLineNodes; /* Routing table knows who it can send info to */
    ArrayList<Node> inLineNodes;  /* and who it can take info from */

    Node(int number){
        Scanner scanner = new Scanner(System.in);
        this.number = number;
        inLines = 0;
        outLines = 0;
        System.out.print("Name : ");
        name = scanner.nextLine();
    }

    void init(){
        buffer = new ArrayList<Packet>(inLines); // Should be actually equal to the number of input links 
        outLineNodes = new ArrayList<Node>();
        inLineNodes = new ArrayList<Node>();
    }
}
