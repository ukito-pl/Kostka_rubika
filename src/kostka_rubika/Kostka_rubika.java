/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kostka_rubika;
import static java.lang.Math.*;


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
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author ukito && Qahu ;)
 */
public class Kostka_rubika extends JFrame implements  ActionListener, KeyListener{
    
    int katX[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    int katY[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    int katZ[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    int katXDocelowy[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    int katYDocelowy[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    int katZDocelowy[][][]={{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}},{{0,0,0},{0,0,0},{0,0,0}}};
    boolean zplus =false;
    boolean zminus =false;
    boolean xplus =false;
    boolean xminus =false;
    boolean yplus =false;
    boolean yminus =false;
    boolean obroconoZ = false;
    boolean obroconoY = false;
    boolean obroconoX = false;
    BranchGroup scena;
    TransformGroup transformacja_kostka;
    Timer tm = new Timer(3,this);
    TransformGroup kostki[][][] = new TransformGroup[3][3][3];
    int nrScianyI=5;
    int nrScianyJ=5;
    int nrScianyK=5;
    int nr =1;
    
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
        canvas3D.addKeyListener(this);
        
        add(canvas3D);
        pack();
        setVisible(true);
        int licz = 0;   
        for (int i=0; i<=2; i++){
            for (int j=0; j<=2; j++){
                for (int k=0; k<=2; k++){
                    kostki[i][j][k] = utworzKostki(licz);
                    licz++;
                }
            }
        }
        licz=0;      
        
        scena = new BranchGroup();
        scena.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        scena.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        

        for (int i=0; i<=2; i++){
            for (int j=0; j<=2; j++){
                for (int k=0; k<=2; k++){
                    scena.addChild(kostki[i][j][k]);
                }
            }
        }
        
        scena.setCapability(BranchGroup.ALLOW_DETACH);
        scena.compile();
        
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        przesuniecie_obserwatora.set(new Vector3f(0.0f,0.0f,2.0f));

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        
        OrbitBehavior orbit = new OrbitBehavior(canvas3D);
        orbit.setSchedulingBounds(new BoundingSphere());
        simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        simpleU.addBranchGraph(scena);
    }
    
   
    public TransformGroup utworzKostki(int j){
        
        BranchGroup aaa = new BranchGroup();
        aaa.setCapability(BranchGroup.ALLOW_DETACH);
        
        transformacja_kostka = new TransformGroup();
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
        
        Shape3D szescian = new Shape3D(szescianGeom);
        szescian.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        
        Appearance wyglad = new Appearance(); 
        TransparencyAttributes transp = new TransparencyAttributes();
        transp.setTransparency(0.15f);
        transp.setTransparencyMode(3);
        wyglad.setTransparencyAttributes(transp);
        if (j>=18)
        szescian.setAppearance(wyglad);;
        
        
        float[] przesuniecieX = {-0.202f,0f,0.202f,-0.202f,0f,0.202f,-0.202f,0f,0.202f,-0.202f,0f,0.202f,-0.202f,0f,0.202f,-0.202f,0f,0.202f,
                                -0.202f,0f,0.202f,-0.202f,0f,0.202f,-0.202f,0f,0.202f};
        float[] przesuniecieY = {0.202f,0.202f,0.202f,0f,0f,0f,-0.202f,-0.202f,-0.202f,0.202f,0.202f,0.202f,0f,0f,0f,-0.202f,-0.202f,-0.202f,
                                0.202f,0.202f,0.202f,0f,0f,0f,-0.202f,-0.202f,-0.202f};
        float[] przesuniecieZ = {-0.202f,-0.202f,-0.202f,-0.202f,-0.202f,-0.202f,-0.202f,-0.202f,-0.202f,0f,0f,0f,0f,0f,0f,0f,0f,0f,
                                0.202f,0.202f,0.202f,0.202f,0.202f,0.202f,0.202f,0.202f,0.202f};
 
        transformacja_kostka.addChild(dodajSzescian(szescian, new Vector3f(przesuniecieX[j],przesuniecieY[j],przesuniecieZ[j])));
        return transformacja_kostka;
    }
    
    
    public void trans(){
        if (nrScianyI!=5)
            for (int j=0; j<=2; j++){
                for (int k=0; k<=2; k++){
                    kostki[nrScianyI][j][k].setTransform(obrot(nrScianyI,j,k));
                }
            }
        else if (nrScianyJ!=5)
            for (int i=0; i<=2; i++){
                for (int k=0; k<=2; k++){
                    kostki[i][nrScianyJ][k].setTransform(obrot(i,nrScianyJ,k));
                }
            }
        else if (nrScianyK!=5)
            for (int i=0; i<=2; i++){
                for (int j=0; j<=2; j++){
                    kostki[i][j][nrScianyK].setTransform(obrot(i,j,nrScianyK));
                }
            }
    }
    
    public void zmien_sciane(int i, int j, int k){
        nrScianyI = i;
        nrScianyJ = j;
        nrScianyK = k;    
    }
    
     public Transform3D obrot(int i, int j, int k){
        
        Transform3D  p_kostki   = new Transform3D();

        Transform3D  tmp_rotX      = new Transform3D();
        tmp_rotX.rotX(PI/180*katX[i][j][k]);
        p_kostki.mul(tmp_rotX);
        Transform3D  tmp_rotY      = new Transform3D();
        tmp_rotY.rotY(PI/180*katY[i][j][k]);      
        p_kostki.mul(tmp_rotY);
        Transform3D  tmp_rotZ      = new Transform3D();
        tmp_rotZ.rotZ(PI/180*katZ[i][j][k]);
        p_kostki.mul(tmp_rotZ);            
     
        return p_kostki;
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
                        zmien_sciane(5,5,5);
                        break; 
                    }
                    case KeyEvent.VK_1:
                    {
                      
                        zmien_sciane(0,5,5);
                        break;
                    }
                    case KeyEvent.VK_2:
                    {
                        nr=1;
                        zmien_sciane(1,5,5);
                        break;
                    }
                    case KeyEvent.VK_3:
                    {
                        nr=2;
                        zmien_sciane(2,5,5);
                        break;
                    }
                    case KeyEvent.VK_4:
                    {
                        nr=3;
                        zmien_sciane(5,0,5);
                        break;
                    }
                    case KeyEvent.VK_5:
                    {
                        nr=4;
                        zmien_sciane(5,1,5);
                        break;
                    }
                    case KeyEvent.VK_6:
                    {
                        nr=5;
                        zmien_sciane(5,2,5);
                        break;
                    }
                    case KeyEvent.VK_7:
                    {
                        nr=6;
                        zmien_sciane(5,5,0);
                        break;
                    }
                    case KeyEvent.VK_8:
                    {
                        nr=7;
                        zmien_sciane(5,5,1);
                        break;
                    }
                    case KeyEvent.VK_9:
                    {
                        nr=8;
                        zmien_sciane(5,5,2);
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
                    case KeyEvent.VK_P: 
                    {
                        yminus = true;
                        break;
                    }
                    case KeyEvent.VK_L: 
                    {  
                        yplus = true;
                        break;
                    }                      
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
                    case KeyEvent.VK_P: 
                    {
                        yminus = false;
                        break;
                    }
                    case KeyEvent.VK_L: 
                    {  
                        yplus = false;
                        break;
                    } 
        }            
    }

    @Override
    public void actionPerformed(ActionEvent e) {                        //ciut krĂłtsza wersja ale nie wiem czy Ci siÄ™ podoba ;) 

        if (nrScianyI!=5){
        if (!obroconoZ){        
            for (int j=0; j<=2; j++){
                for (int k=0; k<=2; k++){                                                          
                        if (zplus) katZDocelowy[nrScianyI][j][k] = katZ[nrScianyI][j][k] + 90;
                        else if (zminus) katZDocelowy[nrScianyI][j][k] = katZ[nrScianyI][j][k] - 90;
                        obroconoZ = true;                 
                }
            }      
        }
        else for (int j=0; j<=2; j++){
            for (int k=0; k<=2; k++){                                                         
                if (katZDocelowy[nrScianyI][j][k] > katZ[nrScianyI][j][k]) katZ[nrScianyI][j][k] = katZ[nrScianyI][j][k] + 1;
                else if (katZDocelowy[nrScianyI][j][k] < katZ[nrScianyI][j][k]) katZ[nrScianyI][j][k] = katZ[nrScianyI][j][k] -1;
                else obroconoZ = false;              
            }
        }      
        }
     
        else if (nrScianyJ!=5){
        if (!obroconoY){
            for (int i=0; i<=2; i++){
                for (int k=0; k<=2; k++){                                                         
                        if (yplus) katYDocelowy[i][nrScianyJ][k] = katY[i][nrScianyJ][k] + 90;
                        else if (yminus) katYDocelowy[i][nrScianyJ][k] = katY[i][nrScianyJ][k] - 90;
                        obroconoY = true;             
                }
            }
        }
        else for (int i=0; i<=2; i++){
            for (int k=0; k<=2; k++){
                if (katYDocelowy[i][nrScianyJ][k] > katY[i][nrScianyJ][k]) katY[i][nrScianyJ][k] = katY[i][nrScianyJ][k] + 1;
                else if (katYDocelowy[i][nrScianyJ][k] < katY[i][nrScianyJ][k]) katY[i][nrScianyJ][k] = katY[i][nrScianyJ][k] -1;
                else  obroconoY = false;
            }
        }
        }
        
        else if (nrScianyK!=5){
        if (!obroconoX){
            for (int i=0; i<=2; i++){
                for (int j=0; j<=2; j++){                
                        if (xplus) katXDocelowy[i][j][nrScianyK] = katX[i][j][nrScianyK] + 90;
                        else if (xminus) katXDocelowy[i][j][nrScianyK] = katX[i][j][nrScianyK] - 90;
                        obroconoX = true;             
                }                   
            }
        } 
        else for (int i=0; i<=2; i++){
            for (int j=0; j<=2; j++){
                if (katXDocelowy[i][j][nrScianyK] > katX[i][j][nrScianyK]) katX[i][j][nrScianyK] = katX[i][j][nrScianyK] + 1;
                else if (katXDocelowy[i][j][nrScianyK] < katX[i][j][nrScianyK]) katX[i][j][nrScianyK] = katX[i][j][nrScianyK] -1;
                else  obroconoX = false;
            }
        }
        }
        
        try {trans();}
        catch(java.lang.NullPointerException b){}
    }

    
    
}