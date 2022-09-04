/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectojgap;

import org.jgap.*;
import org.jgap.data.DataTreeBuilder;
import org.jgap.data.IDataCreators;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.jgap.xml.XMLDocumentBuilder;
import org.jgap.xml.XMLManager;
import org.w3c.dom.Document;

import javax.swing.*;
import java.io.File;


/**
 * @author FLIA BELTRAN MALDONA
 */
public class FPrincipal extends javax.swing.JFrame {

    private static final int MAX_EVOLUCIONES_PERMITIDAS = 2500;
    private static String mensaje;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCalcular;


    // ---------------------------------------------------------------------
    // Este metodo permite guardar en un xml la ultima poblacion calculada
    // ---------------------------------------------------------------------
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextArea txtaResultado;

    /**
     * Creates new form FPrincipal
     */
    public FPrincipal() {
        initComponents();
    }

    public static void calcularCambioMinimo(int Monto) throws Exception {
        Configuration.reset();
        // Se crea una configuracion con valores predeterminados.
        // -------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();

        // Se indica en la configuracion que el elemento mas apto siempre pase a
        // la proxima generacion
        // -------------------------------------------------------------
        conf.setPreservFittestIndividual(true);
        // Se Crea la funcion de aptitud y se setea en la configuracion
        // ---------------------------------------------------------
        FitnessFunction myFunc = new CambioMinimoFuncionAptitud(Monto);

        conf.setFitnessFunction(myFunc);
        // Ahora se debe indicar a la configuracion como seran los cromosomas: en
        // este caso tendran 6 genes (uno para cada tipo de moneda) con un valor
        // entero (cantidad de monedas de ese tipo).
        // Se debe crear un cromosoma de ejemplo y cargarlo en la configuracion
        // Cada gen tendra un valor maximo y minimo que debe setearse.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[6];
        sampleGenes[0] = new IntegerGene(conf, 0, 10); // Moneda 1 Dolar
        sampleGenes[1] = new IntegerGene(conf, 0, 10); // Moneda 50 centavos
        sampleGenes[2] = new IntegerGene(conf, 0, 10); // Moneda 25 centavos
        sampleGenes[3] = new IntegerGene(conf, 0, 10); // Moneda 10 centimos
        sampleGenes[4] = new IntegerGene(conf, 0, 10); // Moneda 5 centimos
        sampleGenes[5] = new IntegerGene(conf, 0, 10); // Moneda 1 centimo
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);
        // Por ultimo se debe indicar el tamaño de la poblacion en la
        // configuracion
        // ------------------------------------------------------------
        conf.setPopulationSize(200);
        Genotype Poblacion;
        // El framework permite obtener la poblacion inicial de archivos xml
        // pero para este caso particular resulta mejor crear una poblacion
        // aleatoria, para ello se utiliza el metodo randomInitialGenotype que
        // devuelve la poblacion random creada
        Poblacion = Genotype.randomInitialGenotype(conf);
        // La Poblacion debe evolucionar para obtener resultados mas aptos
        // ---------------------------------------------------------------
        long TiempoComienzo = System.currentTimeMillis();
        for (int i = 0; i < MAX_EVOLUCIONES_PERMITIDAS; i++) {
            Poblacion.evolve();
        }
        long TiempoFin = System.currentTimeMillis();
        mensaje = "Tiempo total de evolucion: " + (TiempoFin - TiempoComienzo) + " ms \n";
        guardarPoblacion(Poblacion);
        // Una vez que la poblacion evoluciono es necesario obtener el cromosoma
        // mas apto para mostrarlo como solucion al problema planteado para ello
        // se utiliza el metodo getFittestChromosome
        IChromosome cromosomaMasApto = Poblacion.getFittestChromosome();

        mensaje = mensaje + "El cromosoma mas apto encontrado tiene un valor de aptitud de: "
                + cromosomaMasApto.getFitnessValue() + " \n";
        mensaje = mensaje + "Y esta formado por la siguiente distribucion de monedas: " + " \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 0) + " Moneda 1 dólar \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 1) + " Moneda 50 centavos \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 2) + " Moneda 25 centavos \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 3) + " Moneda 10 centavos \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 4) + " Moneda 5 centavos \n";
        mensaje = mensaje + "\t" + CambioMinimoFuncionAptitud
                .getNumeroDeComendasDeGen(cromosomaMasApto, 5) + " Moneda 1 centavos \n";
        mensaje = mensaje + "Para un total de " + CambioMinimoFuncionAptitud
                .montoCambioMoneda(cromosomaMasApto) + " centavos en " + CambioMinimoFuncionAptitud
                .getNumeroTotalMonedas(cromosomaMasApto) + " monedas. \n";
    }

    public static void guardarPoblacion(Genotype Poblacion) throws Exception {
        DataTreeBuilder builder = DataTreeBuilder.getInstance();
        IDataCreators doc2 = builder.representGenotypeAsDocument(Poblacion);
        // create XML document from generated tree
        XMLDocumentBuilder docbuilder = new XMLDocumentBuilder();
        Document xmlDoc = (Document) docbuilder.buildDocument(doc2);
        XMLManager.writeFile(xmlDoc, new File("PoblacionCambioMinimo.xml"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
            java.util.logging.Logger.getLogger(FPrincipal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FPrincipal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FPrincipal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FPrincipal.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FPrincipal().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtMonto = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtaResultado = new javax.swing.JTextArea();
        bCalcular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Monto");

        txtaResultado.setColumns(20);
        txtaResultado.setRows(5);
        jScrollPane1.setViewportView(txtaResultado);

        bCalcular.setText("Calcular");
        bCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCalcularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(bCalcular)
                                                .addGap(0, 364, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bCalcular))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                       231,
                                        Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCalcularActionPerformed
        try {
            int amount = Integer.parseInt(txtMonto.getText());
            if (amount < 1 || amount >= CambioMinimoFuncionAptitud.MAX_MONTO) {
                JOptionPane.showMessageDialog(
                        this,
                        "El monto de dinero debe estar entre 1 y "
                                + (CambioMinimoFuncionAptitud.MAX_MONTO - 1) + ".");
            } else {
                calcularCambioMinimo(amount);
                txtaResultado.setText(mensaje);
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(
                    this,
                    "El (Monto de dinero) debe ser un numero entero valido. Error:" + error);

        }
    }//GEN-LAST:event_bCalcularActionPerformed
    // End of variables declaration//GEN-END:variables
}
