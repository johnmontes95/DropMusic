import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Persona {

    private String nombre;
    private String apellido;
    private String us;
    private String permisos;




    public Persona(String nom, String apellido, String us, String permisos){
        this.nombre = nom;
        this.apellido = apellido;
        this.us = us;
        this.permisos = permisos;
    }

    public String getNombre(){
        return this.nombre;
    }

    public String getApellido(){
        return this.apellido;
    }

    public String getUs(){
        return this.us;
    }

    public String getPermisos(){
        return this.permisos;
    }
}
