package vista;

import controlador.EnvioController;
import modelo.Envio;
import modelo.TipoEnvio;
import servicio.EnvioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaEnvios extends JFrame {

    private final EnvioController controller;

    private JTextField txtCliente, txtCodigo, txtPeso, txtDistancia, txtRetirar;
    private JComboBox<TipoEnvio> cmbTipo;
    private JTable tblEnvios;
    private DefaultTableModel modelo;

    public VentanaEnvios() {
        this.controller = new EnvioController(new EnvioService());

        setTitle("Empresa de Envíos - MVC + Factory");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelRetirar(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel crearPanelFormulario() {
        JPanel pnl = new JPanel(new GridLayout(6, 2, 5, 5));
        pnl.setBorder(BorderFactory.createTitledBorder("Agregar Envío"));

        pnl.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(TipoEnvio.values());
        pnl.add(cmbTipo);

        pnl.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        pnl.add(txtCliente);

        pnl.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        pnl.add(txtCodigo);

        pnl.add(new JLabel("Peso (kg):"));
        txtPeso = new JTextField();
        pnl.add(txtPeso);

        pnl.add(new JLabel("Distancia (km):"));
        txtDistancia = new JTextField();
        pnl.add(txtDistancia);

        JButton btnAgregar = new JButton("Agregar envío");
        btnAgregar.addActionListener(e -> agregarEnvio());
        pnl.add(btnAgregar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        pnl.add(btnLimpiar);

        return pnl;
    }

    private JScrollPane crearPanelTabla() {
        modelo = new DefaultTableModel(
                new Object[]{"Código", "Cliente", "Tipo", "Peso (kg)",
                             "Distancia (km)", "Tarifa"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblEnvios = new JTable(modelo);
        return new JScrollPane(tblEnvios);
    }

    private JPanel crearPanelRetirar() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl.setBorder(BorderFactory.createTitledBorder("Retirar Envío"));

        pnl.add(new JLabel("Código:"));
        txtRetirar = new JTextField(10);
        pnl.add(txtRetirar);

        JButton btnRetirar = new JButton("Retirar");
        btnRetirar.addActionListener(e -> retirarEnvio());
        pnl.add(btnRetirar);

        JButton btnListar = new JButton("Refrescar lista");
        btnListar.addActionListener(e -> refrescarTabla());
        pnl.add(btnListar);

        return pnl;
    }

    private void agregarEnvio() {
        try {
            TipoEnvio tipo = (TipoEnvio) cmbTipo.getSelectedItem();
            String cliente = txtCliente.getText().trim();
            String codigo = txtCodigo.getText().trim();
            double peso = Double.parseDouble(txtPeso.getText().trim());
            double distancia = Double.parseDouble(txtDistancia.getText().trim());

            if (cliente.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Cliente y código son obligatorios.");
                return;
            }
            if (peso <= 0 || distancia <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Peso y distancia deben ser mayores a 0.");
                return;
            }

            controller.agregarEnvio(tipo, cliente, codigo, peso, distancia);
            refrescarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Envío agregado correctamente.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Peso y distancia deben ser numéricos.");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void retirarEnvio() {
        String codigo = txtRetirar.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código.");
            return;
        }
        boolean ok = controller.retirarEnvio(codigo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Envío retirado.");
            refrescarTabla();
            txtRetirar.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No existe un envío con el código: " + codigo,
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        List<Envio> lista = controller.listarEnvios();
        for (Envio e : lista) {
            modelo.addRow(new Object[]{
                    e.getCodigo(), e.getCliente(), e.getTipo(),
                    e.getPeso(), e.getDistancia(),
                    String.format("$%.2f", e.calcularTarifa())
            });
        }
    }

    private void limpiarFormulario() {
        txtCliente.setText("");
        txtCodigo.setText("");
        txtPeso.setText("");
        txtDistancia.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}