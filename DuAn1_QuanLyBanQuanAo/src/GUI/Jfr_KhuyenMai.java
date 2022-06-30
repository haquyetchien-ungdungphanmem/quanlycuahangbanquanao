/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DAO.KhuyenMaiDAO;
import DAO.KhuyenMai_SanPhamDAO;
import DAO.LoaiSanPhamDAO;
import DAO.SanPhamDAO;
import Entity.KhuyenMai;
import Entity.KhuyenMai_SanPham;
import Entity.LoaiSP;
import Entity.SanPham;
import Entity.captcha;
import Ultil.Check;
import Ultil.MsgBox;
import Ultil.XDate;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Jfr_KhuyenMai extends javax.swing.JInternalFrame {

    /**
     * Creates new form Jfr_KhuyenMai
     */
    DefaultComboBoxModel  model_cbbLoaiSP ;
    
    DefaultTableModel model_SP  ;
    DefaultTableModel model_KM ;
    
    LoaiSanPhamDAO daoLoaiSP = new LoaiSanPhamDAO() ;
    SanPhamDAO daoSP = new SanPhamDAO() ;
    KhuyenMaiDAO daoKM = new KhuyenMaiDAO() ;
    KhuyenMai_SanPhamDAO daoKM_SP = new KhuyenMai_SanPhamDAO() ;
    
    Date vn = new Date() ;
     
    public Jfr_KhuyenMai() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        
        model_cbbLoaiSP = (DefaultComboBoxModel) cbbTheLoai.getModel() ;
           
        model_KM = (DefaultTableModel) tbDanhSachKM.getModel() ;
        model_SP = (DefaultTableModel) tbDanhSachSanPham.getModel() ;
        
        
        doVaoCbbHTGG();
        hide_();
        doVaoCbbApDungCho();
        DoVaoTableKM();
    }

    public void hide_() {
        lbDen.setVisible(false);
        lbTu.setVisible(false);
        txtMin.setVisible(false);
        txtMax.setVisible(false);
    }

    public void show_() {
        lbDen.setVisible(true);
        lbTu.setVisible(true);
        txtMin.setVisible(true);
        txtMax.setVisible(true);
    }

    public void doVaoCbbHTGG() {
        cbbHinhThucGG.removeAllItems();
        cbbHinhThucGG.addItem("Tất cả sản phẩm");
        cbbHinhThucGG.addItem("Theo khoảng giá");
    }

    public void doVaoCbbApDungCho(){
        List<LoaiSP> list = daoLoaiSP.selectAll() ;
        model_cbbLoaiSP.removeAllElements(); 
        
        for( LoaiSP x : list ){
            if( x.isTrangThai() == true ){
                cbbTheLoai.addItem( x.getTenLoaiSP() );
            }      
        }
        cbbTheLoai.addItem("Tất cả sản phẩm");
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        lbDen = new javax.swing.JLabel();
        cbbHinhThucGG = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cbbTheLoai = new javax.swing.JComboBox<>();
        txtMax = new javax.swing.JTextField();
        txtMin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lbTu = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbDanhSachSanPham = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTimeBD = new com.toedter.calendar.JDateChooser();
        txtTimeKT = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        txtMucGia = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txtTenChuongTrinh = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        rdbHoatDong = new javax.swing.JRadioButton();
        rdbNgung = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDanhSachKM = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbDen.setForeground(new java.awt.Color(0, 0, 0));
        lbDen.setText("Đến:");
        jPanel2.add(lbDen, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 30, -1, 20));

        cbbHinhThucGG.setBackground(new java.awt.Color(255, 255, 255));
        cbbHinhThucGG.setForeground(new java.awt.Color(0, 0, 0));
        cbbHinhThucGG.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbHinhThucGG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbHinhThucGGActionPerformed(evt);
            }
        });
        jPanel2.add(cbbHinhThucGG, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 180, -1));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Áp dụng cho:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        cbbTheLoai.setBackground(new java.awt.Color(255, 255, 255));
        cbbTheLoai.setForeground(new java.awt.Color(0, 0, 0));
        cbbTheLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbTheLoai.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbTheLoaiItemStateChanged(evt);
            }
        });
        jPanel2.add(cbbTheLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, 120, -1));

        txtMax.setText("0");
        txtMax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaxKeyReleased(evt);
            }
        });
        jPanel2.add(txtMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 30, 110, -1));

        txtMin.setText("0");
        txtMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMinKeyReleased(evt);
            }
        });
        jPanel2.add(txtMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 110, -1));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Hình thức giảm giá:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, -1, 20));

        lbTu.setForeground(new java.awt.Color(0, 0, 0));
        lbTu.setText("Từ:");
        jPanel2.add(lbTu, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 30, -1, 20));

        tbDanhSachSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "Mã SP", "Tên SP", "Kích thước", "Màu sắc", "Chất liệu", "Đơn giá", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDanhSachSanPham.setRowHeight(25);
        jScrollPane2.setViewportView(tbDanhSachSanPham);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 850, 310));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 880, 400));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khuyến mại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Tên chương trình:");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("TG bắt đầu:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Mức giảm giá(%):");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 20));

        txtTimeBD.setBackground(new java.awt.Color(255, 255, 255));
        txtTimeBD.setDateFormatString("dd-MM-yyyy");
        jPanel3.add(txtTimeBD, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 190, 30));

        txtTimeKT.setBackground(new java.awt.Color(255, 255, 255));
        txtTimeKT.setDateFormatString("dd-MM-yyyy");
        jPanel3.add(txtTimeKT, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 190, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("TG kết thúc:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, 30));
        jPanel3.add(txtMucGia, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 190, 30));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("Xóa");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 410, 150, 60));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("Lưu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 300, 60));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("Cập nhật");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 140, 60));
        jPanel3.add(txtTenChuongTrinh, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 40, 190, 30));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setForeground(new java.awt.Color(0, 0, 0));
        jButton4.setText("Mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, 300, 60));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Trạng Thái:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, -1, -1));

        buttonGroup1.add(rdbHoatDong);
        rdbHoatDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdbHoatDong.setForeground(new java.awt.Color(0, 0, 0));
        rdbHoatDong.setSelected(true);
        rdbHoatDong.setText("Đang hoạt động");
        jPanel3.add(rdbHoatDong, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        buttonGroup1.add(rdbNgung);
        rdbNgung.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdbNgung.setForeground(new java.awt.Color(0, 0, 0));
        rdbNgung.setText("Ngừng hoạt động");
        jPanel3.add(rdbNgung, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 290, -1, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 20, 340, 710));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khuyến mại", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbDanhSachKM.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã KM", "Tên KM", "Ngày bắt đầu", "Ngày kết thúc", "Loại SP", "Giảm giá", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDanhSachKM.setRowHeight(25);
        tbDanhSachKM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDanhSachKMMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDanhSachKM);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 850, 230));

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Tìm kiếm:");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, -1, 20));

        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyReleased(evt);
            }
        });
        jPanel4.add(txtTimKiem, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 210, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 430, 880, 300));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1260, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbbHinhThucGGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbHinhThucGGActionPerformed
        if (cbbHinhThucGG.getSelectedItem() != null) {
            if (!cbbHinhThucGG.getSelectedItem().toString().equals("Theo khoảng giá")) {
                hide_();
            } else {
                show_();
            }
        }
    }//GEN-LAST:event_cbbHinhThucGGActionPerformed

    // Đổ vào table
    private void DoVaoDanhSachSP(){
//        LoaiSP lsp = (LoaiSP) model_cbbLoaiSP.getSelectedItem() ;
//        System.out.println(lsp.getMaLoaiSP());
        
        List<SanPham> list ; 
        if( model_cbbLoaiSP.getElementAt( cbbTheLoai.getSelectedIndex()).toString().equalsIgnoreCase("Tất cả sản phẩm") ){
            list = daoSP.selectAll() ;
        }else{
            list = daoSP.selectAll_4( model_cbbLoaiSP.getElementAt( cbbTheLoai.getSelectedIndex()).toString() );
        }
        model_SP.setRowCount(0);
        int sk = 1 ;
        
        for (SanPham x : list) {
            if (x.isTrangThai() == true) {
                model_SP.addRow(new Object[]{sk, x.getMaCTSP(), x.getTenSP(), x.getTenKichThuoc(), x.getTenMauSac(), x.getTenChatLieu(),
                    x.getGia(), true});
                sk++;
            }
        }
        
    }
    
    private void cbbTheLoaiItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbTheLoaiItemStateChanged
       if( cbbTheLoai.getSelectedItem() != null ){
           DoVaoDanhSachSP();
       }
    }//GEN-LAST:event_cbbTheLoaiItemStateChanged

    public void LamMoi(){
        txtTimKiem.setText("");
        txtTenChuongTrinh.setText("");
        txtMucGia.setText("");
        txtTimeBD.setDateFormatString("");
        txtTimeKT.setDateFormatString("");
        rdbHoatDong.setSelected(true);
    }
    
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       LamMoi();
    }//GEN-LAST:event_jButton4ActionPerformed

    // Hàm bắt đầu khuyễn mãi 
