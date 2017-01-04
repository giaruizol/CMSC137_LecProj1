import java.net.* ;

public class UDPServer{
   private final static int PACKETSIZE = 100 ;
   public static void main(String args[]){
      try{
         int port = Integer.parseInt("8080");                    // Convert the argument to ensure that is it valid
         DatagramSocket socket = new DatagramSocket(port);       // Construct the socket
         System.out.println("Server is running in port 8080...");

         for( ;; ) {
            DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);  // Create a packet
            socket.receive(packet);                                                        // Receive a packet (blocking)
            System.out.println(packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()));  // Print the packet
            socket.send(packet);   // Return the packet to the sender
        }
     }catch(Exception e){
        System.out.println(e);
     }
  }
}