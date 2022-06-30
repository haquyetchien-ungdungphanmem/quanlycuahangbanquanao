/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DAO.SanPhamDAO;
import Entity.SanPham;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Jfr_SanPhamAn extends javax.swing.JFrame {

    Color defaulColor, ClickColor;
    SanPhamDAO daoSp = new SanPhamDAO();

    /**
     * Creates new form Jfr_SanPhamAn
     */
    public Jfr_SanPhamAn() {
        initComponents();
        setLocationRelativeTo(null);
        defaulColor = new Color(255, 255, 255);
        ClickColor = new Color(221, 221, 221);
//        sp = new Jfr_SanPham() ;
        sanPhamAn();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPhamAn = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblSanPhamAn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã CTSP", "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Kích thước", "Màu sắc", "Chất liệu", "Đơn giá", "Số lượng"
            }
        ));
        tblSanPhamAn.setRowHeight(25);
        jScrollPane2.setViewportView(tblSanPhamAn);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, 1010, 390));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_show_password_25px.png"))); // NOI18N
        jLabel1.setText("Hiển thị lại ");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 430, 210, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1033, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public String HamDinhDang(String sk) {
        int a = 0, ac;
        String d = "";

        if (sk.charAt(0) == '-') {
            a = 1;
            sk = sk.substring(1);
        }

        ac = sk.indexOf(".");
        if (ac >= 0) {
            sk = sk.substring(0, sk.indexOf("."));
        }
        int k = sk.length();

        if (k >= 4 && k <= 6) {
            if (k == 6 || k == 5) {
                d = sk.substring(0, k / 2) + "," + sk.substring(k / 2, k);
            } else {
                d = sk.substring(0, k / 2 - 1) + "," + sk.substring(k / 2 - 1, k);
            }
        } else if (k == 7 || k == 8) {
            d = sk.substring(0, k / 2 - 2) + "," + sk.substring(k / 2 - 2, k / 2 + 1) + "," + sk.substring(k / 2 + 1, k);
        }

        if (a == 1) {
            return "-" + d + " VNĐ";
        }
        return d + " VNĐ";
    }
    
    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        jPanel2.setBackground(ClickColor);
        int row = tblSanPhamAn.getSelectedRow();
        try {
            int id = (int) tblSanPhamAn.getValueAt(row, 0);
            daoSp.hienThiSanPham(id, (int) tblSanPhamAn.getValueAt(row, 8));
            sanPhamAn();
            Jfr_SanPham.sp.set("a");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    public void sanPhamAn() {
        DefaultTableModel model = (DefaultTableModel) tblSanPhamAn.getModel();
        model.setRowCount(0);

        List<SanPham> list = daoSp.selectAll();
        for (SanPham x : list ) {
            if (x.isTrangThai() == false) {
                model.addRow(new Object[]{x.getMaCTSP(), x.getMaSP(), x.getTenSP(), x.getTenLoai(), x.getTenKichThuoc(), x.getTenMauSac(),
                    x.getTenChatLieu(), HamDinhDang(String.valueOf( x.getGia())), x.getSoLuong()});
            }
        }
    }

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
            java.util.logging.Logger.getLogger(Jfr_SanPhamAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Jfr_SanPhamAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Jfr_SanPhamAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Jfr_SanPhamAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Jfr_SanPhamAn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblSanPhamAn;
    // End of variables declaration//GEN-END:variables

}