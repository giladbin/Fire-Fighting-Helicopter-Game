import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;

public class View extends javax.swing.JFrame
{
    Toolkit tk=Toolkit.getDefaultToolkit();
    Dimension dimension=tk.getScreenSize();
    private String name1,name2;//player names
    private Model model; // the application model
    private BufferedImage offscreen;    // for double buffering
    private  Graphics2D g2;
    private BufferedImage offscreen2;    // for double buffering 12-2010 //===========//
    private  Graphics2D  g2Top;
    private Image bgImage;
    private boolean second=false;
    private Color color1=Color.gray,color2=Color.blue,color3=new Color(0,100,0,200);
    public View()  {
//        // קבע שגודל החלון הראשי יהיה כגודל המסך
        Toolkit  tk=Toolkit.getDefaultToolkit();
        Dimension dimension=tk.getScreenSize();
        setPreferredSize(dimension);
       initComponents();
        this.setPreferredSize(dimension);
        this.pnlCanvas.setSize(new Dimension((int) (dimension.width * 0.8), 
                      (int) (dimension.height * 0.80)));
        this.pnlCanvas.setPreferredSize(this.pnlCanvas.getSize());
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        this.setResizable(false);
        this.pack();              

        appInit(); //o דברים חד פעמיים ראשונים שעושים לפני תחילת ההרצה
        appStart(); //o  התחלת הישום
    }

