public class Cancion {

    private String titulo;
    private Artista a;
    private Album al;
    private int duracion;

    public Cancion(String titulo, Artista a, Album al, int d) {
        this.titulo = titulo;
        this.a = a;
        this.al = al;
        this.duracion = d;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Artista getA() {
        return a;
    }

    public void setA(Artista a) {
        this.a = a;
    }

    public Album getAl() {
        return al;
    }

    public void setAl(Album al) {
        this.al = al;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void datosCancion(){
        System.out.println("Nombre: " + this.getTitulo());
        System.out.println("Artista: " + this.getA().getNombre());
        System.out.println("Album: " + this.getAl().getNombre());
        System.out.println("Duracion: " + this.getDuracion());
    }
}
