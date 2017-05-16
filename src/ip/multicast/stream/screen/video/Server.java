/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ip.multicast.stream.screen.video;

import java.io.File;
import sender.ScreenSender;
import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.opencv.core.Core;
import sender.CameraSender;
import sender.VoiceSender;

/**
 *
 * @author Admin
 */
public class Server extends javax.swing.JFrame {

    private boolean isStreaming = false;
    private boolean isCameraStream = false;
    private boolean isVoiceStream = false;
    private boolean isListeningMyVoice = false;

    private Thread threadStreamCamera, threadStreamVoice, threadStreamScreen;
    private ScreenSender screenSender;
    private CameraSender cameraSender;
    private VoiceSender voiceSender;

    public Server() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupVideo = new javax.swing.ButtonGroup();
        pnControl = new javax.swing.JPanel();
        txtIpPort = new javax.swing.JTextField();
        btnStart = new javax.swing.JButton();
        rdCamera = new javax.swing.JRadioButton();
        rdScreen = new javax.swing.JRadioButton();
        cbVoice = new javax.swing.JCheckBox();
        cbListenVoice = new javax.swing.JCheckBox();
        pnChat = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatView = new javax.swing.JTextArea();
        pnChatView = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblFileSend = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnSendFile = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        pnMain = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnControl.setMinimumSize(new java.awt.Dimension(185, 20));
        pnControl.setPreferredSize(new java.awt.Dimension(200, 35));

