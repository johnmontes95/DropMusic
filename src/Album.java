import java.util.List;

public class Album {
    private String nombre;
    private Artista a;
    private String descripcion;
    private List<Cancion> canciones;
    private int numCanciones;

    public Album(String nombre, Artista a, String descripcion){
        this.nombre = nombre;
        this.a = a;
        this.descripcion = descripcion;
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


    public void menu(){

    }
}
