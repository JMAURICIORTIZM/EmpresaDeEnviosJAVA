package modelo;

public class EnvioFluvial extends Envio {

    private static final double TARIFA_POR_KM = 800;
    private static final double RECARGO_POR_KG = 1000;

    public EnvioFluvial(String cliente, String codigo, double peso, double distancia) {
        super(cliente, codigo, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return calcularTarifaBase(TARIFA_POR_KM, RECARGO_POR_KG);
    }

    @Override
    public TipoEnvio obtenerTipo() { return TipoEnvio.FLUVIAL; }
}