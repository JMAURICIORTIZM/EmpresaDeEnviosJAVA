package fabrica;

import modelo.Envio;
import modelo.EnvioTerrestre;

public class FabricaTerrestre extends FabricaEnvio {
    @Override
    public Envio crearEnvio(String cliente, String codigo,
                            double peso, double distancia) {
        return new EnvioTerrestre(cliente, codigo, peso, distancia);
    }
}