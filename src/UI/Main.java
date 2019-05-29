/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import BO.Answer;
import BO.Objectstream;
import BO.Question;
import BO.Topic;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Admin
 */
public class Main extends javax.swing.JFrame {

    /**
     * Creates new form Main
     */
    private Objectstream ostream;
    private Vector<Question> questList;
    private int timeLeft;
    private DefaultListModel<String> model;
    private Vector<Answer> answerList;
    static Topic currentTopic;
    Timer t1;

    public Main() {
        this.setUndecorated(true);
        new Open(this, true);
        if (currentTopic != null) {
            initMain();
        } else {
            System.exit(0);
        }

    }

    private void initMain() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ostream = new Objectstream();
        lbTime.setText(currentTopic.getTopicTime() + "");
        timeLeft = currentTopic.getTopicTime();
        model = new DefaultListModel<>();
        questList = currentTopic.getQuestList();
        nameList.setModel(model);
        answerList = new Vector();
        for (int i = 0; i < questList.size(); i++) {
            String str = "Câu " + (i + 1);
            model.addElement(str);
            Answer e = new Answer(questList.get(i).getQuestID());
            answerList.add(e);
        }
        for (int i = 0; i < answerList.size(); i++) {
            answerList.get(i).setStudentAnswer("");
        }
        nameList.updateUI();
        nameList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (answerList.size() != 0) {
                    if (!answerList.get(index).getStudentAnswer().equals("")) {
                        setBackground(Color.green);
                    }
                }

