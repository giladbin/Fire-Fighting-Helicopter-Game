import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;
public class Drops {
    private MyPoint3D center;//drops circle center
    private double radius;//circle
    private Rectangle bounds;//pnl bounderies
    private MyPoint3D eye;//point of view
    private Body3D[] drop;
    private double[] speedY;//droping speed
    private int length;//how many drops
    private Random R;
    public Drops(Rectangle bounds, MyPoint3D eye, MyPoint3D center, double radius,int length ,double speed){
        R=new Random();
        //updating the variables
        this.eye=eye;
        this.length=length;
        this.bounds=bounds;
        this.center=center;
        this.radius=radius;
        //creating the drops
        this.drop=new Body3D[length];
        //basic drop
        drop[0]=new Body3D(bounds, "JavaPyramid3.txt");
        drop[0].scale(4,5,4,new MyPoint3D());
        drop[0].setFillColor(Color.cyan);
        drop[0].setDrawColor(Color.cyan);
        drop[0].setLight(null);
        drop[0].setStyle(0);
        drop[0].setEye(eye);
        Service.translate(drop[0],drop[0].getCenter(),center);
        drop[0].translate(0,15,0);
        
        this.speedY=new double[length]; 
        speedY[0]=Math.random()*(speed-1)+1;
        MyPoint3D p1=center,p2=new MyPoint3D(p1);
        p2.y++;
        for(int i=1; i<length; i++){
           drop[i]=new Body3D(drop[0]);
           speedY[i]=Math.random()*speed/2+speed;
           double dx=Math.random()*2*(radius-3)-(radius-3);
           drop[i].translate(dx,0,0);
           drop[i].rotate(Math.random()*180, p1, p2);
        }
    }
    //drawing the drops if nesesary
    public void draw(Graphics2D g2,Body3D boucket){        
        for(int i=1;i<length;i++){
            if(drop[i]!=null&&!drop[i].intersect(boucket))
                drop[i].draw(g2);    
        }    
    }
    //translating all the drops
    public void translate(double tx,double ty,double tz){
        for(int i=0;i<length;i++){
            if(drop[i]!=null)
                drop[i].translate(tx, ty, tz);
        }
    }
    //geting the drops circle center
    public MyPoint3D getCenter(){
        return drop[0].getCenter();
    }
    //droping the drops eccept of the basic one which rimanes a landing mark
    public void empty(double maxY){
        for(int i=1;i<length;i++){
                if(drop[i].maxY()>=maxY)
                    placeOneDrop(i);
                drop[i].translate(0,speedY[i],0);
        }
    }
    //placing one drop back in the circle
    public void placeOneDrop(int i){
        Service.translate(drop[i],drop[i].getCenter(),getCenter());
        double dx=Math.random()*2*(radius-3)-(radius-3);
        MyPoint3D p1=drop[0].getCenter(),p2=new MyPoint3D(p1);
        p2.y++;
        drop[i].translate(dx,0,0);
        drop[i].rotate(Math.random()*180, p1, p2);
    } 
    //placing all the drops back in the circle
    public void reMake(){
        for(int i=1;i<length;i++){
            Service.translate(drop[i],drop[i].getCenter(),getCenter());
            double dx=Math.random()*2*(radius-3)-(radius-3);
            double dz=Math.random()*2*(radius-3)-(radius-3);
            drop[i].translate(dx, 0, dz);            
        }
    }
    //geters
    public int getLength() {
        return length;
    }
    public Body3D dropAt(int i){
        return drop[i];
    }
}
