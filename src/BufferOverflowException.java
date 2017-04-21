

// buffer = inLines * (outLines-inLines)  but not strongly
class BufferOverflowException extends Exception{
    String message;

    BufferOverflowException(Node node,int capacity,Packet packet){
        message = node.name + " capacity " + capacity + "exceeded! Discarding packet " + packet.number;
    }

    void display(){
        System.out.println(message);
    }
}