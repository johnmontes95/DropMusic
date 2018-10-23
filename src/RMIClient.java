
import java.util.Scanner;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIClient {


    public void menuCrearArtista(String usuario){
        System.out.println("1. ");
    }

    public static void main(String args[]) {
        try {
            RMIServerInterface server = (RMIServerInterface) LocateRegistry.getRegistry(7000).lookup("servidor");
            System.out.println(server.sayHello());
            int n;
            do{
                System.out.println("");
                System.out.println("1. Registrarse");
                System.out.println("2. Autenticarse");
                System.out.println("3. Salir");

                Scanner sc = new Scanner(System.in);
                System.out.print("Introduzca un número: ");
                n = sc.nextInt();
            }while(n<0 || n>3);
            boolean r = false;
            if(n == 1){
                do {

                    System.out.println("Autenticación DropMusic");
                    System.out.println("Introduzca su nombre: ");
                    Scanner sc1 = new Scanner(System.in);
                    String nom = sc1.nextLine();
                    System.out.println("Introduzca sus apellidos: ");
                    Scanner sc2 = new Scanner(System.in);
                    String ape = sc2.nextLine();
                    System.out.println("Introduzca su nombre de usuario: ");
                    Scanner sc3 = new Scanner(System.in);
                    String usu = sc3.nextLine();
                    System.out.println("Introduzca su contrasena: ");
                    Scanner sc4 = new Scanner(System.in);
                    String pass = sc4.nextLine();

                    r = server.regisUser(nom, ape, usu, pass);

                    if(r){
                        System.out.print("Usuario registrado correctamente.");
                    }else{
                        System.out.print("Error al registrar usuario. Inténtelo de nuevo.");
                    }

                }while( r == false);
            }else if(n == 2){
                System.out.println("Autenticación DropMusic");
                do {
                    System.out.print("Nombre de usuario: ");
                    Scanner sc1 = new Scanner(System.in);
                    String user = sc1.nextLine();
                    System.out.print("Contrasena: ");
                    Scanner sc2 = new Scanner(System.in);
                    String pass = sc2.nextLine();
                    r = server.login(user, pass);

                    System.out.println(r);
                    if(r){
                        System.out.println("Te has logueado.");
                    }
                }while(r == false);
            }else{
                System.exit(0);
            }

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
