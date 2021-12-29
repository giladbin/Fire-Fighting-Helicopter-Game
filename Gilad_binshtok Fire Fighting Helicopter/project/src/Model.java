// MODEL : 9-2010  3D-BODIES  Memi
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class Model {
    private Game game;
    private int w, h,hTop,wTop,difficulty=1;  //  ממדי הפאנל
    private Timer timer;
    private String name1,name2;//players names
    // keyboard status  מערך מקשים לחוצים
    private boolean keyStatus[] = new boolean[65535];
    private boolean inited=false;
    private MyPoint3D eye; // מרכז הפרספקטיבה
    private Rectangle boundsTop;//top view size
    private Rectangle bounds;//main panel size
    private boolean second=false;//is their an other player?
    private Color color1=Color.gray,color2=Color.blue,color3=new Color(0,100,200);//
    public Model(Rectangle bounds,Rectangle boundsTop) {
        this.timer = new Timer(10);
        this.w = (int) bounds.getWidth();
        this.h = (int) bounds.getHeight();
        this.wTop = (int) boundsTop.getWidth();
        this.hTop = (int) boundsTop.getHeight();
        this.eye = new MyPoint3D(w/2, h / 3, 800); // קבע מרכז פרספקט.
   
      this.bounds=bounds;
      this.boundsTop=boundsTop;
       // init();  // פעולות שיש לעשות באיתחול במודל
    }
    //updating the programs
    public void update() {
        if (this.timer.isTimeToUpdate()&&inited) {
              timer.setLastTimeUpdated();
            //========== טיפול במקשים שבינתיים אולי נלחצו
          // חייב להיות כאן למניעת הבהובי
           //keys for one player
              if(!second){
                if(keyStatus[KeyEvent.VK_RIGHT]){
                    game.right();
                }
                if(keyStatus[KeyEvent.VK_LEFT]){
                    game.left();
                }              
                if(keyStatus[KeyEvent.VK_UP]){
                    game.up();
                }
                if(keyStatus[KeyEvent.VK_DOWN]){
                    game.down();
                } 
                if(keyStatus[KeyEvent.VK_1]){
                    game.drop(); 
                }
                if(keyStatus[KeyEvent.VK_2]){
                    game.lift(); 
                }
            }
              //keys for two players
            else{
                if(keyStatus[KeyEvent.VK_RIGHT]){
                    game.right();
                }
                if(keyStatus[KeyEvent.VK_LEFT]){
                    game.left();
                }              
                if(keyStatus[KeyEvent.VK_UP]){
                    game.up();
                }
                if(keyStatus[KeyEvent.VK_DOWN]){
                    game.down();
                } 
                if(keyStatus[KeyEvent.VK_N]){
                    game.drop(); 
                }
                if(keyStatus[KeyEvent.VK_M]){
                    game.lift(); 
                } 
                if(keyStatus[KeyEvent.VK_D]){
                    game.right2();
                }
                if(keyStatus[KeyEvent.VK_A]){
                    game.left2();
                }              
                if(keyStatus[KeyEvent.VK_W]){
                    game.up2();
                }
                if(keyStatus[KeyEvent.VK_S]){
                    game.down2();
                } 
                if(keyStatus[KeyEvent.VK_C]){
                    game.drop2(); 
                }
                if(keyStatus[KeyEvent.VK_V]){
                    game.lift2(); 
                }                
            }
              //updating the games
              game.doWhatYouHaveToDo();
    }}
    
    //drawing the project
    public void draw(Graphics2D g) {
        //============= REKA ======
        g.setColor(Color.white);
;
        if(inited)
            game.draw( g);
    }
    public     void drawTop(Graphics2D gTop) {
        // ===  draw from top on second panel
        if(inited)
         game.drawTop(gTop);
            }    
    //o טיפול בלחיצת מקש על הפאנל
    public void keyInput(int key, boolean pressed) {
        // מעדכן מערך לחיצות הכפתורים
        keyStatus[key] = pressed;  // true or false
    }

    // handling mouse events
    public void mouseInput(double x, double y, int button) {  
    }
    public void init(boolean second){
        //creating the game
        game=new Game(bounds,boundsTop, eye, null,difficulty,second,name1,name2);
        game.setDifficulty(difficulty);
        this.second=second;
        game.setHelicoptersColors(color1, color2);
        game.setLandColor(color3);
       inited=true;
    }
    //seters
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public void setNames(String name1,String name2){
        this.name1=name1;
        this.name2=name2;
    }
    public void setColors(Color color1,Color color2,Color color3){
        this.color1=color1;    
        this.color2=color2;
        this.color3=color3;
    }
    }








