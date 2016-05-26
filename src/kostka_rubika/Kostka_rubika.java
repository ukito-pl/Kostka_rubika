/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kostka_rubika;
import static java.lang.Math.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import com.sun.prism.paint.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.PI;
import java.util.Random;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import static javax.media.j3d.Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE;
import static javax.media.j3d.Shape3D.ALLOW_APPEARANCE_WRITE;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Marek Buła & Jakub Dreliszak
 */
public class Kostka_rubika extends JFrame implements  ActionListener, KeyListener{
    
    int katX[];     //przechowują kąty aktualnych sześcianów podpiętych do ściany obrotowej
    int katY[];     //
    int katZ[];     //
    int katXDocelowy[];//przechowują docelowe kąty aktualnych sześcianów podpiętych do ściany obrotowej
    int katYDocelowy[];//
    int katZDocelowy[];//
    int kolejnosc[][];  //przechowuje kolejnosć obrotów o 90 stopni wokół określonych osi przy czym znak + oznacz obrót dodatni, a znak - obrót ujemny,
                        //1 - oś X, 2 - oś Y, 3 - oś Z, pierwszy element jest pusty
    int liczba_obrotow[];//liczba wykonanych obrotów każdego szesciana, liczba ta aktualizuje się po rozpoczęciu każdego obrotu
    int liczba_obrotowX[];
    int liczba_obrotowY[];
    int liczba_obrotowZ[];
    int aktywna_sciana ;
    int memory = 1000;  //liczba możliwych ruchów do wykonania
    float a = 0.2f; //szerokość każdego małego sześcianu
    float d = 0.0f; //odległosć między szescianami
    boolean zplus;  //przyjmuje wartość true gdy użytkownik chce obrócić śćianą
    boolean zminus; //
    boolean xplus;  //
    boolean xminus; //
    boolean yplus;  //
    boolean yminus; //
    boolean obroc_Zplus;    //przyjmuuje wartosc trye gdy wykonywany jest obrót
    boolean obroc_Yplus;    //
    boolean obroc_Xplus;    //
    boolean obroc_Zminus;   //
    boolean obroc_Yminus;   //
    boolean obroc_Xminus;   //
    //Kolejnosc podpięcia do siebie transformgroupów i branchgroupów:
    //(BG)scena <-- (TG)kostka <-- (TG)transformacja_kostka <-- (BG)szescian[] <-- (TG) przesunietySzescian[] <-- (T3D) przesuniecie[]
    //                                                                             (TG) przesunietySzescian[] <-- (Box) szescian_Box[]
    //                                                                             (TG) przesunietySzescian[] <-- (T3D) rot_szescianu[]
    //                             (TG)transformacja_kostka <-- (TG)sciana_do_obrotu <-- (BG)szescian[] <-- ...
    TransformGroup kostka;
    TransformGroup transformacja_kostka;
    TransformGroup szescian_tg[];
    TransformGroup sciana_do_obrotu;
    Transform3D przesuniecie[];
    BranchGroup szescian[];
    Box szescian_Box[];
    Vector3f szescianPolozenie[]; //określa położenie każdego szesciana względem układu ustalonego
    Vector3f szescianKaty[];      //określa kąty każdego szesciana względem układu ustalonego, przy czym wartości kątów są zawsze dodatnie
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
        
        inicjalizacja_zmiennych();

        kostka = utworzKostke();
        BranchGroup scena = new BranchGroup();
        scena.addChild(kostka);
        
        Background tlo = new Background(new Color3f(0.9f,0.9f,0.9f));
        tlo.setApplicationBounds(new BoundingSphere());
        scena.addChild(tlo);
        
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
        
