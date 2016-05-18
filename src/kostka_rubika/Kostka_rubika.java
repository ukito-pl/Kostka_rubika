/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kostka_rubika;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.prism.paint.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.PI;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import static javax.media.j3d.Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE;
import static javax.media.j3d.Shape3D.ALLOW_APPEARANCE_WRITE;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author ukito
 */
public class Kostka_rubika extends JFrame implements  ActionListener, KeyListener{
    
    int katX=0;
    int katY=0;
    int katZ=0;
    int katXDocelowy=0;
    int katYDocelowy=0;
    int katZDocelowy=0;
    boolean zplus =false;
    boolean zminus =false;
    boolean xplus =false;
    boolean xminus =false;
    boolean obroconoZ = false;
    boolean obroconoX = false;
    TransformGroup kostka;
    Timer tm = new Timer(5,this);
    
    
   
    
    Kostka_rubika(){
        super("Kostka rubika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tm.start();
        
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(800,600));

        add(canvas3D);
        pack();
        setVisible(true);

        kostka = utworzKostke(obrot());
        BranchGroup scena = new BranchGroup();
        scena.addChild(kostka);
        scena.setCapability(BranchGroup.ALLOW_DETACH);
        scena.compile();
        
        
            
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,2.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        
        OrbitBehavior orbit = new OrbitBehavior(canvas3D);
        orbit.setSchedulingBounds(new BoundingSphere());
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        simpleU.getCanvas().addKeyListener(this);   // to rozwiązało problem z dzialaniem obrotu po obróceniu sceny myszką
        
        simpleU.addBranchGraph(scena);
    }
    
    public Transform3D obrot(){
      
        Transform3D  p_kostki   = new Transform3D();

        Transform3D  tmp_rotX      = new Transform3D();
        tmp_rotX.rotX(PI/180*katX);
        p_kostki.mul(tmp_rotX);
        Transform3D  tmp_rotY      = new Transform3D();
        tmp_rotY.rotY(PI/180*katY);      //nieużywane
        p_kostki.mul(tmp_rotY);
        Transform3D  tmp_rotZ      = new Transform3D();
        tmp_rotZ.rotZ(PI/180*katZ);
        p_kostki.mul(tmp_rotZ);    
        
        return p_kostki;
    }
    
   
    public TransformGroup utworzKostke(Transform3D pio){
       
        

        TransformGroup transformacja_kostka = new TransformGroup(pio);
        transformacja_kostka.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        ColorCube cc = new ColorCube(0.1);
        QuadArray szescianGeom = new QuadArray(24, QuadArray.COORDINATES
                | QuadArray.COLOR_4);
        szescianGeom = (QuadArray) cc.getGeometry();
        for(int i = 0; i <= 3; i++){
            szescianGeom.setColor(i, new Color3f(1,1,1));
        }
        for(int i = 4; i <= 7; i++){
            szescianGeom.setColor(i, new Color3f(0,1,0));
        }
        for(int i = 8; i <= 11; i++){
            szescianGeom.setColor(i, new Color3f(1,0,0));
        }
        for(int i = 12; i <= 15; i++){
            szescianGeom.setColor(i, new Color3f(0,0,1));
        }
        for(int i = 16; i <= 19; i++){
            szescianGeom.setColor(i, new Color3f(1,1,0));
        }
        for(int i = 20; i <= 23; i++){
            szescianGeom.setColor(i, new Color3f(1,0.6f,0));
        }
        
        Shape3D szescian[] = new Shape3D[27];
        szescian[0] = new Shape3D(szescianGeom);
        transformacja_kostka.addChild(szescian[0]);
        TransformGroup przesunietySzescian[] = new TransformGroup[27];
        //wspołrzędne każdego szescianu skłądającego się na kostkę:(wypadałoby zrobić to jakąś funkcją ale nie miałem pomysłu, więc wpisałem po chamsku współrzędne każdego sześcianu)
        float[] przesuniecieX = {0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0f,0f,0f,0f,0f,0f,0f,0f,
                                 -0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f};
        float[] przesuniecieY = {0f,0f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,
                                0f,0f,0f, 0.2f,0.2f,0.2f, -0.2f,-0.2f, -0.2f};
        float[] przesuniecieZ = {0f,0f,0.2f,-0.2f,0f,0.2f, -0.2f,0f,0.2f, -0.2f,0.2f,-0.2f,0f,0.2f,-0.2f,0f,0.2f,-0.2f, 
                                0f,0.2f,-0.2f,0f,0.2f,-0.2f,-0.2f,0.2f,0f};
        
        for(int i=1; i <= 26; i++){
            szescian[i] = new Shape3D(szescianGeom);
            przesunietySzescian[i] = new TransformGroup();
            przesunietySzescian[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            przesunietySzescian[i].addChild(dodajSzescian(szescian[i], new Vector3f(przesuniecieX[i],przesuniecieY[i],przesuniecieZ[i])));
        }
        for (int i = 1; i <=26; i++ ){
        transformacja_kostka.addChild(przesunietySzescian[i]);
        }
    
        return transformacja_kostka;
    }
    
    public TransformGroup dodajSzescian(Shape3D szescian, Vector3f wektorPrzesuniecia){
        
        Transform3D przesuniecie = new Transform3D();
        przesuniecie.set(wektorPrzesuniecia);
        TransformGroup przesuniecieGr = new TransformGroup();
        przesuniecieGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        przesuniecieGr.addChild(szescian);
        przesuniecieGr.setTransform(przesuniecie);
        return przesuniecieGr;
    }

    public static void main(String[] args) {
        
        new Kostka_rubika();
    }

    

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
                    case KeyEvent.VK_SPACE:  
                    {
                      break; 
                    }
                    case KeyEvent.VK_RIGHT:  
                    {
                        zminus = true;
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        xminus = true;
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        xplus = true;
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        zplus = true;
                        break; 
                    }
                    case KeyEvent.VK_E:  
                        break;
                    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
                    case KeyEvent.VK_SPACE:  
                    {
                      break; 
                    }
                    case KeyEvent.VK_RIGHT:  
                    {
                        zminus = false;
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        xminus = false;
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        xplus = false;
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        zplus = false;
                        
                        break; 
                    }
                    case KeyEvent.VK_E:  
                        break;
                    }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (zplus && !obroconoZ) {
            katZDocelowy = katZ + 90;
            obroconoZ = true;
        }
        if (katZDocelowy > katZ){
            katZ = katZ +1;
        }else if (katZDocelowy == katZ){
            obroconoZ = false;
        }
        
        if (zminus && !obroconoZ) {
            katZDocelowy = katZ - 90;
            obroconoZ = true;
        }
        if (katZDocelowy < katZ){
            katZ = katZ -1;
        }else if (katZDocelowy == katZ){
            obroconoZ = false;
        }
        
        if (xplus && !obroconoX) {
            katXDocelowy = katX + 90;
            obroconoX = true;
        }
        if (katXDocelowy > katX){
            katX = katX + 1;
        }else if (katXDocelowy == katX){
            obroconoX = false;
        }
        
        if (xminus && !obroconoX) {
            katXDocelowy = katX - 90;
            obroconoX = true;
        }
        if (katXDocelowy < katX){
            katX = katX -1;
        }else if (katXDocelowy == katX){
            obroconoX = false;
        }
        
        
        try {kostka.setTransform(obrot());}
        catch(java.lang.NullPointerException b){
        }
    }

    
    
}
