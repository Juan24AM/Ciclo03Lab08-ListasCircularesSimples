
package EjercicioPropuesto;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class RegitroEmpleado extends javax.swing.JFrame {

    public class Nodo {
        String numdni;
        String nombre;
        String paterno;
        String materno;
        int sueldoBase;
        int ventasRealizadas;
        String estadoCivil;
        int numHijos;
        double sueldoNeto;
        Nodo enlace;
        
        public Nodo(String dni, String nom, String pat, String mat, int suelBas, int ventRealiz, String estCiv, int numHij, double suelNet) {
            numdni = dni;
            nombre = nom;
            paterno = pat;
            materno = mat;
            sueldoBase = suelBas;
            ventasRealizadas = ventRealiz;
            estadoCivil = estCiv;
            numHijos = numHij;
            sueldoNeto = suelNet;
            enlace = this;
        }
    }
    
    Nodo Buscar (Nodo lc, String dni){
        Nodo actual;
        boolean encontrado = false;
        actual = lc;
        
        //Recorremos la lista para encontrar la informacion
        while ((actual.enlace != lc) && (!encontrado)){
            encontrado = actual.enlace.numdni.equalsIgnoreCase(jTxtNumDNI.getText().trim());
            if(!encontrado)
                actual = actual.enlace;
        }
        return actual.enlace;
    }
    
    Nodo InsertaFinal(Nodo lc, String dni, String nom, String pate, String mate, int suelBas, int ventReal, String EstCiv, int numHij, double suelN){
        Nodo nuevo = new Nodo(dni, nom, pate, mate, suelBas, ventReal, EstCiv, numHij, suelN);
        
        if (lc  != null){
            nuevo.enlace = lc.enlace;
            lc.enlace = nuevo;
        }
        
        lc = nuevo; 
        return lc;
    }
    
    void Eliminar(){
        Nodo actual;
        boolean encontrado = false;
        actual = lc;
        
        while((actual.enlace != lc) && (!encontrado)) { //Bucle de busqueda
            encontrado = actual.enlace.numdni.equalsIgnoreCase(jTxtNumDNI.getText().trim());
            if (!encontrado)
                actual = actual.enlace;
        }
        
        // Verificamos el dato nuevamente
        encontrado = actual.enlace.numdni.equalsIgnoreCase(jTxtNumDNI.getText().trim());
        
        // Realizando los enlaces
        if (encontrado){
            Nodo p;
            p = actual.enlace; // Nodo a eliminar
            if(lc == lc.enlace) lc = null; //Lista con un solo Nodo
            else {
                if(p==lc)
                    lc = actual; // Se borra el elemento referido por lc
                
                actual.enlace = p.enlace;
            }
        }
        VerDatos();
    }
    
    void VerDatos(){
        // Variables para recorrer la lista
        String dni, nom, pate, mate, ec, sn;
        int sb, vr, nh; 
        double snt;
        int s;
        VaciarTable();
        Nodo p;
        
        if(lc != null){
            num = 0;
            p = lc.enlace;
            do {
                dni = p.numdni;
                nom = p.nombre;
                pate = p.paterno;
                mate = p.materno;
                sb = p.sueldoBase;
                vr = p.ventasRealizadas;
                ec = p.estadoCivil;
                nh = p.numHijos;
                snt = p.sueldoNeto;
                DecimalFormat df2 = new DecimalFormat("####.00");
                sn = df2.format(snt);
                num++;
                Object[] fila = {num, dni, nom, pate, mate, sb, vr, ec, nh, sn};
                miModelo.addRow(fila);
                
                p = p.enlace;
                
            } while(p!=lc.enlace);
        }
    }
    
    void Resumen(){
        Nodo p;
        String acumSuelTol, acumTotMontoVent, acumTotDescSeg, acumTotDescImputs;
        double sumaSueldoNeto = 0, sumaVentaRealiz = 0, sumaTotalDecSeguros = 0, sumaTotalDecImpt = 0;
        double s;
        double sb;
        double v;
        int numHij;
        String ec;
        double TA = 0;
        
        if(lc!=null){
            p = lc.enlace;
            do {
                s = p.sueldoNeto;
                v = p.ventasRealizadas;
                sb = p.sueldoBase;

                sumaSueldoNeto+=s;
                sumaVentaRealiz+=v;

                ec = p.estadoCivil;
                numHij = p.numHijos;
                if (ec == "SOLTERO(A)")
                    sumaTotalDecSeguros += 100;
                else
                    if (ec == "CASADO(A)")
                        if (numHij == 0)
                            sumaTotalDecSeguros += 120;
                        else
                            sumaTotalDecSeguros += 50 + (70*numHij);

                if (sb > 1500 && sb < 2300)
                    TA = 0.03;
                else if (sb > 2301 && sb < 3000)
                    TA = 0.04;
                else if (sb > 3001)
                    TA = 0.06;

                sumaTotalDecImpt += TA * sb;
                
                p = p.enlace;
                
            } while(p!=lc.enlace);
            
            // Colocamos la informacion en los objetos
            DecimalFormat df2 = new DecimalFormat("####.00");

            // Caculamos el monto total que la empresa debe pagar en conceptos de sueldos:
            acumSuelTol = df2.format(sumaSueldoNeto);

            // Caculamos el monto por comision de ventas:
            double totalMontConventa = sumaVentaRealiz * 0.05;
            acumTotMontoVent = df2.format(totalMontConventa);

            // Caculamos el monto total del descuento de seguro
            acumTotDescSeg = df2.format(sumaTotalDecSeguros);

            // Caculamos el monto total del descuento de impuestos
            acumTotDescImputs = df2.format(sumaTotalDecImpt);

            jTextField1.setText(acumSuelTol);
            jTextField2.setText(acumTotMontoVent);
            jTextField3.setText(acumTotDescSeg);
            jTextField4.setText(acumTotDescImputs);
        }
    }
    
    void Deshabilitar(){
        BtonActualizar.setEnabled(false);
        BtonEliminar.setEnabled(false);
        BtnGuardar.setEnabled(true);
    }
    
    void Habilitar(){
        BtonActualizar.setEnabled(true);
        BtonEliminar.setEnabled(true);
        BtnGuardar.setEnabled(false);
    }
    
    void LimpiarEntradas(){
        
        // Desactivar el filtro numérico
        FiltroNumericoOFF();
        
        jTxtNumDNI.setText("");
        jTxtSueldoBase.setText("");
        jTxtNom.setText("");
        jTxtPaterno.setText("");
        jTxtMaterno.setText("");
        jTextVentasRealizadas.setText("");
        jCBoxEstadoCivil.setSelectedIndex(0);
        jTextNumHijos.setText("");
        jTxtNumDNI.requestFocus();
        
        // Volver a habilitar el filtro numérico
        FiltroNumerico();
    }
    
    void VaciarTable(){
        int filas = tblRegistros.getRowCount();
        for (int i = 0; i<filas;i++){
            miModelo.removeRow(0);
        }
    }
    
    DefaultTableModel miModelo;
    String[] cabecera = {"N°", "DNI", "NOMBRE", "PATERNO", "MATERNO", "SUELDO BASE", "VENTAS REALIZADAS", "ESTADO CIVIL", "NUM HIJOS", "SUELDO NETO"};
    String[][] data ={};
    
    //Declaracion de variables locales
    public Nodo lc;
    public Nodo pFound;
    int num = 0;
    
    public RegitroEmpleado() {
        initComponents();
        
        // Inicializamos los punteros
        lc = pFound = null;
        
        // Habilitamos los datos de la tabla
        miModelo = new DefaultTableModel(data, cabecera);
        tblRegistros.setModel(miModelo);
        
        Deshabilitar();
        FiltroNumerico();
        FiltroDeLetras();
        
        jLTitle.setVisible(false);
        jLCurrency.setVisible(false);
    }

    
    void FiltroDeLetras(){
        // Aplicar filtro de letra a los textField donde solo se deben agregar letras
        ((AbstractDocument) jTxtNom.getDocument()).setDocumentFilter(new LetterFilterDocumentFilter());
        ((AbstractDocument) jTxtPaterno.getDocument()).setDocumentFilter(new LetterFilterDocumentFilter());
        ((AbstractDocument) jTxtMaterno.getDocument()).setDocumentFilter(new LetterFilterDocumentFilter());
    }
    
    void FiltroNumerico(){
        // Aplicar filtro numérico a los textField donde se ingresan numeros
        ((AbstractDocument) jTxtSueldoBase.getDocument()).setDocumentFilter(new NumericFilterDocumentFilter());
        ((AbstractDocument) jTextVentasRealizadas.getDocument()).setDocumentFilter(new NumericFilterDocumentFilter());
        ((AbstractDocument) jTextNumHijos.getDocument()).setDocumentFilter(new NumericFilterDocumentFilter());
    }
    
    void FiltroNumericoOFF(){
        // Desactivar el filtro numérico
        AbstractDocument sueldoBaseDocument = (AbstractDocument) jTxtSueldoBase.getDocument();
        AbstractDocument ventasRealizadasDocument = (AbstractDocument) jTextVentasRealizadas.getDocument();
        AbstractDocument numHijosDocument = (AbstractDocument) jTextNumHijos.getDocument();

        sueldoBaseDocument.setDocumentFilter(null);
        ventasRealizadasDocument.setDocumentFilter(null);
        numHijosDocument.setDocumentFilter(null);
    }
    
    class NumericFilterDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, text);

            if (isNumeric(sb.toString())) {
                super.insertString(fb, offset, text, attrs);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);

            if (isNumeric(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isNumeric(String str) {
            return str.matches("-?\\d+(\\.\\d+)?"); // Expresión regular para verificar si es un número.
        }
    }
    
    public class LetterFilterDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offset, text);

            if (isOnlyLetters(sb.toString())) {
                super.insertString(fb, offset, text, attrs);
            }
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            StringBuilder sb = new StringBuilder();
            sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offset, offset + length, text);

            if (isOnlyLetters(sb.toString())) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isOnlyLetters(String str) {
            return str.matches("[a-zA-ZñÑ\\s]*");
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTxtNumDNI = new javax.swing.JTextField();
        jTxtSueldoBase = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTxtNom = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTxtPaterno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTxtMaterno = new javax.swing.JTextField();
        BtnGuardar = new javax.swing.JButton();
        BtonRestaurar = new javax.swing.JButton();
        BtonSalir = new javax.swing.JButton();
        BtonEliminar = new javax.swing.JButton();
        BtonActualizar = new javax.swing.JButton();
        BtnConsulta = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jTextVentasRealizadas = new javax.swing.JTextField();
        jCBoxEstadoCivil = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextNumHijos = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRegistros = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLTitle = new javax.swing.JLabel();
        jLCurrency = new javax.swing.JLabel();
        LabelSueldoNeto = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1020, 200));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("DNI");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 130, -1));

        jLabel6.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("SUELDO BASE");
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 80, 120, -1));

        jTxtNumDNI.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTxtNumDNI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNumDNIFocusLost(evt);
            }
        });
        jPanel1.add(jTxtNumDNI, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, 110, 30));

        jTxtSueldoBase.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTxtSueldoBase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtSueldoBaseFocusLost(evt);
            }
        });
        jPanel1.add(jTxtSueldoBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 100, 30));

        jLabel3.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("NOMBRE");
        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 150, 150, -1));

        jTxtNom.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTxtNom.setMaximumSize(null);
        jTxtNom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtNomFocusLost(evt);
            }
        });
        jPanel1.add(jTxtNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 140, 240, 30));

        jLabel4.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("APELLIDO PATERNO");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 150, -1));

        jTxtPaterno.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTxtPaterno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtPaternoFocusLost(evt);
            }
        });
        jPanel1.add(jTxtPaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 180, 240, 30));

        jLabel5.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("APELLIDO MATERNO");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 150, -1));

        jTxtMaterno.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTxtMaterno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTxtMaternoFocusLost(evt);
            }
        });
        jPanel1.add(jTxtMaterno, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 240, 30));

        BtnGuardar.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtnGuardar.setText("GUARDAR");
        BtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarActionPerformed(evt);
            }
        });
        jPanel1.add(BtnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 80, 150, 40));

        BtonRestaurar.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtonRestaurar.setText("RESTAURAR");
        BtonRestaurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtonRestaurarActionPerformed(evt);
            }
        });
        jPanel1.add(BtonRestaurar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 180, 150, 40));

        BtonSalir.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtonSalir.setText("SALIR");
        BtonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtonSalirActionPerformed(evt);
            }
        });
        jPanel1.add(BtonSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 180, 150, 40));

        BtonEliminar.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtonEliminar.setText("ELIMINAR");
        BtonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtonEliminarActionPerformed(evt);
            }
        });
        jPanel1.add(BtonEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, 150, 40));

        BtonActualizar.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtonActualizar.setText("ACTUALIZAR");
        BtonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtonActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(BtonActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 80, 150, 40));

        BtnConsulta.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        BtnConsulta.setText("CONSULTA");
        BtnConsulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnConsultaActionPerformed(evt);
            }
        });
        jPanel1.add(BtnConsulta, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 150, 40));

        jLabel9.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("VENTAS REALIZADAS");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 270, 130, -1));

        jTextVentasRealizadas.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTextVentasRealizadas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextVentasRealizadasFocusLost(evt);
            }
        });
        jPanel1.add(jTextVentasRealizadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 120, 30));

        jCBoxEstadoCivil.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jCBoxEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---SELECCIONE---", "SOLTERO(A)", "CASADO(A)", "DIVORCIADO(A)", "VIUDO(A)" }));
        jPanel1.add(jCBoxEstadoCivil, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 150, 30));

        jLabel8.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("ESTADO CIVIL");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 270, 150, -1));

        jLabel10.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("NUMERO DE HIJOS");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 160, -1));

        jTextNumHijos.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jTextNumHijos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextNumHijosFocusLost(evt);
            }
        });
        jPanel1.add(jTextNumHijos, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 290, 150, 30));

        tblRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblRegistros);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 980, 160));

        jLabel1.setFont(new java.awt.Font("Hack Nerd Font", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("REGISTRO EMPLEADO");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 310, 50));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLTitle.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLTitle.setText("SUELDO NETO");
        jPanel2.add(jLTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 120, 20));

        jLCurrency.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jLCurrency.setText("S/.");
        jPanel2.add(jLCurrency, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, 30));

        LabelSueldoNeto.setFont(new java.awt.Font("Hack Nerd Font", 0, 12)); // NOI18N
        jPanel2.add(LabelSueldoNeto, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 80, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 180, 170, 50));

        jPanel3.setBackground(new java.awt.Color(0, 255, 204));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Hack Nerd Font", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("MONTO TOTAL SUELDOS");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 290, -1));

        jTextField1.setEnabled(false);
        jPanel3.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 290, 40));

        jLabel11.setFont(new java.awt.Font("Hack Nerd Font", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("MONTO TOTAL COMISION DE VENTAS");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 290, -1));

        jTextField2.setEnabled(false);
        jPanel3.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 40, 290, 40));

        jLabel12.setFont(new java.awt.Font("Hack Nerd Font", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Total Imp. Seguro");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 150, -1));

        jTextField3.setEnabled(false);
        jPanel3.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 140, 40));

        jTextField4.setEnabled(false);
        jPanel3.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 40, 150, 40));

        jLabel13.setFont(new java.awt.Font("Hack Nerd Font", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total Imp. Impuesto");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, 160, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, 980, 90));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTxtNumDNIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNumDNIFocusLost
        String text = jTxtNumDNI.getText();
        jTxtNumDNI.setText(text.toUpperCase());
    }//GEN-LAST:event_jTxtNumDNIFocusLost

    private void jTxtSueldoBaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtSueldoBaseFocusLost
        String text = jTxtSueldoBase.getText();
        jTxtSueldoBase.setText(text.toUpperCase());
    }//GEN-LAST:event_jTxtSueldoBaseFocusLost

    private void jTxtNomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtNomFocusLost
        String text = jTxtNom.getText();
        jTxtNom.setText(text.toUpperCase());
    }//GEN-LAST:event_jTxtNomFocusLost

    private void jTxtPaternoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtPaternoFocusLost
        String text = jTxtPaterno.getText();
        jTxtPaterno.setText(text.toUpperCase());
    }//GEN-LAST:event_jTxtPaternoFocusLost

    private void jTxtMaternoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTxtMaternoFocusLost
        String text = jTxtMaterno.getText();
        jTxtMaterno.setText(text.toUpperCase());
    }//GEN-LAST:event_jTxtMaternoFocusLost

    private void BtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarActionPerformed
        String dni = jTxtNumDNI.getText();
        String nom = jTxtNom.getText();
        String pate = jTxtPaterno.getText();
        String mate = jTxtMaterno.getText();
        int suldBase = Integer.parseInt(jTxtSueldoBase.getText());
        int ventRealizas = Integer.parseInt(jTextVentasRealizadas.getText());
        String estadoCivil = jCBoxEstadoCivil.getSelectedItem().toString();
        int numHijos = Integer.parseInt(jTextNumHijos.getText());

        double sueldoNeto = SueldoNeto();

        // Creamos el nodo de la lista en memoria y colocamos la informacion
        lc = InsertaFinal(lc, dni, nom, pate, mate, suldBase, ventRealizas, estadoCivil, numHijos, sueldoNeto);
        LimpiarEntradas();
        VerDatos();
        Resumen();
    }//GEN-LAST:event_BtnGuardarActionPerformed

    public double SueldoNeto(){
        int numHijos = Integer.parseInt(jTextNumHijos.getText()); 
        int indexCBox = jCBoxEstadoCivil.getSelectedIndex();
        int suldBase = Integer.parseInt(jTxtSueldoBase.getText());
        int ventRealizas = Integer.parseInt(jTextVentasRealizadas.getText());
        int descSeguro = 0;
        
        if (indexCBox == 1)
            descSeguro = 100;
        else if (indexCBox == 2){
            if (numHijos == 0)
                descSeguro = 120;
            else
                descSeguro = 50 + (numHijos * 70);
        }
        
        double comVenta = ventRealizas * 0.05;
        
        double sueldoNeto = (suldBase + comVenta);
        
        double montoDI = 0;
        
        if (sueldoNeto <= 1500)
            montoDI = 0;
        
        else if (sueldoNeto > 1500 && sueldoNeto < 2300)
            montoDI = 0.03;
        
        else if (sueldoNeto > 2301 && sueldoNeto < 3000)
            montoDI = 0.04;
        
        else if (sueldoNeto > 3001)
            montoDI = 0.06;
       
        double descImp = sueldoNeto * montoDI;
        
        sueldoNeto = sueldoNeto - descSeguro - descImp;
        
        return sueldoNeto;
    }
    
    private void BtonRestaurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtonRestaurarActionPerformed
        LimpiarEntradas();
        Deshabilitar();
    }//GEN-LAST:event_BtonRestaurarActionPerformed

    private void BtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtonSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_BtonSalirActionPerformed

    private void BtonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtonEliminarActionPerformed
        Eliminar();
        LimpiarEntradas();
        VerDatos();
        if(lc == null)
            JOptionPane.showMessageDialog(this,"La lista esta vacia.");
        Deshabilitar();
        Resumen();
        jLTitle.setVisible(false);
        jLCurrency.setVisible(false);
        LabelSueldoNeto.setText("");
    }//GEN-LAST:event_BtonEliminarActionPerformed

    private void BtonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtonActualizarActionPerformed
        pFound.numdni = jTxtNumDNI.getText();
        pFound.nombre = jTxtNom.getText();
        pFound.paterno = jTxtPaterno.getText();
        pFound.materno = jTxtMaterno.getText();
        pFound.sueldoBase = Integer.parseInt(jTxtSueldoBase.getText());
        pFound.ventasRealizadas = Integer.parseInt(jTextVentasRealizadas.getText());
        pFound.estadoCivil = jCBoxEstadoCivil.getSelectedItem().toString();
        pFound.numHijos = Integer.parseInt(jTextNumHijos.getText());
        double SueldoNeto = SueldoNeto();
        pFound.sueldoNeto = SueldoNeto;
        LimpiarEntradas();
        VerDatos();
        Resumen();
        jLTitle.setVisible(false);
        jLCurrency.setVisible(false);
        LabelSueldoNeto.setText("");

    }//GEN-LAST:event_BtonActualizarActionPerformed

    private void BtnConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnConsultaActionPerformed
        String dni = jTxtNumDNI.getText();
        if (dni.equalsIgnoreCase("")){
            JOptionPane.showMessageDialog(this, "Ingrese un codigo por favor.");
        } else {
            pFound = Buscar (lc, dni);

            if (pFound != null){
                String estCivil;
                estCivil = pFound.estadoCivil;

                if (estCivil.equalsIgnoreCase("SOLTERO(A)"))
                jCBoxEstadoCivil.setSelectedIndex(1);
                else if (estCivil.equalsIgnoreCase("CASADO(A)"))
                jCBoxEstadoCivil.setSelectedIndex(2);
                else if (estCivil.equalsIgnoreCase("DIVORCIADO(A)"))
                jCBoxEstadoCivil.setSelectedIndex(3);
                else if (estCivil.equalsIgnoreCase("VIUDO(A)"))
                jCBoxEstadoCivil.setSelectedIndex(4);

                jTxtNumDNI.setText(pFound.numdni);
                jTxtNom.setText(pFound.nombre);
                jTxtPaterno.setText(pFound.paterno);
                jTxtMaterno.setText(pFound.materno);
                jTxtSueldoBase.setText(String.valueOf(pFound.sueldoBase));
                jTextVentasRealizadas.setText(String.valueOf(pFound.ventasRealizadas));
                jTextNumHijos.setText(String.valueOf(pFound.numHijos));

                jLTitle.setVisible(true);
                jLCurrency.setVisible(true);

                LabelSueldoNeto.setText(String.valueOf(pFound.sueldoNeto));

                // Habilitamos los objetos para eliminar y actualizar
                Habilitar();
            } else {
                JOptionPane.showMessageDialog(this,"El dni: "+ dni + ", no esta en la lista.");
            }

        }
    }//GEN-LAST:event_BtnConsultaActionPerformed

    private void jTextVentasRealizadasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextVentasRealizadasFocusLost
        String text = jTextVentasRealizadas.getText();
        jTextVentasRealizadas.setText(text.toUpperCase());
    }//GEN-LAST:event_jTextVentasRealizadasFocusLost

    private void jTextNumHijosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextNumHijosFocusLost
        String text = jTextNumHijos.getText();
        jTextNumHijos.setText(text.toUpperCase());
    }//GEN-LAST:event_jTextNumHijosFocusLost

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RegitroEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegitroEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegitroEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegitroEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegitroEmpleado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnConsulta;
    private javax.swing.JButton BtnGuardar;
    private javax.swing.JButton BtonActualizar;
    private javax.swing.JButton BtonEliminar;
    private javax.swing.JButton BtonRestaurar;
    private javax.swing.JButton BtonSalir;
    private javax.swing.JLabel LabelSueldoNeto;
    private javax.swing.JComboBox<String> jCBoxEstadoCivil;
    private javax.swing.JLabel jLCurrency;
    private javax.swing.JLabel jLTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextNumHijos;
    private javax.swing.JTextField jTextVentasRealizadas;
    private javax.swing.JTextField jTxtMaterno;
    private javax.swing.JTextField jTxtNom;
    private javax.swing.JTextField jTxtNumDNI;
    private javax.swing.JTextField jTxtPaterno;
    private javax.swing.JTextField jTxtSueldoBase;
    private javax.swing.JTable tblRegistros;
    // End of variables declaration//GEN-END:variables
}
