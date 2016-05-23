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
 * @author ukito
 */
public class Kostka_rubika extends JFrame implements  ActionListener, KeyListener{
    
    int katX[];     //przechowują kąty aktualnych sześcianów podpiętych do ściany obrotowej
    int katY[];     //
    int katZ[];     //
    int katXDocelowy[];
    int katYDocelowy[];
    int katZDocelowy[];
    int kolejnosc[][];
    int ilosc_obrotow[];
    int ilosc_obrotowXplus[];
    int ilosc_obrotowYplus[];
    int ilosc_obrotowZplus[];
    int ilosc_obrotowXminus[];
    int ilosc_obrotowYminus[];
    int ilosc_obrotowZminus[];
    int aktywna_sciana = 1;
    boolean zplus =false;
    boolean zminus =false;
    boolean xplus =false;
    boolean xminus =false;
    boolean yplus = false;
    boolean yminus = false;
    boolean obroconoZplus = false;
    boolean obroconoYplus = false;
    boolean obroconoXplus = false;
    boolean obroconoZminus = false;
    boolean obroconoYminus = false;
    boolean obroconoXminus = false;
    TransformGroup kostka;
    TransformGroup transformacja_kostka;
    TransformGroup przesunietySzescian[];
    TransformGroup sciana_do_obrotu;
    Transform3D przesuniecie[];
    BranchGroup szescian[];
    Shape3D szescianShape[];
    Vector3f przesunietySzescianPolozenie[];
    Vector3f przesunietySzescianPolozeniePocz[];
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
        
        kolejnosc = new int[100][27];
        ilosc_obrotow= new int[27];
        ilosc_obrotowXplus= new int[27];
        ilosc_obrotowYplus= new int[27];
        ilosc_obrotowZplus= new int[27];
        ilosc_obrotowXminus= new int[27];
        ilosc_obrotowYminus= new int[27];
        ilosc_obrotowZminus= new int[27];
        katX = new int[27];
        katY = new int[27];
        katZ = new int[27];
        katXDocelowy = new int[27];
        katYDocelowy = new int[27];
        katZDocelowy = new int[27];
        for (int i = 0; i<9; i++){
            katX[i] = 0;
            katY[i] = 0;
            katZ[i] = 0;
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
        
        simpleU.getCanvas().addKeyListener(this);  
        
        simpleU.addBranchGraph(scena);
    }
    
