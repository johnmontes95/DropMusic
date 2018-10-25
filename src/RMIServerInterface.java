public interface RMIServerInterface extends java.rmi.Remote {
    public String sayHello() throws java.rmi.RemoteException;

    public boolean regisUser(String nom, String ape, String user, String pass) throws java.rmi.RemoteException;

    public boolean login(String user, String pass) throws java.rmi.RemoteException;

    public boolean existeArtista(String nombre) throws java.rmi.RemoteException;

    public boolean anadirArtista(String nombre, String genero) throws java.rmi.RemoteException;

    public boolean editarArtista(Artista a) throws  java.rmi.RemoteException;

    public boolean eliminarArtista(String n) throws java.rmi.RemoteException;

    public boolean anadirAlbum(String n, String a, String d) throws  java.rmi.RemoteException;

    public boolean editarAlbum(String n, String nn, String d) throws java.rmi.RemoteException;

    public boolean eliminarAlbum(String n, String a) throws java.rmi.RemoteException;

    //public boolean login(String user, String pass) throws java.rmi.RemoteException;
}
