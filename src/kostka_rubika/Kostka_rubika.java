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
    
    int katX[];
    int katY[];
    int katZ[];
    int katXDocelowy[];
    int katYDocelowy[];
    int katZDocelowy[];
    boolean zplus =false;
    boolean zminus =false;
    boolean xplus =false;
    boolean xminus =false;
    boolean obroconoZ = false;
    boolean obroconoX = false;
    TransformGroup kostka;
    TransformGroup transformacja_kostka;
    TransformGroup przesunietySzescian[];
    TransformGroup sciana_do_obrotu;
    BranchGroup szescian[];
    Shape3D szescianShape[];
    Vector3f przesunietySzescianPolozenie[];
    Vector3f przesunietySzescianKaty[];
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
        
        katX = new int[9];
        katY = new int[9];
        katZ = new int[9];
        katXDocelowy = new int[9];
        katYDocelowy = new int[9];
        katZDocelowy = new int[9];
        for (int i = 0; i<9; i++){
            katX[i] = 0;
            katY[i] = 0;
            katY[i] = 0;
            katXDocelowy[i] = 0;
            katYDocelowy[i] = 0;
            katZDocelowy[i] = 0;
        }
        
        kostka = utworzKostke();
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
    
    public void obrot(){
        
        int k = 0;
        for (int i = 0; i <= 26; i++){
            for(int j = 0; j <= 8; j++){
                if(szescian[i] == sciana_do_obrotu.getChild(j)){
                    Transform3D  p_kostki   = new Transform3D();                    

                    Transform3D  tmp_rotX      = new Transform3D();
                    tmp_rotX.rotX(PI/180*katX[k]);
                    p_kostki.mul(tmp_rotX);
                    Transform3D  tmp_rotY      = new Transform3D();
                    tmp_rotY.rotY(PI/180*katY[k]);      //nieużywane
                    p_kostki.mul(tmp_rotY);
                    Transform3D  tmp_rotZ      = new Transform3D();
                    tmp_rotZ.rotZ(PI/180*katZ[k]);
                    p_kostki.mul(tmp_rotZ);
                    
                    przesunietySzescian[i].setTransform(p_kostki);
                    przesunietySzescianKaty[i].x = katX[k];
                    przesunietySzescianKaty[i].y = katY[k];
                    przesunietySzescianKaty[i].z = katZ[k];
                    k++;
                }
            }
                
        }
        
        
    }
    
   
    public TransformGroup utworzKostke(){
       
        

        transformacja_kostka = new TransformGroup();
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        
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
        
        szescianShape = new Shape3D[27];
        szescianShape[0] = new Shape3D(szescianGeom);
        transformacja_kostka.addChild(szescianShape[0]);
        przesunietySzescian = new TransformGroup[27];
        przesunietySzescianPolozenie = new Vector3f[27];
        //wspołrzędne każdego szescianu skłądającego się na kostkę:(wypadałoby zrobić to jakąś funkcją ale nie miałem pomysłu, więc wpisałem po chamsku współrzędne każdego sześcianu)
        float[] przesuniecieX = {0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0f,0f,0f,0f,0f,0f,0f,0f,
                                 -0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f};
        float[] przesuniecieY = {0f,0f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,
                                0f,0f,0f, 0.2f,0.2f,0.2f, -0.2f,-0.2f, -0.2f};
        float[] przesuniecieZ = {0f,0f,0.2f,-0.2f,0f,0.2f, -0.2f,0f,0.2f, -0.2f,0.2f,-0.2f,0f,0.2f,-0.2f,0f,0.2f,-0.2f, 
                                0f,0.2f,-0.2f,0f,0.2f,-0.2f,-0.2f,0.2f,0f};
        
        przesunietySzescianKaty = new Vector3f[27];
        
        szescian = new BranchGroup[27];
        
        
        for(int i=1; i <= 26; i++){
            szescianShape[i] = new Shape3D(szescianGeom);
            przesunietySzescian[i] = new TransformGroup();
            przesunietySzescian[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            przesunietySzescianKaty[i] = new Vector3f(0,0,0);
            przesunietySzescianPolozenie[i] = new Vector3f(przesuniecieX[i],przesuniecieY[i],przesuniecieZ[i]);  
            przesunietySzescian[i].addChild(dodajSzescian(szescianShape[i], przesunietySzescianPolozenie[i]));
            szescian[i] = new BranchGroup();
            szescian[i].setCapability(BranchGroup.ALLOW_DETACH);
            szescian[i].addChild(przesunietySzescian[i]);
        }
        for (int i = 1; i <=26; i++ ){
            transformacja_kostka.addChild(szescian[i]);
        }
        
        
        
        sciana_do_obrotu = new TransformGroup();
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        
        int k = 0;
        for(int i = 1; i <= 26; i++){
            if (przesunietySzescianPolozenie[i].x == 0.2f){
                transformacja_kostka.removeChild(szescian[i]);
                sciana_do_obrotu.addChild(szescian[i]);
                katX[k] = (int) przesunietySzescianKaty[i].x;
                katY[k] = (int) przesunietySzescianKaty[i].y;
                katZ[k] = (int) przesunietySzescianKaty[i].z;
                katXDocelowy[k] = katX[k];
                katYDocelowy[k] = katY[k];
                katZDocelowy[k] = katZ[k];
                k++;
            }
        }
        
        transformacja_kostka.addChild(sciana_do_obrotu);
    
        return transformacja_kostka;
    }
    
    public void zmien_sciane_do_obrotu(Vector3f sciana){
        int k = 0;
        if(sciana.x != 0){
            for(int i=1; i <= 26; i++){
                transformacja_kostka.removeChild(szescian[i]);
                sciana_do_obrotu.removeChild(szescian[i]);
                if (przesunietySzescianPolozenie[i].x == sciana.x){
                    sciana_do_obrotu.addChild(szescian[i]);
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);

                }
            }
        }
        k = 0;
        if(sciana.y != 0){
            for(int i=1; i <= 26; i++){
                transformacja_kostka.removeChild(szescian[i]);
                sciana_do_obrotu.removeChild(szescian[i]);
                if (przesunietySzescianPolozenie[i].y == sciana.y){
                    sciana_do_obrotu.addChild(szescian[i]);
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);

                }
            }
        }
        k = 0;
        if(sciana.z != 0){
            for(int i=1; i <= 26; i++){
                transformacja_kostka.removeChild(szescian[i]);
                sciana_do_obrotu.removeChild(szescian[i]);
                if (przesunietySzescianPolozenie[i].z == sciana.z){
                    sciana_do_obrotu.addChild(szescian[i]);
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);

                }
            }
        }

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
                        zmien_sciane_do_obrotu(new Vector3f(-0.2f,0,0));
                      break; 
                    }
                    case KeyEvent.VK_1:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(0.2f,0,0));
                        break;
                    }
                    case KeyEvent.VK_2:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(-0.2f,0,0));
                        break;
                    }
                    case KeyEvent.VK_3:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(0,0.2f,0));
                        break;
                    }
                    case KeyEvent.VK_4:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(0,-0.2f,0));
                        break;
                    }
                    case KeyEvent.VK_5:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(0,0,0.2f));
                        break;
                    }
                    case KeyEvent.VK_6:
                    {
                        zmien_sciane_do_obrotu(new Vector3f(0,0,-0.2f));
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
        try {
            if (zplus && !obroconoZ) {
                for(int i = 0; i < 9; i++){
                    katZDocelowy[i] = katZ[i] + 90;
                }
                obroconoZ = true;
            }
            if (katZDocelowy[0] > katZ[0]){
                for(int i = 0; i < 9; i++){
                    katZ[i] = katZ[i] +1;
                }

            }else if (katZDocelowy[0] == katZ[0]){
                obroconoZ = false;
            }

            if (zminus && !obroconoZ) {
                for(int i = 0; i < 9; i++){
                    katZDocelowy[i] = katZ[i] - 90;
                }
                obroconoZ = true;
            }
            if (katZDocelowy[0] < katZ[0]){
                for(int i = 0; i < 9; i++){
                    katZ[i] = katZ[i] -1;
                }
            }else if (katZDocelowy[0] == katZ[0]){
                obroconoZ = false;
            }

            if (xplus && !obroconoX) {
                for(int i = 0; i < 9; i++){
                    katXDocelowy[i] = katX[i] + 90;
                }
                obroconoX = true;
            }
            if (katXDocelowy[0] > katX[0]){
                for(int i = 0; i < 9; i++){
                    katX[i] = katX[i] +1;
                }
            }else if (katXDocelowy[0] == katX[0]){
                obroconoX = false;
            }

            if (xminus && !obroconoX) {
                for(int i = 0; i < 9; i++){
                    katXDocelowy[i] = katX[i] - 90;
                }
                obroconoX = true;
            }
            if (katXDocelowy[0] < katX[0]){
                for(int i = 0; i < 9; i++){
                    katX[i] = katX[i] -1;
                }
            }else if (katXDocelowy == katX){
                obroconoX = false;
            }

        
        
            //sciana_do_obrotu.setTransform(obrot());
            obrot();
        }
        catch(java.lang.NullPointerException b){
        }
    }
  
}
