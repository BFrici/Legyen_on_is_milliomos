/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.DBModel;
import model.IModel;
import model.Kerdes;

/**
 *
 * @author Frigyes
 */
public class Foablak extends javax.swing.JFrame {

    private IModel model;
    private List<Kerdes> kerdesek;
    Random ran;
    Kerdes aktualis;
    int kerdesIndex;
    boolean felezett; // az adott kérdésen belül feleztek-e
    private Set<Integer> felezo;

    public void exitProc() {
        if (model != null) {
            try {
                model.close();
                System.out.println("Model lezárva");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ujJatek() {
        btnKozonseg.setEnabled(true);
        btnTelefon.setEnabled(true);
        btnFelezo.setEnabled(true);
        btnKozonseg.setSelected(false);
        btnTelefon.setSelected(false);
        btnFelezo.setSelected(false);
        btnFelezo.setBackground(Color.LIGHT_GRAY);
        btnKozonseg.setBackground(Color.LIGHT_GRAY);
        btnTelefon.setBackground(Color.LIGHT_GRAY);
        jlNyeremenySzint.setSelectedIndex(14);
        kerdesek = new ArrayList<>();
        try {
            kerdesek = model.getAllKerdesek();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
        }
        kerdestAd();
    }

    private void kerdestAd() {
        felezett = false;
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn4.setEnabled(true);
        btn1.setBackground(Color.LIGHT_GRAY);
        btn2.setBackground(Color.LIGHT_GRAY);
        btn3.setBackground(Color.LIGHT_GRAY);
        btn4.setBackground(Color.LIGHT_GRAY);
        kerdesIndex = ran.nextInt(kerdesek.size());
        aktualis = kerdesek.get(kerdesIndex);

        jlKerdes.setText(aktualis.getKerdes());

        btn1.setText(kerdesek.get(kerdesIndex).getValasz0());
        btn2.setText(kerdesek.get(kerdesIndex).getValasz1());
        btn3.setText(kerdesek.get(kerdesIndex).getValasz2());
        btn4.setText(kerdesek.get(kerdesIndex).getValasz3());
    }

    private void helyes(int gomb) {
        if (aktualis.getHelyesValasz() == gomb) {
            kerdesek.remove(kerdesIndex);
            if (jlNyeremenySzint.getSelectedIndex() == 0) {
                int valasz = JOptionPane.showConfirmDialog(rootPane, "A válasz helyes! Gratulálunk, Ön megnyerte a főnyereményt! Új játék?", "Jackpot", JOptionPane.YES_NO_OPTION);
                if (valasz == JOptionPane.YES_OPTION) {
                    ujJatek();
                } else {
                    exitProc();
                    System.exit(0);
                }
            } else {
                jlNyeremenySzint.setSelectedIndex(jlNyeremenySzint.getSelectedIndex() - 1);
                kerdestAd();
            }
        } else {
            String nyeremeny = "";
            if (jlNyeremenySzint.getSelectedIndex() < 5) {
                jlNyeremenySzint.setSelectedIndex(5);
                nyeremeny = jlNyeremenySzint.getSelectedValue();
            } else if (jlNyeremenySzint.getSelectedIndex() < 10) {
                jlNyeremenySzint.setSelectedIndex(10);
                nyeremeny = jlNyeremenySzint.getSelectedValue();
            } else {
                jlNyeremenySzint.setSelectedIndex(14);
                nyeremeny = "0 Ft";
            }
            if (aktualis.getHelyesValasz() == 0) {
                btn1.setBackground(Color.GREEN);
            } else if (aktualis.getHelyesValasz() == 1) {
                btn2.setBackground(Color.GREEN);
            } else if (aktualis.getHelyesValasz() == 2) {
                btn3.setBackground(Color.GREEN);
            } else {
                btn4.setBackground(Color.GREEN);
            }
            if (gomb == 0) {
                btn1.setBackground(Color.red);
            } else if (gomb == 1) {
                btn2.setBackground(Color.red);
            } else if (gomb == 2) {
                btn3.setBackground(Color.red);
            } else {
                btn4.setBackground(Color.red);
            }
            int valasz = JOptionPane.showConfirmDialog(rootPane, "A válasz helytelen! A nyeremény: " + nyeremeny + " Új játék?", "Helytelen", JOptionPane.YES_NO_OPTION);
            jlNyeremenySzint.setSelectedIndex(14);
            if (valasz == JOptionPane.YES_OPTION) {
                ujJatek();
            } else {
                exitProc();
                System.exit(0);
            }
        }
    }

    public Foablak() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Legyen Ön is milliomos!");
        ran = new Random();
        jlNyeremenySzint.setSelectedIndex(14);
        felezett = false;
        felezo = new HashSet<>();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitProc();
                dispose();
                System.exit(0);
            }
        });

