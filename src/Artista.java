import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Artista implements Serializable {

    private String nombre;
    private String genero;
    private List<Album> albunes;

    public Artista(String n, String g){

        this.nombre = n;
        this.genero = g;
        albunes=new ArrayList<>();
    }
    public Artista(){

        albunes=new ArrayList<>();
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

    public List<Album> getAlbunes() {
        return albunes;
    }


    public void aniadirAlbum(Album a) {
        albunes.add(a);
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void mostrar(){
        System.out.println("Nombre: " + this.getNombre() + ", género: " + this.getGenero());
    }
}
