
import java.awt.*;
import java.awt.geom.Area;
/* @author  Memi  Gutbir   25-3-2011*/   // gradient

class MyPolygon {
    // תכונות הפוליגון

    private static final int MAX_POINTS = 100;// maximum
    private MyPoint[] points;//   מערך עם מקום לקודקודים
    private int numPoints;//  מספר הקודקודים בפועל
    private Color fillColor;
    private Color drawColor;
    private boolean isFilled;// האם פוליגון מלא או ריק
    protected double dx = 0, dy = 0;  // כמות תזוזה אוטומטית

    public MyPolygon() { // פעולה בונה
        //  איתחול מערך ההפניות לקודקודים
        // כל ההפניות הן בינתיים
        // null
        this.points = new MyPoint[MAX_POINTS];
        this.numPoints = 0;// בינתיים אין קודקודים;
        this.fillColor = new Color(150, 40, 40, 200);
        this.drawColor = Color.BLACK;
        this.isFilled = true;
        this.fillColor = Color.green;
    }

    /**
     * o  פעולה בונה פוליגון משוכלל
     */
    public MyPolygon(MyPoint center, double radius, int numPoints) {
        this(); // מזמן הפעולה הבונה הקודמת
        for (int i = 0; i < numPoints; i++) {
            double x = center.getX() + radius * Math.cos(i * 2 * Math.PI / numPoints);
            double y = center.getY() + radius * Math.sin(i * 2 * Math.PI / numPoints);
            addPoint(new MyPoint(x, y));
        }
    }
    // copy constructor  פעולה בונה מעתיקה

    public MyPolygon(MyPolygon mp) {
        this.numPoints = mp.numPoints;
        this.points = new MyPoint[MAX_POINTS];
        this.drawColor = mp.drawColor;
        this.fillColor = mp.fillColor;
        for (int i = 0; i < this.numPoints; i++) {
            this.points[i] = new MyPoint(mp.points[i]);
        }

        this.isFilled = mp.isFilled;
    }

    public void addPoint(MyPoint p) {// הוסף קודקוד
        if (numPoints < MAX_POINTS)// אם יש מקום במערך
        {
            points[numPoints] = new MyPoint(p);
        }
        numPoints++;
    }

    public void translate(double tx, double ty) { // הזזה
        for (int i = 0; i < numPoints; i++) {
            points[i].setX(points[i].getX() + tx);
            points[i].setY(points[i].getY() + ty);
        }
    }

    public void translate() { // הזזה
        translate(this.dx, this.dy);//   לפי התכונות שלו
    }

    public void translate(Rectangle bounds) {
        // קודם כל הזז רגיל
        translate();

        double x1 = bounds.getX();
        double y1 = bounds.getY();
        double x2 = x1 + bounds.getWidth();
        double y2 = y1 + bounds.getHeight();

        if (this.getMaxX() > x2) {// חרגנו מימין
            this.translate(x2 - this.getMaxX(), 0);
            this.dx = -this.dx;
        }

        if (this.getMinX() < x1) {
            translate(x1 - this.getMinX(), 0);
            this.dx = -this.dx;
        }

        if (this.getMaxY() > y2) {
            translate(0, y2 - this.getMaxY());
            this.dy = -this.dy;
        }
        if (this.getMinY() < y1) {
            translate(0, y1 - this.getMinY());
            this.dy = -this.dy;
        }
    }

    public MyPoint getCenter() {// חישוב מרכז הפוליגון
        double x = 0;
        double y = 0;
        for (int i = 0; i < numPoints; i++) {
            x += points[i].getX();// temp
            y += points[i].getY();// temp
        }

        x = x / numPoints;
        y = y / numPoints;
        return new MyPoint(x, y);
    }

    public void rotate(double zavit, MyPoint fixed) {// סיבוב
        double x;
        double y;
        MyPoint f = new MyPoint(fixed);  // a copy
        double rd = zavit * Math.PI / 180;
        // הזזת נקודת הסיבוב לראשית
        this.translate(-f.getX(), -f.getY());
        for (int i = 0; i < numPoints; i++) {
            x = points[i].getX();// temp
            y = points[i].getY();// temp
            points[i].setX(x * Math.cos(rd) - y * Math.sin(rd));
            points[i].setY(x * Math.sin(rd) + y * Math.cos(rd));
        }

        this.translate(f.getX(), f.getY());// הזזה בחזרה
    }

    public void scale(double sx, double sy, MyPoint fixed) {// סילום
        double x;
        double y;
        MyPoint f = new MyPoint(fixed);  // a copy
        this.translate(-f.getX(), -f.getY());
        for (int i = 0; i < numPoints; i++) {
            x = points[i].getX();// temp
            y = points[i].getY();// temp
            points[i].setX(x * sx);
            points[i].setY(y * sy);
        }
        this.translate(f.getX(), f.getY());
    }

    public String toString() {
        String s = numPoints + " vertices :" + "\n";
        for (int i = 0; i < numPoints; i++) {
            s = s + points[i].toString() + "\n";
        }
        return s;
    }

// האם נקודה נמצאת בתוך פוליגון
    public boolean contains(MyPoint p) {
        int[] xPoints = new int[this.points.length];
        int[] yPoints = new int[this.points.length];
        for (int i = 0; i < numPoints; i++) {
            xPoints[i] = (int) (points[i].getX());
            yPoints[i] = (int) (points[i].getY());
        }
        Polygon poly = new Polygon(xPoints, yPoints, numPoints);
        Point pp = new Point((int) p.getX(), (int) p.getY());
        return poly.contains(pp);
    }

