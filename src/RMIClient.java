
import java.util.Scanner;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIClient {


    private static void menuCrearArtista(RMIServerInterface server){

        int n;
        do {
            System.out.println("1. Añadir artista");
            System.out.println("2. Editar artista");
            System.out.println("3. Eliminar artista");
            System.out.println("4. Volver");
            System.out.println("Selecciona una opción: ");
            Scanner sc = new Scanner(System.in);
            n = sc.nextInt();
        }while(n < 0 || n > 4);

        switch (n){
            case 1:
                System.out.println("Vas a añadir un nuevo artista");
                Scanner sc = new Scanner(System.in);
                System.out.print("Introduce el nombre del artista: ");
                String a = sc.nextLine();
                Scanner sc1 = new Scanner(System.in);
                System.out.print("Introduce el genero de la musica del artista: ");
                String g = sc1.nextLine();
                //Artista ar = new Artista(a, g);
                try {
                    boolean r = server.anadirArtista(a, g);

                    if(r){
                        System.out.println("El artista ha sido añadido correctamente.");
                    }
                } catch (RemoteException e) {
                    System.out.println("No se pudo añadir el artista.");
                    e.printStackTrace();
                }
                menuCrearArtista(server);
                break;
            case 2:

                break;
            case 3:
                System.out.println("Eliminar un artista");
                Scanner sc2 = new Scanner(System.in);
                System.out.print("Introduce el nombre del artista que deseas eliminar: ");
                String a1 = sc2.nextLine();

                try {
                    boolean r = server.eliminarArtista(a1);
                    if(r){
                        System.out.println("Artista eliminado.");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                break;
        }

    }

    public void menuGestion(){
        System.out.println("1. Registrarse");
        System.out.println("2. Autenticarse");
        System.out.println("3. Salir");
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
                        menuCrearArtista(server);
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
