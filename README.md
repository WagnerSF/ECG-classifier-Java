# ECG-classifier-Java
Program pre-processes the ECG signal
This program was developed for the pre-processing phase of the ECG signals, taking time windows, with 128 points by reading the peak R. This way the program reads the files and extracts the characteristics of the signal with this window the transformed wallet, to generate a coefficient for better evaluation in the MLP artificial neural network.
To carry out the hoi transform, the SCHEIBLICH API was used, C.: JWave—java implementation of wavelet transform algorithms (2010).
And for reading and machine learning training, the Weka tool was used (https://www.cs.waikato.ac.nz/ml/weka/)

Este programa foi desenvolvido para a fase de pré-processamento do sinais de ECG, tirando janelas de tempo, com 128 pontos através da leitura do pico R. Desta forma o programa lê os arquivos e extrai as características do sinal com essa janela é feita a transformada wallet, para gerar um coeficiente para melhor avaliação na rede neural artificial MLP.
Para o realização da transformada hoi utilizada a API de SCHEIBLICH, C.: JWave—java implementation of wavelet transform algorithms (2010).
E para leitura e treinamente de aprendiza de maquina foi utilizado a ferramente Weka (https://www.cs.waikato.ac.nz/ml/weka/)
