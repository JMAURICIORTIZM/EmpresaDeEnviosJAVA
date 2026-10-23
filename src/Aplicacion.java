import javax.swing.SwingUtilities;
import vista.VentanaEnvios;


public class Aplicacion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaEnvios::new);
    }
}