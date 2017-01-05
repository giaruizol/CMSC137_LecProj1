import java.io.*; 
import java.net.*;
import java.util.Objects;


public class UDPServer implements Runnable{
	DatagramSocket socket = null;
	Thread t = new Thread(this);
    private final static int PACKETSIZE = 100;

    byte[] buff = new byte[256];
	String serverData;

    public UDPServer() throws SocketException{
    	int port = Integer.parseInt("8080");                    // Convert the argument to ensure that is it valid
        socket = new DatagramSocket(port);       // Construct the socket
        socket.setSoTimeout(100);
        System.out.println("Server is running in port 8080...");
        t.start();
    }

	public void send(String message) throws IOException {
		DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);  // Create a packet
		//byte [] data = "1".getBytes();
		DatagramPacket packet2 = new DatagramPacket(buff, buff.length, packet.getAddress(),packet.getPort());
		socket.send(packet2);   // Return the packet to the sender
	}

    public void run(){
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) { }
			try{
		     //	int port = Integer.parseInt("8080");                    // Convert the argument to ensure that is it valid
         	//	DatagramSocket socket = new DatagramSocket(port);       // Construct the socket
			 	byte[] buff = new byte[256];
		 		String serverData;
			 	for( ;; ) {
		        	DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE], PACKETSIZE);  // Create a packet
		        	socket.receive(packet);                // Receive a packet (blocking)
					buff = packet.getData();
					serverData= new String(buff, 0, packet.getLength()); 	
					System.out.println("-----------------------------------------");		
					if(serverData.contains("0")){
						System.out.println("SYN 0 received from client...");
						System.out.println("SYN: 1");  // Print the packet
						System.out.println("ACK: " + new String(packet.getData()));  // Print the packet
						byte [] data = "1".getBytes();
						DatagramPacket packet2 = new DatagramPacket(buff, buff.length, packet.getAddress(),packet.getPort());
						socket.send(packet2);   // Return the packet to the sender
						System.out.println("ACK received from client...");
					}else{
						System.out.println("SYN 1 received from client...");
						System.out.println("SYN: 0");  // Print the packet
						System.out.println("ACK: " + new String(packet.getData()));  // Print the packet
						byte [] data = "0".getBytes();
						DatagramPacket packet2 = new DatagramPacket(buff, buff.length, packet.getAddress(),packet.getPort());
						socket.send(packet2);   // Return the packet to the sender
						System.out.println("ACK received from client...");
					}
					System.out.println("-----------------------------------------");        
		    	}
			}catch(Exception e){
			    //System.out.println(e);
			}
		}
  	}
  	public static void main(String[] args) throws SocketException {
		new UDPServer();
	}
}
