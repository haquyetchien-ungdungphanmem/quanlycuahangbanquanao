/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import DAO.HTThanhToanDAO;
import DAO.HoaDonCTDAO;
import DAO.HoaDonDAO;
import DAO.KhachHangDAO;
import DAO.NhanVienDAO;
import DAO.SanPhamDAO;
import Entity.HinhThucTT;
import Entity.HoaDon;
import Entity.HoaDonCT;
import Entity.KhachHang;
import Entity.KichThuoc;
import Entity.NhanVien;
import Entity.SanPham;
import Ultil.Auth;
import Ultil.Check;
import Ultil.MsgBox;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class Jfr_HoaDon extends javax.swing.JInternalFrame implements Runnable, ThreadFactory {

    public WebcamPanel panel = null;
    public static Webcam webcam = null;
    public static final long serialVersionUID = 6441489157408381878L;
    public Executor executor = Executors.newSingleThreadExecutor();
    SanPhamDAO daoSP = new SanPhamDAO();
    HTThanhToanDAO daoHTTT = new HTThanhToanDAO();
    HoaDonDAO daoHD = new HoaDonDAO();
    HoaDonCTDAO daoCTHD = new HoaDonCTDAO();
    NhanVienDAO daoNV = new NhanVienDAO();
    KhachHangDAO daoKH = new KhachHangDAO();

    ArrayList<SanPham> listSP = new ArrayList<>();
    ArrayList<HinhThucTT> listHTTT = new ArrayList<>();
    ArrayList<HoaDon> listHD = new ArrayList<>();
    ArrayList<HoaDonCT> listHDCT = new ArrayList<>();
    ArrayList<NhanVien> listNV = new ArrayList<>();
    ArrayList<KhachHang> listKH = new ArrayList<>();

    DefaultTableModel model_tableDSSP;
    DefaultTableModel model_tableGioHang;
    DefaultTableModel model_tableHoaDon;

    DefaultComboBoxModel model_cbb1;
    DefaultComboBoxModel model_cbb2;

    int Index = - 1;
//    int count = 0;
    int NutDoiTra = 0 ; 
    Double TienKhachTra2 = 0.0 ;
 
    

    public Jfr_HoaDon() {
        initComponents();
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        ui.setNorthPane(null);
        initWebcam();

        model_cbb1 = (DefaultComboBoxModel) cbbHTThanhToan.getModel();
        model_cbb2 = (DefaultComboBoxModel) cbbHTThanhToan2.getModel();

        model_tableDSSP = (DefaultTableModel) tbDanhSachSP.getModel();
        model_tableGioHang = (DefaultTableModel) tbGioHang.getModel();
        model_tableHoaDon = (DefaultTableModel) tbDanhSachHD.getModel();

        btnTraHang.setVisible(false);
        DoVaoCbb();
        DoVaoTableDanhSachSP();
        DoVaoTableDanhSachHD();
    }

    private void initWebcam() {
        Dimension size = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        jPanel47.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 180));
        executor.execute(this);

    }

    // ????? d??? li???u v??o cbb
    private void DoVaoCbb() {
        listHTTT = (ArrayList<HinhThucTT>) daoHTTT.selectAll();
        model_cbb1.removeAllElements();
        model_cbb2.removeAllElements();

        for (HinhThucTT x : listHTTT) {
            model_cbb1.addElement(x);
            model_cbb2.addElement(x);
        }
    }

    // ????? d??? v??o b???ng danh s??ch h??a ????n
    private void DoVaoTableDanhSachHD() {
        listHD = (ArrayList<HoaDon>) daoHD.selectAll();
        model_tableHoaDon.setRowCount(0);
        int k = 0;

        for (int i = listHD.size() - 1; i >= 0; i--) {
            if ( listHD.get(i).getTrangThai().equalsIgnoreCase("??ang giao h??ng") || listHD.get(i).getTrangThai().equalsIgnoreCase("Ch??a thanh to??n") 
                    || listHD.get(i).getTrangThai().equalsIgnoreCase("??ang t???o") ) {
                HoaDon hd = listHD.get(i);
                String tenKH = " " ; 
                
                if( hd.getMaKH() != 0   ){
                    KhachHang kh = daoKH.selectByID(String.valueOf(hd.getMaKH())); 
                    tenKH = kh.getTenKH() ;
                }
               
                NhanVien nv = daoNV.selectByID(String.valueOf(hd.getMaNV()));
                
                model_tableHoaDon.addRow(new Object[]{k + 1, hd.getMaHD(), nv.getTenNV(), tenKH ,
                   hd.getTrangThai() , hd.getNgayTao() });
                k++;
            }
        }
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;

            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                result = new MultiFormatReader().decode(bitmap);

                String sk = JOptionPane.showInputDialog(this, "Nh???p s??? l?????ng", "H??? th???ng qu???n l??", JOptionPane.YES_NO_OPTION);
                try {
                    int so = Integer.valueOf(sk);
                    if (so < 0) {
                        MsgBox.alert(this, "Vui l??ng nh???p s??? d????ng");
                    } else {
                        ThemSPVaoGioHang(result.getText(), so);
                        XapXepLaiGioHang();
                    }
                } catch (Exception e) {
                    MsgBox.alert(this, "Vui l??ng nh???p s???");
                }
            } catch (NotFoundException e) {

            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }

    // ????? d??? li???u v??o table danh s??ch s???n ph???m 
    private void DoVaoTableDanhSachSP() {
        listSP = (ArrayList<SanPham>) daoSP.selectAll_3(txtTenSP.getText(), txtMaSP.getText(), txtMauSac.getText(),
                txtChatLieu.getText(), txtKichThuoc.getText(), txtTheLoai.getText());
        model_tableDSSP.setRowCount(1);

        for (SanPham x : listSP) {
            if (x.getSoLuong() == 0) {
                daoSP.Update_2(x);
            }

            if (x.isTrangThai() == true && x.getSoLuong() > 0) {
                model_tableDSSP.addRow(new Object[]{x.getMaCTSP(), x.getTenSP(), x.getTenLoai(), x.getTenChatLieu(), x.getTenKichThuoc(), x.getTenMauSac(),
                    x.getSoLuong(),  HamDinhDang( String.valueOf( x.getGia())) , x.getGiamGia()});
            }

        }
    }

    // ????? d??? li???u v??o gi??? h??ng 
    private void ThemSPVaoGioHang(String Ma, int k) {
//        int count=0 ;
        try {
            if (model_tableGioHang.getRowCount() > 0) {
                for (int i = 0; i < model_tableGioHang.getRowCount(); i++) {
                    if (model_tableGioHang.getValueAt(i, 1).toString().equalsIgnoreCase(Ma)) {
                        MsgBox.alert(this, "S???n ph???m ???? c?? trong gi??? h??ng");
                        return;
                    }
                }
            }

            int sk = tbDanhSachHD.getSelectedRow();
            if (sk >= 0) {
                SanPham sp = daoSP.selectByID2(Ma);
                model_tableGioHang.addRow(new Object[]{1, sp.getMaCTSP(), sp.getTenSP(), k,  HamDinhDang( String.valueOf( sp.getGia() )) ,
                    sp.getGiamGia(), HamDinhDang( String.valueOf( Math.round( k * sp.getGia() * (1 - sp.getGiamGia() / 100))))  , "", false});
                
                ThemVaoHoaDonCT( Integer.valueOf( tbDanhSachHD.getValueAt(sk, 1).toString() ) );  
                DoVaoTableDanhSachSP();
                TinhTien();
            }else{
                MsgBox.alert( this , "Vui l??ng t???o h??a ????n");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // H??m t??nh t???ng th??nh ti???n
    private void TinhTien() {
        Double tien = 0.0;
        for (int i = 0; i < model_tableGioHang.getRowCount(); i++) {
            tien += Double.valueOf( HamDinhDang2(model_tableGioHang.getValueAt(i, 6).toString() ) );
        }
        
        if( tien == 0 ){
            lbTongTienHang.setText("0");
            lbTongTienHang2.setText("0");
            lbKhachTra.setText("0");
            lbKhachTra2.setText("0");
        } else {
            lbTongTienHang.setText(HamDinhDang(String.valueOf(tien)));
            lbTongTienHang2.setText(HamDinhDang(String.valueOf(tien)));
            lbKhachTra.setText(HamDinhDang(String.valueOf(tien)));
            lbKhachTra2.setText(HamDinhDang(String.valueOf(tien)));
        }
        TienKhachTra2 = tien;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tbGioHang = new javax.swing.JTable();
        btnXoa4 = new javax.swing.JButton();
        btnTraHang = new javax.swing.JButton();
        jPanel50 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        txtMaSP = new javax.swing.JTextField();
        txtTenSP = new javax.swing.JTextField();
        txtTheLoai = new javax.swing.JTextField();
        txtChatLieu = new javax.swing.JTextField();
        txtKichThuoc = new javax.swing.JTextField();
        txtMauSac = new javax.swing.JTextField();
        jTextField51 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField52 = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        tbDanhSachSP = new javax.swing.JTable();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tbDanhSachHD = new javax.swing.JTable();
        jPanel51 = new javax.swing.JPanel();
        tabHoaDon = new javax.swing.JTabbedPane();
        jPanel52 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        txtTenKH = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        txtKhachDua = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        lbTienThua = new javax.swing.JLabel();
        lbTongTienHang = new javax.swing.JLabel();
        lbKhachTra = new javax.swing.JLabel();
        cbbHTThanhToan = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jButton13 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        txtTenKH2 = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        txtTienShip = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        txtSDT2 = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        lbTienThua2 = new javax.swing.JLabel();
        lbTongTienHang2 = new javax.swing.JLabel();
        cbbHTThanhToan2 = new javax.swing.JComboBox<>();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel75 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        btnTaoHD = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        lbKhachTra2 = new javax.swing.JLabel();
        txtKhachDua2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));
        jPanel55.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel47.setBackground(new java.awt.Color(255, 255, 255));
        jPanel47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel47.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel55.add(jPanel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 10, 250, 180));

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Gi??? h??ng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel48.setName(""); // NOI18N
        jPanel48.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "M?? SP", "T??n s???n ph???m", "S??? l?????ng ", "????n Gi??", "Gi???m Gi??", "Th??nh Ti???n", "Trang Th??i", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbGioHang.setRowHeight(25);
        tbGioHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbGioHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tbGioHang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbGioHangKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbGioHangKeyReleased(evt);
            }
        });
        jScrollPane10.setViewportView(tbGioHang);
        if (tbGioHang.getColumnModel().getColumnCount() > 0) {
            tbGioHang.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbGioHang.getColumnModel().getColumn(8).setPreferredWidth(15);
        }
        tbGioHang.getAccessibleContext().setAccessibleName("");

        jPanel48.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 910, 140));

        btnXoa4.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa4.setForeground(new java.awt.Color(0, 0, 0));
        btnXoa4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_trash_25px.png"))); // NOI18N
        btnXoa4.setText("Xo??");
        btnXoa4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa4ActionPerformed(evt);
            }
        });
        jPanel48.add(btnXoa4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, 30));

        btnTraHang.setBackground(new java.awt.Color(255, 255, 255));
        btnTraHang.setForeground(new java.awt.Color(0, 0, 0));
        btnTraHang.setText("Ho??n t???t tr??? h??ng");
        btnTraHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraHangActionPerformed(evt);
            }
        });
        jPanel48.add(btnTraHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 150, 30));

        jPanel55.add(jPanel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 930, 230));

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));
        jPanel50.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh s??ch s???n ph???m", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel49.setLayout(new java.awt.GridLayout(1, 0));

        txtMaSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtMaSP);

        txtTenSP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtTenSP);

        txtTheLoai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtTheLoai);

        txtChatLieu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtChatLieu);

        txtKichThuoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtKichThuoc);

        txtMauSac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMaSPKeyReleased(evt);
            }
        });
        jPanel49.add(txtMauSac);

        jTextField51.setEditable(false);
        jPanel49.add(jTextField51);

        jTextField1.setEditable(false);
        jPanel49.add(jTextField1);

        jTextField52.setEditable(false);
        jPanel49.add(jTextField52);

        jPanel50.add(jPanel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 910, 30));

        tbDanhSachSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "M?? SP", "T??n s???n ph???m", "Th??? lo???i", "Ch???t li???u", "K??ch th?????c", "M??u s???c", "S??? l?????ng", "????n gi??", "Gi???m Gi??"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDanhSachSP.setRowHeight(25);
        tbDanhSachSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDanhSachSPMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tbDanhSachSP);

        jPanel50.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 910, 220));

        jPanel55.add(jPanel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 930, 260));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh s??ch h??a ????n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel46.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tbDanhSachHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "M?? H??", "T??n NV", "T??n KH", "Tr???ng th??i", "Ng??y T???o"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbDanhSachHD.setRowHeight(25);
        tbDanhSachHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDanhSachHDMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tbDanhSachHD);
        if (tbDanhSachHD.getColumnModel().getColumnCount() > 0) {
            tbDanhSachHD.getColumnModel().getColumn(0).setMinWidth(10);
            tbDanhSachHD.getColumnModel().getColumn(0).setPreferredWidth(10);
        }

        jPanel46.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 650, 150));

        jPanel55.add(jPanel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 670, 190));

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));
        jPanel51.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "T???o h??a ????n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 16), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel51.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabHoaDon.setBackground(new java.awt.Color(255, 255, 255));
        tabHoaDon.setForeground(new java.awt.Color(0, 0, 0));

        jPanel52.setBackground(new java.awt.Color(255, 255, 255));
        jPanel52.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel53.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Ti???n th???a:");
        jPanel52.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));
        jPanel52.add(txtTenKH, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 180, -1));

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("S??T:");
        jPanel52.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        txtKhachDua.setText("0");
        txtKhachDua.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKhachDuaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKhachDuaFocusLost(evt);
            }
        });
        txtKhachDua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKhachDuaKeyReleased(evt);
            }
        });
        jPanel52.add(txtKhachDua, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 130, -1));
        jPanel52.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 270, -1));

        jLabel55.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("T??n KH:");
        jPanel52.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel56.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("T???ng ti???n h??ng:");
        jPanel52.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Ti???n kh??ch ????a:");
        jPanel52.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, -1, -1));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Kh??ch c???n tr???:");
        jPanel52.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));
        jPanel52.add(txtSDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 180, -1));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 0, 0));
        jLabel60.setText("HT Thanh to??n:");
        jPanel52.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, -1, -1));

        lbTienThua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTienThua.setForeground(new java.awt.Color(0, 0, 0));
        lbTienThua.setText("0");
        jPanel52.add(lbTienThua, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 320, 160, -1));

        lbTongTienHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTongTienHang.setForeground(new java.awt.Color(0, 0, 0));
        lbTongTienHang.setText("0");
        jPanel52.add(lbTongTienHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 140, -1));

        lbKhachTra.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbKhachTra.setForeground(new java.awt.Color(0, 0, 0));
        lbKhachTra.setText("0");
        jPanel52.add(lbKhachTra, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 200, 140, -1));

        cbbHTThanhToan.setBackground(new java.awt.Color(255, 255, 255));
        cbbHTThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbHTThanhToan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbHTThanhToanItemStateChanged(evt);
            }
        });
        cbbHTThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbHTThanhToanActionPerformed(evt);
            }
        });
        jPanel52.add(cbbHTThanhToan, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 240, 130, -1));
        jPanel52.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 270, -1));

        jButton13.setBackground(new java.awt.Color(255, 255, 255));
        jButton13.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jButton13.setForeground(new java.awt.Color(0, 0, 0));
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_25px.png"))); // NOI18N
        jButton13.setText("H???y");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel52.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 550, 120, 60));

        jButton15.setBackground(new java.awt.Color(255, 255, 255));
        jButton15.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jButton15.setForeground(new java.awt.Color(0, 0, 0));
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_create_25px.png"))); // NOI18N
        jButton15.setText("T???o h??a ????n");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel52.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 550, 130, 60));

        jButton16.setBackground(new java.awt.Color(255, 255, 255));
        jButton16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton16.setForeground(new java.awt.Color(0, 0, 0));
        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_pay_25px.png"))); // NOI18N
        jButton16.setText("Thanh to??n");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jPanel52.add(jButton16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 260, 60));

        tabHoaDon.addTab("H??a ????n", jPanel52);

        jPanel54.setBackground(new java.awt.Color(255, 255, 255));
        jPanel54.setForeground(new java.awt.Color(0, 0, 0));
        jPanel54.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Ti???n th???a:");
        jPanel54.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));
        jPanel54.add(txtTenKH2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 180, -1));

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("S??T:");
        jPanel54.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        txtTienShip.setText("0");
        txtTienShip.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTienShipFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTienShipFocusLost(evt);
            }
        });
        txtTienShip.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTienShipKeyReleased(evt);
            }
        });
        jPanel54.add(txtTienShip, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 130, -1));
        jPanel54.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 270, -1));

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("T??n KH:");
        jPanel54.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("T???ng ti???n h??ng:");
        jPanel54.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("Ti???n kh??ch ????a:");
        jPanel54.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        jLabel70.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(0, 0, 0));
        jLabel70.setText("Ti???n Ship");
        jPanel54.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        txtSDT2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSDT2KeyReleased(evt);
            }
        });
        jPanel54.add(txtSDT2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 180, -1));

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(0, 0, 0));
        jLabel71.setText("HT Thanh to??n:");
        jPanel54.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        lbTienThua2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTienThua2.setForeground(new java.awt.Color(0, 0, 0));
        lbTienThua2.setText("0");
        jPanel54.add(lbTienThua2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 160, -1));

        lbTongTienHang2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbTongTienHang2.setForeground(new java.awt.Color(0, 0, 0));
        lbTongTienHang2.setText("0");
        jPanel54.add(lbTongTienHang2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 120, -1));

        cbbHTThanhToan2.setBackground(new java.awt.Color(255, 255, 255));
        cbbHTThanhToan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbbHTThanhToan2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbbHTThanhToan2ItemStateChanged(evt);
            }
        });
        jPanel54.add(cbbHTThanhToan2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, 130, -1));
        jPanel54.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 270, 10));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(0, 0, 0));
        jLabel75.setText("?????a ch???:");
        jPanel54.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));
        jPanel54.add(txtDiaChi, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 180, -1));

        btnTaoHD.setBackground(new java.awt.Color(255, 255, 255));
        btnTaoHD.setForeground(new java.awt.Color(0, 0, 0));
        btnTaoHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_create_25px.png"))); // NOI18N
        btnTaoHD.setText("T???o h??a ????n");
        btnTaoHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHDActionPerformed(evt);
            }
        });
        jPanel54.add(btnTaoHD, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 250, 40));

        jButton18.setBackground(new java.awt.Color(255, 255, 255));
        jButton18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton18.setForeground(new java.awt.Color(0, 0, 0));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_shopping_cart_20px_1.png"))); // NOI18N
        jButton18.setText("Giao h??ng");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 530, 120, 50));

        jButton14.setBackground(new java.awt.Color(255, 255, 255));
        jButton14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jButton14.setForeground(new java.awt.Color(0, 0, 0));
        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_paid_bill_20px.png"))); // NOI18N
        jButton14.setText("???? giao");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 530, 120, 50));

        jLabel72.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 0, 0));
        jLabel72.setText("Kh??ch c???n tr???:");
        jPanel54.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, -1));

        lbKhachTra2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbKhachTra2.setForeground(new java.awt.Color(0, 0, 0));
        lbKhachTra2.setText("0");
        jPanel54.add(lbKhachTra2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 140, -1));

        txtKhachDua2.setText("0");
        txtKhachDua2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtKhachDua2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKhachDua2FocusLost(evt);
            }
        });
        txtKhachDua2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKhachDua2KeyReleased(evt);
            }
        });
        jPanel54.add(txtKhachDua2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, 130, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_25px.png"))); // NOI18N
        jButton1.setText("Ho??n Tr???");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel54.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 250, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Ghi Ch??: ");
        jPanel54.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 70, -1));

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        jPanel54.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 380, 180, 60));

        tabHoaDon.addTab("?????t h??ng", jPanel54);

        jPanel51.add(tabHoaDon, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 270, 680));

        jPanel55.add(jPanel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 290, 720));

        jPanel2.add(jPanel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 730));

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaSPKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMaSPKeyReleased
        DoVaoTableDanhSachSP();
    }//GEN-LAST:event_txtMaSPKeyReleased

    // h??m t???o h??a ????n
    private void LamTrangForm() {
        model_tableGioHang.setRowCount(0);
        NutDoiTra = 0 ;

        cbbHTThanhToan.setSelectedIndex(0);
        cbbHTThanhToan2.setSelectedIndex(0);

        txtKhachDua.setText("0");
        txtTienShip.setText("0");

        txtTenKH.setText("");
        txtTenKH2.setText("");

        txtSDT.setText("");
        txtSDT2.setText("");
        txtDiaChi.setText("");

        lbTongTienHang.setText("0");
        lbTongTienHang2.setText("0");
        
        lbKhachTra.setText("0");
        lbKhachTra2.setText("0");
        
        txtKhachDua2.setText("0");

        lbTienThua.setText("");
        lbTienThua2.setText("");


        txtChatLieu.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtMauSac.setText("");
        txtTheLoai.setText("");
        txtKichThuoc.setText("");
        
        txtGhiChu.setText("");
        
        btnTraHang.setVisible(false);
        
        DoVaoTableDanhSachSP();
    }

    // Click v??o b???ng danh s??ch h??a ????n ????? ????? v??o gi??? h??ng
    private void tbDanhSachSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDanhSachSPMouseClicked
        Index = tbDanhSachSP.getSelectedRow();

        if (MsgBox.comfirm(this, "B???n c?? mu???n th??m kh??ng")) {
            ThemSPVaoGioHang(tbDanhSachSP.getValueAt(Index, 0).toString(), 1);
            XapXepLaiGioHang();
        }
    }//GEN-LAST:event_tbDanhSachSPMouseClicked

    // H??ng c???p nh???p th??ng tin
    private void tbGioHangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbGioHangKeyReleased
              
        if( NutDoiTra == 1 ){          
            int k = tbGioHang.getSelectedRow() ;
            if( k >= 0 ){
                HoaDonCT hdct = new HoaDonCT() ;
                HoaDonCT hdct_1 = daoCTHD.selectByID3( tbDanhSachHD.getValueAt( tbDanhSachHD.getSelectedRow() , 1).toString() , tbGioHang.getValueAt( k, 1).toString()) ;
                if ( hdct_1.getSoLuong() > Integer.valueOf(tbGioHang.getValueAt(k, 3).toString())  ) {
                    String sk = JOptionPane.showInputDialog( this , "Vui l??ng nh???p l?? do mu???n tr??? h??ng", "H??? th???ng qu???n tr???", HEIGHT) ;
                   
                    try {
                        int SoLuong = Integer.valueOf(tbGioHang.getValueAt(k, 3).toString());

                        if (SoLuong >= 0) {
                            Double DonGia = Double.valueOf(HamDinhDang2(tbGioHang.getValueAt(k, 4).toString()));
                            Double GiamGia = Double.valueOf(tbGioHang.getValueAt(k, 5).toString());

                            hdct.setMaHD(Integer.valueOf(tbDanhSachHD.getValueAt(tbDanhSachHD.getSelectedRow(), 1).toString()));
                            hdct.setMaCTSP(Integer.valueOf(tbGioHang.getValueAt(k, 1).toString()));
                            hdct.setSoLuong(SoLuong);
                            hdct.setGia(DonGia);
                            hdct.setGiamGia(GiamGia);
                            hdct.setGhiChu(sk);

                            tbGioHang.setValueAt(HamDinhDang(String.valueOf(SoLuong * DonGia * (1 - GiamGia / 100))), k, 6);
                            hdct.setThanhTien(Double.valueOf(HamDinhDang2(tbGioHang.getValueAt(k, 6).toString())));
                            hdct.setTrangThai(false);

                            daoCTHD.insert(hdct);
                            HamCongNguocSoLuong(k);

                            hdct_1.setSoLuong(hdct_1.getSoLuong() - Integer.valueOf(tbGioHang.getValueAt(k, 3).toString()));
                            hdct_1.setThanhTien(hdct_1.getThanhTien() - (SoLuong * DonGia * (1 - GiamGia / 100)));
                            daoCTHD.update(hdct_1);

                            HienThiNguoc();
                        } else {
                            MsgBox.alert( this , "s??? l?????ng kh??ng ??c ");
                        }
                    } catch (Exception e) {
                        MsgBox.alert( this , "vui l??ng nh???p s??? v??o ?? s??? l?????ng ??? h??ng " + k );
                    }
             
                }else{
                    MsgBox.alert( this , "S??? h??ng kh??ch mua l??: " + hdct_1.getSoLuong() );
                }       
            }
            
        }else {
            
            Index = tbGioHang.getSelectedRow();
            SanPham sp = daoSP.selectByID2(tbGioHang.getValueAt(Index, 1).toString());



            try {
                int SoLuong = Integer.valueOf(tbGioHang.getValueAt(Index, 3).toString());
                
                if (SoLuong > 0) {
                    Double DonGia = Double.valueOf(HamDinhDang2(tbGioHang.getValueAt(Index, 4).toString()));
                    Double GiamGia = Double.valueOf(tbGioHang.getValueAt(Index, 5).toString());
                    
                    if (sp.getSoLuong() < Integer.valueOf(tbGioHang.getValueAt(Index, 3).toString())) {
                        MsgBox.alert(this, "S??? l?????ng s???n ph???m c??n " + sp.getSoLuong());
                        tbGioHang.setValueAt(sp.getSoLuong(), Index, 3);
                    }
                    
                    HoaDonCT hd = daoCTHD.selectByID3(tbDanhSachHD.getValueAt(tbDanhSachHD.getSelectedRow(), 1).toString(), String.valueOf(sp.getMaCTSP()));
                    hd.setSoLuong(SoLuong);
                    hd.setThanhTien(SoLuong * DonGia * (1 - GiamGia / 100));

                    listHDCT = (ArrayList<HoaDonCT>) daoCTHD.selectAll_2(String.valueOf(hd.getMaHD()));
                    if (listHDCT.get(Index).getSoLuong() != SoLuong) {
                        sp.setSoLuong(listHDCT.get(Index).getSoLuong() + sp.getSoLuong());
                        daoSP.Update_1_1(sp);
                    }
                    HamTruSoLuong(Index);

                    daoCTHD.update(hd);

                    DoVaoTableDanhSachSP();
                    tbGioHang.setValueAt(HamDinhDang(String.valueOf(SoLuong * DonGia * (1 - GiamGia / 100))), Index, 6);
                }else{
                    MsgBox.alert( this , "B???n kh??ng th??? nh???p s??? l?????ng ??m");
                }
            } catch (Exception e) {
                MsgBox.alert( this , "vui l??ng nh???p s??? v??o ?? s??? l?????ng ??? h??ng " + Index  );
            }

        }
        TinhTien();
    }//GEN-LAST:event_tbGioHangKeyReleased

    //H??m x??a ??? gi??? h??ng 
    private void XoaDHOGioHang() {
        int k = model_tableGioHang.getRowCount() - 1 ;

        while (k >= 0) {
            if ( tbGioHang.getValueAt(k, 8).toString().equalsIgnoreCase("true") ) { 
                HamCongNguocSoLuong(k);
                daoCTHD.delete_1( tbDanhSachHD.getValueAt( tbDanhSachHD.getSelectedRow(), 1).toString() , model_tableGioHang.getValueAt(k, 1).toString() );
                model_tableGioHang.removeRow(k);               
            }
            k--;
        }
        
        XapXepLaiGioHang();
    }

    // S???p x???p l???i th??? t??? gi??? h??ng
    private void XapXepLaiGioHang() {
        if ( tbGioHang.getRowCount() > 0) {
            for (int i = 0; i < tbGioHang.getRowCount(); i++) {
                model_tableGioHang.setValueAt(i + 1, i, 0);
            }
        }
    }

    // N??t thanh to??n ??? ?????t h??ng
    private void btnXoa4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa4ActionPerformed
        int k = tbDanhSachHD.getSelectedRow();
        if (tbDanhSachHD.getValueAt(k, 4).toString().equalsIgnoreCase("??ang t???o")
                || tbDanhSachHD.getValueAt(k, 4).toString().equalsIgnoreCase("Ch??a thanh to??n")) {
            XoaDHOGioHang();
            DoVaoTableDanhSachSP();
            TinhTien();
        }else{
            MsgBox.alert( this , "B???n kh??ng th??? x??a s???n ph???m khi h??a ????n ??ang giao");
        }
    }//GEN-LAST:event_btnXoa4ActionPerformed

    private void cbbHTThanhToanItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbHTThanhToanItemStateChanged
        Index = cbbHTThanhToan.getSelectedIndex();
        
        if (Index > 0) {
            txtKhachDua.setText(  lbTongTienHang.getText() );
            lbTienThua.setText("0");
            txtKhachDua.setEditable(false);
        } else {
            txtKhachDua.setEditable(true);
            txtKhachDua.setText("0");
//            Double a = Double.valueOf( HamDinhDang2( txtKhachDua.getText() ));
//            Double b = Double.valueOf( HamDinhDang2( lbTongTienHang.getText()) );
//            lbTienThua.setText( HamDinhDang(String.valueOf(a - b) ) );
        }
    }//GEN-LAST:event_cbbHTThanhToanItemStateChanged

    private void cbbHTThanhToan2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbbHTThanhToan2ItemStateChanged
        Index = cbbHTThanhToan2.getSelectedIndex();
        if (Index > 0) {
            txtKhachDua2.setText( lbKhachTra2.getText()  );
            lbTienThua2.setText("0");
            txtKhachDua2.setEditable(false);
        } else {
            txtKhachDua2.setEditable(true);
            txtKhachDua2.setText("0");
//            lbTienThua2.setText(String.valueOf(Double.valueOf(txtTienShip.getText()) - Double.valueOf(lbTongTienHang2.getText())));
        }
    }//GEN-LAST:event_cbbHTThanhToan2ItemStateChanged

    // n??t t???o h??a ????n ??? from h??a ????n
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        if (MsgBox.comfirm(this, "B???n c?? mu???n t???o h??a ????n kh??ng ?")) {
            ThemHoaDon("Ch??a thanh to??n", "Not", 0);
//        ThemVaoHoaDonCT( Integer.valueOf( tbDanhSachHD.getValueAt( 0, 1).toString()) );    
            LamTrangForm();
            tbDanhSachHD.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    // Th??m v??o danh s??ch h??a ????n
    private void ThemHoaDon( String tthai, String ghiChu , double tienShip ) {

        try {
            HinhThucTT tt = (HinhThucTT) cbbHTThanhToan.getSelectedItem();
            KhachHang kh = new KhachHang();

            kh.setTenKH(txtTenKH.getText());
            kh.setSDT(txtSDT.getText());
            kh.setDiaChi("Not");
            daoKH.insert(kh);
            listKH = (ArrayList<KhachHang>) daoKH.selectAll() ;
            
            ThemVaoHoaDon( listKH.get( listKH.size() -1).getMaKH() , tt.getMaHTTT(), tthai, ghiChu, tienShip);
        } catch (Exception e) {
        }
    }
    
    private void ThemVaoHoaDon( int MaKH, int HTTT, String tt, String ghiChu, double tienShip) {

        HoaDon hd = new HoaDon();
        hd.setMaKH(MaKH);
        hd.setMaHTTT(HTTT);
        hd.setMaNV(Auth.user.getMaNV());
        hd.setGhiChu(ghiChu);
        hd.setTrangThai(tt);
        hd.setTienShip(tienShip);

        daoHD.insert(hd);
        DoVaoTableDanhSachHD();
    }

    //Th??m v??o h??a ????n
    private void ThemVaoHoaDon2(  String tt, String ghiChu , double tienShip ) {
//        int k = daoKH.selectByID_2(SDT);
                
        HoaDon hd = new HoaDon();  
//        hd.setMaKH(0);
//        hd.setMaHTTT(HTTT);
        hd.setMaNV(Auth.user.getMaNV());
        hd.setGhiChu(ghiChu);
        hd.setTrangThai(tt);
        hd.setTienShip(tienShip);

        daoHD.insert_1(hd);
        DoVaoTableDanhSachHD();
    }

    // Th??m v??o h??a ????n chi ti???t
    private void ThemVaoHoaDonCT(int k) {
        HoaDonCT hdct = new HoaDonCT();

        hdct.setMaHD(k);
        for (int i = 0; i < tbGioHang.getRowCount(); i++) {
            if ( daoCTHD.selectByID1(tbGioHang.getValueAt(i, 1).toString() , k ) == null ) {
                hdct.setMaCTSP(Integer.valueOf(tbGioHang.getValueAt(i, 1).toString()));
                hdct.setSoLuong(  Integer.valueOf(tbGioHang.getValueAt(i, 3).toString()) );
                HamTruSoLuong(i) ;
                hdct.setGia( Double.valueOf( HamDinhDang2( tbGioHang.getValueAt(i, 4).toString() )));
                hdct.setGiamGia(Double.valueOf(tbGioHang.getValueAt(i, 5).toString()));
                hdct.setThanhTien(Double.valueOf( HamDinhDang2(tbGioHang.getValueAt(i, 6).toString()) )  );
                hdct.setTrangThai(true);
                hdct.setGhiChu("Not");
                daoCTHD.insert(hdct);
            }
        }
    }
    
    // H??m tr??? s??? l?????ng
    private void HamTruSoLuong(int i) {
        int SoLuong = Integer.valueOf(tbGioHang.getValueAt(i, 3).toString());
        SanPham sp = daoSP.selectByID2(tbGioHang.getValueAt(i, 1).toString());
        sp.setSoLuong( sp.getSoLuong() - SoLuong );
        daoSP.Update_1_1(sp);
    }
    
    // N??t th??nh to??n 
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        if (MsgBox.comfirm(this, "B???n c?? mu???n thanh to??n kh??ng?")) {
            int k = tbDanhSachHD.getSelectedRow();
            listHD = (ArrayList<HoaDon>) daoHD.selectAll();
            Double kkk = Double.valueOf( HamDinhDang2( txtKhachDua.getText()) ) - Double.valueOf( HamDinhDang2( lbKhachTra.getText()) );
           
            if (kkk >= 0) {
                if (tbGioHang.getRowCount() == 0) {
                    MsgBox.alert(this, "Gi??? h??ng tr???ng kh??ng th??? thanh to??n");
                } else {
                    if (k >= 0) {
                        String MaHD = tbDanhSachHD.getValueAt(k, 1).toString();
                        HinhThucTT htt = (HinhThucTT) cbbHTThanhToan.getSelectedItem();
                        HoaDon hd = daoHD.selectByID(MaHD);

                        daoHD.update2("???? thanh to??n", MaHD);
                        daoHD.update_HTTT(htt.getMaHTTT(), MaHD);

                        if (!tbDanhSachHD.getValueAt(k, 2).toString().equalsIgnoreCase(txtTenKH.getText())) {
                            daoKH.update_1(txtTenKH.getText(), txtSDT.getText(), hd.getMaKH());
                        }

                        ThemVaoHoaDonCT(Integer.valueOf(MaHD));
                        DoVaoTableDanhSachHD();
                    } else {
                        ThemHoaDon("???? thanh to??n", "Not", 0);
                        int skk = listHD.get(listHD.size() - 1).getMaHD();
                        ThemVaoHoaDonCT(skk);
                    }
                    LamTrangForm();
                    MsgBox.alert(this, "Thanh to??n th??nh c??ng");
                }
            } else {
                MsgBox.alert(this, "Kh??ng th??? thanh to??n khi kh??ch tr??? thi???u ti???n");
            }
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    // H??m ?????i ?????nh d???ng ti???n
    public String HamDinhDang( String sk ){
        int a = 0 , ac ;
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
        
        if( a == 1 ){
            return "-" + d + " VN??" ;
        }
        return  d + " VN??" ;
    }

    // H??m ?????i l???i ?????nh dang ti???n 
    public String HamDinhDang2( String sk ){
        String ab = "";
        int ak = sk.indexOf(" ");

        if (ak >= 0) {
            sk = sk.substring(0, ak);
        }
        
        String a[] = sk.split(",");
        for (int i = 0; i < a.length; i++) {
            ab += a[i];
        }
        return ab ;
    }
    
    // n??t thanh to??n b??n d???t h??ng
    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        if (model_tableGioHang.getRowCount() != 0) {
            if (MsgBox.comfirm(this, "B???n c?? mu???n giao h??ng kh??ng")) {
                if (Check.checkTrongText(txtTenKH2) && Check.checkTrongText(txtSDT2) && Check.checkTrongText(txtDiaChi) && Check.checkSDT(txtSDT2)) {
                    Double kkk = Double.valueOf(HamDinhDang2(txtKhachDua2.getText())) - Double.valueOf(HamDinhDang2(lbKhachTra2.getText()));

                    if (kkk >= 0) {
                        HoaDon hd = daoHD.selectByID(tbDanhSachHD.getValueAt(tbDanhSachHD.getSelectedRow(), 1).toString());

                        KhachHang kh = new KhachHang();
                        int k;

                        if (daoKH.selectByID_2(txtSDT2.getText()) == 0) {
                            kh.setTenKH(txtTenKH2.getText());
                            kh.setDiaChi(txtDiaChi.getText());
                            kh.setSDT(txtSDT2.getText());
                            daoKH.insert(kh);
                            listKH = (ArrayList<KhachHang>) daoKH.selectAll();
                            k = listKH.get(listKH.size() - 1).getMaKH();
                        } else {
                            daoKH.update_dc(txtDiaChi.getText(), txtSDT2.getText());
                            k = daoKH.selectByID_2(txtSDT2.getText());
                        }

                        HinhThucTT htt = (HinhThucTT) cbbHTThanhToan2.getSelectedItem();
                        hd.setMaKH(k);
                        hd.setMaHTTT(htt.getMaHTTT());
                        hd.setTienShip(Double.valueOf(HamDinhDang2(txtTienShip.getText())));

                        daoHD.update2("??ang giao h??ng", String.valueOf(hd.getMaHD()));
                        daoHD.update1(txtGhiChu.getText(), String.valueOf(hd.getMaHD()));
                        daoHD.update(hd);

                        DoVaoTableDanhSachHD();
                        MsgBox.alert(this, "Giao h??ng th??nh c??ng");
                        LamTrangForm();
                    } else {
                        MsgBox.alert(this, "B???n kh??ng th??? giao h??ng khi ch??a thanh to??n ti???n");
                    }

                }
            }
        }else{
            MsgBox.alert( this , "B???n kh??ng th??? giao h??ng v???i gi??? h??ng tr???ng");
        }

    }//GEN-LAST:event_jButton18ActionPerformed

    
    // H??m c???ng l???i s??? l?????ng
    private void HamCongNguocSoLuong( int i) {       
        int SoLuong = Integer.valueOf(tbGioHang.getValueAt(i, 3).toString());
        SanPham sp = daoSP.selectByID2(tbGioHang.getValueAt(i, 1).toString());
        sp.setSoLuong(SoLuong + sp.getSoLuong());
        daoSP.Update_1_1(sp);
    }
    
    //  n??t h???y
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        int k = tbDanhSachHD.getSelectedRow();

        if (k >= 0) {
            if ( MsgBox.comfirm(this, "B???n c?? mu???n h???y kh??ng") == true) {
                String ghiChu = JOptionPane.showInputDialog(this, "Nh???p l?? do b???n mu???n h???y h??a ????n", "H??? th???ng qu???n tr???", HEIGHT);
                for( int i=0 ; i<tbGioHang.getRowCount() ; i++ ){
                    HamCongNguocSoLuong(i);
                }
                daoHD.update1( ghiChu , tbDanhSachHD.getValueAt(k, 1).toString() );
                daoHD.update2( "???? H???y", tbDanhSachHD.getValueAt(k, 1).toString() );
                daoCTHD.update_tt(tbDanhSachHD.getValueAt(k, 1).toString() );
                LamTrangForm();
                DoVaoTableDanhSachHD();
                MsgBox.alert( this , "H???y th??nh c??ng");
            }
        } else {
            MsgBox.alert(this, "Vui l??ng ch???n h??a ????n mu???n h???y");
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    // N??t x??c nh???n ????n h??ng ???? ???????c giao
    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        int k = tbDanhSachHD.getSelectedRow();
        
        if (MsgBox.comfirm(this, "B???n c?? mu???n ho??n t???t giao h??ng kh??ng")) {
            if (tbDanhSachHD.getValueAt(k, 4).toString().equalsIgnoreCase("???? giao h??ng")) {
                MsgBox.alert(this, "H??ng ???? ???????c giao");
            } else {
                String MaHD = tbDanhSachHD.getValueAt(k, 1).toString();
                daoHD.update2("???? giao h??ng", MaHD);
//            ThemVaoHoaDonCT(Integer.valueOf(MaHD));
                DoVaoTableDanhSachHD();
                LamTrangForm();
                MsgBox.alert(this, "Kh??ch ???? nh???n ???????c h??ng");
            }
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    // 
    public void setSelectedComboboxHTTT( int cbbselected, JComboBox cbb) {
        for (int i = 0; i < cbb.getItemCount(); i++) {
            HinhThucTT m = (HinhThucTT) cbb.getItemAt(i);
            if (m != null) {
                if ( cbbselected == Integer.valueOf( m.getMaHTTT() )  ) {
                    cbb.setSelectedItem(m);
                }
            }
        }
    }
    
    // click ??? form danh s??ch h??a ??a??n
    private void HienThiNguoc() {
        Index = tbDanhSachHD.getSelectedRow();

        String MaHD = tbDanhSachHD.getValueAt(Index, 1).toString();
        HoaDon hd = daoHD.selectByID(MaHD);
        listHDCT = (ArrayList<HoaDonCT>) daoCTHD.selectAll_2(MaHD);
        KhachHang kh = daoKH.selectByID(String.valueOf(hd.getMaKH()));

        model_tableGioHang.setRowCount(0);

        for (HoaDonCT x : listHDCT) {
//            if ( x.getTrangThai() == true ) {
                SanPham sp = daoSP.selectByID2(String.valueOf(x.getMaCTSP()));
                model_tableGioHang.addRow(new Object[]{1, x.getMaCTSP(), sp.getTenSP(), x.getSoLuong(), HamDinhDang( String.valueOf( x.getGia())  ) ,
                    x.getGiamGia(), HamDinhDang ( String.valueOf( x.getThanhTien()) ) , x.getTrangThai() ? "" : "H??ng Tr???"} );
//            }
        }
        
        XapXepLaiGioHang();       
        TinhTien(); 
        
        if (  hd.getTrangThai().equalsIgnoreCase("??ang giao h??ng") || hd.getTrangThai().equalsIgnoreCase("??ang t???o") ) {          
           
            tabHoaDon.setSelectedIndex(1);
            txtTenKH2.setText(kh.getTenKH() );
            txtSDT2.setText(kh.getSDT());
            txtDiaChi.setText(kh.getDiaChi());
            if( hd.getTienShip() == 0 ){
                txtTienShip.setText("0");
            }else{
                txtTienShip.setText( HamDinhDang( String.valueOf(hd.getTienShip()) )  ) ;
            }
            
            System.out.println( hd.getTienShip() );
            lbKhachTra2.setText( HamDinhDang( String.valueOf( Double.valueOf( HamDinhDang2( lbTongTienHang2.getText() ) ) + hd.getTienShip() ))   );
            txtKhachDua2.setText( HamDinhDang(String.valueOf( Double.valueOf( HamDinhDang2( lbTongTienHang2.getText() ) ) + hd.getTienShip() ) )   );
            setSelectedComboboxHTTT( hd.getMaHTTT() , cbbHTThanhToan2 );
            txtGhiChu.setText( hd.getGhiChu() );
            
        } else {
            tabHoaDon.setSelectedIndex(0);
            txtTenKH.setText(kh.getTenKH());
            txtSDT.setText(kh.getSDT());
            setSelectedComboboxHTTT( hd.getMaHTTT() , cbbHTThanhToan );         
        }
        
    }

    private void tbDanhSachHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDanhSachHDMouseClicked
         HienThiNguoc();
    }//GEN-LAST:event_tbDanhSachHDMouseClicked

    private void txtKhachDuaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKhachDuaKeyReleased
        double bk =  Double.valueOf( HamDinhDang2( txtKhachDua.getText())  ) - Double.valueOf( HamDinhDang2(lbKhachTra.getText() )  ) ;
        if( bk == 0 ){
            lbTienThua.setText("0");
        }else{
            lbTienThua.setText( HamDinhDang( String.valueOf(bk)) );
        }
    }//GEN-LAST:event_txtKhachDuaKeyReleased

    private void txtTienShipKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTienShipKeyReleased
        // TODO add your handling code here:
        lbKhachTra2.setText( HamDinhDang( String.valueOf( Double.valueOf(txtTienShip.getText()) + TienKhachTra2  ))   );
    }//GEN-LAST:event_txtTienShipKeyReleased

    private void btnTaoHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHDActionPerformed
        if (MsgBox.comfirm(this, "B???n c?? mu???n t???o h??a ????n kh??ng ?") ) {
            ThemVaoHoaDon2("??ang t???o", "Not", 0);
            LamTrangForm();
            tbDanhSachHD.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_btnTaoHDActionPerformed

    private void txtKhachDua2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKhachDua2KeyReleased
        // TODO add your handling code here:
        double bk =  Double.valueOf( HamDinhDang2( txtKhachDua2.getText())  ) - Double.valueOf( HamDinhDang2(lbKhachTra2.getText() )  ) ;
        if( bk == 0 ){
            lbTienThua2.setText("0");
        }else{
            lbTienThua2.setText( HamDinhDang(  String.valueOf( bk)));
        }
    }//GEN-LAST:event_txtKhachDua2KeyReleased

    // Check s??? ??i???n tho???i 
    private void txtSDT2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSDT2KeyReleased
        // TODO add your handling code here:
        int kh = daoKH.selectByID_2( txtSDT2.getText() ) ;
        int count = 1 ; 
        
        for( int i=0 ; i<listHD.size() ; i++ ){
            if( kh == listHD.get(i).getMaKH() && listHD.get(i).getTrangThai().equalsIgnoreCase("????n h??ng ??m") ){
                count++ ;
            }
        }
     
        if( count > 2 ){
            MsgBox.alert( this , "Kh??ch h??ng n??y ???? nhi???u l???n h???y h??a ????n");
        }    
    }//GEN-LAST:event_txtSDT2KeyReleased

