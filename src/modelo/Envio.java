package modelo;

public abstract class Envio {

    private String cliente;
    private String codigo;
    private double peso;       // kg
    private double distancia;  // km

    public Envio(String cliente, String codigo, double peso, double distancia) {
        this.cliente = cliente;
        this.codigo = codigo;
        this.peso = peso;
        this.distancia = distancia;
    }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public abstract double calcularTarifa();
    public abstract TipoEnvio obtenerTipo();

    protected double calcularTarifaBase(double tarifaPorKm, double recargoPorKg) {
        return distancia * tarifaPorKm + peso * recargoPorKg;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %.2f kg | %.2f km | $%.2f",
                codigo, cliente, obtenerTipo(), peso, distancia, calcularTarifa());
    }
}