public interface RMIServerInterface extends java.rmi.Remote {
    public String sayHello() throws java.rmi.RemoteException;

    public boolean regisUser(String nom, String ape, String user, String pass) throws java.rmi.RemoteException;

    public boolean login(String user, String pass) throws java.rmi.RemoteException;

    //public boolean login(String user, String pass) throws java.rmi.RemoteException;
}