                return c;
            }

        });
        t1 = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft -= 1;
                lbTime.setText(timeLeft + "");
            }
        });
        t1.start();

        Thread g = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (nameList.getSelectedIndex() == -1) {
                        rbA.setEnabled(false);
                        rbB.setEnabled(false);
                        rbC.setEnabled(false);
                        rbD.setEnabled(false);
                    } else {
                        rbA.setEnabled(true);
                        rbB.setEnabled(true);
                        rbC.setEnabled(true);
                        rbD.setEnabled(true);
                    }
                    pnAn.updateUI();
                    if (cbSure.isSelected()) {
                        btFin.setEnabled(true);
                    } else {
                        btFin.setEnabled(false);
                    }

                }
            }
        };
        g.start();
        Thread g2 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (timeLeft == 0) {
                        g.stop();
                        t1.stop();
                        nameList.setEnabled(false);
                        cbSure.setSelected(true);
                        rbA.setEnabled(false);
                        rbB.setEnabled(false);
                        rbC.setEnabled(false);
                        rbD.setEnabled(false);
                        lbTime.setText("End");
                        lbFinish.setText("Mời bạn bấm vào finish để kết thúc");
                        cbSure.setEnabled(false);
                        questArea.setEnabled(false);
                        pView.getGraphics().clearRect(0, 0, pView.getWidth(), pView.getHeight());
                        btFin.setEnabled(true);
                    }
                    pnAn.updateUI();
                }
            }

        };
        g2.start();
    }

    private void showImage(Graphics g, BufferedImage currentImage) {
        if (currentImage != null) {
            g.clearRect(0, 0, pView.getWidth(), pView.getHeight());
            int imgWidth = currentImage.getWidth();
            int imgHeight = currentImage.getHeight();
            double ratio = 1.0 * imgWidth / imgHeight;
            int areaWidth = this.pView.getWidth();
            int areaHeight = this.pView.getHeight();
            if (imgWidth <= areaWidth && imgHeight <= areaHeight) {
                areaWidth = imgWidth;
                areaHeight = imgHeight;
            } else if (imgWidth > imgHeight) {
                if (imgWidth < areaWidth) {
                    areaWidth = imgWidth;
                }
                areaHeight = (int) (areaWidth / ratio);
            } else {
                if (imgHeight < areaHeight) {
                    areaHeight = imgHeight;
                }
                areaWidth = (int) (areaHeight * ratio);
            }
            g.drawImage(currentImage, 0, 0, areaWidth, areaHeight, this.pView);
        }

    }

    void showQuest() {
        int choice = nameList.getSelectedIndex();
        Question temp = questList.get(choice);
        questArea.setText(temp.getQuestQuest() + "\n");
        questArea.append("A. " + temp.getQuestAnswer1() + "\n");
        questArea.append("B. " + temp.getQuestAnswer2() + "\n");
        questArea.append("C. " + temp.getQuestAnswer3() + "\n");
        questArea.append("D. " + temp.getQuestAnswer4() + "\n");
        if (!temp.getQuestPath().equals("")) {
            try {
                String str = getClass().getClassLoader().getResource("resources").toString().substring(10);
                int st = str.indexOf("Student.jar");
                str = str.substring(0, st);
                str = str.concat("resources\\" + temp.getQuestPath());
                BufferedImage currentImage = ImageIO.read(new File(str));
                showImage(pView.getGraphics(), currentImage);
            } catch (IOException ex) {
            }
        } else {
            Graphics g = pView.getGraphics();
            g.clearRect(0, 0, pView.getWidth(), pView.getHeight());
        }
        if (!answerList.get(nameList.getSelectedIndex()).getStudentAnswer().equals("")) {
            String answer = answerList.get(nameList.getSelectedIndex()).getStudentAnswer();
            if (answer.equals(questList.get(nameList.getSelectedIndex()).getQuestAnswer1())) {
                rbA.setSelected(true);
            }
            if (answer.equals(questList.get(nameList.getSelectedIndex()).getQuestAnswer2())) {
                rbB.setSelected(true);
            }
            if (answer.equals(questList.get(nameList.getSelectedIndex()).getQuestAnswer3())) {
                rbC.setSelected(true);
            }
            if (answer.equals(questList.get(nameList.getSelectedIndex()).getQuestAnswer4())) {
                rbD.setSelected(true);
            }
        } else {
            buttonGroup1.clearSelection();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nameList = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        questArea = new javax.swing.JTextArea();
        pnAn = new javax.swing.JPanel();
        rbA = new javax.swing.JRadioButton();
        rbB = new javax.swing.JRadioButton();
        rbC = new javax.swing.JRadioButton();
        rbD = new javax.swing.JRadioButton();
        cbSure = new javax.swing.JCheckBox();
        btFin = new javax.swing.JButton();
        pView = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lbTime = new javax.swing.JLabel();
        lbFinish = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Phần mềm làm bài kiểm tra ");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("List câu hỏi"));

        nameList.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        nameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        nameList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                nameListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(nameList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Câu hỏi"));

        questArea.setEditable(false);
        questArea.setBackground(new java.awt.Color(240, 240, 240));
        questArea.setColumns(20);
        questArea.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        questArea.setRows(5);
        questArea.setBorder(null);
        questArea.setFocusable(false);
        jScrollPane2.setViewportView(questArea);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );

        pnAn.setBorder(javax.swing.BorderFactory.createTitledBorder("Đáp án"));

        buttonGroup1.add(rbA);
        rbA.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rbA.setText("A");
        rbA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbAActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbB);
        rbB.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rbB.setText("B");
        rbB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbBActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbC);
        rbC.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rbC.setText("C");
        rbC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbD);
        rbD.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rbD.setText("D");
        rbD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnAnLayout = new javax.swing.GroupLayout(pnAn);
        pnAn.setLayout(pnAnLayout);
        pnAnLayout.setHorizontalGroup(
            pnAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbA)
                .addGap(18, 18, 18)
                .addComponent(rbB)
                .addGap(18, 18, 18)
                .addComponent(rbC)
                .addGap(12, 12, 12)
                .addComponent(rbD)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnAnLayout.setVerticalGroup(
            pnAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnAnLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnAnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbA)
                    .addComponent(rbB)
                    .addComponent(rbC)
                    .addComponent(rbD))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbSure.setBackground(new java.awt.Color(214, 217, 223));
        cbSure.setText("Em đã chắc chắn muốn kết thúc bài làm");

        btFin.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        btFin.setText("Finish");
        btFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pViewLayout = new javax.swing.GroupLayout(pView);
        pView.setLayout(pViewLayout);
        pViewLayout.setHorizontalGroup(
            pViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pViewLayout.setVerticalGroup(
            pViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thời gian còn lại (phút)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14))); // NOI18N

        lbTime.setFont(new java.awt.Font("Dialog", 1, 80)); // NOI18N
        lbTime.setForeground(new java.awt.Color(255, 0, 0));
        lbTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTime.setText("jLabel2");
        lbTime.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbTime, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbTime, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        lbFinish.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        lbFinish.setForeground(new java.awt.Color(204, 0, 0));
        lbFinish.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cbSure)
                        .addGap(34, 34, 34)
                        .addComponent(btFin))
                    .addComponent(pView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 96, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btFin)
                                .addComponent(cbSure))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnAn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nameListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_nameListValueChanged
        // TODO add your handling code here:
        showQuest();


    }//GEN-LAST:event_nameListValueChanged

    private void rbAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbAActionPerformed
        // TODO add your handling code here:
        int index = nameList.getSelectedIndex();
        Answer temp = answerList.get(index);
        temp.setStudentAnswer(questList.get(index).getQuestAnswer1());
        answerList.set(index, temp);
    }//GEN-LAST:event_rbAActionPerformed

    private void rbBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbBActionPerformed
        // TODO add your handling code here:
        int index = nameList.getSelectedIndex();
        Answer temp = answerList.get(index);
        temp.setStudentAnswer(questList.get(index).getQuestAnswer2());
        answerList.set(index, temp);
    }//GEN-LAST:event_rbBActionPerformed

    private void rbCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCActionPerformed
        // TODO add your handling code here:
        int index = nameList.getSelectedIndex();
        Answer temp = answerList.get(index);
        temp.setStudentAnswer(questList.get(index).getQuestAnswer3());
        answerList.set(index, temp);
    }//GEN-LAST:event_rbCActionPerformed

    private void rbDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbDActionPerformed
        // TODO add your handling code here:
        int index = nameList.getSelectedIndex();
        Answer temp = answerList.get(index);
        temp.setStudentAnswer(questList.get(index).getQuestAnswer4());
        answerList.set(index, temp);
    }//GEN-LAST:event_rbDActionPerformed


    private void btFinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFinActionPerformed
        // TODO add your handling code here:
        t1.stop();
        new Result(this, true, questList, answerList, timeLeft);
        System.exit(0);

    }//GEN-LAST:event_btFinActionPerformed

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFin;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbSure;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbFinish;
    private javax.swing.JLabel lbTime;
    private javax.swing.JList nameList;
    private javax.swing.JPanel pView;
    private javax.swing.JPanel pnAn;
    private javax.swing.JTextArea questArea;
    private javax.swing.JRadioButton rbA;
    private javax.swing.JRadioButton rbB;
    private javax.swing.JRadioButton rbC;
    private javax.swing.JRadioButton rbD;
    // End of variables declaration//GEN-END:variables
}