    public void drawMe(Graphics2D g2) {// ציור הפוליגון לפי תכונותיו
        int[] xPoints = new int[this.points.length];
        int[] yPoints = new int[this.points.length];
        for (int i = 0; i < numPoints; i++) {
            xPoints[i] = (int) (points[i].getX());
            yPoints[i] = (int) (points[i].getY());
        }
        if (this.isFilled) {// אם צריך למלא
            g2.setColor(this.fillColor);
            g2.fillPolygon(xPoints, yPoints, numPoints);
        }
        if(this.drawColor==null)
            this.drawColor= Color.red;
        g2.setColor(this.drawColor);// ציור קוי הצלעות
        
        g2.drawPolygon(xPoints, yPoints, numPoints);
    }

//    public void drawMeGradient(Graphics2D g2, int x1, int y1, Color col1,
//            int x2, int y2, Color col2) {
//            int[] xPoints = new int[this.points.length];
//            int[] yPoints = new int[this.points.length];
//            for (int i = 0; i < numPoints; i++) {
//                xPoints[i] = (int) (points[i].getX());
//                yPoints[i] = (int) (points[i].getY());
//            }
//            g2.setPaint(new GradientPaint(x1,y1,col1,x2,y2,col2));
//            g2.fillPolygon(xPoints, yPoints, numPoints);
//
//            g2.setColor(this.drawColor);// ציור קוי הצלעות
//            g2.drawPolygon(xPoints, yPoints, numPoints);
//        }

    public double getMinY() {
        double y = 0;
        double minY = this.points[0].getY();
        for (int i = 1; i < numPoints; i++) {
            y = this.points[i].getY();
            if (y < minY) {
                minY = y;
            }
        }
        return minY;
    }

    public double getMaxY() {
        double y = 0;
        double maxY = this.points[0].getY();
        for (int i = 1; i < numPoints; i++) {
            y = this.points[i].getY();
            if (y > maxY) {
                maxY = y;
            }
        }
        return maxY;
    }

    public Color getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(Color drawColor) {
        this.drawColor = drawColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public void setFilled(boolean filled) {
        this.isFilled = filled;
    }

    public double getMinX() {
        double x = 0;
        double minX = this.points[0].getX();
        for (int i = 1; i < numPoints; i++) {
            x = this.points[i].getX();
            if (x < minX) {
                minX = x;
            }
        }
        return minX;
    }

    public double getMaxX() {
        double x = 0;
        double maxX = this.points[0].getX();
        for (int i = 1; i < numPoints; i++) {
            x = this.points[i].getX();
            if (x > maxX) {
                maxX = x;
            }
        }
        return maxX;
    }

    public int getNumPoints() {
        return numPoints;
    }
//  החזר העתק של אחד הקודקודים

    public MyPoint getPointAt(int i) {
        if (i >= numPoints) {
            return null; // אין כזה קודקוד
        }
        return this.points[i]; // returns NOT a copy..
    }

    public Boolean intersects(MyPolygon other) {
        Polygon a = new Polygon(); // פוליגון של המחלקה בג'אווה
        Polygon b = new Polygon();
        // copy from MyPolygon(s) to Polygon(s) data structure
        for (int i = 0; i < numPoints; i++) {
            a.addPoint((int) this.points[i].getX(), (int) this.points[i].getY());
        }
        for (int i = 0; i < other.getNumPoints(); i++) {
            b.addPoint((int) other.getPointAt(i).getX(), (int) other.getPointAt(i).getY());
        }

        // The Area class is a device-independent
        //specification of an arbitrarily-shaped area.
        //The Area object is defined as an object
        //that performs certain binary operations.
        Area aa = new Area(a);  // the area of first polygon
        Area bb = new Area(b);  // the area of second polygon
        aa.intersect(bb);

        return !aa.isEmpty(); // האם השטח המשותף ריק
    }

    public static double dist(MyPoint p1, MyPoint p2) {
        return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
                + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
    }

    // adjust other poly to this poly :
    public void adjust(MyPolygon other) {
        double dMin, d, tx, ty;
        int ii = 0, jj = 0;
        dMin = dist(this.getPointAt(0), other.getPointAt(0));
        for (int i = 0; i < this.numPoints; i++) {
            for (int j = 0; j < other.numPoints; j++) {
                d = dist(this.getPointAt(i), other.getPointAt(j));
                if (d < dMin) {
                    ii = i;
                    jj = j;
                    dMin = d;
                }
            }
        }
        tx = this.getPointAt(ii).getX() - other.getPointAt(jj).getX();
        ty = this.getPointAt(ii).getY() - other.getPointAt(jj).getY();
        other.translate(tx, ty);
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void drawMeGradient(Graphics2D g2, float x1, float y1, Color col1,
            float x2, float y2, Color col2) {
            int[] xPoints = new int[this.points.length];
            int[] yPoints = new int[this.points.length];
            for (int i = 0; i < numPoints; i++) {
                xPoints[i] = (int) (points[i].getX());
                yPoints[i] = (int) (points[i].getY());
            }
            g2.setPaint(new GradientPaint(x1,y1,col1,x2,y2,col2,true));
            g2.fillPolygon(xPoints, yPoints, numPoints);

            g2.setColor(this.drawColor);// ציור קוי הצלעות
            g2.drawPolygon(xPoints, yPoints, numPoints);
        }
    public void emptyPolygon(){
        this.numPoints=0; // ריקון הפוליגון מקודקודים
    }
}