    public void obrot(){
        
        int k = 0;
        for (int i = 0; i <= 26; i++){
            for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                if( szescian[i] == sciana_do_obrotu.getChild(j)){
                    Transform3D  p_kostki   = new Transform3D();                    
                    
                    int obrocono_Xplus = ilosc_obrotowXplus[i] - 1;
                    int obrocono_Yplus = ilosc_obrotowYplus[i] - 1;
                    int obrocono_Zplus = ilosc_obrotowZplus[i] - 1;
                    int obrocono_Xminus = ilosc_obrotowXminus[i] - 1;
                    int obrocono_Yminus = ilosc_obrotowYminus[i] - 1;
                    int obrocono_Zminus = ilosc_obrotowZminus[i] - 1;
                    int obrocono_X = ilosc_obrotowXplus[i] + ilosc_obrotowXminus[i] - 1;
                    int obrocono_Y = ilosc_obrotowYplus[i] + ilosc_obrotowYminus[i] - 1;
                    int obrocono_Z = ilosc_obrotowZplus[i] + ilosc_obrotowZminus[i] - 1;
                    int kat_X = katX[k];
                    int kat_Y = katY[k];
                    int kat_Z = katZ[k];

                    for(int a = 1; a <= ilosc_obrotow[i]; a++){
                        switch(kolejnosc[a][i]){
                            case 1: Transform3D  tmp_rotXplus      = new Transform3D();
                                    tmp_rotXplus.rotX(PI/180*(kat_X - 90*obrocono_X));
                                    kat_X = 90*obrocono_X;
                                    obrocono_X--;
                                    p_kostki.mul(tmp_rotXplus);
                                    break;
                            case -1: Transform3D  tmp_rotXminus      = new Transform3D();
                                    tmp_rotXminus.rotX(PI/180*(-kat_X + 90*obrocono_X));
                                    kat_X =  90*obrocono_X;
                                    obrocono_X--;
                                    p_kostki.mul(tmp_rotXminus);
                                    break;
                            case 2: Transform3D  tmp_rotYplus      = new Transform3D();
                                    tmp_rotYplus.rotY(PI/180*(kat_Y - 90*obrocono_Y));
                                    kat_Y = 90*obrocono_Y;
                                    obrocono_Y--;
                                    p_kostki.mul(tmp_rotYplus);
                                    break;
                            case -2:Transform3D  tmp_rotYminus      = new Transform3D();
                                    tmp_rotYminus.rotY(PI/180*(-kat_Y + 90*obrocono_Y));
                                    kat_Y =  90*obrocono_Y;
                                    obrocono_Y--;
                                    p_kostki.mul(tmp_rotYminus);
                                    break;
                            case 3: Transform3D  tmp_rotZplus      = new Transform3D();
                                    tmp_rotZplus.rotZ(PI/180*(kat_Z - 90*obrocono_Z));
                                    kat_Z = 90*obrocono_Z;
                                    obrocono_Z--;
                                    p_kostki.mul(tmp_rotZplus);
                                    break;
                            case -3:Transform3D  tmp_rotZminus     = new Transform3D();
                                    tmp_rotZminus.rotZ(PI/180*(-kat_Z + 90*obrocono_Z));
                                    kat_Z =  90*obrocono_Z;
                                    obrocono_Z--;
                                    p_kostki.mul(tmp_rotZminus);
                                    break;
                        }
                    }
                    
                    przesunietySzescian[i].setTransform(p_kostki);
                    przesunietySzescianKaty[i].x = katX[k];
                    przesunietySzescianKaty[i].y = katY[k];
                    przesunietySzescianKaty[i].z = katZ[k];
                    k++;
                }
            }
                
        }
        
        
    }
    
    public void dodaj_kolejnosc(int os){
        //to generuje kolejność wykonywania rotacji dla każdej kostki odwrotną do kolejnosći obrotów wykonywanych przez użytkownika
       
        for (int i = 0; i <= 26; i++){
                for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                    if( szescian[i] == sciana_do_obrotu.getChild(j)){
                        ilosc_obrotow[i]++;
                        for (int a = ilosc_obrotow[i]; a > 0; a--){
                        if( a == 1){
                            switch(os){
                                case 1: kolejnosc[1][i] = 1; ilosc_obrotowXplus[i]++;  break;
                                case -1: kolejnosc[1][i] = -1; ilosc_obrotowXminus[i]++;  break;
                                case 2: kolejnosc[1][i] = 2; ilosc_obrotowYplus[i]++; break;
                                case -2: kolejnosc[1][i] = -2; ilosc_obrotowYminus[i]++; break;
                                case 3: kolejnosc[1][i] = 3; ilosc_obrotowZplus[i]++; break;
                                case -3: kolejnosc[1][i] = -3; ilosc_obrotowZminus[i]++; break;
                            }
                        }else{
                            kolejnosc[a][i] = kolejnosc[a-1][i];

                        }
                        }
                }
            }
        }
    }
    
    public void obliczPolozenie(int os,int obr){
        //zmienia wartości położenia każdego szescianu względem nieruchomego układu współrzędnych
        for (int i = 1; i <= 26; i++){
            for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                if( szescian[i] == sciana_do_obrotu.getChild(j)){
                    Vector3f obecnePolozenie = new Vector3f();
                    obecnePolozenie.x = przesunietySzescianPolozenie[i].x;
                    obecnePolozenie.y = przesunietySzescianPolozenie[i].y;
                    obecnePolozenie.z = przesunietySzescianPolozenie[i].z;


                    switch(os){
                        case 0: if(obr == 1 ){
                                    przesunietySzescianPolozenie[i].y = - obecnePolozenie.z;
                                    przesunietySzescianPolozenie[i].z = obecnePolozenie.y;
                                }
                                if(obr == -1 ){
                                    przesunietySzescianPolozenie[i].y = obecnePolozenie.z;
                                    przesunietySzescianPolozenie[i].z = - obecnePolozenie.y;
                                }
                                break;
                        case 1: if(obr == 1){
                                    przesunietySzescianPolozenie[i].x =  obecnePolozenie.z;
                                    przesunietySzescianPolozenie[i].z = - obecnePolozenie.x;
                                }
                                if(obr == -1){
                                    przesunietySzescianPolozenie[i].x = - obecnePolozenie.z;
                                    przesunietySzescianPolozenie[i].z =  obecnePolozenie.x;
                                }
                                break;
                        case 2: if(obr == 1){
                                    przesunietySzescianPolozenie[i].x = - obecnePolozenie.y;
                                    przesunietySzescianPolozenie[i].y = obecnePolozenie.x;
                                }
                                if(obr == -1){
                                    przesunietySzescianPolozenie[i].x =  obecnePolozenie.y;
                                    przesunietySzescianPolozenie[i].y = - obecnePolozenie.x;
                                }
                                break;      
                        }
                }
            }
        }
    }
            
                        
            
               
            
            
