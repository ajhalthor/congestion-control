import java.util.Scanner;

public class policy{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of Packets : ");
        int noOfPackets = scanner.nextInt();
        int noOfNodes = 8;

        Network network = new Network(noOfNodes, noOfPackets);
        network.init();
        network.createNodes();
        network.createMatrix();
        network.createPackets();
        network.displayNode();

        network.initSource();
        network.startTransmit();
        System.out.println("--------------------------");
        System.out.println("FINAL STATUS");
        System.out.println("--------------------------");
        network.status();
    }
}