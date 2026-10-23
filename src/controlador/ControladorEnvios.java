package controlador;

import fabrica.FabricaAerea;
import fabrica.FabricaEnvio;
import fabrica.FabricaExpress;
import fabrica.FabricaFluvial;
import fabrica.FabricaTerrestre;
import java.util.List;
import modelo.Envio;
import modelo.TipoEnvio;
import servicio.IServicioEnvios;

public class ControladorEnvios {

    private final IServicioEnvios servicio;

    public ControladorEnvios(IServicioEnvios servicio) {
        this.servicio = servicio;
    }


    private FabricaEnvio obtenerFabrica(TipoEnvio tipo) {
        switch (tipo) {
            case TERRESTRE: return new FabricaTerrestre();
            case AEREO:     return new FabricaAerea();
            case FLUVIAL:   return new FabricaFluvial();
            case EXPRESS:   return new FabricaExpress();
            default:
                throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        }
    }

    public Envio agregarEnvio(TipoEnvio tipo, String cliente, String codigo,
                              double peso, double distancia) {
        FabricaEnvio fabrica = obtenerFabrica(tipo);
        Envio envio = fabrica.crearEnvio(cliente, codigo, peso, distancia);
        servicio.agregar(envio);
        return envio;
    }

    public boolean retirarEnvio(String codigo) {
        return servicio.retirar(codigo);
    }

    public List<Envio> listarEnvios() {
        return servicio.listar();
    }

    public void actualizarEnvio(TipoEnvio tipo, String cliente, String codigo,
                            double peso, double distancia) {
        FabricaEnvio fabrica = obtenerFabrica(tipo);
        Envio envio = fabrica.crearEnvio(cliente, codigo, peso, distancia);
        servicio.actualizar(envio);
    }
}