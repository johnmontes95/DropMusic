
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface, Runnable{

    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public RMIServer() throws java.rmi.RemoteException{
        super();
    }

    public String sayHello() throws RemoteException {
        System.out.println("print do lado do servidor...!.");

        return "Bienvenido a DropMusic.";
    }



    @Override
    public boolean regisUser(String nom, String ape, String user, String pass) throws RemoteException {
        String datos = "type | register; nombre | " + nom + "; apellido | " + ape + "; username | " + user + "; password | " + pass + "\n";
        try {
            sendUDPMessage(datos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean login(String user, String pass) throws RemoteException {
        boolean r = false;
        String datos = "type | login; username | " + user + "; password | " + pass + "\n";
        try {
            sendUDPMessage(datos);
            String msg = mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());
            System.out.print(msg);
            if(msg.equals("on")){
                r = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }


    public String receiveUDPMessage() throws IOException {
        byte[] buffer=new byte[1024];
        MulticastSocket socket=new MulticastSocket(PORT);
        InetAddress group=InetAddress.getByName(MULTICAST_ADDRESS);
        socket.joinGroup(group);
        String msg = null;
        while(true) {
            System.out.println("Waiting for multicast message...");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            msg = new String(packet.getData(), packet.getOffset(), packet.getLength());
            System.out.println("[Multicast UDP message received]" + msg);

            if(!msg.isEmpty()){
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();

        return msg;
    }

    public String mensajeUDP(String  c){
        String[] datos = c.split(";");
        String[] valor = null;
        HashMap<String, String> mapa = new HashMap<>();
        for(String item : datos)
        {
            valor = item.split("\\|");
            mapa.put(valor[0], valor[1]);
        }

        String msg = null;
        String type = mapa.get("type");
        switch(type){
            case "status":
                msg = mapa.get("logueado");
                break;
            default:
                System.out.println("Error");
        }

        return msg;
    }

    public static void sendUDPMessage(String message) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, PORT);
        socket.send(packet);
        socket.close();
    }




    public static void main(String args[]) {
       /* System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());*/
        try {
            RMIServer server = new RMIServer();
            Registry r = LocateRegistry.createRegistry(7000);
            r.rebind("servidor", server);
            System.out.println("Servidor iniciado.");
            Thread client = new Thread(server);
            client.start();
        } catch (RemoteException e) {
            System.out.println("Exception: " + e);
        }

    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
