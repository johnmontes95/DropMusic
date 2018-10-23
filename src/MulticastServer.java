import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.*;
import java.util.HashMap;

public class MulticastServer extends Thread{
    private static String MULTICAST_ADDRESS = "224.0.224.0";
    private static int PORT = 4321;
    private static long SLEEP_TIME = 5000;

    static {
        try {
            //String driver = getValor("driverClass");
            Class.forName(Config.DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("No puedo cargar el driver JDBC de la BD");
        }
    }

    public static void main(String[] args) {
        MulticastServer server = new MulticastServer();
        server.start();
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }



    public void cadena(String c){
        String[] datos = c.split(";");
        String[] valor = null;
        HashMap<String, String> mapa = new HashMap<>();
        for(String item : datos)
        {
            valor = item.split("\\|");
            mapa.put(valor[0], valor[1]);
        }



        if(mapa.containsKey("type")){
            String type = mapa.get("type");
            switch (type){
                case "login":
                    System.out.println("Estás en el login");
                    String usu = mapa.get("username");
                    String pass = mapa.get("password");
                    String mensaje = null;
                    Connection con = null;
                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "select * from persona where usuario = ? and contraseña=?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, usu);
                        ps.setString(2, pass);

                        ResultSet rs = ps.executeQuery();
                        if(rs.next()){
                            mensaje = "type | status; logueado | on";
                        }else{
                            mensaje = "type | status; logueado | off";
                        }

                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            System.out.print("Estoy aquí");
                            e.printStackTrace();
                        }


                        ps.close();
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case "register":
                    System.out.println("Estás en el registro");

                    String usu1 = mapa.get("username");
                    String pass1 = mapa.get("password");
                    String nombre = mapa.get("nombre");
                    String apellidos = mapa.get("apellido");
                    Connection con1 = null;
                    String mensaje1 = null;
                    int n = 0;
                    try {
                        con1 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);
                        con1.setAutoCommit(false);
                        String sql3 = "insert into persona values(?, ?, ?, ?, ?)";
                        String sql4 = "select count(*) as cuenta from persona";
                        Statement s = con1.createStatement();
                        PreparedStatement ps1 = con1.prepareStatement(sql3);

                        ResultSet rs1 = s.executeQuery(sql4);
                        if(rs1.next()) {
                            n = rs1.getInt(1);
                        }
                        ps1.setString(1, usu1);
                        ps1.setString(2, nombre);
                        ps1.setString(3, apellidos);
                        ps1.setString(4, pass1);

                        if(n > 0){
                            ps1.setString(5, "usuario");
                        }else{
                            ps1.setString(5,"editor");
                        }


                        int i = ps1.executeUpdate();

                        if(i == 1){
                            mensaje1 = "type | registro; registrado |" + " true";
                           System.out.println("Usuario creado");
                        }else{
                            mensaje1 = "type | registro; registrado |" + " false";
                            System.out.println("Usuario no creado");
                        }

                        try {
                            sendUDPMessage(mensaje1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps1.close();
                        rs1.close();
                        con1.commit();
                    } catch (SQLException e) {

                        try {
                            if(con1 != null) {
                                con1.rollback();
                            }
                        } catch (SQLException e1) {
                                e1.printStackTrace();
                        }


                        e.printStackTrace();
                    }finally{
                        try {
                            if(con1 != null) {
                                con1.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    System.out.print("");
            }
        }else{
            System.out.println("Ha ocurrido un error.");
        }

    }




    public static void sendUDPMessage(String message) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
        byte[] msg = message.getBytes();
        DatagramPacket packet = new DatagramPacket(msg, msg.length, group, PORT);
        socket.send(packet);
        socket.close();
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket without binding it (only for sending)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message1 = new String(packet.getData(), 0, packet.getLength());
                message1 = message1.replaceAll(" ", "").trim();
                cadena(message1);

                try {
                    sleep((long) (Math.random() * SLEEP_TIME));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
