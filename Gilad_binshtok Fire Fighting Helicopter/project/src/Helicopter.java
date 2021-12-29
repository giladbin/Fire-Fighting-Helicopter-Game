import java.awt.*;
public class Helicopter {
    private Body3D body;
    private Body3D prop,cyl,water;
    public Body3D bucket;
    private Body3D land;//
    private String name="";//player name
    private MyPoint3D eye,light;
    private MyPoint3D b1,b2,b3,b4;//bucket relayed points
    private double w,h;
    private MyPoint3D center;//helicopter body center
    private Rectangle bounds;
    private double degree; //propelor roloing degree 
    private boolean emptying=false,empty=true;//bucket situation booleans
    private Drops drops;
    public static double propLength;//in order to check other helicopter intersaction
    //constractor
    public Helicopter(Rectangle bounds,MyPoint3D eye,MyPoint3D center,Body3D land,String name){
        //updating the variables
        this.land=land;
        this.name=name;
        body=new Body3D(bounds,"JavaBall.txt" );
        this.eye=eye;
        this.bounds=bounds;
        this.w=bounds.width;
        this.h=bounds.height;
        //creating body
        body.scale(100,100,100,new MyPoint3D());
        body.setFillColor(Color.gray);
        body.setLight(light);
        body.setEye(eye);
        body.setStyle(0);
        body.setDrawColor(Color.gray);
        Service.translate(body, body.getCenter(),center);//translating body to center
        cyl=new Body3D(bounds,"Cylinder.txt");
        cyl.setFillColor(Color.black);
        cyl.setDrawColor(Color.black);
        cyl.setLight(light);
        cyl.setEye(eye);
        cyl.setStyle(0);        
        cyl.scale(5,45,5,new MyPoint3D());
        Service.translate(cyl,cyl.getCenter(),center);
        cyl.translate(0,-50,0);
        //creating propelor
        prop=new Body3D(bounds,"Cube.txt");
        prop.setEye(eye);
        prop.setFillColor(Color.red);
        prop.setDrawColor(Color.black);
        prop.setLight(light);
        prop.setStyle(0);
        prop.scale(300,2,2,new MyPoint3D());
        propLength=prop.maxX()-prop.minX();
        Service.translate(prop, prop.wallCenter(0),cyl.wallCenter(12));
        degree=12;
        this.light=light;
        //creating bucket
        bucket=new Body3D(bounds, "Cylinder.txt");     
        bucket.setFillColor(new Color(0,0,255,100));
        bucket.setDrawColor(Color.black);
        bucket.setEye(eye);
        bucket.setLight(light);
        bucket.setStyle(2);
        bucket.scale(340,100,340,new MyPoint3D());
        Service.translate(bucket, bucket.wallCenter(12),new MyPoint3D(center.x,body.maxY(),center.z));       
        bucket.translate(0,50,0);
        b1=new MyPoint3D(bucket.wallCenter(12));
        b2=new MyPoint3D(b1);
        b2.y=body.maxY();
        //create water
        Color c=new Color(51,204,255);
        water=new Body3D(bucket);
        water.setStyle(0);
        water.setFillColor(c);
        water.setDrawColor(Color.white);
        b3=new MyPoint3D(water.getCenter());
        b3.y=water.maxY();
        for(int i=0;i<12;i++){
            water.vertices[i].y=water.maxY();
        }
        b4=new MyPoint3D(bucket.getCenter());
        b4.y=land.minY();
        drops=new Drops(bounds,eye,bucket.wallCenter(12),(bucket.maxX()-bucket.minX())/2,50,6);
    }
    //drawing the helicopter
    public void draw(Graphics2D g2){
        if(light!=null){
            cyl.drawShadow(g2, land.minY(), 150);
            body.drawShadow(g2, land.minY(), 150);
            prop.drawShadow(g2, land.minY(), 150);
            bucket.drawShadow(g2, land.minY(), 150);            
        }        
     
        cyl.draw(g2);
        body.draw(g2);
        prop.draw(g2);
        if(!empty){
            water.draw(g2);
        }        
        bucket.draw(g2);
        Service.line3D(g2, b1, b2, eye,Color.black);

        if(emptying){
             drops.draw(g2,bucket);            
        }
    }
    public void drawTop(Graphics2D g2Top, MyPoint3D p1, MyPoint3D p2,int w, int h, int wTop, int hTop){        
        body.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
        prop.drawTop(g2Top, p1, p2, w, h, wTop, hTop);
    }
    //drow two helicopters in arrygment
    public void drawArry(Graphics2D g2,Helicopter helicopter){
        if(helicopter.maxZ()>this.maxZ()){
            this.drawHelicopter(g2);
            helicopter.drawHelicopter(g2);
        }
        else{
            helicopter.drawHelicopter(g2);
            this.drawHelicopter(g2);
        }
    }
    //draw helicopter top part
    public void drawHelicopter(Graphics2D g2){
        cyl.draw(g2);
        body.draw(g2);
        prop.draw(g2); 
    }
    //draw helicopter button part
    public void drawBucket(Graphics2D g2){
        if(!empty){
            water.draw(g2);
        }         
        bucket.draw(g2);
        Service.line3D(g2, b1, b2, eye,Color.black);        
        if(emptying&&!empty){
             drops.draw(g2,bucket);
        }        
    }
    //write name on top of the helicopter
    public void drawName(Graphics2D g2){
        MyPoint3D p=new MyPoint3D(prop.getCenter());
        p.y-=10;
        p.x-=5;
        p=Service.onePointPerespective(p, eye);
        g2.setColor(Color.black);
        g2.setFont(new Font("Tahoma",Font.BOLD,10));
        g2.drawString(name, (int)p.x,(int)p.y);
    }
    //moving the helicopter
    public void translate(double tx,double ty,double tz){        
            cyl.translate(tx, ty, tz);
            body.translate(tx, ty, tz);
            prop.translate(tx, ty, tz);
            bucket.translate(tx, ty, tz);
            b1.x+=tx;
            b1.y+=ty;
            b1.z+=tz;
            b2.x+=tx;
            b2.y+=ty;
            b2.z+=tz;    
            b3.x+=tx;
            b3.y+=ty;
            b3.z+=tz;  
            b4.x+=tx;  
            b4.z+=tz;  
            water.translate(tx, ty, tz);
            drops.translate(tx, ty, tz);
        
    }
    //translating the helicopter to the sides
    public void right(){
        translate(10,0,0);
        if(b1.y>=land.minY()&&b1.x<land.maxX()&&b1.x>land.minX()&&b1.z<land.maxZ()&&b1.z>land.minZ()||bucket.intersect(land))
            translate(-10,0,0);
    }
    public void left(){
        translate(-10,0,0); 
        if(b1.y>=land.minY()&&b1.x<land.maxX()&&b1.x>land.minX()&&b1.z<land.maxZ()&&b1.z>land.minZ()||bucket.intersect(land))
            translate(10,0,0);
    }    
    public void up(){
        translate(0,0,-10);  
        if(b1.y>=land.minY()&&b1.x<land.maxX()&&b1.x>land.minX()&&b1.z<land.maxZ()&&b1.z>land.minZ()||bucket.intersect(land))
            translate(0,0,10);
    }     
    public void down(){
        translate(0,0,10);  
        if(b1.y>=land.minY()&&b1.x<land.maxX()&&b1.x>land.minX()&&b1.z<land.maxZ()&&b1.z>land.minZ()||bucket.intersect(land))
            translate(0,0,-10);
    }
    //updating the program
    public void doWhatYouHaveToDo(){
        MyPoint3D p1=prop.getCenter(),p2=new MyPoint3D(p1);
        p2.y++;
        prop.rotate(degree, p1, p2);
        if(body.maxZ()>-250)
            translate(0,0,-250-body.maxZ());
        if(body.minZ()<-2100){
            translate(0,0,-body.minZ()-2100);
        }
        if(body.maxX()>land.maxX()+200)
            translate(land.maxX()+200-body.maxX(),0,0);
        if(body.minX()<land.minX()-200)
            translate(land.minX()-200-body.minX(),0,0);        
        if(bucket.minY()>land.minY()&&!bucket.intersect(land))
             fill();       
        if(bucket.maxY()<land.minY())
            empty();
        else 
            emptying=false;
    }
    //moving the helicopter automatic
    public void autoMove(Helicopter other){
        if(other==null){
            if(empty){
              if(body.maxX()<land.minX()&&bucket.minY()<land.maxY()) {
                  drop();
              }
              else
                  left();
            }
        }
        doWhatYouHaveToDo();
    }
    public void moveToWater(){
        
    }
    //droping and lifting the bucket
    public void drop(){
        bucket.translate(0,5,0);
        water.translate(0,5,0);
        drops.translate(0,5,0);
        b1.y+=5;
        b3.y+=5;
        if(bucket.intersect(land)){
            bucket.translate(0,-5,0);
            water.translate(0,-5,0);
            drops.translate(0,-5,0);
            b1.y-=5;
            b3.y-=5; 
        }
    }
    public void lift(){
        
       bucket.translate(0,-5,0);
       water.translate(0,-5,0);
       drops.translate(0,-5,0);
       b1.y-=5;
       b3.y-=5;
       if(bucket.intersect(land)||b1.y<body.maxY()){
            bucket.translate(0,5,0);
            water.translate(0,5,0);
            drops.translate(0,5,0);
            b1.y+=5;
            b3.y+=5;           
       }
    }
    //filling and emptying the bucket
    public void fill(){
        if(water.vertices[0].y>bucket.vertices[0].y){
            for(int i=0;i<12;i++){
                water.vertices[i].y-=0.5;
            }
            emptying=false;
            empty=false;
        }    
       
    }
    public void empty(){
        if(water.vertices[0].y<water.vertices[12].y){
            for(int i=0;i<12;i++){
                water.vertices[i].y+=0.1;
            }
            emptying=true;     
            drops.empty(land.minY());
        }
        else{
            empty=true;
            emptying=false;
            drops.reMake();
        }
    }
    //geters
    public MyPoint3D getB4() {
        return b4;
    }

