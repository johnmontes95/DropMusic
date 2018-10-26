
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface, Runnable{
    List<RMIClientInterface> lista;
    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public RMIServer() throws java.rmi.RemoteException{
        super();
        lista = new ArrayList<>();
    }

    @Override
    public void addCliente(RMIClientInterface client) throws RemoteException {
        lista.add(client);
    }

    @Override
    public void enviarMensajeACliente(String msg, String cliente) throws RemoteException {
        /*for(RMIClientInterface c : lista) {
            if (c.getUsuario().equals(cliente)) {
                c.mensaje(msg);
            }
        }*/
    }

    @Override
    public void enviarMensajeAClientes(String msg) throws RemoteException {
        for(RMIClientInterface c: lista){
            System.out.println(c.getUsuario());
            c.mensaje(msg);
        }
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

/*    public Persona getPersona(String nomUsu) throws RemoteException {
        String datos = "type | g_persona; nombre | " + nomUsu + "\n";
        try {
            sendUDPMessage(datos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }*/

    @Override
    public boolean login(String user, String pass) throws RemoteException {
        boolean r = false;
        String datos = "type | login; username | " + user + "; password | " + pass + "\n";
        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());

            if(msg.equals("on")){
                r = true;
            }

            enviarMensajeAClientes("Se ha logueado el usuario: " + user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean existeArtista(String nombre) throws RemoteException {
        boolean r = false;
        String datos = "type | s_artist; nombre | " + nombre + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());
            r = msg.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean anadirArtista(String nombre, String genero) throws RemoteException {
        boolean r = false;
        String datos = "type | a_artist; nombre | " + nombre + "; genero | " + genero + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());
            r = msg.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean editarArtista(Artista a) throws RemoteException {
        return false;
    }

    public Artista obtenerArtista(String nombre) throws RemoteException{
        String datos = "type | oa_artist; nombre | " + nombre + "\n";
        Artista a = null;
        try {
            sendUDPMessage(datos);
             a = (Artista) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public boolean eliminarArtista(String n) throws RemoteException {
        boolean r = false;

        String datos = "type | e_artist; nombre | " + n + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());
            r = msg.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean anadirAlbum(Album a) throws RemoteException {
        boolean r = false;

        String datos = "type | a_album; nombre | " + a.getNombre() + "; artista | " + a.getA().getNombre() + "; descripcion |"  + a.getDescripcion() + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().replaceAll(" ", "").trim());
            r = msg.equals("true");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean editarAlbum(String n, String nn, String d) throws RemoteException {
        return false;
    }

    @Override
    public boolean eliminarAlbum(String n, String a) throws RemoteException {
        return false;
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

    public Object mensajeUDP(String  c){
        String[] datos = c.split(";");
        String[] valor = null;
        HashMap<String, String> mapa = new HashMap<>();
        for(String item : datos)
        {
            valor = item.split("\\|");
            mapa.put(valor[0], valor[1]);
        }

        Object msg = null;
        String type = mapa.get("type");
        switch(type){
            case "status":
                msg = mapa.get("logueado");
                break;
            case "ca_artist":
                msg = mapa.get("creado");
                break;
            case "ea_artist":
                msg = mapa.get("eliminado");
                break;
            case "ba_artist":
                msg = mapa.get("eliminado");
                break;
            case "da_artist":
                msg = new Artista(mapa.get("nombre"), mapa.get("genero"));
                break;
            case "ca_album":
                msg = mapa.get("creado");
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