        txtIpPort.setText("224.0.0.1:4444");
        txtIpPort.setEnabled(false);
        txtIpPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpPortActionPerformed(evt);
            }
        });
        pnControl.add(txtIpPort);

        btnStart.setText("Start Stream");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        pnControl.add(btnStart);

        btnGroupVideo.add(rdCamera);
        rdCamera.setSelected(true);
        rdCamera.setText("Camera");
        rdCamera.setEnabled(false);
        rdCamera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdCameraActionPerformed(evt);
            }
        });
        pnControl.add(rdCamera);

        btnGroupVideo.add(rdScreen);
        rdScreen.setText("Screen");
        rdScreen.setEnabled(false);
        rdScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdScreenActionPerformed(evt);
            }
        });
        pnControl.add(rdScreen);

        cbVoice.setText("Voice");
        cbVoice.setEnabled(false);
        cbVoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVoiceActionPerformed(evt);
            }
        });
        pnControl.add(cbVoice);

        cbListenVoice.setText("Listen myVoice");
        cbListenVoice.setEnabled(false);
        cbListenVoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbListenVoiceActionPerformed(evt);
            }
        });
        pnControl.add(cbListenVoice);

        getContentPane().add(pnControl, java.awt.BorderLayout.NORTH);

        pnChat.setBackground(new java.awt.Color(0, 153, 153));
        pnChat.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtChatView.setColumns(20);
        txtChatView.setRows(5);
        jScrollPane1.setViewportView(txtChatView);

        pnChat.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnChatView.setLayout(new java.awt.BorderLayout());

        jLabel3.setText("Chat >>");
        pnChatView.add(jLabel3, java.awt.BorderLayout.WEST);

        jTextField1.setColumns(15);
        pnChatView.add(jTextField1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Send");
        pnChatView.add(jButton1, java.awt.BorderLayout.EAST);

        lblFileSend.setText("file: ....");
        jPanel1.add(lblFileSend);

        jButton2.setText("Browse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);

        btnSendFile.setText("SendFile");
        btnSendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendFileActionPerformed(evt);
            }
        });
        jPanel1.add(btnSendFile);

        pnChatView.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pnChat.add(pnChatView, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnChat, java.awt.BorderLayout.EAST);

        lblStatus.setText("Status : ... everything in here");
        getContentPane().add(lblStatus, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout pnMainLayout = new javax.swing.GroupLayout(pnMain);
        pnMain.setLayout(pnMainLayout);
        pnMainLayout.setHorizontalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );
        pnMainLayout.setVerticalGroup(
            pnMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );

        getContentPane().add(pnMain, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbVoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVoiceActionPerformed
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        cbListenVoice.setEnabled(selected);
        if (selected) {
            boolean isListend = cbListenVoice.isSelected();
            voiceSender = new VoiceSender();
            voiceSender.setMyListening(isListend);
            threadStreamVoice = new Thread(voiceSender);
            threadStreamVoice.start();
        } else {

            voiceSender.isRunging = false;
            voiceSender.stop();
            voiceSender.setMyListening(false);
            threadStreamVoice.stop();
        }

        // abstractButton.setText(newLabel);
    }//GEN-LAST:event_cbVoiceActionPerformed

    private void rdScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdScreenActionPerformed
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {
            cameraSender.isRunning = false;
            threadStreamCamera.stop();
            screenSender = new ScreenSender(pnMain);
            threadStreamScreen = new Thread(screenSender);
            threadStreamScreen.start();
        }
    }//GEN-LAST:event_rdScreenActionPerformed

    private void rdCameraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdCameraActionPerformed
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {
            screenSender.isRunning = false;
            threadStreamScreen.stop();
            cameraSender = new CameraSender(pnMain);
            threadStreamCamera = new Thread(cameraSender);
            threadStreamCamera.start();
        }
    }//GEN-LAST:event_rdCameraActionPerformed

    private void cbListenVoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbListenVoiceActionPerformed
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        voiceSender.setMyListening(selected);
    }//GEN-LAST:event_cbListenVoiceActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    public void stopAll() {
        if (screenSender != null) {
            screenSender.isRunning = false;

        }
        if (threadStreamCamera != null) {
            threadStreamCamera.stop();
        }
        if (cameraSender != null) {
            cameraSender.isRunning = false;

        }
        if (threadStreamScreen != null) {
            threadStreamScreen.stop();
        }
        if (voiceSender != null) {
            voiceSender.isRunging = false;
            voiceSender.stop();

        }
        if (threadStreamVoice != null) {
            threadStreamVoice.stop();
        }
    }

    private void setGUIStart(boolean start) {
        if (start) {
            //start
            rdCamera.setSelected(start);
            btnStart.setText("Stop Stream");
        } else {
            //turn off
            pnMain.getGraphics().clearRect(0, 0, pnMain.getWidth(), pnMain.getHeight());
            btnStart.setText("Start Stream");
        }
        rdCamera.setEnabled(start);
        rdScreen.setEnabled(start);
        rdCamera.setEnabled(start);
        cbVoice.setEnabled(start);
        //txtIpPort.setEditable(!start);
    }
    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed

        if (isStreaming) {
            stopAll();
            setGUIStart(isStreaming);
            isStreaming = false;
            System.out.println("turn offf");
        } else {
            System.out.println("turn on");
            setGUIStart(!isStreaming);
            String txtIpPort = this.txtIpPort.getText();
            String[] split = txtIpPort.split(":");
            // CameraSender.IP_ADDRESS = split[0];
            // CameraSender.PORT = Integer.parseInt(split[1]);
            cameraSender = new CameraSender(pnMain);
            threadStreamCamera = new Thread(cameraSender);
            threadStreamCamera.start();
            isStreaming = true;

            if (cbVoice.isSelected()) {
                boolean isListend = cbListenVoice.isSelected();
                voiceSender = new VoiceSender();
                voiceSender.setMyListening(isListend);
                threadStreamVoice = new Thread(voiceSender);
                threadStreamVoice.start();
            }
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void txtIpPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpPortActionPerformed

    }//GEN-LAST:event_txtIpPortActionPerformed
    final JFileChooser fc = new JFileChooser();
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int retVal = fc.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File selectedfiles = fc.getSelectedFile();
            StringBuilder sb = new StringBuilder();
            sb.append(selectedfiles.getName());
            lblFileSend.setText(selectedfiles.getName());
            lblFileSend.setToolTipText(selectedfiles.getPath());
            
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnSendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendFileActionPerformed
        
    }//GEN-LAST:event_btnSendFileActionPerformed

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
            java.util.logging.Logger.getLogger(Server.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Server.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Server.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Server.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // load native library of opencv     
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupVideo;
    private javax.swing.JButton btnSendFile;
    private javax.swing.JButton btnStart;
    private javax.swing.JCheckBox cbListenVoice;
    private javax.swing.JCheckBox cbVoice;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblFileSend;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel pnChat;
    private javax.swing.JPanel pnChatView;
    private javax.swing.JPanel pnControl;
    private javax.swing.JPanel pnMain;
    private javax.swing.JRadioButton rdCamera;
    private javax.swing.JRadioButton rdScreen;
    private javax.swing.JTextArea txtChatView;
    private javax.swing.JTextField txtIpPort;
    // End of variables declaration//GEN-END:variables

}