    public Body3D getBucket() {
        return bucket;
    }

    public boolean isEmptying() {
        return emptying;
    }

    public boolean isEmpty() {
        return empty;
    }
    public double maxY(){
        return bucket.maxY();
    }
    public double minY(){
        return bucket.minY();
    }
    public double maxZ(){
        return bucket.maxZ();
    }
    public double minZ(){
        return bucket.minZ();
    }
    public MyPoint3D getCenter(){
        return body.getCenter();
    }

    public Drops getDrops() {
        return drops;
    }
    //seters
    public void setLight(MyPoint3D light) {
        this.light = light;
    }
    public void setColor(Color color){
        this.body.setFillColor(color);
        this.body.setDrawColor(color);
    }
    public void setName(String name) {
        this.name = name;
    }
    //other helicopter intersection
    public boolean intersect(Helicopter helicopter){
        MyPoint3D p1=new MyPoint3D(this.getCenter()),p2=new MyPoint3D(helicopter.getCenter());
        p1.y=0;
        p2.y=0;
        return Service.length(p1, p2)<=propLength;
    }
    //arry two helicopters by z valus
    public Helicopter[] arryHelicopters(Helicopter helicopter){
        Helicopter[] hel=new Helicopter[2]; 
        if(helicopter.maxZ()>this.maxZ()){
          hel[0]=helicopter;
          hel[1]=this;
        }
        else{
            hel[0]=this;
            hel[1]=helicopter;
        }
        return hel;
    }
    
    
}
