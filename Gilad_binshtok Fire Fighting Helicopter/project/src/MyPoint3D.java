
import java.awt.Graphics2D;
import java.text.DecimalFormat;

public class MyPoint3D {
    // תכונות
    public   double x,y,z;  // NOT PRIVATE !!!!

    // פעולות בונות
    public  MyPoint3D(double x, double y,double z) {
        this.x = x;
        this.y = y;
        this.z=z;
    }
  public  MyPoint3D() {
        this.x = 0;
        this.y = 0;
        this.z=0;
    }

    // פעולה בונה מעתיקה
    public MyPoint3D(MyPoint3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z=p.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("####0.00");
        String sx = formatter.format(x);
        String sy = formatter.format(y);
        String sz = formatter.format(z);
        return "<" + sx + "," + sy + ","+sz+">";
    }

    /**
     * הפעולה מציירת הנקודה (פיקסל) על העצם הגרפי שהועבר כפרמטר
     */
    public void draw(Graphics2D g2) {
        g2.fillOval((int) x, (int) y,(int) x, (int) y );
    }
}


