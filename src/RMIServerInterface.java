import java.util.ArrayList;
import java.util.List;

public interface RMIServerInterface extends java.rmi.Remote {

    void addCliente(RMIClientInterface client) throws java.rmi.RemoteException;

    void eliminarCliente(RMIClientInterface client) throws java.rmi.RemoteException;

    void enviarMensajeACliente(String msg, String cliente) throws java.rmi.RemoteException;

    void enviarMensajeAClientes(String msg) throws java.rmi.RemoteException;


    void cambiarPermisos(String editor, String usu) throws java.rmi.RemoteException;

    String sayHello() throws java.rmi.RemoteException;

    boolean regisUser(String nom, String ape, String user, String pass) throws java.rmi.RemoteException;

    boolean login(String user, String pass) throws java.rmi.RemoteException;

    boolean existeArtista(String nombre) throws java.rmi.RemoteException;

    Artista obtenerArtista(String nombre) throws java.rmi.RemoteException;

    boolean anadirArtista(String nombre, String genero) throws java.rmi.RemoteException;

    boolean editarArtista(String a ,String aa,String genero) throws  java.rmi.RemoteException;

    boolean eliminarArtista(String n) throws java.rmi.RemoteException;

    boolean estaAlbum(String al,String ar) throws java.rmi.RemoteException;

    boolean anadirAlbum(Album a) throws  java.rmi.RemoteException;

    boolean editarAlbum(Album a,Album n) throws java.rmi.RemoteException;



    boolean eliminarAlbum(String n, String a) throws java.rmi.RemoteException;

    boolean anadirCancion(Cancion c) throws  java.rmi.RemoteException;

    boolean editarCancion(Cancion c,Cancion n) throws  java.rmi.RemoteException;

    boolean eliminarCancion(Cancion c) throws  java.rmi.RemoteException;

    boolean estaCancion(Cancion c) throws java.rmi.RemoteException;

    Artista buscarArtista(Artista a) throws java.rmi.RemoteException;

    Album buscarCanciones(Album al) throws java.rmi.RemoteException;

    void aniadirCritica(String autor, String artista, String album, int punt, String cr) throws java.rmi.RemoteException;

    ArrayList<Album> buscarAlbum(String al) throws java.rmi.RemoteException;
    ArrayList<Artista> buscarGenero(String genero) throws java.rmi.RemoteException;
    //public boolean login(String user, String pass) throws java.rmi.RemoteException;
}
