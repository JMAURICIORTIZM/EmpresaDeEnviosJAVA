package servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import modelo.Envio;

public class ServicioEnvios implements IServicioEnvios {

    private final List<Envio> envios = new ArrayList<>();

    @Override
    public void agregar(Envio envio) {
        if (buscarPorCodigo(envio.getCodigo()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un envío con el código " + envio.getCodigo());
        }
        envios.add(envio);
    }

    @Override
    public boolean retirar(String codigo) {
        Optional<Envio> encontrado = buscarPorCodigo(codigo);
        encontrado.ifPresent(envios::remove);
        return encontrado.isPresent();
    }

    @Override
    public Optional<Envio> buscarPorCodigo(String codigo) {
        return envios.stream()
                .filter(e -> e.getCodigo().equalsIgnoreCase(codigo))
                .findFirst();
    }

    @Override
    public List<Envio> listar() {
        return new ArrayList<>(envios); // copia defensiva
    }

    @Override
    public int cantidad() {
        return envios.size();
    }

    @Override
    public void actualizar(Envio envio) {
        for (int i = 0; i < envios.size(); i++) {
            if (envios.get(i).getCodigo().equalsIgnoreCase(envio.getCodigo())) {
                envios.set(i, envio);
                return;
            }
        }
    }
}