    public void appInit()
    {
        //difult game setings
        radEasy.setSelected(true);
        radOne.setSelected(true);
        btnColor2.setBackground(color2);
        btnColor1.setBackground(color1);
        btnColor3.setBackground(color3);
        URL url = this.getClass().getClassLoader().getResource("sea.jpg");
        try { 
              this.bgImage = ImageIO.read(url); 
        } catch (IOException ex){}
        // Reference to the application model
        //o איתחול המודל        
        this.model = new Model(this.pnlCanvas.getBounds(),this.pnlTop.getBounds());

        // Create offscreen graphics image for double-buffering
        this.offscreen = new BufferedImage(this.pnlCanvas.getWidth(), this.pnlCanvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.offscreen2 = new BufferedImage(this.pnlTop.getWidth(), this.pnlTop.getHeight(), BufferedImage.TYPE_INT_RGB);
//        Ignor OS repaint callback No flickers        //o  למניעת היבהובים
 /////     this.setIgnoreRepaint(true);
        this.pnlCanvas.setIgnoreRepaint(true);
        this.setVisible(false);
        new Menu(this).setVisible(true);
    }

    public void appStart()    {
 //       model.setSolid(true);
          
        this.pnlCanvas.requestFocus();
         while (1<2)        {
            // request focuse to handle keyboard & mouse events
         this.pnlCanvas.requestFocus();//// === cancelled =====

            // Get offscreen graphics context
          g2 =  this.offscreen.createGraphics();
          g2Top =  this.offscreen2.createGraphics();
            // Make the graphics smooth
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(this.bgImage, 0, 0, this.pnlCanvas.getWidth(),  this.pnlCanvas.getHeight(), this); 

            //==============
            // game logic
            this.model.update();
            this.model.draw(g2);
            
            this.model.drawTop(g2Top);
            // Put offscreen graphics context on the panel canvas (double buffering)
            this.pnlCanvas.getGraphics().drawImage(offscreen, 0, 0, null);
            this.pnlTop.getGraphics().drawImage(offscreen2, 0, 0, null);
            // תו הזדמנות גם לתהליכים אחרים לעשות משהו         
            Thread.yield();     
        }
    }
    public static void main(String[] args)
    {
        new View();
    }
     
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlCanvas = new javax.swing.JPanel();
        pnlTop = new javax.swing.JPanel();
        btnStart = new javax.swing.JButton();
        radEasy = new javax.swing.JRadioButton();
        radAmateur = new javax.swing.JRadioButton();
        radRegular = new javax.swing.JRadioButton();
        radDifficult = new javax.swing.JRadioButton();
        radHard = new javax.swing.JRadioButton();
        lblDif = new javax.swing.JLabel();
        radOne = new javax.swing.JRadioButton();
        radTwo = new javax.swing.JRadioButton();
        btnColor1 = new javax.swing.JButton();
        btnColor3 = new javax.swing.JButton();
        btnColor2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gilad Binshtok");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(204, 204, 204));
        setForeground(new java.awt.Color(102, 255, 255));

        pnlCanvas.setBackground(new java.awt.Color(0, 0, 0));
        pnlCanvas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlCanvas.setName(""); // NOI18N
        pnlCanvas.setOpaque(false);
        pnlCanvas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlCanvasMousePressed(evt);
            }
        });
        pnlCanvas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pnlCanvasKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pnlCanvasKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnlCanvasLayout = new javax.swing.GroupLayout(pnlCanvas);
        pnlCanvas.setLayout(pnlCanvasLayout);
        pnlCanvasLayout.setHorizontalGroup(
            pnlCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 910, Short.MAX_VALUE)
        );
        pnlCanvasLayout.setVerticalGroup(
            pnlCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlTop.setBackground(new java.awt.Color(255, 255, 255));
        pnlTop.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 207, Short.MAX_VALUE)
        );

        btnStart.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnStart.setText("START");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        radEasy.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        radEasy.setText("EASY");
        radEasy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        radEasy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radEasyActionPerformed(evt);
            }
        });

        radAmateur.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        radAmateur.setText("AMATEUR");
        radAmateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radAmateurActionPerformed(evt);
            }
        });

        radRegular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        radRegular.setText("REGULAR");
        radRegular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radRegularActionPerformed(evt);
            }
        });

        radDifficult.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        radDifficult.setText("DIFFICULT");
        radDifficult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radDifficultActionPerformed(evt);
            }
        });

        radHard.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        radHard.setText("HARD");
        radHard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radHardActionPerformed(evt);
            }
        });

        lblDif.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDif.setText("Dificulty - ");
        lblDif.setAutoscrolls(true);

        radOne.setText("One Player");
        radOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radOneActionPerformed(evt);
            }
        });

        radTwo.setText("Tow Players");
        radTwo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radTwoActionPerformed(evt);
            }
        });

        btnColor1.setText("Chose First Player Color");
        btnColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColor1ActionPerformed(evt);
            }
        });

        btnColor3.setText("Chose Land Color");
        btnColor3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColor3ActionPerformed(evt);
            }
        });

        btnColor2.setText("Chose Second Player Color");
        btnColor2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColor2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlCanvas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(radAmateur, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(radRegular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(radDifficult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(radHard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(radEasy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(81, 81, 81))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblDif, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(radOne)
                                    .addComponent(radTwo)
                                    .addComponent(btnColor3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnColor2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnColor1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCanvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(lblDif, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radEasy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radAmateur)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radRegular)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radDifficult)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radHard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radOne)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radTwo)
                .addGap(18, 18, 18)
                .addComponent(btnColor1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnColor2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnColor3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        pnlCanvas.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //o לחצו על מקש, העבר הטיפול למודל
    private void pnlCanvasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlCanvasKeyPressed
        if (evt.getKeyCode()==KeyEvent.VK_ESCAPE){ // יציאה         
            System.exit(0);
        }
         if (evt.getKeyCode()==KeyEvent.VK_P)    // save Picture
             saveScreen(this.getBounds(),"Image3D", "PNG");
        //  אחרת העבר הטיפול
        this.model.keyInput(evt.getKeyCode(),true);
    }//GEN-LAST:event_pnlCanvasKeyPressed

    private void pnlCanvasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlCanvasMousePressed
 //         this.model.mouseInput(evt.getX(), evt.getY(), evt.getButton());
    }//GEN-LAST:event_pnlCanvasMousePressed

    private void pnlCanvasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pnlCanvasKeyReleased
  this.model.keyInput(evt.getKeyCode(),false);  
    }//GEN-LAST:event_pnlCanvasKeyReleased

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
       //start game
        this.model.setNames(name1, name2);
        this.model.setColors(color1, color2, color3);
        this.model.init(second); 
        pnlCanvas.requestFocus();
    }//GEN-LAST:event_btnStartActionPerformed
    //seting dificulties for different radio buttens
    private void radEasyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radEasyActionPerformed
        this.model.setDifficulty(1);
        radEasy.setSelected(true);
        radAmateur.setSelected(false);
        radRegular.setSelected(false);
        radDifficult.setSelected(false);
        radHard.setSelected(false);
    }//GEN-LAST:event_radEasyActionPerformed

    private void radAmateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radAmateurActionPerformed
        this.model.setDifficulty(2);
        radAmateur.setSelected(true);
        radEasy.setSelected(false);
        radRegular.setSelected(false);
        radDifficult.setSelected(false);
        radHard.setSelected(false);        
    }//GEN-LAST:event_radAmateurActionPerformed

    private void radRegularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radRegularActionPerformed
        this.model.setDifficulty(3);
        radRegular.setSelected(true);
        radAmateur.setSelected(false);
        radEasy.setSelected(false);
        radDifficult.setSelected(false);
        radHard.setSelected(false);        
    }//GEN-LAST:event_radRegularActionPerformed

    private void radDifficultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radDifficultActionPerformed
        this.model.setDifficulty(4);
        radDifficult.setSelected(true);
        radAmateur.setSelected(false);
        radRegular.setSelected(false);
        radEasy.setSelected(false);
        radHard.setSelected(false);        
    }//GEN-LAST:event_radDifficultActionPerformed

    private void radHardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radHardActionPerformed
        this.model.setDifficulty(5);
        radHard.setSelected(true);
        radAmateur.setSelected(false);
        radRegular.setSelected(false);
        radDifficult.setSelected(false);
        radEasy.setSelected(false);        
    }//GEN-LAST:event_radHardActionPerformed
    //sets one or two players by radio buttens valus
    private void radOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radOneActionPerformed
        this.second=false;
        radOne.setSelected(true);
        radTwo.setSelected(false);
    }//GEN-LAST:event_radOneActionPerformed

    private void radTwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radTwoActionPerformed
        this.second=true;
        radOne.setSelected(false);
        radTwo.setSelected(true);        
    }//GEN-LAST:event_radTwoActionPerformed

    private void btnColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColor1ActionPerformed
   //sets the p1 helicopter color
   JColorChooser clr = new JColorChooser();
   color1 = clr.showDialog(pnlCanvas, "Choose Color", Color.white);
   btnColor1.setBackground(color1);
    }//GEN-LAST:event_btnColor1ActionPerformed

    private void btnColor2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColor2ActionPerformed
   //sets the p1 helicopter color 
   JColorChooser clr = new JColorChooser();
   color2 = clr.showDialog(pnlCanvas, "Choose Color", Color.white);
   btnColor2.setBackground(color2);
    }//GEN-LAST:event_btnColor2ActionPerformed

    private void btnColor3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColor3ActionPerformed
   //sets the land color 
   JColorChooser clr = new JColorChooser();
   color3 = clr.showDialog(pnlCanvas, "Choose Color", Color.white);
   btnColor3.setBackground(color3);
    }//GEN-LAST:event_btnColor3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColor1;
    private javax.swing.JButton btnColor2;
    private javax.swing.JButton btnColor3;
    private javax.swing.JButton btnStart;
    private javax.swing.JLabel lblDif;
    private javax.swing.JPanel pnlCanvas;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JRadioButton radAmateur;
    private javax.swing.JRadioButton radDifficult;
    private javax.swing.JRadioButton radEasy;
    private javax.swing.JRadioButton radHard;
    private javax.swing.JRadioButton radOne;
    private javax.swing.JRadioButton radRegular;
    private javax.swing.JRadioButton radTwo;
    // End of variables declaration//GEN-END:variables

    public void saveScreen(java.awt.Rectangle rect, String fileName, String fileType) {
        java.awt.Robot rob = null;  // of class Robot
        try {
            rob = new java.awt.Robot();
        } catch (java.awt.AWTException ex) {
            System.out.println("Problem saving image file");
        }
        //=======  capture the whole jFrame  : ==========
        //   תפוס לאימג' מה שנכלל במלבן
        java.awt.image.BufferedImage im = rob.createScreenCapture(rect);
        // שמור הקובץ
        java.io.File outputFile = new java.io.File(fileName + "." + fileType);
        try {
            javax.imageio.ImageIO.write(im, fileType, outputFile);
        } catch (java.io.IOException ex) {
            System.out.println("Problem saving image file");
        }
    }
    public void setNames(String name1,String name2){
        
 
        this.name1=name1;
        this.name2=name2;
    }
}