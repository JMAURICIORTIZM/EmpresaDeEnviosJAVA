package controlador;

import factory.EnvioFactory;
import factory.TerrestreFactory;
import factory.AereoFactory;
import factory.MaritimoFactory;
import factory.ExpressFactory;
import modelo.Envio;
import modelo.TipoEnvio;
import servicio.EnvioService;

import java.util.List;

public class EnvioController {

    private final EnvioService service;

    public EnvioController(EnvioService service) {
        this.service = service;
    }

    private EnvioFactory getFactory(TipoEnvio tipo) {
        switch (tipo) {
            case TERRESTRE: return new TerrestreFactory();
            case AEREO:     return new AereoFactory();
            case MARITIMO:  return new MaritimoFactory();
            case EXPRESS:   return new ExpressFactory();
            default: throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        }
    }

    public Envio agregarEnvio(TipoEnvio tipo, String cliente, String codigo,
                              double peso, double distancia) {
        EnvioFactory factory = getFactory(tipo);
        Envio envio = factory.crearEnvio(cliente, codigo, peso, distancia);
        service.agregar(envio);
        return envio;
    }

    public boolean retirarEnvio(String codigo) {
        return service.retirar(codigo);
    }

    public List<Envio> listarEnvios() {
        return service.listar();
    }
}