import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
public class Fire {
   public Body3D[][]fire;
   public boolean[][] bearning;
   private int mone=0,difficulty=1,lx,lz;
   private Rectangle bounds;
   private MyPoint3D eye;
   
   private Random R;
   public Fire (Rectangle bounds,double w1,double h1, MyPoint3D eye,Body3D land,int lx,int lz,int difficulty){
       //updating the variables
       this.lx=lx;
       this.lz=lz;
       this.difficulty=difficulty;
       //creating the fire
       fire = new Body3D[lx][lz];
       bearning = new boolean[lx][lz];
               fire[0][0]=new Body3D(bounds, "Pyramid4.txt");           
               fire[0][0].setFillColor(Color.red);
               fire[0][0].setLight(null);
               fire[0][0].scale(w1/lx,w1/lx,h1/lz,new MyPoint3D());
               MyPoint3D p1= fire[0][0].getCenter(),p2=new MyPoint3D(p1);
               p2.x++;
               fire[0][0].rotate(-90, p1, p2);
               fire[0][0].setEye(eye);
               fire[0][0].setStyle(0);
               fire[0][0].setDrawColor(Color.red);
               fire[0][0].translate(land.minX()-fire[0][0].minX(),land.minY()-fire[0][0].maxY(),land.minZ()-fire[0][0].minZ());
   
          for(int i=0;i<lx;i++){
           for(int t=0; t<lz;t++){
               bearning[i][t]=false;
               fire[i][t]=new Body3D(fire[0][0]);
               fire[i][t].translate(i*w1/lx, 0, t*h1/lz);
               
           }
       }
       //choose which flames start bearning
       R=new Random();
       for(int i=0;i<difficulty;i++){
        int m=R.nextInt(lx),n=R.nextInt(lz);       
        bearning[m][n] =true;
        bearn(m,n-1);
        bearn(m+1,n);
        bearn(m,n+1);
        bearn(m-1,n);
       }  
   }
   //basic drawing method
   public void draw(Graphics2D g2){
        for(int i=0;i<lx;i++)
           for(int t=0; t<lz;t++)
               if(bearning[i][t]){
                   fire[i][t].draw(g2);
                  
               }  
   }
   //draw behind maxZ value
   public void drawBefore(Graphics2D g2,double maxZ){
        for(int i=0;i<lx;i++)
           for(int t=0; t<lz;t++)
               if(bearning[i][t]&&fire[i][t].maxZ()<=maxZ){
                   fire[i][t].draw(g2);
                  
               }  
   }
   //draw between two z valus
   public void drawBetween(Graphics2D g2,double z1,double z2){
        for(int i=0;i<lx;i++)
           for(int t=0; t<lz;t++)
               if(bearning[i][t]&&fire[i][t].maxZ()<=z1&&bearning[i][t]&&fire[i][t].maxZ()>z2){
                   fire[i][t].draw(g2);
                  
               }        
   }
   //draw after z valu
   public void drawAfter(Graphics2D g2,double maxZ){
        for(int i=0;i<lx;i++)
           for(int t=0; t<lz;t++)
               if(bearning[i][t]&&fire[i][t].maxZ()>maxZ){
                   fire[i][t].draw(g2);
                  
               }  
   } 
   //drawing top
  public void drawTop(Graphics2D g2Top, MyPoint3D p1, MyPoint3D p2,int w, int h, int wTop, int hTop) {
        for(int i=0;i<lx;i++)
           for(int t=0; t<lz;t++)
               if(bearning[i][t]){
                   fire[i][t].drawTop(g2Top, p1, p2, w, h, wTop, hTop);               
               }
 } 
  //updating program
 public void doWhatYouHaveToDo(){
     spread();
 }                  
           
   //spreading the fire in the fiald    
   public void spread(){
       for(int i=0;i<lx;i++){
           for(int t=0;t<lz;t++){
               if(bearning[i][t]){
                   int num=R.nextInt(4000);
                   //צדדים
                   if(num==0||num==1)
                       bearn(i,t-1);
                   if(num==5||num==6)
                       bearn(i+1,t); 
                   if(num==10||num==11)
                       bearn(i,t+1); 
                   if(num==15||num==16)
                       bearn(i-1,t);
                   //פינות
                   if(num==20)
                       bearn(i+1,t-1);
                   if(num==25)
                       bearn(i+1,t+1); 
                   if(num==30)
                       bearn(i-1,t+1);
                   if(num==35)
                       bearn(i-1,t-1);                   
                }
           }
       }
   }
   //putting of the fire slowly
   public void putOf(int a,int b){
       int i= fire[a][b].getFillColor().getRed();
       if(i<=100){
           bearning[a][b]=false;
           fire[a][b].setFillColor(Color.red);
           fire[a][b].setDrawColor(Color.red);
   
       }
       else{
            fire[a][b].setFillColor(new Color(i-30,0,0));
            fire[a][b].setDrawColor(new Color(i-30,0,0));
       } 
   }
   //geters
   public Body3D fireAt(int a,int b){
       return fire [a][b];
   }
   public boolean bearningAt(int i ,int t){
       return bearning[i][t];
   }
   //start bearning again after bein puted of
   public void rebearn(int a ,int b){
      int i=fire[a][b].getFillColor().getRed();
      if(i<255){
          fire[a][b].setFillColor(new Color(i+1,0,0));
          fire[a][b].setDrawColor(new Color(i+1,0,0));
    
      }
   }
   //light the flame if can
   private void bearn(int a,int b){
     if(a<lx&&a>=0&&b<lz&&b>=0)
         bearning[a][b]=true;
   }
   //seting how many fire spot to start
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    //checks if their is steel any fire
    public boolean isBearning(){
        for(int i=0;i<lx;i++)
          for(int t=0;t<lz;t++)
              if(bearning[i][t])
                  return true;
        return false;
    }
    //puting of fure faster and delaying fire
    public void superPutOf(int a , int b){
        if(bearning[a][b]){
          putOf(a, b);
          putOf(a, b); 
        }
        else{
           int i= fire[a][b].getFillColor().getRed();
           if(i>100){
            fire[a][b].setFillColor(new Color(i-10,0,0));
            fire[a][b].setDrawColor(new Color(i-10,0,0));               
           }
        }
    }
        
        }
   