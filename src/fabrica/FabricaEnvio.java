package fabrica;

import modelo.Envio;

public abstract class FabricaEnvio {
    public abstract Envio crearEnvio(String cliente, String codigo,
                                     double peso, double distancia);
}