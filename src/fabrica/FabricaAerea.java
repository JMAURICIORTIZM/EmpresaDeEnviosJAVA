package fabrica;

import modelo.Envio;
import modelo.EnvioAereo;

public class FabricaAerea extends FabricaEnvio {
    @Override
    public Envio crearEnvio(String cliente, String codigo,
                            double peso, double distancia) {
        return new EnvioAereo(cliente, codigo, peso, distancia);
    }
}