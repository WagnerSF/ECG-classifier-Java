package classificadorecg;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MW
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PreProcessamento tpr = new PreProcessamento();
//        tpr.detecssaoOndas("ECG/sample100.txt", "ECG/rr100.txt");
        tpr.lerArquivos("ECG/samples203.txt","ECG/rr203.txt","ECG/annotations203.txt");
        tpr.seguimentar(); // retorna as janela de 120 amostras ja transformadas de wavelet
//        System.out.println("Arritmia--> "+tpr.verifAnotacao("47.656"));
//        System.out.println("Arritmia--> "+tpr.verifAnotacao("1082.861"));
        tpr.ritmos() ;
//        tpr.arqTreino();// analisa o onde ha presença de arritmias e sinal noram pelas anotações e retorna o arquivo de teste arrtf
//        tpr.arqTeste();// retorna o arquivo de teste
        
        



        
        
//        Para cada análise
// ler os três arquivos
        
// Seguimentação
//	PicoR
//	valor de amostra
//      
//identicar na anotação a arritmia/normalida corresponde ao janela de amostra
//gerar documento de treino
//gerar documento de teste
//
//integra a api weka


    }

}




