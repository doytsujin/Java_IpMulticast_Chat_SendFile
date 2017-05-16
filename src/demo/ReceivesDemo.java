package demo;

import java.awt.TextArea;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ReceivesDemo extends javax.swing.JFrame {

    MulticastSocket mcSocket = null;
    DefaultListModel listJoinIP;
    Thread receiveThread;
    public ReceivesDemo() {
        initComponents();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtIp = new javax.swing.JComboBox<>();
        btnJoin = new javax.swing.JButton();
        btnLeave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtResult = new javax.swing.JTextArea();
        txtPort = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        listJoin = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtIp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "224.0.0.1", "225.0.0.1", "226.0.0.1" }));

        btnJoin.setText("Join");
        btnJoin.setEnabled(false);
        btnJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJoinActionPerformed(evt);
            }
        });

        btnLeave.setText("Leave");
        btnLeave.setEnabled(false);
        btnLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveActionPerformed(evt);
            }
        });

        txtResult.setColumns(20);
        txtResult.setRows(5);
        txtResult.setFocusable(false);
        jScrollPane1.setViewportView(txtResult);

        txtPort.setText("1235");

        jToggleButton1.setText("Start");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        listJoin.setFocusable(false);
        jScrollPane2.setViewportView(listJoin);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtPort)
                        .addGap(18, 18, 18)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLeave, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtIp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToggleButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnJoin)
                            .addComponent(btnLeave))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJoinActionPerformed
        try {
            String mcIPStr = txtIp.getItemAt(txtIp.getSelectedIndex());
            InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
            mcSocket.joinGroup(mcIPAddress);
            listJoinIP.addElement(mcIPStr);
            btnLeave.setEnabled(true);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ReceivesDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReceivesDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnJoinActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        //Bắt đầu nhận thông tin multicast với port
        if (jToggleButton1.isSelected()) {
            try {
                int port = Integer.parseInt(txtPort.getText());
                mcSocket = new MulticastSocket(port);
                receiveThread = new Thread(new ReceiveThreadMulticast(mcSocket, txtResult));
                receiveThread.start();
                listJoinIP = new DefaultListModel();
                listJoin.setModel(listJoinIP);
                JOptionPane.showMessageDialog(this, "Started.");
                jToggleButton1.setText("Stop");
                btnJoin.setEnabled(true);
            } catch (java.lang.NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Port kiểu in.");
                jToggleButton1.setSelected(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Port đã được sử dụng, thử lại sau.");
            }
        } else {
            mcSocket.close();
            receiveThread.stop();
            listJoin.removeAll();
            jToggleButton1.setText("Start");
            btnLeave.setEnabled(false);
            btnJoin.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Closed.");

        }

    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void btnLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveActionPerformed
        try {
            String mcIPStr = txtIp.getItemAt(txtIp.getSelectedIndex());
            InetAddress mcIPAddress = InetAddress.getByName(mcIPStr);
            mcSocket.leaveGroup(mcIPAddress);
            listJoinIP.removeElement(mcIPStr);
            if (listJoinIP.isEmpty()) {
                btnLeave.setEnabled(false);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(ReceivesDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReceivesDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLeaveActionPerformed

    public static void main(String args[]) {

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
            java.util.logging.Logger.getLogger(ReceivesDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReceivesDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReceivesDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReceivesDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>  
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReceivesDemo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJoin;
    private javax.swing.JButton btnLeave;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JList<String> listJoin;
    private javax.swing.JComboBox<String> txtIp;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextArea txtResult;
    // End of variables declaration//GEN-END:variables
}

class ReceiveThreadMulticast implements Runnable {

    MulticastSocket multicastSocket;
    JTextArea txtOutput;

    public ReceiveThreadMulticast(MulticastSocket multicastSocket, JTextArea txtOutput) {
        this.multicastSocket = multicastSocket;
        this.txtOutput =txtOutput;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                multicastSocket.receive(packet);
                String msg = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength());
                String address = new String(packet.getAddress().toString()+ packet.getPort());
                txtOutput.append("\n"+address+":"+msg);
            } catch (IOException ex) {
                Logger.getLogger(ReceiveThreadMulticast.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
