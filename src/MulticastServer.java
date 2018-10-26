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
            
            Connection con=null;
            String usu;
            String pass;
            String nombre;
            String apellidos;
            String album;
            String albumN;
            String artista;
            String artistaN;
            String genero;
            String generoN;
            String descr;
            String descrN;
            String cancion;
            String cancionN;
            int duracion;
            int duracionN;

            
            
            
            switch (type){
                
                
                case "login":
                    System.out.println("Estás en el login");
                    usu = mapa.get("username");
                    pass = mapa.get("password");

                    
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

                    usu = mapa.get("username");
                    pass = mapa.get("password");
                    nombre = mapa.get("nombre");
                    apellidos = mapa.get("apellido");

                    int n = 0;
                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);
                        String sql3 = "insert into persona values(?, ?, ?, ?, ?)";
                        String sql4 = "select count(*) as cuenta from persona";
                        Statement s = con.createStatement();
                        PreparedStatement ps1 = con.prepareStatement(sql3);

                        ResultSet rs1 = s.executeQuery(sql4);
                        if(rs1.next()) {
                            n = rs1.getInt(1);
                        }
                        ps1.setString(1, usu);
                        ps1.setString(2, nombre);
                        ps1.setString(3, apellidos);
                        ps1.setString(4, pass);

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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "a_artist":
                    System.out.println("Añadir artista");

                    nombre = mapa.get("nombre");
                    genero = mapa.get("genero");



                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "insert into artista values(?, ?)";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, nombre);
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "e_artist":
                    System.out.println("Eliminar artista");
                    nombre = mapa.get("nombre");

                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "delete from artista where nombre = ?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, nombre);

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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "oa_artist":
                    System.out.println("Obtener datos artista");
                    nombre = mapa.get("nombre");

                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "select * from artista where nombre = ?";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, nombre);

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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "a_album":
                    System.out.println("Añadir album");

                    nombre = mapa.get("nombre");
                    artista = mapa.get("artista");
                    descr = mapa.get("descripcion");



                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "insert into album values(?, ?, ?)";

                        PreparedStatement ps = con.prepareStatement(sql);

                        ps.setString(1, nombre);
                        ps.setString(2, artista);
                        if(descr.isEmpty()){
                            descr="";
                        }

                        ps.setString(3, descr);

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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "s_artist":
                    System.out.println("Comprobacion de artista");

                    nombre = mapa.get("nombre");

                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "Select * from artista where nombre='"+nombre+"'";
                       Statement ps = con.createStatement();
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }


                    break;

                case "edit_artist":
                    System.out.println("Editar artista");
                    artista= mapa.get("nombre");
                    artistaN=mapa.get("nnombre");
                    genero=mapa.get("genero");



                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);


                        String sql = "UPDATE `artista` SET `Nombre` = '"+artistaN+"', Genero='"+genero+"' WHERE `artista`.`Nombre` = '"+artista+"'";
                        PreparedStatement ps = con.prepareStatement(sql);

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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "existe_album":

                    System.out.println("Comprobacion de album");

                    album = mapa.get("nombre");
                    artista = mapa.get("artista");

                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "Select titulo, nombreartista from album where titulo='"+album+"' and NombreArtista='"+artista+"'";
                        Statement ps = con.createStatement();
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }




                    break;
                    
                case "edit_album":
                    System.out.println("Editar album");
                     album= mapa.get("nombre");
                     artista =mapa.get("artista");

                    albumN= mapa.get("nombreN");
                    artistaN =mapa.get("artistaN");
                    descrN=mapa.get("descripcionN");


                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);




                        String sql = "UPDATE `album` SET `titulo` = '"+albumN+"', NombreArtista='"+artistaN+"',descripcion='"+descrN+"' WHERE titulo = '"+album+"'and NombreArtista='"+artista+"'";
                        PreparedStatement ps = con.prepareStatement(sql);



                        String mensaje = null;
                        int i = ps.executeUpdate();

                        if (i>=1) {

                            mensaje = "type|redit_album;editado|" + "true";
                        } else {
                            mensaje = "type|redit_album;editado|" + "false";
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "delete_album":
                    System.out.println("Eliminar album");
                    album= mapa.get("nombre");
                    artista =mapa.get("artista");


                    try{

                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                    String sql = "delete from `album` WHERE titulo = '"+album+"'and NombreArtista='"+artista+"'";
                    PreparedStatement ps = con.prepareStatement(sql);



                    String mensaje = null;
                    int i = ps.executeUpdate();
                    if (i>=1) {

                        mensaje = "type|rdelete_album;delete|" + "true";
                    } else {
                        mensaje = "type|rdelete_album;delete|" + "false";
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
                    if(con != null) {
                        con.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            break;

                case "existe_cancion":

                    System.out.println("Comprobacion de la cancion");

                    cancion = mapa.get("titulo");
                    artista = mapa.get("artista");
                    album = mapa.get("album");

                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "Select * from musica where titulo='"+cancion+"' and Artista='"+artista+"' and Album='"+album+"'";
                        Statement ps = con.createStatement();
                        ResultSet rs= ps.executeQuery(sql);


                        String mensaje = null;

                        rs.next();

                        if (rs.getRow() > 0) {

                            mensaje = "type|rexiste_cancion;existe|" + "true";
                        } else {
                            mensaje = "type|rexiste_cancion;existe|" + "false";
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }




                    break;
                case "a_cancion":
                    System.out.println("Añadir cancion");

                    nombre = mapa.get("nombre");
                    artista = mapa.get("artista");
                    album = mapa.get("album");
                    duracion=Integer.parseInt(mapa.get("duracion"));


                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);



                        String sql = "insert into musica values('"+nombre+"','" +artista+"','" +album+"','"+duracion+"')";

                        PreparedStatement ps = con.prepareStatement(sql);

                        int i = ps.executeUpdate();
                        String mensaje = null;
                        if(i == 1){
                            mensaje = "type|ra_cancion;creado|" + "true";
                        }else{
                            mensaje = "type|ra_cancion;creado|" + "false";
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "edit_cancion":
                    System.out.println("Editar cancion");
                    cancion= mapa.get("nombre");
                    artista =mapa.get("artista");
                    album=mapa.get("album");

                    cancionN= mapa.get("nombreN");
                    artistaN =mapa.get("artistaN");
                    albumN =mapa.get("albumN");
                    duracionN=Integer.parseInt(mapa.get("duracionN"));


                    try {
                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);




                        String sql = "UPDATE `musica` SET `titulo` = '"+cancionN+"', Artista='"+artistaN+"',Album='"+albumN+"',duracion='"+duracionN+"' WHERE titulo = '"+cancion+"'and Artista='"+artista+"'and Album='"+album+"'";
                        PreparedStatement ps = con.prepareStatement(sql);



                        String mensaje = null;
                        int i = ps.executeUpdate();

                        if (i>=1) {

                            mensaje = "type|redit_cancion;editado|" + "true";
                        } else {
                            mensaje = "type|redit_cancion;editado|" + "false";
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
                            if(con != null) {
                                con.close();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "delete_cancion":
                    System.out.println("Eliminar cancion");
                    cancion= mapa.get("nombre");
                    artista =mapa.get("artista");
                    album=mapa.get("album");


                    try{

                        con = DriverManager.getConnection(Config.URL, Config.USERDB, Config.PASSDB);

                        String sql = "delete from `musica` WHERE titulo = '"+cancion+"'and Artista='"+artista+"'and album='"+album+"'";
                        PreparedStatement ps = con.prepareStatement(sql);



                        String mensaje = null;
                        int i = ps.executeUpdate();
                        if (i>=1) {

                            mensaje = "type|rdelete_album;delete|" + "true";
                        } else {
                            mensaje = "type|rdelete_album;delete|" + "false";
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
                            if(con != null) {
                                con.close();
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
