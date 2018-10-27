
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
import java.util.List;
import java.util.ArrayList;

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
    public void eliminarCliente(RMIClientInterface client) throws RemoteException {
        lista.remove(client);
    }

    @Override
    public void enviarMensajeACliente(String msg, String cliente) throws RemoteException {
        for(RMIClientInterface c : lista) {
            if (c.getUsuario().equals(cliente)) {
                c.mensaje(msg);
            }
        }
    }

    @Override
    public void enviarMensajeAClientes(String msg) throws RemoteException {
        for(RMIClientInterface c: lista){
            System.out.println(c.getUsuario());
            c.mensaje(msg);
        }
    }

    @Override
    public void cambiarPermisos(String editor, String usu) throws RemoteException {
        String datos = "type|upermisos;editor|" + editor + ";usuario|" + usu + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().trim());
            if(msg.equals("true")){
                String mensaje = "El usuario " + editor + " ha cambiado sus permisos a editor.";
                enviarMensajeACliente(mensaje, usu);
            }else{
                throw new RemoteException("El usuario no existe o no tienes permisos.");
            }
        } catch (IOException e) {

            throw new RemoteException("Ha ocurrido un error al actualizar los permisos del usuario.");
        }

    }



    @Override
    public boolean regisUser(String nom, String ape, String user, String pass) throws RemoteException {
        String datos = "type|register;nombre|" + nom + ";apellido|" + ape + ";username|" + user + ";password|" + pass + "\n";
        boolean r = false;
        try {
            sendUDPMessage(datos);
            r = ((String) mensajeUDP(receiveUDPMessage().trim())).equals("true");
        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error al registrarse.");
        }
        return r;
    }


    @Override
    public boolean login(String user, String pass) throws RemoteException {
        boolean r = false;
        String datos = "type|login;username|" + user + ";password|" + pass + "\n";
        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().trim());

            if(msg.equals("on")){
                r = true;
            }else{
                throw new RemoteException("No ha podido loguearse.");
            }
        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error.");
        }
        return r;
    }

    @Override
    public boolean existeArtista(String nombre) throws RemoteException {
        boolean r = false;
        String datos = "type|s_artist;nombre|" + nombre + "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().trim());
            r = msg.equals("true");
        } catch (IOException e) {
           throw new RemoteException("Ha ocurrido un error.");
        }
        return r;
    }

    @Override
    public boolean anadirArtista(String nombre, String genero) throws RemoteException {
        boolean r = false;
        String datos = "type|a_artist;nombre|" + nombre + ";genero|" + genero + "\n";

        try {

            if(!existeArtista(nombre)) {
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");
            }else{
                throw new RemoteException("Ya existe ese artista.");
            }
        } catch (IOException e) {
            throw new RemoteException("Error al añadir al artista.");
        }
        return r;
    }

    @Override
    public boolean editarArtista(String a,String aa,String genero) throws RemoteException {
        boolean r = false;

        String datos = "type|edit_artist;nombre|" + a + ";nnombre|"+aa+";genero|"+genero+"\n";

        try {

            if(existeArtista(a)) {
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");
            }else{
                throw new RemoteException("No existe el artista buscado");
            }

        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error");
        }
        return r;
    }

    public Artista obtenerArtista(String nombre) throws RemoteException{
        String datos = "type|oa_artist;nombre|" + nombre + "\n";
        Artista a = null;
        try {
            if(existeArtista(nombre)) {
                sendUDPMessage(datos);
                a = (Artista) mensajeUDP(receiveUDPMessage().trim());
            }else{
                throw new RemoteException("No existe el artista buscado");
            }

        } catch (IOException e) {
            throw new RemoteException("No existe el artista buscado");
        }
        return a;
    }

    @Override
    public boolean eliminarArtista(String n) throws RemoteException {
        boolean r = false;

        String datos = "type|e_artist;nombre|" + n + "\n";

        try {
            if(existeArtista(n)) {
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");
            }
        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error.");
        }
        return r;
    }

    @Override
    public boolean estaAlbum(String al,String ar) throws RemoteException{

        boolean r = false;

        String datos = "type|existe_album;nombre|" + al+";artista|"+ar + "\n";

        try {

                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");

        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error.");
        }
        return r;



    }
    @Override
    public boolean anadirAlbum(Album a) throws RemoteException {
        boolean r = false;

        String datos = "type|a_album;nombre|" + a.getNombre() + ";artista|" + a.getA().getNombre() + ";descripcion|"  + a.getDescripcion() + "\n";

        try {
            if(existeArtista(a.getA().getNombre())) {
                if (estaAlbum(a.getNombre(), a.getA().getNombre())) {

                    throw new RemoteException("Ya existe ese album");
                }else {

                    sendUDPMessage(datos);
                    String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                    r = msg.equals("true");
                }

            }else{

                anadirArtista(a.getA().getNombre(),"desconocido");
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");

            }


        } catch (IOException e) {
            throw new RemoteException("Ha ocurrido un error.");
        }
        return r;
    }





    @Override
    public boolean editarAlbum(Album a,Album n) throws RemoteException {

        boolean r=false;
        String datos = "type|edit_album;nombre|" + a.getNombre() + ";artista|" + a.getA().getNombre()  +";nombreN|" + n.getNombre() + ";artistaN|" + n.getA().getNombre() + ";descripcionN|"  + n.getDescripcion() + "\n";

        try {

            // Si no existe ese Album no hace nada, si existe y existe un album con los nuevos datos de album borra ese album y actualiza el original.
            if (estaAlbum(a.getNombre(), a.getA().getNombre())) {

                if (estaAlbum(n.getNombre(), n.getA().getNombre())) {
                    //Habria quie eliminar uno de los dos  registros

                    if(n.getA().getNombre().equals(a.getA().getNombre())){

                        sendUDPMessage(datos);
                        String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                        r = msg.equals("true");
                    }


                }else{
                    //Se conmprueba si existe el nuevo artista y si no se crea

                    if (existeArtista(n.getA().getNombre())) {

                        //Como existe el artista no se hace nada



                    } else {
                        //Como no existe el nuevo artista se crea

                        anadirArtista(n.getA().getNombre(),"desconocido");


                    }

                    //Se edita el album
                    sendUDPMessage(datos);
                    String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                    r = msg.equals("true");
                    if(!a.getDescripcion().equals(n.getDescripcion()) && r){
                        String mensaje = "Se ha editado el album " + a.getNombre() + " del artista " + a.getA().getNombre();
                        enviarMensajeAClientes(mensaje);
                    }
                }
            }else{
                throw new RemoteException("No existe el album");
            }



        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;




    }

    @Override
    public boolean eliminarAlbum(String n, String a) throws RemoteException {

        boolean r = false;
        String datos = "type|delete_album;nombre|" + n + ";artista|" + a +"\n";

        try {

            // Si no existe ese Album no hace nada
            if (estaAlbum(n, a)) {

                //Se elimina el album
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");

            }
        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;
    }


    @Override
    public boolean estaCancion(Cancion c) throws RemoteException{

        boolean r = false;
        String datos = "type|existe_cancion;titulo|" + c.getTitulo()+";artista|"+c.getAl().getA().getNombre() +";album|"+c.getAl().getNombre()+ "\n";

        try {
            sendUDPMessage(datos);
            String msg = (String) mensajeUDP(receiveUDPMessage().trim());
            r = msg.equals("true");
        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;
    }
    public boolean anadirCancion(Cancion c) throws  RemoteException{
        boolean r = false;

        String datos = "type|a_cancion;nombre|" + c.getTitulo() + ";artista|" + c.getAl().getA().getNombre() + ";album|"  + c.getAl().getNombre() +";duracion|"+c.getDuracion()+ "\n";

        try {
            //Comprobar si existe la cancion
            if(estaCancion(c)) {
                System.out.println("Ya existe esa cancion");


            }else{
                //comprobar si esta el album
                if (!estaAlbum(c.getAl().getNombre(),c.getAl().getA().getNombre())) {
                    //Si no esta se crea el album (Este metodo se encarga tambien de crear el artista si no existiese)
                    // c.getAl().setDescripcion("Desconoccida");
                    anadirAlbum(c.getAl());
                }

                //Una vez con la certeza de que no existe esa cancion y si el album y artista se introduce la cancion
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");
            }


        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;
    }
    public boolean editarCancion(Cancion c,Cancion n) throws  RemoteException{
        boolean r=false;
        String datos = "type|edit_cancion;nombre|" + c.getTitulo() + ";artista|" + c.getAl().getA().getNombre()  +";album|"+c.getAl().getNombre()+";nombreN|" + n.getTitulo() + ";artistaN|" + n.getAl().getA().getNombre() +";albumN|"+n.getAl().getNombre() +";duracionN|"  + n.getDuracion() + "\n";

        try {

            // Si no existe esa cancion no hace nada
            if (estaCancion(c)) {

                //Si existe la nueva se elimina el registro antiguo
                if (estaCancion(n)) {
                    //Habria quie eliminar uno de los dos  registros
                    eliminarCancion(c);

                }else{
                    //Se conmprueba si existe el album nuevo

                    if (estaAlbum(n.getAl().getNombre(),n.getAl().getA().getNombre())) {

                        //Como existe el album no se hace nada


                    } else {
                        //Si no existe se crea
                        anadirAlbum(n.getAl());
                    }

                    //Se introduce la cancion
                    sendUDPMessage(datos);
                    String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                    r = msg.equals("true");
                }
            }



        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;

    }
    public boolean eliminarCancion(Cancion c) throws  RemoteException{
        boolean r = false;
        String datos = "type|delete_cancion;nombre|" + c.getTitulo() + ";artista|" + c.getAl().getA().getNombre()+";album|"+c.getAl().getNombre() +"\n";

        try {

            // Si no existe esa cancion
            if (estaCancion(c)) {

                //Se elimina la cancion
                sendUDPMessage(datos);
                String msg = (String) mensajeUDP(receiveUDPMessage().trim());
                r = msg.equals("true");

            }
        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return r;
    }

    @Override
    public Artista buscarArtista(Artista a)throws  RemoteException {

        String datos = "type|buscar_artista;nombre|" + a.getNombre() +"\n";
        Artista n=null;
        try {
            // Si no existe el artista no se ejecuta

            if (existeArtista(a.getNombre())) {
                sendUDPMessage(datos);

                n = (Artista) mensajeUDP(receiveUDPMessage().trim());


            }
        } catch (IOException e) {
            throw new RemoteException("No existe el album");
        }
        return n;
    }

    public Album buscarCanciones(Album al)throws  RemoteException{

        String datos = "type|buscar_canciones;album|" + al.getNombre() +";artista|"+al.getA().getNombre()+"\n";
        Album n=null;
        try {
            // Si no existe el artista no se ejecuta

            if (estaAlbum(al.getNombre(),al.getA().getNombre())) {
                sendUDPMessage(datos);

                n = (Album) mensajeUDP(receiveUDPMessage().trim());

            }else{
                throw new RemoteException("No existe el álbum");
            }
        } catch (IOException e) {
           throw new RemoteException("Ha ocurrido un error");
        }
        return n;
    }

    @Override
    public void aniadirCritica(String autor, String artista, String album, int punt, String cr) throws RemoteException {
        String datos = "type|a_crit;autor|" + autor + ";artista|" + artista + ";album|"  + album + ";punt|" + punt + ";crit|" + cr + "\n";

        try {
            sendUDPMessage(datos);
            String result = (String) mensajeUDP(receiveUDPMessage().trim());
            if(!result.equals("true")){
                throw new RemoteException("No se ha podido insertar la crítica.");
            }
        } catch (IOException e) {
            throw new RemoteException("No se ha podido insertar la crítica.");
        }
    }


    public ArrayList<Album> buscarAlbum(String al)throws  RemoteException{

        String datos = "type|buscar_album;album|" + al +"\n";
        ArrayList<Album> albunes=null;
        try {
                sendUDPMessage(datos);
                albunes=(ArrayList<Album>) (mensajeUDP(receiveUDPMessage().trim()));
        } catch (IOException e) {
            throw new RemoteException("No se puede obtener el album");
        }
        return  albunes;


    }

    public ArrayList<Artista> buscarGenero(String genero) throws RemoteException{

        String datos = "type|buscar_genero;genero|" + genero +"\n";
        ArrayList<Artista> artistas=null;
        try {
            sendUDPMessage(datos);
            artistas=(ArrayList<Artista>) (mensajeUDP(receiveUDPMessage().trim()));

        } catch (IOException e) {
            //throw new RemoteException("No existe el album");
            throw new RemoteException("No se puede obtener los artistas de ese genero");
        }
        return  artistas;
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
        int i;
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
            case "registro":
                msg = mapa.get("registrado");
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
            case "ss_artist":
                msg=mapa.get("existe");
                break;
            case "redit_artist":
                msg=mapa.get("editado");
                break;
            case "rexiste_album":
                msg=mapa.get("existe");
                break;
            case "redit_album":
                msg=mapa.get("editado");
                break;
            case "rdelete_album":
                msg=mapa.get("delete");
                break;
            case "rexiste_cancion":
                msg=mapa.get("existe");
                break;
            case "ra_cancion":
                msg=mapa.get("creado");
                break;
            case "redit_cancion":
                msg=mapa.get("editado");
                break;
            case "pupdate":
                msg = mapa.get("actualizados");
                break;
            case "rbusca_artista":
                msg = new Artista(mapa.get("nombre"), mapa.get("genero"));
                i =Integer.parseInt( mapa.get("cont"));
                String nombre;
                String desc;
                Album al=null;
                ((Artista) msg).setnAlbumes(i);

                for(int j=0;j<i;j++){

                    nombre=mapa.get("item_"+ j);
                    al = new Album();
                    al.setNombre(nombre);
                    desc=mapa.get("desc_"+ j);
                    al.setDescripcion(desc);
                    ((Artista) msg).aniadirAlbum(al);


                }
                break;
            case "rbusca_canciones":



                Artista a=new Artista();
                a.setNombre(mapa.get("artista"));
                msg = new Album(mapa.get("nombre"),a,mapa.get("desc"));

                i =Integer.parseInt( mapa.get("cont"));
                String titulo;
                String durac;
                Cancion ca=null;
                ((Album)msg).setNumCanciones(i);

                for(int j=0;j<i;j++){

                    titulo=mapa.get("item_"+ j);
                    ca = new Cancion();
                    ca.setTitulo(titulo);
                    durac=mapa.get("durac_"+ j);
                    ca.setDuracion(Integer.parseInt(durac));
                    ((Album) msg).anadirCancion(ca);


                }
                break;
            case "rbusca_album":


                Album aal;
                Artista aa;

                msg = new ArrayList<Album>();

                i =Integer.parseInt( mapa.get("cont"));
                String album=mapa.get("album");
                for(int j=0;j<i;j++){
                    aa=new Artista();
                    aa.setNombre(mapa.get("artista_"+j));
                    aal=new Album();
                    aal.setNombre(album);
                    aal.setDescripcion(mapa.get("desc_"+j));
                    aal.setA(aa);

                    ((ArrayList<Album>)msg).add(aal);
                }


                break;

            case "rbusca_genero":



                Artista aartista;

                msg = new ArrayList<Artista>();

                i =Integer.parseInt( mapa.get("cont"));
                String genero=mapa.get("genero");

                for(int j=0;j<i;j++){

                    aartista=new Artista();
                    aartista.setNombre(mapa.get("artista_"+j));
                    aartista.setGenero(genero);


                    ((ArrayList<Artista>)msg).add(aartista);
                }


                break;

            case "t_crit":
                msg = mapa.get("aniadida");
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
            socket = new MulticastSocket(PORT); // create socket and bind it
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
