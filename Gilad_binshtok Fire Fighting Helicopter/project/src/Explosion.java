import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
public class Explosion {
    private MyPolygon[] poly;
    private double speed,w,h;
    private MyPoint center;
    private Color color;
    private int num=0;
    private Random r=new Random();
    private double[] dx=new double[300],dy=new double[300],v=new double[300];
    public Explosion(double speed,double w,double h,MyPoint center){
        this.center=center;
        this.w=w;
        this.h=h;
        this.speed=speed;
        this.color=Service.randomColor();
        this.poly=new MyPolygon[300];
        for(int i=0;i<300;i++){
            poly[i]=new MyPolygon(center, 4,6);
            poly[i].setFillColor(color);
        }
        setRandomDiractions();
    }
    private void setRandomDiractions(){
        for(int i=0;i<300;i++){
        v[i]=Math.random()*speed;
        dx[i]=Math.random()*2*v[i]-v[i];
        dy[i]=Math.sqrt(v[i]*v[i]-dx[i]*dx[i])*Math.pow(-1,r.nextInt(2)+1);
        }
    }
    public void doWhatYouHaveToDo(){
        for(int i=0;i<300;i++){
            poly[i].translate(dx[i],dy[i]);
        }
    }
    public void draw(Graphics2D g2){
        for(int i=0;i<300;i++)
            poly[i].drawMe(g2);
    }
    
}










