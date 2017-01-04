import java.net.* ;

public class UDPClient{
   private final static int PACKETSIZE = 100;

   public static void main(String args[]){
      DatagramSocket socket = null ;

      try{
         // Convert the arguments first, to ensure that they are valid
         InetAddress host = InetAddress.getByName("localhost");
         int port = Integer.parseInt("8080");
			int sequence_number = Integer.parseInt("1");
			int window_size = Integer.parseInt("1");
			int ack = Integer.parseInt("2");

         socket = new DatagramSocket();     // Construct the socket

         // Construct the datagram packet
         byte [] data = "Hello Server".getBytes();
         DatagramPacket packet = new DatagramPacket(data, data.length, host, port);

         socket.send(packet);
         socket.setSoTimeout(4000);
         packet.setData(new byte[PACKETSIZE]);  // Prepare the packet for receive
         socket.receive(packet);                // Wait for a response from the server 

         System.out.println(new String(packet.getData()));  // Print the response

      }catch(Exception e){
         System.out.println(e);
      }
      finally{
         if(socket != null)
            socket.close();
      }
   }
}