//            if(obr == 1 ){
//                przesunietySzescianPolozenie[i].y = - obecnePolozenie.z;
//                przesunietySzescianPolozenie[i].z = obecnePolozenie.y;
//               
//            }
//            if(ilosc_obrotow_X == 2 || ilosc_obrotow_X == -2){
//                przesunietySzescianPolozenie[i].y = - obecnePolozenie.y;
//                przesunietySzescianPolozenie[i].z = - obecnePolozenie.z;
//            }
//            if(obr == -1 ){
//                przesunietySzescianPolozenie[i].y = obecnePolozenie.z;
//                przesunietySzescianPolozenie[i].z = - obecnePolozenie.y;
//            }
            
            //wokół Y
//            int ilosc_obrotow_Y;
//            ilosc_obrotow_Y = (int) przesunietySzescianKaty[i].y;
//            while(ilosc_obrotow_Y > 3){
//                ilosc_obrotow_Y -= 4;
//            }
//            while(ilosc_obrotow_Y < -3){
//                ilosc_obrotow_Y += 4;
//            }
//            if(ilosc_obrotow_Y == 0){
//                przesunietySzescianPolozenie[i].x = obecnePolozenie.x;
//                przesunietySzescianPolozenie[i].z = obecnePolozenie.y; 
//            }
//            if(ilosc_obrotow_Y == 1 || ilosc_obrotow_Y == -3){
//                przesunietySzescianPolozenie[i].x =  obecnePolozenie.z;
//                przesunietySzescianPolozenie[i].z = - obecnePolozenie.z;
//                
//            }
//            if(ilosc_obrotow_Y == 2 || ilosc_obrotow_Y == -2){
//                przesunietySzescianPolozenie[i].x = - obecnePolozenie.x;
//                przesunietySzescianPolozenie[i].z = - obecnePolozenie.z;
//            }
//            if(ilosc_obrotow_Y == -1 || ilosc_obrotow_Y == 3){
//                przesunietySzescianPolozenie[i].x = - obecnePolozenie.z;
//                przesunietySzescianPolozenie[i].z = obecnePolozenie.z;
//                
//            }
            
            //wokół Z
//            int ilosc_obrotow_Z;
//            ilosc_obrotow_Z = (int) przesunietySzescianKaty[i].z;
//            while(ilosc_obrotow_Z > 3){
//                ilosc_obrotow_Z -= 4;
//            }
//            while(ilosc_obrotow_Z < -3){
//                ilosc_obrotow_Z += 4;
//            }
//            if(ilosc_obrotow_Z == 0){
//                przesunietySzescianPolozenie[i].y = obecnePolozenie.z;
//                przesunietySzescianPolozenie[i].x = obecnePolozenie.x; 
//            }
//            if(ilosc_obrotow_Z == 1 || ilosc_obrotow_Z == -3){
//                przesunietySzescianPolozenie[i].x = - obecnePolozenie.y;
//                przesunietySzescianPolozenie[i].y = obecnePolozenie.x;
//            }
//            if(ilosc_obrotow_Z == 2 || ilosc_obrotow_Z == -2){
//                przesunietySzescianPolozenie[i].x = - obecnePolozenie.x;
//                przesunietySzescianPolozenie[i].y = - obecnePolozenie.y;
//            }
//            if(ilosc_obrotow_Z == -1 || ilosc_obrotow_Z == 3){
//                przesunietySzescianPolozenie[i].x = obecnePolozenie.y;
//                przesunietySzescianPolozenie[i].y = - obecnePolozenie.x;
//            }
            
            
            