//    // H??m n??t h???y b??n ?????t h??ng 
    private void HuyDatHang(){
        int k = tbDanhSachHD.getSelectedRow();
        
        int sk = MsgBox.confirm_2( this , "M???i b???n ch???n h??nh th???c ho??n tr???", "Tr??? to??n ph???n" , "Tr??? m???t ph???n" ) ;
        if (k >= 0) {
            if (sk == 0 && MsgBox.comfirm(this, "B???n c?? mu???n tr??? kh??ng") == true) {
                String ghiChu = JOptionPane.showInputDialog(this, "Nh???p l?? do b???n mu???n tr??? h??a ????n", "H??? th???ng qu???n tr???", HEIGHT);

                for (int i = 0; i < tbGioHang.getRowCount(); i++) {
                    HamCongNguocSoLuong(i);
                }

                daoHD.update1(ghiChu, tbDanhSachHD.getValueAt(k, 1).toString());
                daoHD.update2("????n h??ng ??m", tbDanhSachHD.getValueAt(k, 1).toString());
                daoCTHD.update_tt(tbDanhSachHD.getValueAt(k, 1).toString() );
                LamTrangForm();
                DoVaoTableDanhSachHD();

                MsgBox.alert(this, "Tr??? th??nh c??ng");
            } else if (sk == 1 && MsgBox.comfirm(this, "B???n c?? mu???n tr??? kh??ng") == true) {
                btnTraHang.setVisible(true);
                NutDoiTra = 1;
            }
        }else{
            MsgBox.alert( this , "Vui l??ng ch???n h??a ????n mu???n ho??n tr???");
        }

    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        HuyDatHang();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Tr??? h??ng    
    // n??t tr??? h??ng
    private void btnTraHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraHangActionPerformed
        daoHD.update2( "???? giao h??ng", tbDanhSachHD.getValueAt( tbDanhSachHD.getSelectedRow() , 1).toString() );
//        HienThiNguoc();
        LamTrangForm();
        DoVaoTableDanhSachHD() ;      
        MsgBox.alert( this , "Tr??? h??ng th??nh c??ng");
        NutDoiTra = 0 ;
        btnTraHang.setVisible( false );
    }//GEN-LAST:event_btnTraHangActionPerformed

    private void tbGioHangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbGioHangKeyPressed

    }//GEN-LAST:event_tbGioHangKeyPressed

    private void txtTienShipFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienShipFocusLost
        // TODO add your handling code here:
        txtTienShip.setText( HamDinhDang( txtTienShip.getText()));
    }//GEN-LAST:event_txtTienShipFocusLost

    private void txtTienShipFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTienShipFocusGained
        txtTienShip.setText("");
        lbKhachTra2.setText(lbTongTienHang2.getText());
    }//GEN-LAST:event_txtTienShipFocusGained

    private void txtKhachDua2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKhachDua2FocusLost
        txtKhachDua2.setText( HamDinhDang( txtKhachDua2.getText()) );
    }//GEN-LAST:event_txtKhachDua2FocusLost

    private void txtKhachDua2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKhachDua2FocusGained
        txtKhachDua2.setText("");
        lbTienThua2.setText("0");
    }//GEN-LAST:event_txtKhachDua2FocusGained

    private void txtKhachDuaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKhachDuaFocusGained
        txtKhachDua.setText("");
        lbTienThua2.setText("0");
    }//GEN-LAST:event_txtKhachDuaFocusGained

    private void txtKhachDuaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKhachDuaFocusLost
        txtKhachDua.setText( HamDinhDang( txtKhachDua.getText()) );
    }//GEN-LAST:event_txtKhachDuaFocusLost

    private void cbbHTThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbHTThanhToanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbbHTThanhToanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaoHD;
    private javax.swing.JButton btnTraHang;
    private javax.swing.JButton btnXoa4;
    private javax.swing.JComboBox<String> cbbHTThanhToan;
    private javax.swing.JComboBox<String> cbbHTThanhToan2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton18;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JLabel lbKhachTra;
    private javax.swing.JLabel lbKhachTra2;
    private javax.swing.JLabel lbTienThua;
    private javax.swing.JLabel lbTienThua2;
    private javax.swing.JLabel lbTongTienHang;
    private javax.swing.JLabel lbTongTienHang2;
    private javax.swing.JTabbedPane tabHoaDon;
    private javax.swing.JTable tbDanhSachHD;
    private javax.swing.JTable tbDanhSachSP;
    private javax.swing.JTable tbGioHang;
    private javax.swing.JTextField txtChatLieu;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtKhachDua;
    private javax.swing.JTextField txtKhachDua2;
    private javax.swing.JTextField txtKichThuoc;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtMauSac;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtSDT2;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTenKH2;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTheLoai;
    private javax.swing.JTextField txtTienShip;
    // End of variables declaration//GEN-END:variables
}
