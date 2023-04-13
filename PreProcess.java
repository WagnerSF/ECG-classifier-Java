/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificadorecg;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jwave.Transform;
import jwave.transforms.WaveletPacketTransform;
import jwave.transforms.wavelets.daubechies.*;

/**
 *
 * @author Wagner Freitas
 */
public class PreProcess {

    private String samples[];
    private String picoR[];
    private String anot[];
    private String ritmo[];
    

    @SuppressWarnings("empty-statement")
    public PreProcess() {
        samples = new String[20000];
        picoR = new String[65];
        anot = new String[3000];
        ritmo = new String[65]; // para cada pico uma janela de amostras e uma classificão de arritmias
    }

    public static long distanciaRR(String ini, String fin) throws ParseException {
        Date d1, d2 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("mm:ss.SS");
        SimpleDateFormat sdf2 = new SimpleDateFormat("mm:ss.SS");
        d1 = sdf1.parse(ini);
        d2 = sdf2.parse(fin);
        System.out.println("diferença: " + (d2.getTime() - d1.getTime()));
        return d2.getTime() - d1.getTime();
    }



    public String[] lerPicoR(String nomeArquivo) {
        String ecg[] = new String[4];
        try {

            File arquivo = new File(nomeArquivo);
            Scanner sc = new Scanner(arquivo);
            int count = 0;

            while (sc.hasNext()) {
//				System.out.println(sc.nextLine());
                ecg = sc.nextLine().split(";");
                count++;
//                                System.out.println(ecg[4]);
            }
            System.out.println("numero de linhas Pico R: " + count);
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return ecg;
    }
//




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
            while (sc1.hasNext() && count1 < 20000) {
                samples[count1] = sc1.nextLine();
                count1++;
            }
            while (sc2.hasNext() && count2 < 65) {
                picoR[count2] = sc2.nextLine();
                count2++;
            }
            while (sc3.hasNext() && count3 < 3000) {
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

        double array[][] = new double[picoR.length][129];

        for (int j = 0; j < picoR.length; j++) {
            for (int i = 64; i < samples.length; i++) {

                if (((picoR[j].split(";")[0].equals(
                        samples[i].split(";")[0])) && (i + 64) < samples.length)) { //identifica nas amostra o pico
                    int k = i - 64;
                    int janela = 0;
                    String[] n = new String[212];

                    while (janela <= 128) {
                        double x = Double.parseDouble(samples[k].split(";")[1]);
                        array[j][janela] = x;
//                                System.out.println("j:"+j+" picoR[j]"+picoR[j].split(";")[0]);              

                             
                            if (verifAnotacao(samples[k].split(";")[0]).equals("Normal")) {
                            ritmo[j]="Normal";
                            }
                            else{
                                ritmo[j]=verifAnotacao(samples[k].split(";")[0]);
                               
                            }    
                             
                        
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

        for (int coluna = 1; coluna < 65; coluna++) {
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

            for (int linha = 0; linha < 128; linha++) {

                System.out.print(baub[linha] + ",");
            }

            System.out.println(ritmo[coluna]);
        }

    }

    void arqTreino() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void arqTeste() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String verifAnotacao(String s) {
        

        for (int i = 0; i < anot.length; i++) {
  
                if ((anot[i] != null) && (anot[i].length() > 32)) {
                    if (anot[i].contains("-")) {
                         System.out.println("S:"+s+" é igual a "+anot[i]);
                        System.out.print("segundos: "+anot[i].split(";")[1]);
                        System.out.println(" arritmia"+anot[i].split("-")[1]);
//                        System.out.println(" S:"+s+" é igual a "+anot[i].split(";")[1]);
                    }
                    

                    if ((anot[i].substring(4, 9).equals(s)) )
//                            || 
//                            (anot[i].substring(3, 9).equals(s)) ||
//                            (anot[i].substring(2, 9).equals(s))) 
                    {
                        System.out.println("S:"+s+" é igual a "+anot[i]);
                        
                        if (anot[i].substring(52).equals("AFIB")) {
                            return "FibrilacaoAtrial";
                        }
                        else if (anot[i].substring(52).equals("VT")) {
                            return "TaquicardiaVentricular";
                        }
                        else {
                            return "Normal";
                        }
                    }
                }
        }
      return "Normal";
    }

}
