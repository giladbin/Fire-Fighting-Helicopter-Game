/**
 * @author Memi Gutbir  16/07/2009
 */
import java.awt.Graphics2D;
import java.text.DecimalFormat;
/**
 * מחלקה שמייצגת נקודה בדיוק כפול
 * לצורך דיוק בחישובים בגרפיקה
 * ותוספות נחוצות אחרות
 */
public class MyPoint {
    // תכונות
    public double x;
    public double y;

    // פעולות בונות
    public MyPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // פעולה בונה מעתיקה
    public MyPoint(MyPoint p) {
        this.x = p.x;
        this.y = p.y;
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

    public void setY(double newY) {
        this.y = newY;
    }

    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("####0");
        String sx = formatter.format(x);
        String sy = formatter.format(y);
        return "<" + sx + "," + sy + ">";
    }

    /**
     * הפעולה מציירת הנקודה (פיקסל) על העצם הגרפי שהועבר כפרמטר
     */
    public void drawMe(Graphics2D g2) {
        g2.fillOval((int) x, (int) y,(int) x, (int) y );
    }
}
