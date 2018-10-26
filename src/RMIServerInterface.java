import java.rmi.server.RMIClassLoader;
import java.util.List;

public interface RMIServerInterface extends java.rmi.Remote {
     void addCliente(RMIClientInterface client) throws java.rmi.RemoteException;

    void eliminarCliente(RMIClientInterface client) throws java.rmi.RemoteException;

     void enviarMensajeACliente(String msg, String cliente) throws java.rmi.RemoteException;

     void enviarMensajeAClientes(String msg) throws java.rmi.RemoteException;

     String sayHello() throws java.rmi.RemoteException;

     boolean regisUser(String nom, String ape, String user, String pass) throws java.rmi.RemoteException;

     boolean login(String user, String pass) throws java.rmi.RemoteException;

     boolean existeArtista(String nombre) throws java.rmi.RemoteException;

     Artista obtenerArtista(String nombre) throws java.rmi.RemoteException;

     boolean anadirArtista(String nombre, String genero) throws java.rmi.RemoteException;

     boolean editarArtista(Artista a) throws  java.rmi.RemoteException;

     boolean eliminarArtista(String n) throws java.rmi.RemoteException;

     boolean anadirAlbum(Album a) throws  java.rmi.RemoteException;

     boolean editarAlbum(String n, String nn, String d) throws java.rmi.RemoteException;

     boolean eliminarAlbum(String n, String a) throws java.rmi.RemoteException;

    //List<Album> listaAlbum() throws java.rmi.RemoteException;

    //public boolean login(String user, String pass) throws java.rmi.RemoteException;
}
