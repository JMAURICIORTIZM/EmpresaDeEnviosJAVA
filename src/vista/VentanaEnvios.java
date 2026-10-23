package vista;

import controlador.ControladorEnvios;
import java.awt.*;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import modelo.Envio;
import modelo.TipoEnvio;
import servicio.ServicioEnvios;


public class VentanaEnvios extends JFrame {

    private final ControladorEnvios controlador;

    private JTextField campoNumero;
    private JTextField campoCliente;
    private JTextField campoPeso;
    private JTextField campoDistancia;
    private JComboBox<TipoEnvio> comboTipo;

    private JTable tablaEnvios;
    private DefaultTableModel modeloTabla;

    private JButton botonAgregarCamion;
    private JButton botonRetirarCamion;

    private boolean modoEdicion = false;
    private String codigoOriginal = "";

    private static final int ANCHO_BOTON = 120;
    private static final int ALTO_BOTON = 80;

    public VentanaEnvios() {
        this.controlador = new ControladorEnvios(new ServicioEnvios());

        setTitle("Operador Logístico");
        setSize(750, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        add(crearPanelSuperior(), BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.add(crearPanelFormulario(), BorderLayout.NORTH);
        panelCentral.add(crearPanelTabla(), BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        actualizarEstadoBotones();
        setVisible(true);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon iconoMas = cargarYEscalarIcono("Icono/mas.jpg", ANCHO_BOTON, ALTO_BOTON);
        ImageIcon iconoMenos = cargarYEscalarIcono("Icono/menos.jpg", ANCHO_BOTON, ALTO_BOTON);

        botonAgregarCamion = crearBotonConIcono(iconoMas, "Guardar / Actualizar envío");
        botonAgregarCamion.addActionListener(e -> guardarOActualizarEnvio());

        botonRetirarCamion = crearBotonConIcono(iconoMenos, "Retirar envío seleccionado o ingresado");
        botonRetirarCamion.addActionListener(e -> retirarEnvio());

        panel.add(botonAgregarCamion);
        panel.add(botonRetirarCamion);

        return panel;
    }

    private ImageIcon cargarYEscalarIcono(String ruta, int ancho, int alto) {
        URL url = getClass().getClassLoader().getResource(ruta);
        if (url == null) {
            java.io.File archivo = new java.io.File(ruta);
            if (archivo.exists()) {
                ImageIcon icono = new ImageIcon(ruta);
                Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            System.err.println("❌ No se encontró la imagen en: " + ruta);
            return null;
        }

        ImageIcon iconoOriginal = new ImageIcon(url);
        Image imagenEscalada = iconoOriginal.getImage()
                .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    private JButton crearBotonConIcono(ImageIcon icono, String tooltip) {
        JButton boton = new JButton(icono);
        boton.setPreferredSize(new Dimension(ANCHO_BOTON, ALTO_BOTON));
        boton.setMargin(new Insets(0, 0, 0, 0));
        boton.setBorderPainted(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setToolTipText(tooltip);
        return boton;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Número"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        campoNumero = new JTextField();
        // Listener para detectar cuando se escribe o borra el código y actualizar el botón del camión (-)
        campoNumero.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotones(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotones(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarEstadoBotones(); }
        });
        panel.add(campoNumero, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Tipo"), gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1;
        comboTipo = new JComboBox<>(TipoEnvio.values());
        panel.add(comboTipo, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Cliente"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        campoCliente = new JTextField();
        panel.add(campoCliente, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Distancia en Km"), gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1;
        campoDistancia = new JTextField();
        panel.add(campoDistancia, gbc);


        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Peso"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        campoPeso = new JTextField();
        panel.add(campoPeso, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(e -> guardarOActualizarEnvio());
        panel.add(botonGuardar, gbc);

        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 0;
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> limpiarFormulario());
        panel.add(botonCancelar, gbc);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[]{"Tipo", "Código", "Cliente", "Peso", "Distancia", "Costo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tablaEnvios = new JTable(modeloTabla);

        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaEnvios.getColumnCount(); i++) {
            tablaEnvios.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }


        tablaEnvios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
                actualizarEstadoBotones();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaEnvios);
        scroll.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        return scroll;
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = tablaEnvios.getSelectedRow() != -1;
        boolean hayCodigo = !campoNumero.getText().trim().isEmpty();

        botonRetirarCamion.setEnabled(haySeleccion || hayCodigo);
    }

    private void cargarDatosSeleccionados() {
        int fila = tablaEnvios.getSelectedRow();
        if (fila != -1) {
            modoEdicion = true;
            codigoOriginal = (String) tablaEnvios.getValueAt(fila, 1);

            comboTipo.setSelectedItem(tablaEnvios.getValueAt(fila, 0));
            campoNumero.setText(codigoOriginal);
            campoCliente.setText((String) tablaEnvios.getValueAt(fila, 2));
            campoPeso.setText(String.valueOf(tablaEnvios.getValueAt(fila, 3)));
            campoDistancia.setText(String.valueOf(tablaEnvios.getValueAt(fila, 4)));
        }
    }

    private void guardarOActualizarEnvio() {
        try {
            TipoEnvio tipo = (TipoEnvio) comboTipo.getSelectedItem();
            String cliente = campoCliente.getText().trim();
            String codigo = campoNumero.getText().trim();
            double peso = Double.parseDouble(campoPeso.getText().trim());
            double distancia = Double.parseDouble(campoDistancia.getText().trim());

            if (cliente.isEmpty() || codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cliente y número son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (peso <= 0 || distancia <= 0) {
                JOptionPane.showMessageDialog(this, "Peso y distancia deben ser mayores a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (modoEdicion) {
                if (!codigo.equalsIgnoreCase(codigoOriginal)) {
                    if (controlador.retirarEnvio(codigoOriginal)) {
                        controlador.agregarEnvio(tipo, cliente, codigo, peso, distancia);
                    }
                } else {
                    controlador.actualizarEnvio(tipo, cliente, codigo, peso, distancia);
                }
                JOptionPane.showMessageDialog(this, "Envío actualizado correctamente.");
            } else {
                controlador.agregarEnvio(tipo, cliente, codigo, peso, distancia);
                JOptionPane.showMessageDialog(this, "Envío guardado correctamente.");
            }

            refrescarTabla();
            limpiarFormulario();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Peso y distancia deben ser numéricos.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void retirarEnvio() {
        String codigoEscrito = campoNumero.getText().trim();
        String codigoSeleccionado = "";
        int filaSeleccionada = tablaEnvios.getSelectedRow();
        if (filaSeleccionada != -1) {
            codigoSeleccionado = (String) tablaEnvios.getValueAt(filaSeleccionada, 1);
        }

        String codigoABorrar = "";


        if (!codigoEscrito.isEmpty()) {
            codigoABorrar = codigoEscrito;
        } else if (!codigoSeleccionado.isEmpty()) {
            codigoABorrar = codigoSeleccionado;
        } else {
            JOptionPane.showMessageDialog(this, "Ingrese un número o seleccione un envío para retirar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de retirar el envío con código " + codigoABorrar + "?", "Confirmar retiro", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean retirado = controlador.retirarEnvio(codigoABorrar);
            if (retirado) {
                JOptionPane.showMessageDialog(this, "Envío retirado correctamente.");
                refrescarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No existe un envío con el código: " + codigoABorrar, "No encontrado", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        List<Envio> lista = controlador.listarEnvios();
        for (Envio envio : lista) {
            modeloTabla.addRow(new Object[]{
                    envio.obtenerTipo(),
                    envio.getCodigo(),
                    envio.getCliente(),
                    envio.getPeso(),
                    envio.getDistancia(),
                    String.format("%.2f", envio.calcularTarifa())
            });
        }
        actualizarEstadoBotones();
    }

    private void limpiarFormulario() {
        campoNumero.setText("");
        campoCliente.setText("");
        campoPeso.setText("");
        campoDistancia.setText("");
        comboTipo.setSelectedIndex(0);
        tablaEnvios.clearSelection();

        modoEdicion = false;
        codigoOriginal = "";

        actualizarEstadoBotones();
        campoNumero.requestFocus();
    }
}
