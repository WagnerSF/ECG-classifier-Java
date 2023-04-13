/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificadorecg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwave.Transform;
import jwave.transforms.WaveletPacketTransform;
import jwave.transforms.wavelets.daubechies.Daubechies9;

/**
 *
 * @author wfreitas
 */
public class PreProcessamento {

    private String samples[];
    private String picoR[];
    private String anot[];
    private String ritmo[];
    private int tam;
    private int tamSamples;


    public PreProcessamento() {
        tam = 2980;
        tamSamples = 650000;
        samples = new String[tamSamples];
        picoR = new String[tam];
        anot = new String[tam];
        ritmo = new String[tam]; // para cada pico uma janela de amostras e uma classificão de arritmias
        
    }

     public void lerArquivos(String s, String rr, String a) {

        File arquivo1 = new File(s);
        File arquivo2 = new File(rr);
        File arquivo3 = new File(a);
        Scanner sc1;
        Scanner sc2;
        Scanner sc3;
        try {
            sc1 = new Scanner(arquivo1);
            sc2 = new Scanner(arquivo2);
            sc3 = new Scanner(arquivo3);
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            while (sc1.hasNext() && count1 < tamSamples) {
                samples[count1] = sc1.nextLine();
                count1++;
            }
            while (sc2.hasNext() && count2 < tam) {
                picoR[count2] = sc2.nextLine();
                count2++;
            }
            while (sc3.hasNext() && count3 < tam) {
                anot[count3] = sc3.nextLine();
                count3++;

            }
            sc1.close();
            sc2.close();
            sc3.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     
    
 public void seguimentar() {

        double array[][] = new double[tam][129];
        
        for (int j = 0; j < tam; j++) {
            for (int i = 64; i < tamSamples; i++) {                

              if(((picoR[j].split(";")[0].equals(samples[i].split(";")[0])) ) && ((i + 64) < samples.length)) {//identifica nas amostra o pico
                    
//                    System.out.println("j:"+j+" picoR[j] ="+picoR[j].split(";")[0]+"  samples[i].split[0]= "+samples[i].split(";")[0]);  

                    int k = i - 64;
                    int janela = 0;
                      
                    while (janela < 128) {
                        double x = Double.parseDouble(samples[k].split(";")[1]);
                        array[j][janela] = x;
                        
                            if (verifAnotacao(samples[k].split(";")[0]).equals("Normal")) {
                            ritmo[j]=verifAnotacao(samples[k].split(";")[0]);
                            }
                            else{ritmo[j]=verifAnotacao(samples[k].split(";")[0]);}
                               
                             
                        
//                        System.out.println("Janela[" + j + "] " + "samples[k].split(\";\")[0]):" + (samples[k].split(";")[0]));
                       // if (samples[k].split(";")[0]) {
                            
                       // }
//                        System.out.println("array["+j+"]"+"["+janela+"]: "+"="+x);
                        janela++;
                        k++;

                    }
//                    System.out.println("  verificaAnotação:"+ritmo[j]);

                }
            }
        }

        System.out.println("@relation ECGteste\n");
        for (int i = 1; i <= 128; i++) {
            System.out.println("@attribute X" + i + " numeric");
        }

        System.out.println("@attribute ECG {Normal, FibrilacaoAtrial, TaquicardiaVentricular}\n" + "@data");

        for (int coluna = 0; coluna < tam; coluna++) {
            double[] samp = new double[128]; // 128 tamanha do arquivo para TW
            for (int linha = 0; linha < 128; linha++) { // mudar para 128 amoas!!!!!!!!
                samp[linha] = array[coluna][linha];
//                    System.out.println("array[][]:"+array [coluna] [linha]+"   samp: "+samp[linha]+ " coluna:"+coluna+"  linha:"+linha);

            }

            Transform t = new Transform(new WaveletPacketTransform(new Daubechies9()));
            double[] baub = new double[t.forward(samp).length];
//              double[ ] baubReverso = new double[t.forward(samp).length];
            baub = t.forward(samp);
//              baubReverso = t.reverse(baub);

            for (int x = 0; x < 128; x++) {

                System.out.print("Daubechies9 "+baub[x] + ",");
                   System.out.print("original  "+samp[x] + ",");
            }

            System.out.println(ritmo[coluna]);
        }
    }
   
  
    public String verifAnotacao(String s) {
        ritmo[0] = anot[0].split("-")[1];
        String[] r = new String[ritmos().length];
         r = ritmos();

        for (int i = 0; i < ritmos().length; i++) {
            if (s.equals( r[i].split(";")[0])) {
                 System.out.println(r[i].split(";")[1]);
                 
                    return (r[i].split(";")[1]);
                }
            return "Normal";

        }
        return "Normal";
    }
    
    
    public String[] ritmos() {
        ArrayList<String> list = new ArrayList();

        for (int i = 0; i <tam; i++) {
            if ((anot[i] != null) && (anot[i].length() > 38)) {
                list.add(anot[i]);
            }
        }
        String[] x = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            x[i] = (list.get(i).split(";")[1])+(";")+(list.get(i).split("-")[1]);
//            System.out.println(x[i]);
           
        }        
        return x;
    }
}
