import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
public class Game {
    private Helicopter helicopter,helicopter2;
    private Land land;
    private Fire fire;
    private MyPoint3D eye,light,center,p1,p2;//usable points
    private Rectangle bounds;
    private boolean second; //is thire a second player
    private int w,h;
    private int wTop,hTop;
    private int difficulty=1;//game difficulty set by number of fire spots
    private MyPolygon poly;
    public Game(Rectangle bounds,Rectangle boundsTop, MyPoint3D eye, MyPoint3D light,int difficulty,boolean second,String name1,String name2){
        //updating variables
        w=bounds.width;
        h=bounds.height;
        this.second=second;
        wTop=boundsTop.width;
        hTop=boundsTop.height;
        center=new MyPoint3D(w/2,h/2+600,-1200);
        land = new Land(bounds, eye,center );
        //creating one or two helicopters
        helicopter=new Helicopter(bounds, eye, new MyPoint3D(w/2, h/2, -500),land.getLand(),name1);
        if(second){
            helicopter2=new Helicopter(bounds, eye, new MyPoint3D(w/2-200,h/2,-500),land.getLand(),name2);
            helicopter.translate(200,0,0);
            helicopter2.setColor(Color.blue);
        }
        Body3D l=land.getLand();
        fire=new Fire(bounds,l.maxX()-l.minX(),l.maxZ()-l.minZ(),eye,l,30,30,difficulty );
        //creating a top view background
        poly=new MyPolygon();
        poly.addPoint(new MyPoint(0,0));
        poly.addPoint(new MyPoint(0,hTop));
        poly.addPoint(new MyPoint(wTop,hTop));
        poly.addPoint(new MyPoint(wTop,0));
        poly.setDrawColor(Color.cyan);
        poly.setFillColor(Color.cyan);
        
    }
    //updating the programs
    public void doWhatYouHaveToDo(){
        //helicopter.autoMove(null);
        helicopter.doWhatYouHaveToDo();
        if(second)
            helicopter2.doWhatYouHaveToDo();
        fire.doWhatYouHaveToDo();
        //chacking if which drops intercrct with each helicopter
        if(helicopter.getB4()!=null){
            for(int i=0;i<30;i++)
                for(int t=0; t<30;t++){
                    Body3D f=fire.fireAt(i, t);
                    MyPoint3D b4=helicopter.getB4();
                    if(fire.bearningAt(i, t)){
                        boolean bool=false;//if the fire have been puted of yet
                        if(helicopter.isEmptying()){
                            for(int x=0;x<helicopter.getDrops().getLength();x++)
                                if(helicopter.getDrops().dropAt(x).intersect(f)){
                                    fire.putOf(i,t);
                                    helicopter.getDrops().placeOneDrop(x);
                                    bool=true;
                                }                     
                        }
                       if(second&&helicopter2.isEmptying()) {
                            for(int x=0;x<helicopter2.getDrops().getLength();x++)
                                if(helicopter2.getDrops().dropAt(x).intersect(f)){
                                    fire.putOf(i,t);
                                    helicopter2.getDrops().placeOneDrop(x);
                                    bool=true;
                                }                           
                       }
                            if(!bool)
                                fire.rebearn(i, t);                        
                    }
                }
        }
    }
    //drawing all the game
    public void draw(Graphics2D g2){
        //only one helicopter
        if(!second){
            helicopter.drawName(g2);
            if(land.getLand().minY()<helicopter.maxY()&&land.getLand().maxZ()>helicopter.maxZ()){
                helicopter.draw(g2);

                land.draw(g2);
                fire.draw(g2);
            }
            else{
                land.draw(g2);
                fire.drawBefore(g2,helicopter.maxZ());
                helicopter.draw(g2); 
                fire.drawAfter(g2,helicopter.maxZ());
            }
        }
        //two helicopters
        else{
            helicopter.drawName(g2);
            helicopter2.drawName(g2);
            Helicopter[] hel=helicopter.arryHelicopters(helicopter2);            
            helicopter.drawArry(g2, helicopter2);            
            double ly=land.getLand().minY(),hy=helicopter.maxY(),h2y=helicopter2.maxY();
            if(ly<hy&&ly<h2y){//both above land
                hel[1].drawBucket(g2);
                hel[0].drawBucket(g2);
                land.draw(g2);
                fire.draw(g2);
            }
            else{

                if(ly>=hy&&ly>=h2y){//both under land
                    land.draw(g2);

                    fire.drawBefore(g2, hel[1].maxZ());
                    hel[1].draw(g2);
                    fire.drawBetween(g2, hel[0].maxZ(),hel[1].maxZ());
                    hel[0].draw(g2);
                    fire.drawAfter(g2, hel[0].maxZ());
                }
                else{
                   if(ly<hy){
                        helicopter.drawBucket(g2);
                        land.draw(g2);
                        fire.draw(g2);            
                        helicopter2.drawBucket(g2);
                   }
                   else{
                        helicopter2.drawBucket(g2);
                        land.draw(g2);
                        fire.draw(g2);            
                        helicopter.drawBucket(g2);                       
                   }
                }
            }
        }
        
    }
    //drawing top view
    public void drawTop(Graphics2D g2Top){
        poly.drawMe(g2Top);
        p1=land.land.getCenter();
        p2=new MyPoint3D(p1);
        p2.x++;
        land.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
        fire.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
        helicopter.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
        if(second)
            helicopter2.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
    }
    //moving p1 helicopter to the sides
    public void up(){
        helicopter.up();
        if(second&&helicopter.intersect(helicopter2))
            helicopter.down();
    }
    public void down(){
        helicopter.down();
        if(second&&helicopter.intersect(helicopter2))
            helicopter.up();        
    }    
    public void left(){
        helicopter.left();
        if(second&&helicopter.intersect(helicopter2))
            helicopter.right();        
    }
    public void lift(){
        helicopter.lift();
    }
    public void drop(){
        helicopter.drop();
    }
    public void right(){
        helicopter.right();
        if(second&&helicopter.intersect(helicopter2))
            helicopter.left();    
    }
    //moving p1 helicopter to the sides    
    public void up2(){
        helicopter2.up();
        if(second&&helicopter2.intersect(helicopter))
            helicopter2.down();
    }
    public void down2(){
        helicopter2.down();
        if(second&&helicopter2.intersect(helicopter))
            helicopter2.up();        
    }    
    public void left2(){
        helicopter2.left();
        if(second&&helicopter2.intersect(helicopter))
            helicopter2.right();        
    }
    public void lift2(){
        helicopter2.lift();
    }
    public void drop2(){
        helicopter2.drop();
    }
    public void right2(){
        helicopter2.right();
        if(second&&helicopter2.intersect(helicopter))
            helicopter2.left();    
    } 
    //setters
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        fire.setDifficulty(difficulty);
    }
    
    public void setLight(MyPoint3D light) {
        this.light = light;
    }
    public void setHelicoptersColors(Color c1,Color c2){
        helicopter.setColor(c1);
        if(second)
            helicopter2.setColor(c2);
    }
    public void setLandColor(Color color){
        this.land.setColor(color);
    }    
    public void setSecond(boolean second){
        this.second=second;
    }
    //set helicpters names
    public void insertNames(String name1,String name2){
        System.out.println(name1+" Game");
        helicopter.setName(name1);
        if(second)
            helicopter2.setName(name2);
    }   
}