//        private void BDKhuyenMai(){
//        List<KhuyenMai> listKM = daoKM.selectAll() ; 
//        
//        for( KhuyenMai x : listKM ){
//            if( x.isTrangThai() && XDate.toDay(vn).equals(XDate.toDay(x.getNgayBD()))  && XDate.toMonth(vn).equals(XDate.toMonth(x.getNgayBD()) )   ){
//                List<SanPham> listSP = daoSP.selectAll_6(x.getMaKM()) ;
//                for( SanPham k : listSP ){
//                    if( k.getGiamGia() == 0 || ( x.getGiamGia() > k.getGiamGia() ) ){
//                        daoSP.Update_4( x.getGiamGia() , k.getMaCTSP() );
//                    }
//                }
//            }
//        }
//    }
    
    // Check trùng 
    public boolean CheckTrung( String s ){
        if( daoKM.selectByID(s) == null ){
            return true ;
        }
        return false ;
    }
    
    // Thêm chương trình khuyến mãi ;
    public void ThemChuongTrinh(){
        if( Check.checkTrongText(txtTenChuongTrinh) && Check.checkTrongText(txtMucGia) && Check.checkTrongJdate(txtTimeBD) && Check.checkTrongJdate(txtTimeKT)
                && Check.checkSoDuong(txtMucGia ) ){
            KhuyenMai kh = new KhuyenMai() ;
            kh.setTenKM( txtTenChuongTrinh.getText() ) ;
            kh.setNgayBD( txtTimeBD.getDate());
            kh.setNgayKT( txtTimeKT.getDate() );
            kh.setGiamGia( Double.valueOf(txtMucGia.getText()) );
            kh.setTrangThai( rdbHoatDong.isSelected() ? true : false );
            
            captcha cp ;
            do{
                cp = new captcha() ;    
                kh.setMaKM(cp.getCaptcha());
                
            }while( CheckTrung( cp.getCaptcha() ) == false );
            daoKM.insert(kh);
            System.out.println(kh.getMaKM() );
            
            for (int i = 0; i < tbDanhSachSanPham.getRowCount() ;  i++) {
                if ( tbDanhSachSanPham.getValueAt(i, 7).toString().equalsIgnoreCase("true") ) {
                    KhuyenMai_SanPham km_sp = new KhuyenMai_SanPham();
                    km_sp.setMaCTSP(Integer.valueOf(tbDanhSachSanPham.getValueAt(i, 1).toString()));
                    km_sp.setMaKM( kh.getMaKM() );
                    daoKM_SP.insert(km_sp);
                }
            }
            LamMoi();
            DoVaoTableKM();
        }
    }
    
    public String TraVeTheLoai( String MaKM ) {
        List<SanPham> list = daoSP.selectAll_6(MaKM) ;
        
        for( int i=0 ; i < list.size()-1 ; i++ ){
            if( list.get(i).getTenLoai().equalsIgnoreCase( list.get(i+1).getTenLoai()) ){
                
            }else{
                return "Tất cả sản phẩm" ;
            }   
        }
        return list.get(0).getTenLoai() ;
    }
    
    // Dổ vào table danh sách khuyến mãi 
    private void DoVaoTableKM(){
        List<KhuyenMai> list = daoKM.selectAll() ; 
        model_KM.setRowCount(0);
        
        for( KhuyenMai x : list ){
            model_KM.addRow( new Object[] { x.getMaKM() , x.getTenKM() , x.getNgayBD() , x.getNgayKT() ,  TraVeTheLoai(x.getMaKM())  , x.getGiamGia() 
                    , x.isTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động" });
        }
        
    }
    
    private void DoVaoDanhSachSP2(){
//        LoaiSP lsp = (LoaiSP) model_cbbLoaiSP.getSelectedItem();
//        System.out.println(lsp.getMaLoaiSP());
        
        List<SanPham> list ;
        
        if( model_cbbLoaiSP.getElementAt(cbbTheLoai.getSelectedIndex() ).toString().equalsIgnoreCase("Tất cả sản phẩm") ) {
            list = daoSP.selectAll_7(  Double.valueOf(txtMin.getText()) , Double.valueOf(txtMax.getText()) );
        }else{
            list = daoSP.selectAll_5( model_cbbLoaiSP.getElementAt(cbbTheLoai.getSelectedIndex() ).toString() , Double.valueOf(txtMin.getText()) , Double.valueOf(txtMax.getText()) );
        }
       
        model_SP.setRowCount(0);
        int sk = 1;

        for (SanPham x : list) {
            model_SP.addRow(new Object[]{sk, x.getMaCTSP(), x.getTenSP(), x.getTenKichThuoc(), x.getTenMauSac(), x.getTenChatLieu(),
                x.getGia(), true});
            sk++ ;
        }
        
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if( MsgBox.comfirm(  this , "Bạn có muốn thêm khuyến mãi không")){
            ThemChuongTrinh();
        }      
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtMinKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinKeyReleased
        DoVaoDanhSachSP2();
    }//GEN-LAST:event_txtMinKeyReleased

    private void txtMaxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaxKeyReleased
        DoVaoDanhSachSP2();
    }//GEN-LAST:event_txtMaxKeyReleased

    // Hàm hiển thị ngược
    public void HienThiNguoc( ) {
        int Index = tbDanhSachKM.getSelectedRow() ;
        List<SanPham> list = daoSP.selectAll_6(tbDanhSachKM.getValueAt( Index , 0).toString());
        int sk = 1;
        model_SP.setRowCount(0);

        for (SanPham x : list) {
            model_SP.addRow(new Object[]{sk, x.getMaCTSP(), x.getTenSP(), x.getTenKichThuoc(), x.getTenMauSac(), x.getTenChatLieu(),
                x.getGia(), true});
            sk++;

        }
        model_cbbLoaiSP.setSelectedItem( tbDanhSachKM.getValueAt( Index , 4 ).toString() );
        txtTenChuongTrinh.setText( tbDanhSachKM.getValueAt( Index , 1 ).toString() );
        txtMucGia.setText( tbDanhSachKM.getValueAt( Index , 5 ).toString()  );
        txtTimeBD.setDate( XDate.toDate( tbDanhSachKM.getValueAt( Index , 2 ).toString()) );
        txtTimeKT.setDate( XDate.toDate( tbDanhSachKM.getValueAt( Index , 3 ).toString()) );
        if(  tbDanhSachKM.getValueAt( Index , 6 ).toString().equalsIgnoreCase("Đang hoạt động")  ){
            rdbHoatDong.setSelected( true);
        }else{
            rdbNgung.setSelected(true);
        }
    }
    
    private void tbDanhSachKMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDanhSachKMMouseClicked
        HienThiNguoc();
    }//GEN-LAST:event_tbDanhSachKMMouseClicked

    private void txtTimKiemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyReleased
        List<KhuyenMai> list = daoKM.selectAll_1( txtTimKiem.getText() ) ;
        model_KM.setRowCount(0);

        for (KhuyenMai x : list) {
            model_KM.addRow(new Object[]{x.getMaKM(), x.getTenKM(), x.getNgayBD(), x.getNgayKT(), TraVeTheLoai(x.getMaKM()), x.getGiamGia(),
                 x.isTrangThai() ? "Đang hoạt động" : "Ngừng hoạt động"});
        }
    }//GEN-LAST:event_txtTimKiemKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (MsgBox.comfirm(this, "Bạn có muốn cập nhập không ? ")) {
            KhuyenMai km = daoKM.selectByID(tbDanhSachKM.getValueAt(tbDanhSachKM.getSelectedRow(), 0).toString());
            km.setTenKM(txtTenChuongTrinh.getText());
            km.setGiamGia(Double.valueOf(txtMucGia.getText()));
            km.setNgayBD(txtTimeBD.getDate());
            km.setNgayKT(txtTimeKT.getDate());
            km.setTrangThai( rdbHoatDong.isSelected() ? true : false );
            daoKM.Update_2(km);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    // Hàm xóa khuyến mãi
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (MsgBox.comfirm(this, "Bạn có muốn xóa không")) {
            int k = tbDanhSachKM.getSelectedRow();
            String MaKM = tbDanhSachKM.getValueAt(k, 0).toString() ;
            List<SanPham> listSP = daoSP.selectAll_6(MaKM);
            for (SanPham x : listSP) {
                daoSP.Update_4(0, x.getMaCTSP());
            }
            daoKM_SP.delete(MaKM);
            daoKM.delete(MaKM);
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbHinhThucGG;
    private javax.swing.JComboBox<String> cbbTheLoai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbDen;
    private javax.swing.JLabel lbTu;
    private javax.swing.JRadioButton rdbHoatDong;
    private javax.swing.JRadioButton rdbNgung;
    private javax.swing.JTable tbDanhSachKM;
    private javax.swing.JTable tbDanhSachSanPham;
    private javax.swing.JTextField txtMax;
    private javax.swing.JTextField txtMin;
    private javax.swing.JTextField txtMucGia;
    private javax.swing.JTextField txtTenChuongTrinh;
    private javax.swing.JTextField txtTimKiem;
    private com.toedter.calendar.JDateChooser txtTimeBD;
    private com.toedter.calendar.JDateChooser txtTimeKT;
    // End of variables declaration//GEN-END:variables
}