        String connURL = "jdbc:mysql://db4free.net:3306/bfrici_db";
        String user = "bfrici88";
        String pass = "88888888";

        try {
            Connection conn = DriverManager.getConnection(connURL, user, pass);
            model = new DBModel(conn);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        try {
            kerdesek = model.getAllKerdesek();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex, "Adatbázis hiba", JOptionPane.ERROR_MESSAGE);
        }

        kerdestAd();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jlNyeremenySzint = new javax.swing.JList<>();
        jlKerdes = new javax.swing.JLabel();
        btn2 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btnMegall = new javax.swing.JButton();
        btnKozonseg = new javax.swing.JToggleButton();
        btnTelefon = new javax.swing.JToggleButton();
        btnFelezo = new javax.swing.JToggleButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuUj = new javax.swing.JMenuItem();
        menuKilep = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuKerdesek = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jlNyeremenySzint.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jlNyeremenySzint.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "40000000 Ft", "20000000 Ft", "10000000 Ft", "5000000 Ft", "3000000 Ft", "1500000 Ft", "800000 Ft", "500000 Ft", "300000 Ft", "200000 Ft", "100000 Ft", "50000 Ft", "25000 Ft", "10000 Ft", "5000 Ft" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jlNyeremenySzint.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jlNyeremenySzint.setEnabled(false);
        jlNyeremenySzint.setSelectionBackground(new java.awt.Color(255, 0, 0));
        jScrollPane1.setViewportView(jlNyeremenySzint);

