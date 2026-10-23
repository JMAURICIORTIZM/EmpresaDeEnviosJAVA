package fabrica;

import modelo.Envio;
import modelo.EnvioExpress;

public class FabricaExpress extends FabricaEnvio {
    @Override
    public Envio crearEnvio(String cliente, String codigo,
                            double peso, double distancia) {
        return new EnvioExpress(cliente, codigo, peso, distancia);
    }
}