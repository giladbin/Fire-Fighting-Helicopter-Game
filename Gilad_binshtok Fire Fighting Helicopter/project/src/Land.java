
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Land {
 public Body3D land,gasTank;
 //0 153 51
 
 private MyPoint3D center,eye,light;
 private double aw,ah,w,h;
 private Rectangle bounds;
 
 public Land(Rectangle bounds,MyPoint3D eye,MyPoint3D center){
        land=new Body3D(bounds, "Cube.txt");
        land.setEye(eye);
        land.setFillColor(new Color(0,100,0,200));
        //land.setDrawColor(Color.white);
        land.setLight(null);
        land.setStyle(0);
        land.scale(1400,40,1400,new MyPoint3D());
        Service.translate(land, land.getCenter(),center);
 }
 public void draw(Graphics2D g2){
    land.setStyle(2); 
    land.draw(g2);
    land.setStyle(0);
    land.draw(g2);
 }
 public void drawTop(Graphics2D g2Top, MyPoint3D p1, MyPoint3D p2,int w, int h, int wTop, int hTop) {

     land.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
 }


 public Body3D getLand() {
        return land;
 }
 public void setColor(Color color){
     this.land.setFillColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),200));
 }
 
}
