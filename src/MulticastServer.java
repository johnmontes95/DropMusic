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

                    Connection con = null;
                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "select * from persona where usuario = ? and contraseña=?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, usu);
                        ps.setString(2, pass);

                        ResultSet rs = ps.executeQuery();
                        String mensaje = null;
                        if(rs.next()){
                            mensaje = "type|status;logueado|on";
                        }else{
                            mensaje = "type|status;logueado|off";
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
                    int n = 0;
                    try {
                        con1 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);
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
                        String mensaje1 = null;
                        if(i == 1){
                            mensaje1 = "type|registro;registrado|" + "true";
                           System.out.println("Usuario creado");
                        }else{
                            mensaje1 = "type|registro;registrado|" + "false";
                            System.out.println("Usuario no creado");
                        }

                        try {
                            sendUDPMessage(mensaje1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps1.close();
                        rs1.close();
                        s.close();
                    } catch (SQLException e) {
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

                case "a_artist":
                    System.out.println("Añadir artista");

                    String nom = mapa.get("nombre");
                    String genero = mapa.get("genero");
                    Connection con2 = null;


                    try {
                        con2 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "insert into artista values(?, ?)";

                        PreparedStatement ps = con2.prepareStatement(sql);

                        ps.setString(1, nom);
                        ps.setString(2, genero);

                        int i = ps.executeUpdate();
                        String mensaje = null;
                        if(i == 1){
                            mensaje = "type|ca_artist;creado|" + "true";

                        }else{
                            mensaje = "type|ca_artist;creado|" + "false";

                        }

                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con2 != null) {
                                con2.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "e_artist":
                    System.out.println("Eliminar artista");
                    String nomb = mapa.get("nombre");
                    Connection con3 = null;
                    try {
                        con3 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "delete from artista where nombre = ?";

                        PreparedStatement ps = con3.prepareStatement(sql);

                        ps.setString(1, nomb);

                        int i = ps.executeUpdate();
                        String mensaje = null;
                        if(i == 1){
                            mensaje = "type|ea_artist;eliminado|" + "true";

                        }else{
                            mensaje = "type|ea_artist;eliminado|" + "false";
                        }

                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con3 != null) {
                                con3.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "oa_artist":
                    System.out.println("Obtener datos artista");
                    String nombrea = mapa.get("nombre");
                    Connection con4 = null;
                    try {
                        con4 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "select * from artista where nombre = ?";

                        PreparedStatement ps = con4.prepareStatement(sql);

                        ps.setString(1, nombrea);

                        ResultSet rs = ps.executeQuery();
                        String mensaje = null;
                        if(rs.next()){
                            mensaje = "type|da_artist;nombre|" + rs.getString(1) + ";genero|" + rs.getString(2);
                        }

                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();
                        rs.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con4 != null) {
                                con4.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "a_album":
                    System.out.println("Añadir album");

                    String nomal = mapa.get("nombre");
                    String artista = mapa.get("artista");
                    String d = mapa.get("descripcion");
                    Connection con5 = null;


                    try {
                        con5 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "insert into album values(?, ?, ?)";

                        PreparedStatement ps = con5.prepareStatement(sql);

                        ps.setString(1, nomal);
                        ps.setString(2, artista);
                        ps.setString(3, d);

                        int i = ps.executeUpdate();
                        String mensaje = null;
                        if(i == 1){
                            mensaje = "type|ca_album;creado|" + "true";
                        }else{
                            mensaje = "type|ca_album;creado|" + "false";
                        }

                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con5 != null) {
                                con5.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "s_artist":
                    System.out.println("Comprobacion de artista");

                    String eArtista = mapa.get("nombre");
                    Connection con6 = null;
                    try {
                        con6 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "Select * from artista where nombre='"+eArtista+"'";
                       Statement ps = con6.createStatement();
                       ResultSet rs= ps.executeQuery(sql);

                        String mensaje = null;

                            rs.next();

                            if (rs.getRow() > 0) {

                                mensaje = "type|ss_artist;existe|" + "true";
                            } else {
                                mensaje = "type|ss_artist;existe|" + "false";
                            }


                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();
                        rs.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con6 != null) {
                                con6.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                    break;

                case "edit_artist":
                    System.out.println("Editar artista");
                    String  editArtista= mapa.get("nombre");
                    String editNartista=mapa.get("nnombre");
                    String editGenero=mapa.get("genero");


                    Connection con7 = null;
                    try {
                        con7 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);


                        String sql = "UPDATE `artista` SET `Nombre` = '"+editNartista+"', Genero='"+editGenero+"' WHERE `artista`.`Nombre` = '"+editArtista+"'";
                        PreparedStatement ps = con7.prepareStatement(sql);

                        String mensaje = null;
                        int i = ps.executeUpdate();

                        if (i>=1) {

                            mensaje = "type|redit_artist;editado|" + "true";
                        } else {
                            mensaje = "type|redit_artist;editado|" + "false";
                        }


                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con7 != null) {
                                con7.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "existe_album":

                    System.out.println("Comprobacion de album");

                    String exAlbum = mapa.get("nombre");
                    String exArtista = mapa.get("artista");
                    Connection con8 = null;
                    try {
                        con8 = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "Select * from album where titulo='"+exAlbum+"' and NombreArtista='"+exArtista+"'";
                        Statement ps = con8.createStatement();
                        ResultSet rs= ps.executeQuery(sql);

                        String mensaje = null;

                        rs.next();

                        if (rs.getRow() > 0) {

                            mensaje = "type|rexiste_album;existe|" + "true";
                        } else {
                            mensaje = "type|rexiste_album;existe|" + "false";
                        }


                        try {
                            sendUDPMessage(mensaje);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ps.close();
                        rs.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally{
                        try {
                            if(con8 != null) {
                                con8.close();
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
            socket = new MulticastSocket(PORT); // create socket without binding it (only for sending)
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Received packet from " + packet.getAddress().getHostAddress() + ":" + packet.getPort() + " with message:");
                String message1 = new String(packet.getData(), 0, packet.getLength());
                message1 = message1.trim();
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
