package servicio;

import modelo.Envio;
import java.util.List;
import java.util.Optional;

public interface IServicioEnvios {

    void agregar(Envio envio);

    void actualizar(Envio envio);

    boolean retirar(String codigo);

    Optional<Envio> buscarPorCodigo(String codigo);

    List<Envio> listar();

    int cantidad();
}