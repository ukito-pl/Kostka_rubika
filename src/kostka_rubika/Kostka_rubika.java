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
    boolean zplus =false;
    boolean zminus =false;
    boolean xplus =false;
    boolean xminus =false;
    TransformGroup kostka;
    Timer tm = new Timer(1,this);
    
    
   
    
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
        //p_kostki.set(new Vector3f(-0.4f,0.0f,0.0f));                             //przemieszczenie w przestrzeni o wektor

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
       
        

        TransformGroup transformacja_k = new TransformGroup(pio);
        transformacja_k.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        ColorCube cc = new ColorCube(0.1);
        QuadArray kostka = new QuadArray(24, QuadArray.COORDINATES
                | QuadArray.COLOR_4);
        kostka = (QuadArray) cc.getGeometry();
        for(int i = 0; i <= 3; i++){
            kostka.setColor(i, new Color3f(1,1,1));
        }
        for(int i = 4; i <= 7; i++){
            kostka.setColor(i, new Color3f(0,1,0));
        }
        for(int i = 8; i <= 11; i++){
            kostka.setColor(i, new Color3f(1,0,0));
        }
        for(int i = 12; i <= 15; i++){
            kostka.setColor(i, new Color3f(0,0,1));
        }
        for(int i = 16; i <= 19; i++){
            kostka.setColor(i, new Color3f(1,1,0));
        }
        for(int i = 20; i <= 23; i++){
            kostka.setColor(i, new Color3f(1,0.6f,0));
        }
        
        Shape3D kostka1 = new Shape3D(kostka);
        transformacja_k.addChild(kostka1);
    
        return transformacja_k;
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
                        zplus = true;
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        xplus = true;
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        xminus = true;
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        zminus = true;
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
                        zplus = false;
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        xplus = false;
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        xminus = false;
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        zminus = false;
                        break; 
                    }
                    case KeyEvent.VK_E:  
                        break;
                    }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (zplus) katZ = katZ +1;
        if (zminus) katZ = katZ -1;
        if (xplus) katX = katX +1;
        if (xminus) katX = katX -1;
        try {kostka.setTransform(obrot());}
        catch(java.lang.NullPointerException b){
        }
    }

    
    
}