        ImageIcon img = new ImageIcon("tekstury/Kostka_rubika_icon.png");
        setIconImage(img.getImage());
    }
    
    public TransformGroup utworzKostke(){
       
        

        transformacja_kostka = new TransformGroup();
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        transformacja_kostka.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        
        
        szescian_Box= new Box[27];

        szescian_tg = new TransformGroup[27];
        szescianPolozenie = new Vector3f[27];
        szescianKaty = new Vector3f[27];
        ustaw_polozenie_pocz_szescianow();
        
        szescian = new BranchGroup[27];
        przesuniecie = new Transform3D[27];
        
        Appearance wyglad = new Appearance();
        
        for(int i=0; i <= 26; i++){
            przesuniecie[i] = new Transform3D();
            
            szescian_Box[i] = new Box(a/2,a/2,a/2,Primitive.GENERATE_TEXTURE_COORDS, wyglad);
            szescian_Box[i].setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            Appearance wyglad_sciany1 = new Appearance();
            wyglad_sciany1.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany1.setTexture(zaladuj_teksture("tekstury/sciana1.png"));
            szescian_Box[i].setAppearance(Box.BACK, wyglad_sciany1);
            Appearance wyglad_sciany2 = new Appearance();
            wyglad_sciany2.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany2.setTexture(zaladuj_teksture("tekstury/sciana2.png"));
            szescian_Box[i].setAppearance(Box.TOP, wyglad_sciany2);
            Appearance wyglad_sciany3 = new Appearance();
            wyglad_sciany3.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany3.setTexture(zaladuj_teksture("tekstury/sciana3.png"));
            szescian_Box[i].setAppearance(Box.FRONT, wyglad_sciany3);
            Appearance wyglad_sciany4 = new Appearance();
            wyglad_sciany4.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany4.setTexture(zaladuj_teksture("tekstury/sciana4.png"));
            szescian_Box[i].setAppearance(Box.BOTTOM, wyglad_sciany4);
            Appearance wyglad_sciany5 = new Appearance();
            wyglad_sciany5.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany5.setTexture(zaladuj_teksture("tekstury/sciana5.png"));
            szescian_Box[i].setAppearance(Box.LEFT, wyglad_sciany5);
            Appearance wyglad_sciany6 = new Appearance();
            wyglad_sciany6.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
            wyglad_sciany6.setTexture(zaladuj_teksture("tekstury/sciana6.png"));
            szescian_Box[i].setAppearance(Box.RIGHT, wyglad_sciany6);
            
            szescian_tg[i] = new TransformGroup();
            szescian_tg[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            
            szescian_tg[i].addChild(dodajSzescian(szescian_Box[i], i));
            szescian[i] = new BranchGroup();
            szescian[i].setCapability(BranchGroup.ALLOW_DETACH);
            szescian[i].addChild(szescian_tg[i]);
            
            transformacja_kostka.addChild(szescian[i]);
        }
       
        
        
        
        sciana_do_obrotu = new TransformGroup();
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        sciana_do_obrotu.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        
//    
        transformacja_kostka.addChild(sciana_do_obrotu);
    
        return transformacja_kostka;
    }
    
    public TransformGroup dodajSzescian(Box szescian, int i){
        
        
        przesuniecie[i].set(szescianPolozenie[i]);
        TransformGroup przesuniecieGr = new TransformGroup();
        przesuniecieGr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        przesuniecieGr.addChild(szescian);
        przesuniecieGr.setTransform(przesuniecie[i]);
        return przesuniecieGr;
    }
    
    public void inicjalizacja_zmiennych(){
        kolejnosc = new int[memory][27];
        liczba_obrotow= new int[27];
        liczba_obrotowX= new int[27];
        liczba_obrotowY= new int[27];
        liczba_obrotowZ= new int[27];
        katX = new int[27];
        katY = new int[27];
        katZ = new int[27];
        katXDocelowy = new int[27];
        katYDocelowy = new int[27];
        katZDocelowy = new int[27];
        zplus =false;
        zminus =false;
        xplus =false;
        xminus =false;
        yplus = false;
        yminus = false;
        obroc_Zplus = false;
        obroc_Yplus = false;
        obroc_Xplus = false;
        obroc_Zminus = false;
        obroc_Yminus = false;
        obroc_Xminus = false;
    }
    
    public Texture2D zaladuj_teksture(String sciezka){
        TextureLoader loader1 = new TextureLoader(sciezka,this);
        ImageComponent2D image = loader1.getImage();
        Texture2D tekstura = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        tekstura.setImage(0, image);
        tekstura.setBoundaryModeS(Texture.CLAMP);
        tekstura.setBoundaryModeT(Texture.CLAMP);
        return tekstura;
    }
    
    public void obrot(){
        //generuje nowe transformacje rotacji dla szescianów należących do ściany_do_obrotu
        int k = 0;
        for (int i = 0; i <= 26; i++){
            for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                if( szescian[i] == sciana_do_obrotu.getChild(j)){
                    Transform3D  rot_szescianu   = new Transform3D();                    
                    
                    int obrocono_X = liczba_obrotowX[i] - 1;
                    int obrocono_Y = liczba_obrotowY[i] - 1;
                    int obrocono_Z = liczba_obrotowZ[i] - 1;
                    int kat_X = katX[k];
                    int kat_Y = katY[k];
                    int kat_Z = katZ[k];

                    for(int a = 1; a <= liczba_obrotow[i]; a++){
                        switch(kolejnosc[a][i]){
                            case 1: Transform3D  tmp_rotXplus      = new Transform3D();
                                    tmp_rotXplus.rotX(PI/180*(kat_X - 90*obrocono_X));
                                    kat_X = 90*obrocono_X;
                                    obrocono_X--;
                                    rot_szescianu.mul(tmp_rotXplus);
                                    break;
                            case -1: Transform3D  tmp_rotXminus      = new Transform3D();
                                    tmp_rotXminus.rotX(PI/180*(-kat_X + 90*obrocono_X));
                                    kat_X =  90*obrocono_X;
                                    obrocono_X--;
                                    rot_szescianu.mul(tmp_rotXminus);
                                    break;
                            case 2: Transform3D  tmp_rotYplus      = new Transform3D();
                                    tmp_rotYplus.rotY(PI/180*(kat_Y - 90*obrocono_Y));
                                    kat_Y = 90*obrocono_Y;
                                    obrocono_Y--;
                                    rot_szescianu.mul(tmp_rotYplus);
                                    break;
                            case -2:Transform3D  tmp_rotYminus      = new Transform3D();
                                    tmp_rotYminus.rotY(PI/180*(-kat_Y + 90*obrocono_Y));
                                    kat_Y =  90*obrocono_Y;
                                    obrocono_Y--;
                                    rot_szescianu.mul(tmp_rotYminus);
                                    break;
                            case 3: Transform3D  tmp_rotZplus      = new Transform3D();
                                    tmp_rotZplus.rotZ(PI/180*(kat_Z - 90*obrocono_Z));
                                    kat_Z = 90*obrocono_Z;
                                    obrocono_Z--;
                                    rot_szescianu.mul(tmp_rotZplus);
                                    break;
                            case -3:Transform3D  tmp_rotZminus     = new Transform3D();
                                    tmp_rotZminus.rotZ(PI/180*(-kat_Z + 90*obrocono_Z));
                                    kat_Z =  90*obrocono_Z;
                                    obrocono_Z--;
                                    rot_szescianu.mul(tmp_rotZminus);
                                    break;
                        }
                    }
                    
                    szescian_tg[i].setTransform(rot_szescianu);
                    szescianKaty[i].x = katX[k];
                    szescianKaty[i].y = katY[k];
                    szescianKaty[i].z = katZ[k];
                    k++;
                }
            }
                
        }
        
        
    }
    
    public void dodaj_kolejnosc(int os){
        //to generuje kolejność wykonywania rotacji dla każdej kostki odwrotną do
        //kolejnosći obrotów wykonywanych przez użytkownika
        for (int i = 0; i <= 26; i++){
                for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                    if( szescian[i] == sciana_do_obrotu.getChild(j)){
                        liczba_obrotow[i]++;
                        for (int a = liczba_obrotow[i]; a > 0; a--){
                        if( a == 1){
                            switch(os){
                                case 1: kolejnosc[1][i] = 1; liczba_obrotowX[i]++;  break;
                                case -1: kolejnosc[1][i] = -1; liczba_obrotowX[i]++;  break;
                                case 2: kolejnosc[1][i] = 2; liczba_obrotowY[i]++; break;
                                case -2: kolejnosc[1][i] = -2; liczba_obrotowY[i]++; break;
                                case 3: kolejnosc[1][i] = 3; liczba_obrotowZ[i]++; break;
                                case -3: kolejnosc[1][i] = -3; liczba_obrotowZ[i]++; break;
                            }
                        }else{
                            kolejnosc[a][i] = kolejnosc[a-1][i];

                        }
                        }
                }
            }
        }
    }
    
    public void obliczPolozenie(int os){
        //zmienia wartości położenia każdego szescianu względem nieruchomego układu współrzędnych
        for (int i = 0; i <= 26; i++){
            for(int j = 0; j < (sciana_do_obrotu.numChildren()); j++){
                if( szescian[i] == sciana_do_obrotu.getChild(j)){
                    Vector3f obecnePolozenie = new Vector3f();
                    obecnePolozenie.x = szescianPolozenie[i].x;
                    obecnePolozenie.y = szescianPolozenie[i].y;
                    obecnePolozenie.z = szescianPolozenie[i].z;

                    switch(os){
                        case 1: szescianPolozenie[i].y = - obecnePolozenie.z;
                                szescianPolozenie[i].z = obecnePolozenie.y;
                                break;
                                
                                
                        case -1:szescianPolozenie[i].y = obecnePolozenie.z;
                                szescianPolozenie[i].z = - obecnePolozenie.y;
                                break;
                        case 2: szescianPolozenie[i].x =  obecnePolozenie.z;
                                szescianPolozenie[i].z = - obecnePolozenie.x;
                                break;
                                
                        case -2:szescianPolozenie[i].x = - obecnePolozenie.z;
                                szescianPolozenie[i].z =  obecnePolozenie.x;
                                break;
                        case 3: szescianPolozenie[i].x = - obecnePolozenie.y;
                                szescianPolozenie[i].y = obecnePolozenie.x;
                                break;
                                
                        case -3:szescianPolozenie[i].x =  obecnePolozenie.y;
                                szescianPolozenie[i].y = - obecnePolozenie.x;
                                break;      
                        }
                }
            }
        }
    }
            
    
    public void ustaw_polozenie_pocz_szescianow(){
        int ind = 0;
        
            for(int i = -1; i <=1; i++){
                for (int j = -1; j <= 1; j++){
                    for (int k = -1; k<= 1; k++){
                        if(ind < szescianPolozenie.length){
                        szescianPolozenie[ind] = new Vector3f((a+d)*i,(a+d)*j,(a+d)*k);
                        szescianKaty[ind] = new Vector3f(0,0,0);
                        ind++;
                        }
                    }
                }
                    
            }
        
    }
    
    public void ustaw_przezroczystosc(int ustaw, int i){
        //Appearance wyglad = new Appearance(); 
        TransparencyAttributes transp = new TransparencyAttributes();
        transp.setTransparency(0.15f * ustaw);
        transp.setTransparencyMode(3);
        //wyglad.setTransparencyAttributes(transp);
        TransformGroup prz1 =(TransformGroup) szescian[i].getChild(0);
        TransformGroup prz2 = (TransformGroup)prz1.getChild(0);
        Box box = (Box) prz2.getChild(0);
        Appearance app1 = box.getAppearance(Box.LEFT);
        app1.setTransparencyAttributes(transp);
        Appearance app2 = box.getAppearance(Box.RIGHT);
        app2.setTransparencyAttributes(transp);
        Appearance app3 = box.getAppearance(Box.TOP);
        app3.setTransparencyAttributes(transp);
        Appearance app4 = box.getAppearance(Box.BOTTOM);
        app4.setTransparencyAttributes(transp);
        Appearance app5 = box.getAppearance(Box.FRONT);
        app5.setTransparencyAttributes(transp);
        Appearance app6 = box.getAppearance(Box.BACK);
        app6.setTransparencyAttributes(transp);
    }
    
    public boolean obraca_sie(){
        if(obroc_Zplus || obroc_Zminus || obroc_Yplus || obroc_Yminus || obroc_Xplus || obroc_Xminus){
            return true;
        }else return false;
        
    }
    
    public void zmien_sciane_do_obrotu(int numer_sciany){
        if(!obraca_sie()){
            aktywna_sciana = numer_sciany;
        
            float polozenie_szescianow[] = new float[27]; //wartości położenia szescianow w osi obrotu
            float wsp_sciany = 0; //wspołrzędna ściany na osi obrotu
            switch(aktywna_sciana){
                case 1: wsp_sciany = a+d;
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].x;
                        }
                        break;
                case 2: wsp_sciany = -(a+d) ;
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].x;
                        }
                        break;
                case 3: wsp_sciany = a+d;
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].y;
                        }
                        break;
                case 4: wsp_sciany = -(a+d);
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].y;
                        }
                        break;
                case 5: wsp_sciany = a+d;
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].z;
                        }
                        break;
                case 6: wsp_sciany = -(a+d);
                        for(int i = 0; i < 27; i++){
                            polozenie_szescianow[i] = szescianPolozenie[i].z;
                        }
                        break;

            }
            int k = 0;
            if(wsp_sciany != 0){
                for(int i=0; i <= 26; i++){
                    transformacja_kostka.removeChild(szescian[i]);  //odczepienie szescianu od rodzica
                    sciana_do_obrotu.removeChild(szescian[i]);      //
                    if (polozenie_szescianow[i] == wsp_sciany){
                        sciana_do_obrotu.addChild(szescian[i]);     //doczepienie do ściany obrotowej...
                        ustaw_przezroczystosc(1, i);
                        katX[k] = (int) szescianKaty[i].x;
                        katY[k] = (int) szescianKaty[i].y;
                        katZ[k] = (int) szescianKaty[i].z;
                        katXDocelowy[k] = katX[k];
                        katYDocelowy[k] = katY[k];
                        katZDocelowy[k] = katZ[k];
                        k++;
                    }else {
                        transformacja_kostka.addChild(szescian[i]);//...lub do transformacji_kostka
                        ustaw_przezroczystosc(0,i);                 

                    }
                }
            }   
        }

    }
    
    
    
    public boolean sprawdz_czy_obrocic(boolean kierunek, boolean obroc, int[] kat, int[] katDocelowy, int os){

        if (kierunek && !obroc) {
                for(int i = 0; i < 9; i++){
                    katDocelowy[i] = kat[i] + 90;
                }
                obroc = true;
                
                dodaj_kolejnosc(os);
            }
        if (katDocelowy[0] > kat[0]){
            for(int i = 0; i < 9; i++){
                kat[i] = kat[i] +1;
            }
        }else if (katDocelowy[0] == kat[0]){
            if(obroc) obliczPolozenie(os);
            obroc = false;
        }
        return obroc;
    }
    
    public void resetuj(){
        zminus = false;
        zplus = false;
        yminus = false;
        yplus = false;
        xminus = false;
        xplus = false;
    }
    
    public void losuj_ulozenie(){
        Random generator = new Random();
        for(int i = 1; i < 15; i++){
            int losowa1 = generator.nextInt(6);
            int losowa2 = generator.nextInt(2);
            losowa2 = -1*losowa2 + -1*(losowa2-1);
            switch(losowa1){
                case 0:     zmien_sciane_do_obrotu(1);
                            for(int k = 0; k< 9; k++){
                                katX[k] = katX[k] + 90;
                            }
                            dodaj_kolejnosc(losowa2);
                            obliczPolozenie(losowa2);
                            break;
                case 1:     zmien_sciane_do_obrotu(2);
                            for(int k = 0; k< 9; k++){
                                katX[k] = katX[k] + 90;
                            }
                            dodaj_kolejnosc(losowa2);
                            obliczPolozenie(losowa2);
                            break;
                case 2:     zmien_sciane_do_obrotu(3);
                            for(int k = 0; k< 9; k++){
                                katY[k] = katY[k] + 90;
                            }
                            dodaj_kolejnosc(2*losowa2);
                            obliczPolozenie(2*losowa2);
                            break;
                case 3:     zmien_sciane_do_obrotu(4); 
                            for(int k = 0; k< 9; k++){
                                katY[k] = katY[k] + 90;
                            }
                            dodaj_kolejnosc(-2*losowa2);
                            obliczPolozenie(-2*losowa2);
                            break;     
                case 4:     zmien_sciane_do_obrotu(5);
                            for(int k = 0; k< 9; k++){
                                katZ[k] = katZ[k] + 90;
                            }
                            dodaj_kolejnosc(3*losowa2);
                            obliczPolozenie(3*losowa2);
                            break;
                case 5:     zmien_sciane_do_obrotu(6); 
                            for(int k = 0; k< 9; k++){
                                katZ[k] = katZ[k] + 90;
                            }
                            dodaj_kolejnosc(-3*losowa2);
                            obliczPolozenie(-3*losowa2);
                            break; 
            }
            obrot();
           
        }
    }
    
    public static void main(String[] args) {
        
        new Kostka_rubika();
    }

    

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
                    case KeyEvent.VK_RIGHT:  
                    {
                        
                        if (aktywna_sciana == 1 || aktywna_sciana == 2)     xminus = true;
                        else if(aktywna_sciana == 3 || aktywna_sciana == 4) yminus = true;
                        else if(aktywna_sciana == 5 || aktywna_sciana == 6) zminus = true;
                        break; 
                    }
                    case KeyEvent.VK_LEFT:  
                    {
                        
                        if (aktywna_sciana == 1 || aktywna_sciana == 2) xplus = true;
                        else if(aktywna_sciana == 3 || aktywna_sciana == 4) yplus = true;
                        else if(aktywna_sciana == 5 || aktywna_sciana == 6) zplus = true;
                        
                        break; 
                    }
        }   
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
                    case KeyEvent.VK_1:     zmien_sciane_do_obrotu(1); break;
                    case KeyEvent.VK_2:     zmien_sciane_do_obrotu(2); break;
                    case KeyEvent.VK_3:     zmien_sciane_do_obrotu(3); break;
                    case KeyEvent.VK_4:     zmien_sciane_do_obrotu(4); break;     
                    case KeyEvent.VK_5:     zmien_sciane_do_obrotu(5); break;
                    case KeyEvent.VK_6:     zmien_sciane_do_obrotu(6); break;
                    case KeyEvent.VK_SPACE: losuj_ulozenie();
                    case KeyEvent.VK_RIGHT: resetuj(); break; 
                    case KeyEvent.VK_LEFT:  resetuj(); break;
                    
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            
            obroc_Zplus = sprawdz_czy_obrocic(zplus,obroc_Zplus,katZ,katZDocelowy, 3);
            obroc_Zminus = sprawdz_czy_obrocic(zminus,obroc_Zminus,katZ,katZDocelowy, -3);
            obroc_Yplus = sprawdz_czy_obrocic(yplus,obroc_Yplus,katY,katYDocelowy, 2);
            obroc_Yminus = sprawdz_czy_obrocic(yminus,obroc_Yminus,katY,katYDocelowy, -2);
            obroc_Xplus = sprawdz_czy_obrocic(xplus,obroc_Xplus,katX,katXDocelowy, 1);
            obroc_Xminus = sprawdz_czy_obrocic(xminus,obroc_Xminus,katX,katXDocelowy, -1);

            obrot();
        }
        catch(java.lang.NullPointerException b){
        }
    }
}
