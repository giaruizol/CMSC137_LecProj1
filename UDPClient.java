import java.io.*; 
import java.net.*;
import java.util.*;

public class UDPClient implements Runnable{
	Thread t = new Thread(this);
    private final static int PACKETSIZE = 100;
    DatagramSocket socket = null ;

	int seq_num = 0;
	byte[] buff = new byte[256];
	String clientData;

	double [] packet_drop = {0.00, 0.25, 0.50, 0.75};

	public UDPClient() throws SocketException {
		socket = new DatagramSocket();     // Construct the socket
		socket.setSoTimeout(100);
		t.start();
	}

	public void send(String message) throws IOException {
		// Construct the datagram packet
		byte [] data = message.getBytes();
		// Convert the arguments first, to ensure that they are valid
		InetAddress host = InetAddress.getByName("127.0.0.1");
		int port = Integer.parseInt("8080");
		int window_size = Integer.parseInt("1");
		int ack = Integer.parseInt("2");
		DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
		socket.send(packet);
	}

	public boolean packetDrop(double percent){
		double random = Math.random();

		if(percent == 0.0){
			return true;
		}else if(percent == 0.25){
			if (random <= 0.25){
				return true;
			}else{
				return false;
			}	
		}else if(percent == 0.50){
			if (random <= 0.50){
				return true;
			}else{
				return false;
			}
		}else if(percent == 0.75){
			if (random <= 0.75){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	public void run(){
		while(true){
		    try{
				Thread.sleep(2000);
			}
			catch (InterruptedException e){ }

			try{
				InetAddress host = InetAddress.getByName("127.0.0.1");
				int port = Integer.parseInt("8080");
				Random num  = new Random();
				double percent = packet_drop[num.nextInt(4)];
				System.out.println("-----------------------------------------");
				 if(seq_num == 0){
					if(!packetDrop(percent)){
						byte [] data = "0".getBytes();
						seq_num = 1;
						DatagramPacket packet = new DatagramPacket(data, data.length, host, port);
						System.out.println("SYN: 0");
						System.out.println("Sending SYN 0 to server...");
						socket.setSoTimeout(4000);
						packet.setData(new byte[PACKETSIZE]);  // Prepare the packet for receive
						socket.receive(packet);                // Wait for a response from the server 

						buff = packet.getData();
						clientData= new String(buff, 0, packet.getLength()); 			
						if(clientData.contains("0")){
							System.out.println("SEQ: " + seq_num);
							System.out.println("ACK: 1");
						}else{
							System.out.println("SEQ: " + seq_num);
							System.out.println("ACK: 1");
						}	 
						 if(socket != null){
							socket.close();
						}
					}	
					else{
						System.out.println("packet dropped");
					} 
				}else{
					if(!packetDrop(percent)){
						// Construct the datagram packet
					 	byte [] data = "1".getBytes();
					 	seq_num = 0;
						DatagramPacket packet = new DatagramPacket(data, data.length, host, port); 
						System.out.println("SYN: 1");
						System.out.println("Sending SYN 1 to server...");
						socket.send(packet);
						 socket.setSoTimeout(4000);
						 packet.setData(new byte[PACKETSIZE]);  // Prepare the packet for receive
						 socket.receive(packet);                // Wait for a response from the server 
						System.out.println("ACK received from server...");
						 buff = packet.getData();
						clientData= new String(buff, 0, packet.getLength()); 			
						if(clientData.contains("0")){
							System.out.println("SEQ: " + seq_num);
							System.out.println("ACK: 1");
						}else{
							System.out.println("SEQ: " + seq_num);
							System.out.println("ACK: 1");
						}
					}
					else{
						System.out.println("packet dropped");
					}		
				}

				System.out.println("-----------------------------------------");
			}
			catch(Exception e){
			 	//System.out.println(e);
		    }
		//	finally{
	      //   if(socket != null)
	      //      socket.close();
	      //	}
      	}
	}
	public static void main(String[] args) throws SocketException {
		new UDPClient();
	}
}
