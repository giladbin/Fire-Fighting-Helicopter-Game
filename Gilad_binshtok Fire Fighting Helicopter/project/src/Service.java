
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * @author Memi Gutbir  11/07/2010
 */
public class Service {
/**
     *
     * @param g2
     * @param p1
     * @param p2
     * @param eye
     */
    //drawing line in prespective
    public static void line3D(Graphics2D g2,MyPoint3D p1,
            MyPoint3D p2,MyPoint3D eye){

        MyPoint3D q1=new MyPoint3D(p1);
        MyPoint3D q2=new MyPoint3D(p2);
        MyPoint3D qp1=onePointPerespective(q1,eye);
        MyPoint3D qp2=onePointPerespective(q2,eye);
        g2.drawLine((int)qp1.x,(int)qp1.y,(int)qp2.x,(int)qp2.y);

    }
    public static void line3D(Graphics2D g2,MyPoint3D p1,
            MyPoint3D p2,MyPoint3D eye,Color color){

        MyPoint3D q1=new MyPoint3D(p1);
        MyPoint3D q2=new MyPoint3D(p2);
        MyPoint3D qp1=onePointPerespective(q1,eye);
        MyPoint3D qp2=onePointPerespective(q2,eye);
        g2.setColor(color);
        g2.drawLine((int)qp1.x,(int)qp1.y,(int)qp2.x,(int)qp2.y);

    }
    //cleaning the panel
    public static void cleanPanel(JPanel pnl){
        Graphics2D g2=(Graphics2D)pnl.getGraphics();
        g2.setColor(pnl.getBackground());
        g2.fillRect(0,0,pnl.getWidth(),pnl.getHeight());
    }    
    /**
     *
     * @param q original point (kodkod)
     * @param eye the perespective center
     * @return a perespective copy pf point
     */
    public static MyPoint3D  onePointPerespective(MyPoint3D q,
            MyPoint3D eye){
        MyPoint3D qRet=new MyPoint3D(q); // copy it
    if (eye!=null){
           double u = -q.z / (eye.z - q.z);
            qRet.x = q.x + u * (eye.x - q.x);
            qRet.y = q.y + u * (eye.y -q.y);
    }
        return qRet;
    }
       public static double min(double a, double b) {
        if (a < b) {
            return a;
        }
        return b;
    }

    public static double max(double a, double b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    /**
     *
     * @param p1
     * @param p2
     * @param p3
     * @return
     */
    public static MyPoint3D normal(MyPoint3D p1, MyPoint3D p2,
            MyPoint3D p3) {
        MyPoint3D rc = new MyPoint3D();
        MyPoint3D v1 = new MyPoint3D();
        MyPoint3D v2 = new MyPoint3D();

        v1.x = p2.x - p1.x;
        v1.y = p2.y - p1.y;
        v1.z = p2.z - p1.z;

        v2.x = p3.x - p2.x;
        v2.y = p3.y - p2.y;
        v2.z = p3.z - p2.z;

        rc.x = v1.y * v2.z - v1.z * v2.y;
        rc.y = v1.z * v2.x - v1.x * v2.z;
        rc.z = v1.x * v2.y - v1.y * v2.x;

        return rc;
    }
    public static Color randomColor() {
        return new Color((int) (Math.random() * 255),
                (int) (Math.random() * 255),
                (int) (Math.random() * 255));
    }
    //normalize vectors 
    public static MyPoint3D normVec(MyPoint3D p1,MyPoint3D p2){
        MyPoint3D v=new MyPoint3D(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
        double length=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
        double x=v.x/length,y=v.y/length,z=v.z/length;
        return new MyPoint3D(x, y, z);
    }
    public static MyPoint3D normVec(MyPoint3D v){
        
        double length=Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
        double x=v.x/length,y=v.y/length,z=v.z/length;
        return new MyPoint3D(x, y, z);
    } 
    //dot product between two vectors
    public static double dotProduct(MyPoint3D v1,MyPoint3D v2){
        return v1.x*v2.x+v1.y*v2.y+v1.z*v2.z;
    }
    //cross product between two vectors
    public static MyPoint3D crossProduct(MyPoint3D v1,MyPoint3D v2){
        return new MyPoint3D(v1.y*v2.z- v1.z*v2.y,v1.z*v2.x-v1.x*v2.z,v1.x*v2.y-v1.y*v2.x);
    }
    //returns the length between two points
    public static double length(MyPoint3D p1,MyPoint3D p2){
        MyPoint3D p=new MyPoint3D(p1.x-p2.x,p1.y-p2.y,p1.z-p2.z);
        return Math.sqrt(p.x*p.x+p.y*p.y+p.z*p.z);
    }
    //translate body from p1 to p2
    public static void translate(Body3D body,MyPoint3D p1,MyPoint3D p2){
        body.translate(p2.x-p1.x,p2.y-p1.y,p2.z-p1.z);
    }
}
