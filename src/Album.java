import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Album implements Serializable {
    private String nombre;
    private Artista a;
    private String descripcion;
    private List<Cancion> canciones;
    private List<Critica> criticas;
    private int numCanciones;
    private int puntuacion;

    public Album() {
    }

    public Album(String nombre, Artista a, String descripcion) {
        this.nombre = nombre;
        this.a = a;
        this.descripcion = descripcion;
        canciones=new ArrayList<>();
    }

    public Album(String nombre, Artista a){
        this.nombre = nombre;
        this.a = a;
        canciones=new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Artista getA() {
        return a;
    }

    public void setA(Artista a) {
        this.a = a;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    public int getNumCanciones() {
        return numCanciones;
    }

    public void setNumCanciones(int numCanciones) {
        this.numCanciones = numCanciones;
    }

    public List<Critica> getCriticas() {
        return criticas;
    }

    public void setCriticas(List<Critica> criticas) {
        this.criticas = criticas;
    }


    public void datosAlbumArtista() {
        System.out.print("Nombre: " + this.nombre);
        System.out.print(" / Artista: " + this.getA().getNombre());
        System.out.println(" / Descripción: " + this.getDescripcion());
    }
    public void datosAlbum() {
        System.out.print("Nombre Album : " + this.nombre);
        System.out.println(" / Descripción: " + this.getDescripcion());
    }

    public void anadirCancion(Cancion c){

        canciones.add(c);
    }

    public void verCanciones() {
        List<Cancion> lista = this.getCanciones();
        for (Cancion c : lista) {
            c.datosCancion();
        }
    }
    public void verCancionesAlbum() {

        for (Cancion c : canciones) {
            c.datosCancionAlbum();
        }
    }

    public void verCriticas() {
        List<Critica> lista = this.getCriticas();
        for (Critica c : lista) {
            c.detalles();
        }
    }

    public void menu() {

        int n = 0;
        do {
            System.out.println("Album: " + this.getNombre());
            System.out.println("1. Obtener detalles album");
            System.out.println("2. Ver canciones");
            System.out.println("3. Ver críticas");
            System.out.println("4. Ver puntuación media");
            System.out.println("5. Consultar detalles del artista");
            System.out.println("Presione otro número para salir");
            Scanner sc = new Scanner(System.in);
            System.out.print("Selecciona una opción: ");
            n = sc.nextInt();

            switch (n) {
                case 1:
                    this.datosAlbum();
                    break;
                case 2:
                    this.verCanciones();
                    break;
                case 3:
                    this.verCriticas();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Has salido.");
            }

        } while (n > 0 && n < 6);
    }
}