        jlKerdes.setBackground(new java.awt.Color(255, 255, 255));
        jlKerdes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jlKerdes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        btnMegall.setText("Megállok");
        btnMegall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMegallActionPerformed(evt);
            }
        });

        btnKozonseg.setText("Közönség");
        btnKozonseg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKozonsegActionPerformed(evt);
            }
        });

        btnTelefon.setText("Telefon");
        btnTelefon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTelefonActionPerformed(evt);
            }
        });

        btnFelezo.setText("Felező");
        btnFelezo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFelezoActionPerformed(evt);
            }
        });

        jMenu1.setText("Játék");

        menuUj.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuUj.setText("Új játék");
        menuUj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUjActionPerformed(evt);
            }
        });
        jMenu1.add(menuUj);

        menuKilep.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menuKilep.setText("Kilépés");
        menuKilep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKilepActionPerformed(evt);
            }
        });
        jMenu1.add(menuKilep);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Kérdések");

        menuKerdesek.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.SHIFT_MASK));
        menuKerdesek.setText("Kérdésszerkesztő");
        menuKerdesek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuKerdesekActionPerformed(evt);
            }
        });
        jMenu2.add(menuKerdesek);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlKerdes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(btnTelefon)
                                            .addComponent(btnFelezo)
                                            .addComponent(btnKozonseg, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnMegall)
                                        .addGap(301, 301, 301)))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 25, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn1, btn3, btn4});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnFelezo, btnKozonseg, btnMegall, btnTelefon});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnKozonseg)
                        .addGap(27, 27, 27)
                        .addComponent(btnTelefon)
                        .addGap(27, 27, 27)
                        .addComponent(btnFelezo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMegall)
                        .addGap(35, 35, 35)))
                .addComponent(jlKerdes, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        helyes(0);
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        helyes(1);
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        helyes(2);
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        helyes(3);
    }//GEN-LAST:event_btn4ActionPerformed

    private void menuUjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUjActionPerformed
        int valasz = JOptionPane.showConfirmDialog(rootPane, "Biztos újra kezdi?", "Új játék", JOptionPane.YES_NO_OPTION);
        if (valasz == JOptionPane.YES_OPTION) {
            ujJatek();
        }
    }//GEN-LAST:event_menuUjActionPerformed

    private void menuKilepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKilepActionPerformed
        int valasz = JOptionPane.showConfirmDialog(rootPane, "Biztosan kilép?", "Kilépés", JOptionPane.YES_NO_OPTION);
        if (valasz == JOptionPane.YES_OPTION) {
            exitProc();
            System.exit(0);
        }
    }//GEN-LAST:event_menuKilepActionPerformed

    private void menuKerdesekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuKerdesekActionPerformed
        KerdesekJelszo kj = new KerdesekJelszo(this, rootPaneCheckingEnabled, model);
        kj.setVisible(true);
    }//GEN-LAST:event_menuKerdesekActionPerformed

    private void btnMegallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMegallActionPerformed
        int valasz = JOptionPane.showConfirmDialog(rootPane, "Biztosan meg szeretne állni?", "Megáll?", JOptionPane.YES_NO_OPTION);
        if (valasz == JOptionPane.YES_OPTION) {
            String nyeremeny;
            if (jlNyeremenySzint.getSelectedIndex() == 14) {
                nyeremeny = "0 Ft";
            } else {
                jlNyeremenySzint.setSelectedIndex(jlNyeremenySzint.getSelectedIndex() + 1);
                nyeremeny = jlNyeremenySzint.getSelectedValue();
            }
            int valasz2 = JOptionPane.showConfirmDialog(rootPane, "Gratulálunk, az ön nyereménye: " + nyeremeny + ". Új játék?", "Játék vége", JOptionPane.YES_NO_OPTION);
            if (valasz2 == JOptionPane.YES_OPTION) {
                ujJatek();
            } else {
                exitProc();
                System.exit(0);
            }
        }
    }//GEN-LAST:event_btnMegallActionPerformed

    private void btnKozonsegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKozonsegActionPerformed
        btnKozonseg.setEnabled(false);
        List<Integer> kozonseg = new ArrayList<>(); // minimum 40 % a helyes válaszra
        Map<Integer, Integer> szazalek = new HashMap<>();
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;

        if (!felezett) {
            for (int i = 0; i < 40; i++) {
                kozonseg.add(aktualis.getHelyesValasz());
            }
            for (int i = 0; i < 60; i++) {
                kozonseg.add(ran.nextInt(4));
            }

            for (Integer k : kozonseg) {
                switch (k) {
                    case 0:
                        a++;
                        break;
                    case 1:
                        b++;
                        break;
                    case 2:
                        c++;
                        break;
                    case 3:
                        d++;
                        break;
                }
            }
            szazalek.put(0, a);
            szazalek.put(1, b);
            szazalek.put(2, c);
            szazalek.put(3, d);
        } else {
            for (int i = 0; i < 60; i++) {
                kozonseg.add(ran.nextInt(2));
            }
            int r1 = 0;
            int r2 = 0;
            for (Integer k : kozonseg) {
                if (k == 0) {
                    r1++;
                } else {
                    r2++;
                }
            }
            szazalek.put(0, 0);
            szazalek.put(1, 0);
            szazalek.put(2, 0);
            szazalek.put(3, 0);
            szazalek.put(aktualis.getHelyesValasz(), r2 + 40);

            if (felezo.contains(0) && aktualis.getHelyesValasz() != 0) {
                szazalek.put(0, r1);
            } else if (felezo.contains(1) && aktualis.getHelyesValasz() != 1) {
                szazalek.put(1, r1);
            } else if (felezo.contains(2) && aktualis.getHelyesValasz() != 2) {
                szazalek.put(2, r1);
            } else {
                szazalek.put(3, r1);
            }
        }

        Segitseg help = new Segitseg(this, true, szazalek);

        help.setTitle("Közönség segítség");
        help.setVisible(true);

    }//GEN-LAST:event_btnKozonsegActionPerformed

    private void btnFelezoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFelezoActionPerformed
        btnFelezo.setEnabled(false);
        felezett = true;

        felezo.add(aktualis.getHelyesValasz());
        while (felezo.size() < 2) {
            felezo.add(ran.nextInt(4));
        }
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        btn3.setEnabled(false);
        btn4.setEnabled(false);
        if (felezo.contains(0)) {
            btn1.setEnabled(true);
        }
        if (felezo.contains(1)) {
            btn2.setEnabled(true);
        }
        if (felezo.contains(2)) {
            btn3.setEnabled(true);
        }
        if (felezo.contains(3)) {
            btn4.setEnabled(true);
        }
    }//GEN-LAST:event_btnFelezoActionPerformed

    private void btnTelefonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTelefonActionPerformed
        btnTelefon.setEnabled(false);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        List<Integer> telefon = new ArrayList<>(); // minimum 40 % a helyes válaszra
        Map<Integer, Integer> szazalek = new HashMap<>();

        if (!felezett) {
            for (int i = 0; i < 40; i++) {
                telefon.add(aktualis.getHelyesValasz());
            }
            for (int i = 0; i < 60; i++) {
                telefon.add(ran.nextInt(4));
            }

            for (Integer t : telefon) {
                switch (t) {
                    case 0:
                        a++;
                        break;
                    case 1:
                        b++;
                        break;
                    case 2:
                        c++;
                        break;
                    case 3:
                        d++;
                        break;
                }
            }
            szazalek.put(0, a);
            szazalek.put(1, b);
            szazalek.put(2, c);
            szazalek.put(3, d);
        } else {
            for (int i = 0; i < 60; i++) {
                telefon.add(ran.nextInt(2));
            }
            int r1 = 0;
            int r2 = 0;
            for (Integer t : telefon) {
                if (t == 0) {
                    r1++;
                } else {
                    r2++;
                }
            }
            szazalek.put(0, 0);
            szazalek.put(1, 0);
            szazalek.put(2, 0);
            szazalek.put(3, 0);
            szazalek.put(aktualis.getHelyesValasz(), r2 + 40);

            if (felezo.contains(0) && aktualis.getHelyesValasz() != 0) {
                szazalek.put(0, r1);
            } else if (felezo.contains(1) && aktualis.getHelyesValasz() != 1) {
                szazalek.put(1, r1);
            } else if (felezo.contains(2) && aktualis.getHelyesValasz() != 2) {
                szazalek.put(2, r1);
            } else {
                szazalek.put(3, r1);
            }
        }

        Segitseg help = new Segitseg(this, true, szazalek);

        help.setTitle("Telefonos segítség");
        help.setVisible(true);


    }//GEN-LAST:event_btnTelefonActionPerformed

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
            java.util.logging.Logger.getLogger(Foablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Foablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Foablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Foablak.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Foablak().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JToggleButton btnFelezo;
    private javax.swing.JToggleButton btnKozonseg;
    private javax.swing.JButton btnMegall;
    private javax.swing.JToggleButton btnTelefon;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlKerdes;
    private javax.swing.JList<String> jlNyeremenySzint;
    private javax.swing.JMenuItem menuKerdesek;
    private javax.swing.JMenuItem menuKilep;
    private javax.swing.JMenuItem menuUj;
    // End of variables declaration//GEN-END:variables
}
