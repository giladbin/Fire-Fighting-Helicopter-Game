
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.*;
import java.util.*;

/**
 * מחלקה זו מייצגת גוף תלת מימדי
 *@author Memi Gutbir  18-8-2011
 */
public class Body3D {
    /**
     * // מה שקשור למבנה הנתונים :
     */
    protected MyPoint3D[] vertices;  // מערך קודקודים
    protected int[][] walls; // מערך קירות דו מימדי

    // === how to draw it :
    /**
     *
     */
    protected Color fillColor;  // may be null if not to be filled
    protected Color drawColor;
    protected int style;     // 0= solid, colored
                                                             // 1= solid, not colored
                                                             //  2= wire

    protected MyPoint3D eye; // מרכז הפרספקטיבה
    protected MyPoint3D light; // מקור האור
   // לצרכי נוחיות :
    protected int w, h;

    // משתנים פנימיים
    MyPolygon temp = new MyPolygon();  // משרת אתפונקציות הציור 2-2011
    MyPoint3D[] verticesTemp;  // =     // עותק של מערך קודקודים
    MyPoint3D p1     = new MyPoint3D();
    MyPoint p2d = new MyPoint(0, 0);
    MyPoint3D    u = new MyPoint3D();      //  19-2-2011
    MyPoint3D   q1 = new MyPoint3D();
    MyPoint3D  q2 = new MyPoint3D();

    /**
     *
     * @param bounds  : מלבן שמייצג גודל שטח הציור
     * @param fileName : שם הקובץ בו נמצא הגוף
     */
    public Body3D(Rectangle bounds, String fileName) {
        readBodyFromFile(fileName);// קרא קובץ גוף מדיסק
        antiClock(); // סדר את הקודקודים בכל קיר נגד כיוון השעון
        normalizeVertices();// סדר את כל הקואורדינטות שיהיו בין אפס לאחד

        // נוסיף קודקוד שהוא מרכז הגוף
        MyPoint3D c = center();// / פעולה פנימית פרטית
        // יצירת מערך קודקדים גדול באחד
        MyPoint3D[] tmp = new MyPoint3D[this.vertices.length + 1];
        // העתקת כל הקדקודים שבאו מהקובץ
        System.arraycopy(this.vertices, 0, tmp, 0, this.vertices.length);
        // הוספת קדקוד אחרון שהוא המרכז
        tmp[this.vertices.length] = c;
        this.vertices = tmp;   // החלפת הפנייה

        // קביעת צבע וסגנון ברירת מחדל
        this.fillColor = new Color(10, 250, 20, 200);
        this.drawColor = Color.white;
        this.style = 0;

        this.w = bounds.width;
        this.h = bounds.height;
    }

    /**
     *  פעולה בונה מעתיקה
     * @param guf   the body to copy
     */
    public Body3D(Body3D guf) { // copy constructor
        this.h = guf.h;
        this.w = guf.w;
        int numWalls = guf.walls.length;
        this.walls = new int[numWalls][];// צור מערך קירות דו ממדי חדש
        for (int i = 0; i < numWalls; i++) {
            // צריך לאתחל כל שורה במערך הדו ממדי
            this.walls[i] = new int[guf.walls[i].length];
            // ולהעתיק מספר מספר
            for (int j = 0; j < walls[i].length; j++) {
                this.walls[i][j] = guf.walls[i][j];
            }
        }
        int numVertices = guf.vertices.length;
        this.vertices = new MyPoint3D[numVertices]; // איתחול מערך קדקדים???
        for (int i = 0; i < numVertices; i++) // העתק קודקוד קודקוד
        {
            this.vertices[i] = new MyPoint3D(guf.vertices[i]);
        }
        this.fillColor = guf.fillColor;   // may be null
        this.drawColor = guf.drawColor;
        this.style = guf.style;
        this.light = guf.light;
        this.eye = guf.eye;
    }

    public int getStyle() {
        return this.style;
    }

