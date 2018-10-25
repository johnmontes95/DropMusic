import java.io.Serializable;

public class Critica implements Serializable {
    private String usuario;
    private String artista;
    private String album;
    private int puntuacion;
    private String descripcion;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void detalles(){
        System.out.println("Usuario: " + this.getUsuario() + " | puntuación: " +
                this.getPuntuacion() + " | descripción: " + this.getDescripcion());
    }
}