//        }
//    }
    
   
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
        przesunietySzescianPolozeniePocz = new Vector3f[27];
        //wspołrzędne każdego szescianu skłądającego się na kostkę:(wypadałoby zrobić to jakąś funkcją ale nie miałem pomysłu, więc wpisałem po chamsku współrzędne każdego sześcianu)
        float[] przesuniecieX = {0f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0.2f,0f,0f,0f,0f,0f,0f,0f,0f,
                                 -0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f,-0.2f};
        float[] przesuniecieY = {0f,0f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,0f,0f,0.2f,0.2f,0.2f, -0.2f,-0.2f,-0.2f,
                                0f,0f,0f, 0.2f,0.2f,0.2f, -0.2f,-0.2f, -0.2f};
        float[] przesuniecieZ = {0f,0f,0.2f,-0.2f,0f,0.2f, -0.2f,0f,0.2f, -0.2f,0.2f,-0.2f,0f,0.2f,-0.2f,0f,0.2f,-0.2f, 
                                0f,0.2f,-0.2f,0f,0.2f,-0.2f,-0.2f,0.2f,0f};
        
        przesunietySzescianKaty = new Vector3f[27];
        
        szescian = new BranchGroup[27];
        przesuniecie = new Transform3D[27];
        
        for(int i=1; i <= 26; i++){
            przesuniecie[i] = new Transform3D();
            
            szescianShape[i] = new Shape3D(szescianGeom);
            szescianShape[i].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            przesunietySzescian[i] = new TransformGroup();
            przesunietySzescian[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            przesunietySzescianKaty[i] = new Vector3f(0f,0f,0f);
            przesunietySzescianPolozenie[i] = new Vector3f(przesuniecieX[i],przesuniecieY[i],przesuniecieZ[i]);  
            przesunietySzescianPolozeniePocz[i] = new Vector3f(przesuniecieX[i],przesuniecieY[i],przesuniecieZ[i]);  
            przesunietySzescian[i].addChild(dodajSzescian(szescianShape[i], i));
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
                    //ustawienie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(3);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);
                    
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);
                    //zresetowanie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(4);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);

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
                    //ustawienie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(3);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);
                    
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);
                    //zresetowanie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(4);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);

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
                    //ustawienie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(3);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);
                    
                    katX[k] = (int) przesunietySzescianKaty[i].x;
                    katY[k] = (int) przesunietySzescianKaty[i].y;
                    katZ[k] = (int) przesunietySzescianKaty[i].z;
                    katXDocelowy[k] = katX[k];
                    katYDocelowy[k] = katY[k];
                    katZDocelowy[k] = katZ[k];
                    k++;
                }else {
                    transformacja_kostka.addChild(szescian[i]);
                    //zresetowanie przezroczystosci
                    Appearance wyglad = new Appearance(); 
                    TransparencyAttributes transp = new TransparencyAttributes();
                    transp.setTransparency(0.15f);
                    transp.setTransparencyMode(4);
                    wyglad.setTransparencyAttributes(transp);
                    TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
                    TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
                    Shape3D sze = (Shape3D) prz2.getChild(0);
                    sze.setAppearance(wyglad);

                }
            }
        }

    }
    
    public TransformGroup dodajSzescian(Shape3D szescian, int i){
        
        
        przesuniecie[i].set(przesunietySzescianPolozenie[i]);
        TransformGroup przesuniecieGr = new TransformGroup();
        przesuniecieGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        przesuniecieGr.addChild(szescian);
        przesuniecieGr.setTransform(przesuniecie[i]);
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
                        
                        if (aktywna_sciana == 1 || aktywna_sciana == 2){
                            xminus = true;
                        } else if(aktywna_sciana == 3 || aktywna_sciana == 4){
                            yminus = true;
                        } else if(aktywna_sciana == 5 || aktywna_sciana == 6) {
                            zminus = true;
                        }
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        
                        if (aktywna_sciana == 1 || aktywna_sciana == 2) xplus = true;
                        else if(aktywna_sciana == 3 || aktywna_sciana == 4) yplus = true;
                        else if(aktywna_sciana == 5 || aktywna_sciana == 6) zplus = true;
                        
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
                        aktywna_sciana = 1;
                        zmien_sciane_do_obrotu(new Vector3f(0.2f,0,0));
                        break;
                    }
                    case KeyEvent.VK_2:
                    {
                        aktywna_sciana = 2;
                        zmien_sciane_do_obrotu(new Vector3f(-0.2f,0,0));
                        break;
                    }
                    case KeyEvent.VK_3:
                    {
                        aktywna_sciana = 3;
                        zmien_sciane_do_obrotu(new Vector3f(0,0.2f,0));
                        break;
                    }
                    case KeyEvent.VK_4:
                    {
                        aktywna_sciana = 4;
                        zmien_sciane_do_obrotu(new Vector3f(0,-0.2f,0));
                        break;
                    }
                    case KeyEvent.VK_5:
                    {
                        aktywna_sciana = 5;
                        zmien_sciane_do_obrotu(new Vector3f(0,0,0.2f));
                        break;
                    }
                    case KeyEvent.VK_6:
                    {
                        aktywna_sciana = 6;
                        zmien_sciane_do_obrotu(new Vector3f(0,0,-0.2f));
                        break;
                    }
                    case KeyEvent.VK_RIGHT:  
                    {
                        zminus = false;
                        zplus = false;
                        yminus = false;
                        yplus = false;
                        xminus = false;
                        xplus = false;
                        break; 
                    }
                    case KeyEvent.VK_UP:  
                    {
                        
                        break; 
                    }
                    case KeyEvent.VK_DOWN:  
                    {
                        
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        zminus = false;
                        zplus = false;
                        yminus = false;
                        yplus = false;
                        xminus = false;
                        xplus = false;
                        
                        break; 
                    }
                    case KeyEvent.VK_E:  
                        break;
                    }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (zplus && !obroconoZplus) {
                for(int i = 0; i < 9; i++){
                    katZDocelowy[i] = katZ[i] + 90;
                }
                obroconoZplus = true;
                
                dodaj_kolejnosc(3);
            }
            if (katZDocelowy[0] > katZ[0]){
                for(int i = 0; i < 9; i++){
                    katZ[i] = katZ[i] +1;
                }
            }else if (katZDocelowy[0] == katZ[0]){
                if(obroconoZplus) obliczPolozenie(2,1);
                obroconoZplus = false;
                
            }
            //
            if (zminus && !obroconoZminus) {
                for(int i = 0; i < 9; i++){
                    katZDocelowy[i] = katZ[i] + 90;
                }
                obroconoZminus = true;
                dodaj_kolejnosc(-3);
            }
            if (katZDocelowy[0] < katZ[0]){
                for(int i = 0; i < 9; i++){
                    katZ[i] = katZ[i] +1;
                }
            }else if (katZDocelowy[0] == katZ[0]){
                if(obroconoZminus) obliczPolozenie(2,-1);
                obroconoZminus = false;
            }
            ////////////////////////
            if (yplus && !obroconoYplus) {
                for(int i = 0; i < 9; i++){
                    katYDocelowy[i] = katY[i] + 90;
                }
                obroconoYplus = true;
                
                dodaj_kolejnosc(2);
            }
            if (katYDocelowy[0] > katY[0]){
                for(int i = 0; i < 9; i++){
                    katY[i] = katY[i] +1;
                }
            }else if (katYDocelowy[0] == katY[0]){
                if(obroconoYplus) obliczPolozenie(1,1);
                obroconoYplus = false;
                
            }
            //
            if (yminus && !obroconoYminus) {
                for(int i = 0; i < 9; i++){
                    katYDocelowy[i] = katY[i] + 90;
                }
                obroconoYminus = true;
                dodaj_kolejnosc(-2);
            }
            if (katYDocelowy[0] < katY[0]){
                for(int i = 0; i < 9; i++){
                    katY[i] = katY[i] +1;
                }
            }else if (katYDocelowy[0] == katY[0]){
                if(obroconoYminus) obliczPolozenie(1,-1);
                obroconoYminus = false;
            }
            ////////////////////////    
            if (xplus && !obroconoXplus) {
                for(int i = 0; i < 9; i++){
                    katXDocelowy[i] = katX[i] + 90;
                }
                obroconoXplus = true;
                dodaj_kolejnosc(1);
            }
            if (katXDocelowy[0] > katX[0]){
                for(int i = 0; i < 9; i++){
                    katX[i] = katX[i] +1;
                }
            }else if (katXDocelowy[0] == katX[0]){
                if(obroconoXplus) obliczPolozenie(0,1);
                obroconoXplus = false;
            }
            //
            if (xminus && !obroconoXminus) {
                for(int i = 0; i < 9; i++){
                    katXDocelowy[i] = katX[i] + 90;
                }
                obroconoXminus = true;
                dodaj_kolejnosc(-1);
            }
            if (katXDocelowy[0] < katX[0]){
                for(int i = 0; i < 9; i++){
                    katX[i] = katX[i] +1;
                }
            }else if (katXDocelowy[0] == katX[0]){
                if(obroconoXminus){
                    obliczPolozenie(0,-1);
                }
                
                obroconoXminus = false;
            }

        
        
           
            obrot();
        }
        catch(java.lang.NullPointerException b){
        }
    }
}