    /**
     *  Sets the drawing style for the body :
     *   0= solid, filled with color
     *   1= solid, not filled with color
     *   2= wireframe
     * @param style
     */
    public void setStyle(int style) {
        this.style = style;
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

    /**
     *       *  מצייר רק צל של הגוף על שולחן
     * @param g2
     * @param groundY
     */
    public void drawShadow(Graphics2D g2, double groundY,int den) {
        if (this.style < 2 && this.light != null) {
            Body3D shaded = createShadow(groundY);
            shaded.style = 0; // שיהיה מלא
            shaded.fillColor = new Color(0,0,0,den);
            shaded.drawColor = new Color(0,0,0,den);
            shaded.draw(g2);
        }
    }

    private void drawWireNoPerespective(Graphics2D g2) {
        MyPoint p2d;
        MyPolygon temp = new MyPolygon();
        for (int kir = 0; kir < this.walls.length; kir++) {
            for (int i = 0; i < walls[kir].length; i++) {
                p2d = new MyPoint(vertices[walls[kir][i]].x,
                        vertices[walls[kir][i]].y);
                temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
            }
            temp.setFilled(false); // wire
            temp.setDrawColor(this.drawColor);
            temp.drawMe(g2);
            temp.emptyPolygon();
        }
    }

    private void drawSolidNoPerespective(Graphics2D g2, boolean filled) {
        MyPoint p2d;
        //MyPolygon temp= new MyPolygon();
        this.temp.emptyPolygon();
        MyPoint3D n;
        for (int kir = 0; kir < this.walls.length; kir++) {
            n = Service.normal( // האם הקיר פונה אלינו
                    vertices[walls[kir][0]],
                    vertices[walls[kir][1]],
                    vertices[walls[kir][2]]);

            if (n.z > 0) {

                if (filled) {
                    temp.setFilled(true);
                } else {
                    temp.setFilled(false);
                }
                for (int i = 0; i < walls[kir].length; i++) {
                    p2d = new MyPoint(vertices[walls[kir][i]].x,
                            vertices[walls[kir][i]].y);
                    temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
                }
                temp.setFillColor(this.fillColor);
                temp.setDrawColor(this.drawColor);
//                temp.setFilled(true);
              //  System.out.println("g2="+g2+"     *******************************");
                temp.drawMe(g2);
                temp.emptyPolygon();
            }
        }
    }

    /**
     *
     * @param g2
     */
    public void draw(Graphics2D g2) {
        if (this.eye == null && this.style == 0) {
            drawSolidNoPerespective(g2, true);
        }

        if (this.eye == null && this.style == 2) {
            drawWireNoPerespective(g2);
        }

        if (this.eye != null && this.style == 0) {
            drawSolidPerespective(g2, true);
        }

        if (this.eye != null && this.style == 2) {
            drawWirePerespective(g2);
        }

        if (this.eye == null && this.style == 1) {
            drawSolidNoPerespective(g2, false);
        }

        if (this.eye != null && this.style == 1) {
            drawSolidPerespective(g2, false);
        }
        if(this.eye==null && this.style==3){
            drawSolidNoPerespective(g2, true);
        }
        if(this.eye!=null && this.style==3){
            drawWirePerespectiveSolid(g2);
        }
    }

    private void drawSolidPerespective(Graphics2D g2, boolean filled) {
               this.verticesTemp =     // עותק של מערך קודקודים
                      new MyPoint3D[this.vertices.length];

        //MyPoint3D p1 = new MyPoint3D();
        //MyPoint p2d = new MyPoint(0, 0);
        MyPoint3D n;
        //===============
        for (int i = 0; i < this.vertices.length; i++) { // בצע פרספקטיבה
            double u = -vertices[i].z / (eye.z - vertices[i].z);
            p1.x = vertices[i].x + u * (eye.x - vertices[i].x);
            p1.y = vertices[i].y + u * (eye.y - vertices[i].y);
            p1.z=0;
            verticesTemp[i] = new MyPoint3D(p1); // בוצעה פרספקטיבה
        }
        //MyPolygon temp= new MyPolygon();
        temp.emptyPolygon(); // נקה כל הקודקודים
        temp.setFillColor(this.fillColor);
        temp.setDrawColor(this.drawColor);
        temp.setFilled(filled);

        for (int kir = 0; kir < this.walls.length; kir++) {
            n = Service.normal( // האם הקיר פונה אלינו
                    verticesTemp[walls[kir][0]],
                    verticesTemp[walls[kir][1]],
                    verticesTemp[walls[kir][2]]);

          if (n.z > 0)
            {
                for (int i = 0; i < walls[kir].length; i++) {
                         p2d.x=(verticesTemp[walls[kir][i]].x);
                         p2d.y=(verticesTemp[walls[kir][i]].y);

                    temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
                }
                temp.drawMe(g2);
                temp.emptyPolygon();
            }
        }
    }

    private void drawWirePerespective(Graphics2D g2) {
        MyPoint3D[] verticesTemp = // העתק מערך קודקודים
                new MyPoint3D[this.vertices.length];
        //===============
        for (int i = 0; i < this.vertices.length; i++) {
            double u = -vertices[i].z / (eye.z - vertices[i].z);
            double x = vertices[i].x + u * (eye.x - vertices[i].x);
            double y = vertices[i].y + u * (eye.y - vertices[i].y);

            verticesTemp[i] = new MyPoint3D(x, y, 0); // בוצעה פרספקטיבה
        }
        MyPoint p2d;
        MyPolygon temp = new MyPolygon();
        MyPoint3D n;

        for (int kir = 0; kir < this.walls.length; kir++) {
            temp = new MyPolygon();
            temp.setFilled(false); // wire
            for (int i = 0; i < walls[kir].length; i++) {
                p2d = new MyPoint(verticesTemp[walls[kir][i]].x,
                        verticesTemp[walls[kir][i]].y);
                temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
            }
            temp.setDrawColor(this.drawColor);
            //            temp.setFillColor(this.fillColor);
            temp.setFillColor(fillColor);
            temp.drawMe(g2);
            temp.emptyPolygon();
        }
    }
    private void drawWirePerespectiveSolid(Graphics2D g2) {
        MyPoint3D[] verticesTemp = // העתק מערך קודקודים
                new MyPoint3D[this.vertices.length];
        //===============
        for (int i = 0; i < this.vertices.length; i++) {
            double u = -vertices[i].z / (eye.z - vertices[i].z);
            double x = vertices[i].x + u * (eye.x - vertices[i].x);
            double y = vertices[i].y + u * (eye.y - vertices[i].y);

            verticesTemp[i] = new MyPoint3D(x, y, 0); // בוצעה פרספקטיבה
        }
        MyPoint p2d;
        MyPolygon temp = new MyPolygon();
        MyPoint3D n;

        for (int kir = 0; kir < this.walls.length; kir++) {
            temp = new MyPolygon();
           // temp.setFilled(false); // wire
            for (int i = 0; i < walls[kir].length; i++) {
                p2d = new MyPoint(verticesTemp[walls[kir][i]].x,
                        verticesTemp[walls[kir][i]].y);
                temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
            }
            temp.setDrawColor(this.drawColor);
            //            temp.setFillColor(this.fillColor);
            temp.setFillColor(fillColor);
            temp.drawMe(g2);
            temp.emptyPolygon();
        }
    }    
    // מחזיר גוף שהוא הצל של גוף זה

    private Body3D createShadow(double groundY) {
        Body3D shadow = new Body3D(this);
        shadow.setFillColor(Color.black);
        shadow.setDrawColor(Color.black);

        for (int i = 0; i < shadow.vertices.length; i++) {
            double u = (groundY - shadow.vertices[i].y)
                    / (light.y - shadow.vertices[i].y);
            shadow.vertices[i].x = shadow.vertices[i].x + u * (light.x - shadow.vertices[i].x);
            shadow.vertices[i].y = groundY;
            shadow.vertices[i].z = shadow.vertices[i].z + u * (light.z - shadow.vertices[i].z);

            //JOptionPane.showMessageDialog(null,shadow.vertices[i]);

            //    JOptionPane.showMessageDialog(null, "U=  " + u);
            //           JOptionPane.showMessageDialog(null, shadow.toString());
        }
        return shadow;
    }

    /**
     *
     * @param tx
     * @param ty
     * @param tz
     */
    public void translate(double tx, double ty, double tz) {
        for (int i = 0; i < this.vertices.length; i++) {
            MyPoint3D p = vertices[i];
            p.x = p.getX() + tx;
            p.y = p.getY() + ty;
            p.z = p.getZ() + tz;
        }
    }

    /**
     *
     * @param sx
     * @param sy
     * @param sz
     * @param fixed
     */
    public void scale(double sx, double sy, double sz,
            MyPoint3D fixed) {
        MyPoint3D p;
        for (int i = 0; i < this.vertices.length; i++) {
            p = vertices[i];
            p.x = p.getX() - fixed.x;// הזז נק שבת לראשית
            p.x = p.x * sx; // סילום ביחס לראשית
            p.x = p.x + fixed.x;// החזר נק שבת בחזרה

            p.y = p.getY() - fixed.y;// הזז נק שבת לראשית
            p.y = p.y * sy; // סילום ביחס לראשית
            p.y = p.y + fixed.y;// החזר נק שבת בחזרה

            p.z = p.getZ() - fixed.z;// הזז נק שבת לראשית
            p.z = p.z * sz; // סילום ביחס לראשית
            p.z = p.z + fixed.z;// החזר נק שבת בחזרה
        }
    }

    /**
     *
     * @param degrees
     * @param p1
     * @param p2
     */
    public void rotate(double degrees, MyPoint3D p1, MyPoint3D p2) {
        MyPoint3D kodkod;
        double radians = degrees * Math.PI / 180; // המרה
        double sinRd = Math.sin(radians);
        double cosRd = Math.cos(radians);
        for (int i = 0; i < this.vertices.length; i++) {
            kodkod = this.vertices[i];

            //סובב קודקוד אחד
   //         System.out.println("kodkod="+kodkod);
            kodkod = rotatePointFAST(kodkod, sinRd, cosRd, p1, p2);
            this.vertices[i].x = kodkod.x;
            this.vertices[i].y = kodkod.y;
            this.vertices[i].z = kodkod.z;
        }
    }
    private MyPoint3D rotatePointFAST(MyPoint3D p, double sinRd, double cosRd,
            MyPoint3D p1, MyPoint3D p2) {
        double d;
//        MyPoint3D u, q1, q2;       // 19-2-2011
//        u = new MyPoint3D();        19-2-2011
//        q1 = new MyPoint3D();
//        q2 = new MyPoint3D();

        //* Step 1 */
        q1.x = (p.x - p1.x);
        q1.y = (p.y - p1.y);
        q1.z = (p.z - p1.z);

        u.x = (p2.x - p1.x);
        u.y = (p2.y - p1.y);
        u.z = (p2.z - p1.z);

        //Normalize u
        double l = Math.sqrt(u.x * u.x + u.y * u.y + u.z * u.z);
        if (l != 0) {
            u.x = (u.x / l);
            u.y = (u.y / l);
            u.z = (u.z / l);
        }
        d = Math.sqrt(u.y * u.y + u.z * u.z);

        //'/* Step 2 */
        if (d != 0) {
            q2.x = q1.x;
            q2.y = ((q1.y * u.z) / d - (q1.z * u.y) / d);
            q2.z = ((q1.y * u.y) / d + (q1.z * u.z) / d);
        } else {
            q2 = new MyPoint3D(q1);
        }

        //'/* Step 3 */
        q1.x = (q2.x * d - q2.z * u.x);
        q1.y = (q2.y);
        q1.z = (q2.x * u.x + q2.z * d);

        // '/* Step 4 */
        q2.x = q1.x * cosRd - q1.y * sinRd;
        q2.y = q1.x * sinRd + q1.y * cosRd;
        q2.z = q1.z;

        //'/* Inverse of step 3 */
        q1.x = q2.x * d + q2.z * u.x;
        q1.y = q2.y;
        q1.z = -q2.x * u.x + q2.z * d;

        //'/* Inverse of step 2 */
        if (d != 0) {
            q2.x = q1.x;
            q2.y = (q1.y * u.z / d + q1.z * u.y / d);
            q2.z = (-q1.y * u.y / d + q1.z * u.z / d);
        } else {
            q2 = new MyPoint3D(q1);
        }

        //'/* Inverse of step 1 */
        q1.x = (q2.x + p1.x);
        q1.y = (q2.y + p1.y);
        q1.z = (q2.z + p1.z);

        return q1;
    }

    /**
     *
     * @param i
     * @return
     */
    public MyPoint3D getKodkodAt(int i) {
        return this.vertices[i];
    }

    /**
     *
     * @return
     */
    public MyPoint3D getCenter() {
        return new MyPoint3D(this.vertices[this.vertices.length - 1]); // קדקןד אחרון הוא המרכז
    }

    /**
     *  Computes and return the body center point
     * @return center of Body
     */
    private MyPoint3D center() {
        MyPoint3D c = new MyPoint3D(0, 0, 0);
        for (int i = 0; i < this.vertices.length; i++) {  // 10-2-2011 Memi
            c.x = c.x + this.vertices[i].x;
            c.y = c.y + this.vertices[i].y;
            c.z = c.z + this.vertices[i].z;
        }
        c.x = c.x / this.vertices.length;
        c.y = c.y / this.vertices.length;
        c.z = c.z / this.vertices.length;
        return c;
    }

    private void readBodyFromFile(String fileName) {
        int numVertices = 0;
        int numWalls = 0;
        ArrayList<Double> list;
        try {
            // Open the file :

            FileInputStream fstream = new FileInputStream(fileName);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // read numVertices from first line of file :
            strLine = br.readLine();
            if (strLine != null) {
                numVertices = Integer.parseInt(strLine);
            }
//            System.out.println("numVertices=" + numVertices);
            this.vertices = new MyPoint3D[numVertices]; // איתחול מערך קדקדים

            //Read the Vertices data from File Line By Line :
            for (int i = 0; i < numVertices; i++) {
                // read a line
                strLine = br.readLine();
//      System.out.println (strLine);
                //.... split
                list = splitItems(strLine);
                double x = (Double) list.get(0);
                double y = (Double) list.get(1);
                double z = (Double) list.get(2);
                this.vertices[i] = new MyPoint3D(x, y, z);// הוסף הקדקוד למערך הקדקדים בגוף
            }
            // NOW WALLS :
            //  read numWalls :
            strLine = br.readLine();
            if (strLine != null) {
                numWalls = Integer.parseInt(strLine);
            }
//            System.out.println("numWalls=" + numWalls);
            this.walls = new int[numWalls][];// אתחל מערך קירות
// read walls data into array of walls:
            for (int i = 0; i < numWalls; i++) {
                strLine = br.readLine();
//             System.out.println (strLine);

                list = splitItems(strLine);
                // אתחל שורה במערך הקירות
                this.walls[i] = new int[list.size()];
                for (int j = 0; j < list.size(); j++) {
                    double d = list.get(j);
                    walls[i][j] = (int) d;
                }
            }
            //Close the input stream
            in.close();
            br.close(); // ?
            fstream.close();//??

        } catch (Exception e) {//Catch exception if any
            System.err.println("readBodyFromFile Error : " + e.getMessage());
            System.exit(0);
        }
    }

    private ArrayList splitItems(String a) {
        ArrayList<Double> list = new ArrayList<Double>();

        // this statement will break the string into the words which are separated by space.
        StringTokenizer tempStringTokenizer = new StringTokenizer(a);

        while (tempStringTokenizer.hasMoreTokens()) {
            double d = Double.parseDouble((String) tempStringTokenizer.nextElement());
            list.add(d);
        }
        return list;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String s = "Vertices=" + this.vertices.length
                + "  Walls=" + this.walls.length + "\n" + "Center=" + getCenter()
                + "\n";
        for (int i = 0; i < this.vertices.length; i++) {
            s = s + vertices[i].toString() + "\n";
        }

        for (int i = 0; i < this.walls.length; i++) {
            for (int j = 0; j < walls[i].length; j++) {
                s = s + walls[i][j] + "   ";
            }
            s = s + "\n";
        }
        return s;
    }

// מסדר קודקודי גוף קמור נכל קיר
    // נגד כיוון השעון לצורךציור בסוליד
    @SuppressWarnings("empty-statement")
    private void antiClock() {
        MyPoint3D c = center();
        MyPoint3D n;  // וקטור ניצב לקיר
        MyPoint3D c2vertex = new MyPoint3D(); // וקטור ממרכז כובד ל(קודקוד) בקיר
        double sp; // מונה של מכפלה סקלרית
        int[] temp;

        for (int kir = 0; kir < this.walls.length; kir++) {
            n = Service.normal(vertices[walls[kir][0]],
                    vertices[walls[kir][1]],
                    vertices[walls[kir][2]]);
            c2vertex.x = vertices[walls[kir][0]].x - c.x;
            c2vertex.y = vertices[walls[kir][0]].y - c.y;
            c2vertex.z = vertices[walls[kir][0]].z - c.z;

            sp = n.x * c2vertex.x
                    + n.y * c2vertex.y
                    + n.z * c2vertex.z;

            if (sp < 0) { // צריך להפוך הסדר
                temp = new int[this.walls[kir].length];

                for (int k = 0; k < walls[kir].length; k++) {
                    temp[k] = this.walls[kir][k];
                }
//        System.out.println(Arrays.toString(temp));
                for (int k = 0; k < walls[kir].length; k++) {
                    this.walls[kir][k] = temp[walls[kir].length - k - 1];
                    ;
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public double minX() {
        double rc = this.vertices[0].getX();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc > this.vertices[i].getX()) {
                rc = this.vertices[i].getX();
            }
        }
        return rc;
    }

    /**
     *
     * @return
     */
    public double minY() {
        double rc = this.vertices[0].getY();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc > this.vertices[i].getY()) {
                rc = this.vertices[i].getY();
            }
        }
        return rc;
    }

    /**
     *
     * @return
     */
    public double minZ() {
        double rc = this.vertices[0].getZ();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc > this.vertices[i].getZ()) {
                rc = this.vertices[i].getZ();
            }
        }
        return rc;
    }

    /**
     *
     * @return
     */
    public double maxX() {
        double rc = this.vertices[0].getX();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc < this.vertices[i].getX()) {
                rc = this.vertices[i].getX();
            }
        }
        return rc;
    }

    /**
     *
     * @return
     */
    public double maxY() {
        double rc = this.vertices[0].getY();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc < this.vertices[i].getY()) {
                rc = this.vertices[i].getY();
            }
        }
        return rc;
    }

    /**
     *
     * @return
     */
    public double maxZ() {
        double rc = this.vertices[0].getZ();
        for (int i = 1; i < this.vertices.length; i++) {
            if (rc < this.vertices[i].getZ()) {
                rc = this.vertices[i].getZ();
            }
        }
        return rc;
    }

    private void normalizeVertices() {
        //==============
        double difX, difY, difZ, factor;
        double minX, minY, minZ;
        double maxX, maxY, maxZ;
        //Dim Normalized  As Boolean
        //Normalized = True

        minX = this.minX();
        minY = this.minY();
        minZ = this.minZ();

        maxX = this.maxX();
        maxY = this.maxY();
        maxZ = this.maxZ();

        //If Min(minX, Min(MinY, MinZ)) < 0 Then Normalized = False
        // If MAX(MaxX, MAX(MaxY, MaxZ)) > 1 Then Normalized = False

        difX = maxX - minX;
        difY = maxY - minY;
        difZ = maxZ - minZ;
        factor = Service.max(Service.max(difX, difY), difZ);
//   If Not Normalized Then

        for (int i = 0; i < this.vertices.length; i++) {
            vertices[i].x = (vertices[i].x - minX) / factor;
            vertices[i].y = (vertices[i].y - minY) / factor;
            vertices[i].z = (vertices[i].z - minZ) / factor;
        }
    }

    /**
     *
     * @return
     */
    public MyPoint3D getEye() {
        return eye;
    }

    /**
     *
     * @param eye
     */
    public void setEye(MyPoint3D eye) {
        this.eye = eye;
    }

    /**
     *
     * @return
     */
    public MyPoint3D getLight() {
        return light;
    }

    /**
     *
     * @param light
     */
    public void setLight(MyPoint3D light) {
        this.light = light;
    }

    /**
     *
     * @param kir
     * @return
     */
    public MyPoint3D wallCenter(int kir) {
        MyPoint3D c = new MyPoint3D(0, 0, 0);
        for (int i = 0; i < this.walls[kir].length; i++) {
            c.x = c.x + this.vertices[this.walls[kir][i]].x;
            c.y = c.y + this.vertices[this.walls[kir][i]].y;
            c.z = c.z + this.vertices[this.walls[kir][i]].z;
        }
        c.x = c.x / this.walls[kir].length;
        c.y = c.y / this.walls[kir].length;
        c.z = c.z / this.walls[kir].length;
        return c;
    }
    // in Body3D
    /**
     *
     * @param g
     * @param p1
     * @param p2
     * @param w
     * @param h
     * @param wTop
     * @param hTop
     */
    public void drawTop(Graphics2D g, MyPoint3D p1, MyPoint3D p2,
            int w, int h, int wTop, int hTop) {

        Body3D tmp = new Body3D(this);
        tmp.scale(0.5,0.5,0.4, p1);
        tmp.rotate(-90, p1, p2);
        tmp.setEye(null);
        tmp.setStyle(2);
        tmp.setDrawColor(drawColor);
        
       // tmp.setFillColor(fillColor);
        // fitting guf size to pnlTop size :
        for (int i = 0; i < tmp.vertices.length; i++) {
            tmp.vertices[i].x = tmp.vertices[i].x * wTop / (w);
            tmp.vertices[i].y = tmp.vertices[i].y * hTop / (h);
            //       tmp.vertices[i].z=tmp.vertices[i].z * wTop / w;
        }
        //tmp.scale(0.6,0.5,1,tmp.getCenter());
 //       tmp.translate(0, -hTop / 4, 0); // ??????????   ADJUST EACH PROJECT
               tmp.translate(0, -170, 0); // ??????????   ADJUST EACH PROJECT
        tmp.setStyle(0);
        tmp.setEye(null);
        tmp.draw(g);
    }

    /**
     * If this body intersects with another
     * @param other 
     * @return
     */
    public boolean intersect(Body3D other) {

//   בדיקה ראשונית גסה
        if (this.minX() > other.maxX()) {
            return false;
        }
        if (this.minY() > other.maxY()) {
            return false;
        }
        if (this.minZ() > other.maxZ()) {
            return false;
        }

        if (other.minX() > this.maxX()) {
            return false;
        }
        if (other.minY() > this.maxY()) {
            return false;
        }
        if (other.minZ() > this.maxZ()) {
            return false;
        }
        //  בדיקה מדוייקת
            MyPolygon[]  polysXZ=new MyPolygon[this.walls.length];
        MyPolygon[]  polysXY=new MyPolygon[this.walls.length];
        MyPolygon[]  polysZY=new MyPolygon[this.walls.length];

        MyPolygon[]  othersXZ=new MyPolygon[other.walls.length];
        MyPolygon[]  othersXY=new MyPolygon[other.walls.length];
        MyPolygon[]  othersZY=new MyPolygon[other.walls.length];

        MyPoint p = new MyPoint(0, 0); // ליעילות
        MyPoint3D a;

        for (int kir = 0; kir < this.walls.length; kir++) {
            polysXZ[kir]=new MyPolygon();
            polysXY[kir]=new MyPolygon();
            polysZY[kir]=new MyPolygon();
            for (int q = 0; q < walls[kir].length; q++) {
                a = this.getKodkodAt(this.walls[kir][q]);
                p.setX(a.x);
                p.setY(a.z);
                polysXZ[kir].addPoint(p);
                p.setY(a.y);
                polysXY[kir].addPoint(p);
                p.setX(a.z);
                p.setY(a.y);
                polysZY[kir].addPoint(p);
            }
        }
            for (int i = 0; i < other.walls.length; i++) {
                othersXZ[i]=new MyPolygon();
                othersXY[i]=new MyPolygon();
                othersZY[i]=new MyPolygon();

                for (int q = 0; q < other.walls[i].length; q++) {
                    a = other.getKodkodAt(other.walls[i][q]);
                    p.setX(a.x);
                    p.setY(a.z);
                    othersXZ[i].addPoint(p);
                    p.setY(a.y);
                    othersXY[i].addPoint(p);
                    p.setX(a.z);
                    p.setY(a.y);
                    othersZY[i].addPoint(p);
                }
        }

        boolean hitXZ=false;
        boolean hitXY=false;
        boolean hitZY=false;
        // בדיקת חיתוכים
            for (int i=0;i<this.walls.length;i++)
                for (int j=0;j<other.walls.length;j++){
                    hitXZ=hitXZ ||  polysXZ[i].intersects(othersXZ[j] );
                   hitXY=hitXY ||  polysXY[i].intersects(othersXY[j] );
                   hitZY=hitZY ||  polysZY[i].intersects(othersZY[j] );
                   if (hitXZ && hitXY && hitZY) return true;// ליעילות אולי אפשר להפסיק
                }
            return hitXZ && hitXY && hitZY;
}

    /**
     *
     * @param g2
     * @param x1
     * @param y1
     * @param col1
     * @param x2
     * @param y2
     * @param col2
     */
    public  void drawSolidPerespectiveGradient(Graphics2D g2,
                     float x1,float y1,Color col1,float x2,float y2,Color col2)
           {
               this.verticesTemp =     // עותק של מערך קודקודים
                      new MyPoint3D[this.vertices.length];

        //MyPoint3D p1 = new MyPoint3D();
        //MyPoint p2d = new MyPoint(0, 0);
        MyPoint3D n;
        //===============
        for (int i = 0; i < this.vertices.length; i++) { // בצע פרספקטיבה
            double u = -vertices[i].z / (eye.z - vertices[i].z);
            p1.x = vertices[i].x + u * (eye.x - vertices[i].x);
            p1.y = vertices[i].y + u * (eye.y - vertices[i].y);
            p1.z=0;
            verticesTemp[i] = new MyPoint3D(p1); // בוצעה פרספקטיבה
        }
        //MyPolygon temp= new MyPolygon();
        temp.emptyPolygon(); // נקה כל הקודקודים
//        temp.setFillColor(this.fillColor);
        temp.setDrawColor(this.drawColor);
        temp.setFilled(true);

        for (int kir = 0; kir < this.walls.length; kir++) {
            n = Service.normal( // האם הקיר פונה אלינו
                    verticesTemp[walls[kir][0]],
                    verticesTemp[walls[kir][1]],
                    verticesTemp[walls[kir][2]]);

          if (n.z > 0)
            {
                for (int i = 0; i < walls[kir].length; i++) {
                         p2d.x=(verticesTemp[walls[kir][i]].x);
                         p2d.y=(verticesTemp[walls[kir][i]].y);

                    temp.addPoint(p2d);  // הוסף קודקוד לפוליגון
                }
                temp.drawMeGradient(g2,x1,y1,col1,x2,y2,col2);
                temp.emptyPolygon();
            }
        }
    }
    public static void drawArrayOfBodies(Graphics2D g2, Body3D[] bodiesMekory) {
        Body3D temp;
        if (bodiesMekory != null && bodiesMekory[0] != null) {  
            // copy the array
            Body3D[] bodies = Arrays.copyOf(bodiesMekory, bodiesMekory.length);

            double c;
            for (int i = 0; i < bodies.length - 1; i++) {    
                c = bodies[i].getCenter().z;
                for (int j = i + 1; j < bodies.length; j++) {
                    if (c > bodies[j].getCenter().z) {
                        temp = bodies[j];
                        bodies[j] = bodies[i];
                        bodies[i] = temp;
		c=bodies[i].getCenter().z;
                    }
                }
            }
            for (int i = 0; i < bodies.length; i++) {  // צייר לפי העומק
                bodies[i].draw(g2);
            }
        } else {
            System.out.println("Body array is still null. Can't sort");
        }
    }
    
}
