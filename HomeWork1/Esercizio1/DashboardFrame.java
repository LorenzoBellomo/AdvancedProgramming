package dashboardframe;


import controller.Controller;
import moisturesensor.MoistureSensor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lorenzo Bellomo, 531423
 */
public class DashboardFrame extends javax.swing.JFrame {

    /**
     * Creates new form DashboardFrame
     */
    public DashboardFrame() {
        initComponents();
        // I also want to set my labels text to show the current status
        // of the system 
        // checking for current humidity value
        jLabel1.setText("Humidity: " + moistureSensor1.getCurrentHumidity());
        jLabel1.paintImmediately(jLabel1.getVisibleRect());
        // checking if irrigator is on or off
        String toDisplay = (moistureSensor1.getDecreasing()) ? "DECREASING" :" INCREASING";
        jLabel2.setText("Humidity is: " + toDisplay);
        jLabel2.paintImmediately(jLabel2.getVisibleRect());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controller1 = new controller.Controller();
        moistureSensor1 = new moisturesensor.MoistureSensor();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        controller1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                controller1PropertyChange(evt);
                controller1PropertyChange1(evt);
            }
        });

        moistureSensor1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                moistureSensor1PropertyChange(evt);
                moistureSensor1PropertyChange1(evt);
                moistureSensor1PropertyChange2(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");
        jLabel1.setName(""); // NOI18N

        jLabel2.setText("jLabel2");

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addComponent(jLabel2)
                .addContainerGap(148, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void controller1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_controller1PropertyChange
        // I had a problem and I created a lot of unused events. I am sorry but
        // I couldn't find a way to get rid of those
    }//GEN-LAST:event_controller1PropertyChange

    private void moistureSensor1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_moistureSensor1PropertyChange
        // 2 possible properties, either humidity or decreasing.
        if(evt.getPropertyName().equals(MoistureSensor.PROP_CURRENT_HUMIDITY)) {
            // I have to change the label text and update controller's local 
            // humidity, so that it can turn on and off the irrigator
            jLabel1.setText("Humidity: " + evt.getNewValue());
            controller1.setLocHumidity((int) evt.getNewValue());
        } else {
            // property is decreasing, I have to update label 2
            String toDisplay = ((Boolean)evt.getNewValue()) ? "DECREASING" : "INCREASING";
            jLabel2.setText("Humidity is: " + toDisplay);
        }
        // Invalidating view to get a repaint
        this.invalidate();
    }//GEN-LAST:event_moistureSensor1PropertyChange

    private void moistureSensor1PropertyChange1(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_moistureSensor1PropertyChange1
    }//GEN-LAST:event_moistureSensor1PropertyChange1

    private void moistureSensor1PropertyChange2(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_moistureSensor1PropertyChange2
    }//GEN-LAST:event_moistureSensor1PropertyChange2

    private void controller1PropertyChange1(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_controller1PropertyChange1
        // 2 Properties in controller, On and LocalHumidity. I want to react 
        // only to on changing events
        if(evt.getPropertyName().equals(Controller.PROP_ON)) {
            // I have to change decreasing value
            moistureSensor1.setDecreasing(!(Boolean)evt.getNewValue());
        }
    }//GEN-LAST:event_controller1PropertyChange1

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // button just makes the moisture method start
        moistureSensor1.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private controller.Controller controller1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private moisturesensor.MoistureSensor moistureSensor1;
    // End of variables declaration//GEN-END:variables
}
