import java.util.ArrayList;
import java.util.Scanner;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class RMIClient {

    private String usuario;

    public RMIClient(String usu){
        this.usuario = usu;
    }

    public String getUsuario() {
        return usuario;
    }


    private static void menuEditar(RMIServerInterface server){

            int n;

            do {

                System.out.println("");
                System.out.println("1. Editar Artista");
                System.out.println("2. Editar Album");
                System.out.println("3. Editar Cancion");
                System.out.println("4. Volver");

                Scanner sc = new Scanner(System.in);
                System.out.print("Introduzca un número: ");
                n = sc.nextInt();

                switch (n) {
                    case 1:
                        menuCrearArtista(server);
                        break;
                    case 2:
                        menuCrearAlbum(server);
                        break;
                    case 3:
                        menuCrearCancion(server);
                        break;
                    default:
                        break;
                }
            } while (n != 4);


    }

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
                    }else{
                        System.out.println("Ya existe un artista con ese nombre");
                    }
                } catch (RemoteException e) {
                    System.out.println("No se pudo añadir el artista.");
                    e.printStackTrace();
                }
                menuCrearArtista(server);
                break;
            case 2:
                System.out.println("Editar un artista");
                Scanner sc2 = new Scanner(System.in);
                System.out.print("Introduce el nombre del artista que deseas editar: ");
                String a1 = sc2.nextLine();
                System.out.print("Introduce el nombre del artista que deseas editar: ");
                String a11 = sc2.nextLine();
                System.out.print("Introduce el nuevo genero del artista: ");
                String a111 = sc2.nextLine();




                try {

                    boolean r = server.editarArtista(a1,a11,a111);
                    if(r){
                        System.out.println("Se han modificado los datos");
                    }else{
                        System.out.println("No existe ese artista o el nuevo nombre ya existe");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                menuCrearArtista(server);
                break;




            case 3:
                System.out.println("Eliminar un artista");
                Scanner sc3 = new Scanner(System.in);
                System.out.print("Introduce el nombre del artista que deseas eliminar: ");
                String a2 = sc3.nextLine();

                try {
                    boolean r = server.eliminarArtista(a2);
                    if(r){
                        System.out.println("Artista eliminado.");
                    }else{
                        System.out.println("No existe ese artista");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                break;
        }

    }


   private static void menuCrearAlbum(RMIServerInterface server){

       int n;
       Scanner sc = new Scanner(System.in);
       String titulo;
       String nombArtista;
       String descripcion;

       do {
           System.out.println("1. Añadir Album");
           System.out.println("2. Editar Album");
           System.out.println("3. Eliminar Album");
           System.out.println("4. Volver");
           System.out.println("Selecciona una opción: ");
           n = sc.nextInt();
       }while(n < 0 || n > 4);


       switch (n){
           case 1:

               System.out.println("Vas a añadir un nuevo album");
                sc.nextLine();
               System.out.print("Introduce el titulo del album: ");
               titulo = sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
               nombArtista = sc.nextLine();
               System.out.print("Introduce una descripccion del album: ");
               descripcion = sc.nextLine();

               Artista artista=new Artista();
               artista.setNombre(nombArtista);
               Album album = new Album(titulo,artista,descripcion);




               try {
                   boolean r = server.anadirAlbum(album);

                   if(r){
                       System.out.println("El album ha sido añadido correctamente.");
                   }else{
                       System.out.println("Ya existe un album con ese nombre");
                   }
               } catch (RemoteException e) {
                   System.out.println("No se pudo añadir el album.");
                   e.printStackTrace();
               }
               menuCrearAlbum(server);
               break;
           case 2:
               System.out.println("Vas a editar un album");
               sc.nextLine();
               System.out.print("Introduce el titulo del album: ");
               titulo = sc.nextLine();
               System.out.print("Introduce el nuevo titulo del album: ");
               String tituloN = sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
                nombArtista = sc.nextLine();
               System.out.print("Introduce el nuevo nombre del artista: ");
               String nombArtistaN = sc.nextLine();
               System.out.print("Introduce la nueva descripccion del album: ");
               String descripcionN = sc.nextLine();

               Artista artista1=new Artista();
               artista1.setNombre(nombArtista);
               Album album1 = new Album(titulo,artista1);

               Artista artistaN=new Artista();
               artistaN.setNombre(nombArtistaN);
               Album albumN = new Album(tituloN,artistaN,descripcionN);

               try {
                   boolean r = server.editarAlbum(album1,albumN);

                   if(r){
                       System.out.println("El album ha sido editado correctamente.");
                   }else{
                       System.out.println("No ha sido editado el album");
                   }
               } catch (RemoteException e) {
                   System.out.println("No se pudo editar el album.");
                   e.printStackTrace();
               }
               menuCrearAlbum(server);
               break;

           case 3:
               System.out.println("Vas a eliminar un album");
               sc.nextLine();
               System.out.print("Introduce el titulo del album: ");
               titulo = sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
               nombArtista = sc.nextLine();

               try {
                   boolean r = server.eliminarAlbum(titulo,nombArtista);

                   if(r){
                       System.out.println("El album ha sido eliminado correctamente.");
                   }else{
                       System.out.println("No ha sido eliminado el album");
                   }
               } catch (RemoteException e) {
                   System.out.println("Error No se pudo eliminar el album.");
                   e.printStackTrace();
               }
               menuCrearAlbum(server);
               break;




           case 4:
               break;
       }

   }

   private static void menuCrearCancion(RMIServerInterface server){

       int n;
       Scanner sc = new Scanner(System.in);
       String titulo;
       String nombArtista;
       String nombAlbum;
       int duracion;
       Cancion cancion=new Cancion();
       Album album = new Album();
       Artista artista=new Artista();

       do {
           System.out.println("1. Añadir Cancion");
           System.out.println("2. Editar Cancion");
           System.out.println("3. Eliminar Cancion");
           System.out.println("4. Volver");
           System.out.println("Selecciona una opción: ");
           n = sc.nextInt();
       }while(n < 0 || n > 4);


       switch (n){

           case 1:
               System.out.println("Vas a añadir una nueva cancion");
               sc.nextLine();
               System.out.print("Introduce el titulo de la cancion: ");
               titulo = sc.nextLine();
               System.out.print("Introduce la duracion de la cancion en segundos: ");
               duracion = sc.nextInt();
               sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
               nombArtista = sc.nextLine();
               System.out.print("Introduce el nombre del album: ");
               nombAlbum = sc.nextLine();

               artista.setNombre(nombArtista);
               album.setA(artista);
               album.setNombre(nombAlbum);
               cancion.setAl(album);
               cancion.setDuracion(duracion);
               cancion.setTitulo(titulo);




               try {
                   boolean r = server.anadirCancion(cancion);

                   if(r){
                       System.out.println("La cancion ha sido añadido correctamente.");
                   }else{
                       System.out.println("Ya existe una cancion con ese nombre");
                   }
               } catch (RemoteException e) {
                   System.out.println("No se pudo añadir el album.");
                   e.printStackTrace();
               }
               menuCrearCancion(server);
               break;


           case 2:
               System.out.println("Vas a editar una cancion");
               sc.nextLine();
               System.out.print("Introduce el titulo de la cancion: ");
               titulo = sc.nextLine();
               System.out.print("Introduce el nuevo titulo de la cancion: ");
               String tituloN = sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
               nombArtista = sc.nextLine();
               System.out.print("Introduce el nuevo nombre del artista: ");
               String nombArtistaN = sc.nextLine();
               System.out.print("Introduce el nombre del album: ");
               nombAlbum = sc.nextLine();
               System.out.print("Introduce el nuevo nombre del album: ");
               String nombAlbumN = sc.nextLine();
               System.out.print("Introduce la nueva duraccion de la cancion: ");
               duracion = sc.nextInt();



               artista.setNombre(nombArtista);
               album.setA(artista);
               album.setNombre(nombAlbum);
               cancion.setTitulo(titulo);
               cancion.setAl(album);

               Artista artistaN=new Artista();
               artistaN.setNombre(nombArtistaN);
               Album albumN = new Album(nombAlbumN,artistaN);
               Cancion cancionN= new Cancion();
               cancionN.setAl(albumN);
               cancionN.setDuracion(duracion);
               cancionN.setTitulo(tituloN);

               try {
                   boolean r = server.editarCancion(cancion,cancionN);

                   if(r){
                       System.out.println("La cancion ha sido editado correctamente.");
                   }else{
                       System.out.println("No ha sido editada la cancion");
                   }
               } catch (RemoteException e) {
                   System.out.println("No se pudo editar la cancion.");
                   e.printStackTrace();
               }
               menuCrearCancion(server);

               break;
           case 3:
               System.out.println("Vas a eliminar una cancion");
               sc.nextLine();
               System.out.print("Introduce el titulo de la cancion: ");
               titulo = sc.nextLine();
               System.out.print("Introduce el nombre del artista: ");
               nombArtista = sc.nextLine();
               System.out.print("Introduce el nombre del album: ");
               nombAlbum = sc.nextLine();


               artista.setNombre(nombArtista);
               album.setA(artista);
               album.setNombre(nombAlbum);
               cancion.setTitulo(titulo);
               cancion.setAl(album);

               try {
                   boolean r = server.eliminarCancion(cancion);

                   if(r){
                       System.out.println("La cancion ha sido eliminada correctamente.");
                   }else{
                       System.out.println("No ha sido eliminada la cancion");
                   }
               } catch (RemoteException e) {
                   System.out.println("Error No se pudo eliminar la cancion.");
                   e.printStackTrace();
               }
               menuCrearCancion(server);
               break;
           default:
               break;



       }



   }

   private static void menuBuscar(RMIServerInterface server){
                 int n;

                 Artista a;
                 Album al;
                 Cancion c;
                 int pos;


           do {

               System.out.println("");
               System.out.println("1. Buscar Artista");
               System.out.println("2. Buscar Genero");
               System.out.println("3. Buscar Album");
               System.out.println("4. Volver");

               Scanner sc = new Scanner(System.in);
               System.out.print("Introduzca un número: ");
               n = sc.nextInt();
               sc.nextLine();

               switch (n) {

                   case 1:

                       System.out.print("Introduzca el nombre del Artista a buscar: ");
                       String artista = sc.nextLine();

                       try {
                           a=new Artista();
                           a.setNombre(artista);
                           a=  server.buscarArtista(a);
                           System.out.println("Artista: "+a.getNombre()+" / Genero: "+a.getGenero());
                           a.mostrarAlbum();

                           System.out.println("Si quieres ver las canciones de un album introduce el nombre");
                           String nalbum=sc.nextLine();


                           al=new Album();
                           al.setNombre(nalbum);
                           al.setA(a);
                           al=server.buscarCanciones(al);

                           System.out.println("Album: "+al.getNombre()+" / Descripccion: "+al.getDescripcion());
                           //muesta las canciones
                           al.verCancionesAlbum();

                       } catch (RemoteException e) {
                           System.out.println("No se pudo añadir el album.");
                           e.printStackTrace();
                       }
                       menuBuscar(server);

                       break;
                   case 2:

                       break;
                   case 3:

                       System.out.print("Introduzca el nombre del Album a buscar: ");
                       String album = sc.nextLine();
                       List <Album> albunes;
                       try {
                           albunes=  server.buscarAlbum(album);


                           for (Album f:albunes){

                               f.datosAlbumArtista();
                           }

                           System.out.println("1. Ver las canciones de un album");
                           System.out.println("2. Ver los datos de ese artista");
                           System.out.println("3. Salir");
                           pos=sc.nextInt();

                           switch (pos){

                               case 1:
                                   int nal;
                                   do {
                                       System.out.println("Elige la posicion del album(Empezando por 0)");
                                       nal = sc.nextInt();
                                   }while(nal>albunes.size()||nal<0);
                                   al=albunes.get(nal);

                                   al=server.buscarCanciones(al);

                                   System.out.println("Album: "+al.getNombre()+" / Descripccion: "+al.getDescripcion());
                                   //muesta las canciones
                                   al.verCancionesAlbum();


                                   break;
                               case 2:
                                   do {
                                       System.out.println("Elige la posicion del artista/album(Empezando por 0)");
                                       nal = sc.nextInt();
                                   }while(nal>albunes.size()||nal<0);

                                   //obtienes el objeto artista de ese album en a
                                   a=albunes.get(nal).getA();

                                   break;
                               default:
                                       break;

                           }


                       } catch (RemoteException e) {
                           System.out.println("No se pudo añadir el album.");
                           e.printStackTrace();
                       }
                       menuBuscar(server);

                       break;
                   default:
                       break;
               }
           } while (n != 4);



   }


    public static void main(String args[]) {
        try {
            RMIServerInterface server = (RMIServerInterface) LocateRegistry.getRegistry(7000).lookup("servidor");
            System.out.println(server.sayHello());
            RMIClientImp cliente = null;
            int intentos = 0;
            int n;
            boolean r = false;
            do{
                System.out.println("");
                System.out.println("1. Registrarse");
                System.out.println("2. Autenticarse");
                System.out.println("3. Salir");

                Scanner sc = new Scanner(System.in);
                System.out.print("Introduzca un número: ");
                n = sc.nextInt();

                switch (n){
                    case 1:

                        do {
                            System.out.println("Registro DropMusic");
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

                            try {
                                r = server.regisUser(nom, ape, usu, pass);
                                System.out.println("Usuario registrado correctamente");
                            } catch (RemoteException e1) {
                                e1.getMessage();
                            }
                            if(r){
                                r = server.login(usu, pass);
                                System.out.println("He entrado");
                            }

                        }while( r == false);
                        break;
                    case 2:
                        System.out.println("Autenticación DropMusic");
                        do {
                            System.out.print("Nombre de usuario: ");
                            Scanner sc1 = new Scanner(System.in);
                            String user = sc1.nextLine();
                            System.out.print("Contrasena: ");
                            Scanner sc2 = new Scanner(System.in);
                            String pass = sc2.nextLine();
                            try {
                                r = server.login(user, pass);
                            } catch (RemoteException e1) {
                                e1.getMessage();
                            }

                            try {
                                cliente = new RMIClientImp(user);
                            } catch (RemoteException e1) {
                                e1.getMessage();
                            }

                        }while(r == false);
                        break;
                    case 3:
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Introduce una opción correcta.");
                    }


            }while(n<0 || n>3);

            if(r){
                do {

                    System.out.println("");
                    System.out.println("1. Editar Artista Album o Cancion(Editor)");
                    System.out.println("2. Ver Musica Artista o Album");
                    System.out.println("3. Consulta detalles de Album o Artista");
                    System.out.println("4. Escribe una critica de un Album");
                    System.out.println("5. Dar privilegios de editor(Editor)");
                    System.out.println("6. Subir musica");
                    System.out.println("7. Compartir musica");
                    System.out.println("8. Bajar musica");
                    System.out.println("9. Salir ");

                    Scanner sc = new Scanner(System.in);
                    System.out.print("Introduzca un número: ");
                    n = sc.nextInt();

                    switch(n){
                        case 1:
                            menuEditar(server);
                            break;
                        case 2:
                            menuBuscar(server);
                            break;
                        case 3:
                            break;
                        case 4:
                            Scanner scan = new Scanner(System.in);
                            System.out.println("Escribir critica a álbum");
                            System.out.println("Escribe el nombre del artista del álbum");
                            String ar = scan.nextLine();
                            sc.nextLine();
                            System.out.println("Escribe el nombre del álbum");
                            String album = scan.nextLine();
                            scan.nextLine();
                            int n = 0;
                            do{
                                System.out.println("Puntuación del álbum");
                                n = scan.nextInt();
                            }while(n.);
                            break;
                        case 5:
                            System.out.println("Vas a cambiar los permisos de un usuario");
                            sc.nextLine();
                            System.out.println("Introduce el nombre del usuario:");
                            String usuario = sc.nextLine();
                            server.cambiarPermisos(cliente.getUsuario(), usuario);
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        case 8:
                            break;
                        case 9:
                            System.exit(0);
                            break;
                        default:
                            break;

                    }



                }while (n!=9);

            }

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            System.out.println("Servidor desconectado.");
           e.getMessage();
        }
    }
}
