import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Persona {

    private String nombre;
    private String apellido;
    private String us;
    private String pass;


    static {
        try {
            //String driver = getValor("driverClass");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No puedo cargar el driver JDBC de la BD");
        }
    }

    public Persona(String nom, String apellido, String us, String pass){
        Connection con = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/proyecto", "root", "Cidacos1718");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int numUsuarios(){
        return 0;
    }

    public String nombreUsuario(){
        return "";
    }

    public Persona datosUsuario(String us){
        return new Persona("", "", "", "");
    }

    public static void main(String[] args) {
        Persona p = new Persona("john", "montes", "grandu1", "12345");
    }
}
