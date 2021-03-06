/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard;

import controller.Controller;
import moisturesensor.MoistureSensor;

/**
 *
 * @author Lorenzo Bellomo, 531423
 */
public class DashboardFrameEs3 extends javax.swing.JFrame {

    /**
     * Creates new form DashboardFrameEs3
     */
    public DashboardFrameEs3() {
        initComponents();
        // I also want to set my labels text to show the current status
        // of the system 
        // checking for current humidity value
        jLabel1.setText("Humidity: " + moistureSensor1.getCurrentHumidity());
        jLabel1.paintImmediately(jLabel1.getVisibleRect());
        // checking if irrigator is on or off
        String irrigatorOn = (controller1.getOn()) ? "ON" :" OFF";
        jLabel2.setText("Irrigator is: " + irrigatorOn);
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

        moistureSensor1 = new moisturesensor.MoistureSensor();
        controller1 = new controllerveto.ControllerVeto();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        moistureSensor1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                moistureSensor1PropertyChange(evt);
            }
        });

        controller1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                controller1PropertyChange(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Flip Irrigator");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(96, 96, 96))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton2))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // just trying to flip the controller status
        controller1.setOn(!controller1.getOn());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void moistureSensor1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_moistureSensor1PropertyChange
        // 2 possible properties, either humidity or decreasing, I want to react 
        // to humidity only changes
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        moistureSensor1.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void controller1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_controller1PropertyChange
        // 2 Properties in controller, ON and LocalHumidity. I want to react 
        // only to on changing events
        if(evt.getPropertyName().equals(Controller.PROP_ON)) {
            // I have to change decreasing value
            moistureSensor1.setDecreasing(!(Boolean)evt.getNewValue());
        }
    }//GEN-LAST:event_controller1PropertyChange

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
            java.util.logging.Logger.getLogger(DashboardFrameEs3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashboardFrameEs3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashboardFrameEs3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardFrameEs3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashboardFrameEs3().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private controllerveto.ControllerVeto controller1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private moisturesensor.MoistureSensor moistureSensor1;
    // End of variables declaration//GEN-END:variables
}
