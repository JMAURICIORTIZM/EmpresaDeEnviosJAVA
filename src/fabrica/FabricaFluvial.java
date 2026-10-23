package fabrica;

import modelo.Envio;
import modelo.EnvioFluvial;

public class FabricaFluvial extends FabricaEnvio {
    @Override
    public Envio crearEnvio(String cliente, String codigo,
                            double peso, double distancia) {
        return new EnvioFluvial(cliente, codigo, peso, distancia);
    }
}