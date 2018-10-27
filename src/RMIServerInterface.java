public interface RMIServerInterface extends java.rmi.Remote {
    public String sayHello() throws java.rmi.RemoteException;

    public boolean regisUser(String nom, String ape, String user, String pass) throws java.rmi.RemoteException;

    public boolean login(String user, String pass) throws java.rmi.RemoteException;

    public boolean existeArtista(String nombre) throws java.rmi.RemoteException;

    public Artista obtenerArtista(String nombre) throws java.rmi.RemoteException;

    public boolean anadirArtista(String nombre, String genero) throws java.rmi.RemoteException;

    public boolean editarArtista(String a ,String aa,String genero) throws  java.rmi.RemoteException;

    public boolean eliminarArtista(String n) throws java.rmi.RemoteException;

    public boolean estaAlbum(String al,String ar) throws java.rmi.RemoteException;

    public boolean anadirAlbum(Album a) throws  java.rmi.RemoteException;

    public boolean editarAlbum(Album a,Album n) throws java.rmi.RemoteException;

    public boolean eliminarAlbum(String n, String a) throws java.rmi.RemoteException;

    public boolean anadirCancion(Cancion c) throws  java.rmi.RemoteException;
    public boolean editarCancion(Cancion c,Cancion n) throws  java.rmi.RemoteException;
    public boolean eliminarCancion(Cancion c) throws  java.rmi.RemoteException;
    public boolean estaCancion(Cancion c) throws java.rmi.RemoteException;

    Artista buscarArtista(Artista a) throws java.rmi.RemoteException;

    //public boolean login(String user, String pass) throws java.rmi.RemoteException;
}
