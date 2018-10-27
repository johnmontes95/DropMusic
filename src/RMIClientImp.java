import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIClientImp extends UnicastRemoteObject implements RMIClientInterface {

    private String nombre;

    public RMIClientImp(String n) throws RemoteException{
        this.nombre = n;
    }

    @Override
    public void mensaje(String msg) throws RemoteException {
        System.out.println();
        System.out.println("Notificación: " +  msg );
    }

    @Override
    public String getUsuario() throws RemoteException {
        return nombre;
    }
}
