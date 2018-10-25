import java.io.Serializable;

public class Artista implements Serializable {

    private String nombre;
    private String genero;

    public Artista(String n, String g){
        this.nombre = n;
        this.genero = g;
    }
    public Artista(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void mostrar(){
        System.out.println("Nombre: " + this.getNombre() + ", género: " + this.getGenero());
    }
}
