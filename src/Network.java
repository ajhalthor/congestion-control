import java.util.Scanner;
import java.util.ArrayList;

class Network{
	int noOfNodes, noOfPackets;
	Node[] nodes;
	Packet[] packets;
	boolean[][] matrix;
	Node source, destination;
	ArrayList<Packet> recievedPackets;

	Network(int noOfNodes,int noOfPackets){
		this.noOfPackets = noOfPackets;
		this.noOfNodes = noOfNodes;
	}

	void init(){
		nodes = new Node[noOfNodes];
		packets = new Packet[noOfPackets];
		matrix = new boolean[noOfNodes][noOfNodes];
		recievedPackets = new ArrayList<Packet>(noOfPackets);
	}


	void createNodes(){
		for(int i=0;i<noOfNodes;i++){
			System.out.println("NODE " + (i+1) + "INFO");
			nodes[i] = new Node(i);
			nodes[i].init();
		}
	}

	void createMatrix(){
		Scanner scanner = new Scanner(System.in);
		for(int i=0;i<noOfNodes;i++){
			for(int j=0;j<noOfNodes;j++){
				if(i<j){
					System.out.print(nodes[i].name + " - " + nodes[j].name + " : ");
					int connection= scanner.nextInt();
					if(connection > 0){
						matrix[i][j] = true;
						nodes[i].outLines++;
						nodes[j].inLines++;
						nodes[i].outLineNodes.add(nodes[j]);
						nodes[j].inLineNodes.add(nodes[i]);
					}else{
						matrix[i][j] = false;
					}

				}else if(i>j){
					matrix[i][j] = matrix[j][i];
				}else{
					matrix[i][j] = true;
					// no outlines or inlines to itself
				}
				
			}
		}
	}

	void createPackets(){
		for(int i=0;i<noOfPackets;i++){
			packets[i] = new Packet(i);
			packets[i].init();
		}
	}


	void initSource(){
		source = nodes[0];
		destination = nodes[noOfNodes-1];
		source.buffer.ensureCapacity(noOfPackets);
		destination.buffer.ensureCapacity(noOfPackets);

		for(int i=0;i<noOfPackets;i++){
			source.buffer.add(packets[i]);
			packets[i].path.add(source);
		}

	}

	void displayNode(){
		System.out.println("NODE NAME 		|		OUT 	| 		IN 		|");
		for(int i=0;i<noOfNodes;i++){
			System.out.println("NODE " + nodes[i].name + " 		|		 " + nodes[i].outLines + " 		|		 " + nodes[i].inLines);
		}
	}

	void startTransmit(){
		for(int a=0;a<noOfPackets;a+=source.outLines){
			for(int i=0;i<noOfNodes;i++){
				transmit(nodes[i]);
			}
		}
/*
		After transmitting the nodes to the network, we need to wait for all the packets 
		to reach the destination
*/
		System.out.println("ALL NODES SENT IN NETWORK!");
		int destinationTimeOut = 10;
		int oldLength = recievedPackets.size();

		while(destinationTimeOut > 0){
			for(int i=0;i<noOfNodes;i++){
				transmit(nodes[i]);
			}
			//System.out.println(recievedPackets.size());
			if(recievedPackets.size() == oldLength){

				destinationTimeOut--;
			}else{
				oldLength = recievedPackets.size() ;
				destinationTimeOut = 10;
			}
		}
	}

	void transmit(Node node){
		for(int i=0;i<node.outLines;i++){
			if(!node.buffer.isEmpty()){
				Packet packet = node.buffer.remove(0);	// take first packet in buffer 
				try{
					if(node.outLineNodes.get(i).buffer.size() > node.outLineNodes.get(i).inLines 
						&& node.outLineNodes.get(i) != destination ){
						throw new BufferOverflowException(node.outLineNodes.get(i), node.outLineNodes.get(i).inLines ,packet );
					}else{
						node.outLineNodes.get(i).buffer.add(packet);
						System.out.println("sent " + packet.number + " from " + node.name + " to " + node.outLineNodes.get(i).name);
						if(node.outLineNodes.get(i) == destination){
							recievedPackets.add(packet);
						}
						packet.path.add(node.outLineNodes.get(i));
					}

				}catch(BufferOverflowException e){
					e.display();
				}
			}
		}
	}

	void status(){

		System.out.println("PATHS");
		for(Packet packet : packets){
			System.out.print("Packet " + packet.number + " : ");
			for(Node node : packet.path){
				System.out.print(node.name + " -> ");
			}
			if(packet.path.get(packet.path.size() - 1) == destination){
				System.out.println("RECIEVED");
			}else{
				System.out.println("LOST");
			}
			System.out.print("\n");
		}
		System.out.println("RECIEVED PACKETS");
		for(Packet packet : recievedPackets){
			System.out.print(packet.number + ",");
		}

		System.out.println(recievedPackets.size() +  " / " + noOfPackets + " recieved!");
	}

}
