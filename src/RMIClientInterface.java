import java.rmi.RemoteException;

public interface RMIClientInterface extends java.rmi.Remote{
    void mensaje(String msg) throws RemoteException;
    String getUsuario() throws RemoteException;
}
