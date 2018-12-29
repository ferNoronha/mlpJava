/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redesneurais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import redesneurais.classes.Neuronios;
import redesneurais.classes.Olocotio;

/**
 *
 * @author Fernando A. Noronha
 */
public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private MenuItem btArquivo;
    @FXML
    private RadioMenuItem rbLogistica;
    @FXML
    private RadioMenuItem rbLinear;
    @FXML
    private RadioMenuItem rbHiperbolica;
    @FXML
    private TextField txEntrada;
    @FXML
    private TextField txSaida;
    @FXML
    private TextField txOculta;
    @FXML
    private TextField txErro;
    @FXML
    private TextField txIteracao;
    @FXML
    private TextField txN;
    @FXML
    private Button btnAvancar;
    @FXML
    private TableView<ObservableList<String>> tbN;

    @FXML
    private ToggleGroup grupo1;

    @FXML
    private Button btnProx;
    @FXML
    private Button btnTreinar;
    @FXML
    private BarChart<Number, Number> grTreinar;

    ArrayList<TableColumn> tabelas = null;
    ArrayList<TableColumn> tabelasnormalizada = null;
    ArrayList<TableColumn> tabelasteste = null;
    ArrayList<TableColumn> tabelastestenormalizado = null;
    ArrayList<Neuronios> neu = null;
    ArrayList<Neuronios> normalizado = null;
    ArrayList<String> nomestabelas = null;
    ArrayList<String> classes = null;
    ArrayList<Neuronios> entradas = null;
    ArrayList<Neuronios> ocultas = null;
    ArrayList<Neuronios> saidas = null;
    ArrayList<Neuronios> teste = null;
    ArrayList<Neuronios> normalTeste = null;
    ArrayList<String> nomestabelasteste = null;
    boolean arquivotreinamento = false;
    boolean arquivoteste = false;
    String nomearquivo;
    String nomearquivoteste;
    float vmaior[];
    float erros[];
    float vmenor[];
    int tam;
    int iteracoes, oculta;
    float entrada, saida, erro, n;
    boolean flagTab = false;
    @FXML
    private Button btnTeste;
    @FXML
    private TextField txTreinamento;
    @FXML
    private TextField txTeste;
    @FXML
    private TableView<ObservableList<String>> tbTeste;
    @FXML
    private TableView<ObservableList<String>> tbConfusao;
    @FXML
    private MenuItem btArquivoTeste;
    @FXML
    private Button btnNormalizado;
    @FXML
    private TableView<ObservableList<String>> tbTreinoNormal;
    @FXML
    private TableView<ObservableList<String>> tbTesteNormal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbLinear.setSelected(true);
        rbHiperbolica.setSelected(false);
        rbLogistica.setSelected(false);

        btnProx.setDisable(true);
        btnTreinar.setDisable(true);
        btnAvancar.setDisable(true);
        btnTeste.setDisable(true);
        /*TableColumn Teste = new TableColumn<>("Teste");
        Teste.setCellValueFactory(new PropertyValueFactory<>("Teste"));
        
       // tbN.setItems(int);
        tbN.getColumns().add(Teste);*/
    }

    private void lerArquivo() {
        BufferedReader br = null;
        String linha;

        int mat[][];
        Neuronios n;
        neu = new ArrayList<>();
        nomestabelas = new ArrayList();
        classes = new ArrayList<>();
        neu.clear();
        classes.clear();
        nomestabelas.clear();
        tam = 0;
        try {
            if (nomearquivo.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Arquivo inexistente");
                a.showAndWait();
            } else {

                br = new BufferedReader(new FileReader(nomearquivo));

                linha = br.readLine();
                String l[] = linha.split(",");

                for (int i = 0; i < l.length; i++) {
                    nomestabelas.add(l[i]);
                }
                tam = l.length - 1;
                int j = 0;

                String lin;
                int i = 0;
                while ((lin = br.readLine()) != null) {
                    String li[] = lin.split(",");
                    float vet[] = new float[li.length];
                    for (i = 0; i < li.length - 1; i++) {
                        vet[i] = Float.parseFloat(li[i]);
                    }
                    neu.add(new Neuronios(vet, j, li[i]));
                    j++;
                    if (!classes.contains(li[i])) {
                        classes.add(li[i]);
                    }
                }

                txTreinamento.setText(nomearquivo);
                //mostrar();

                criarTabelas();
                normalizar();

            }
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Erro no arquivo de treinamento: " + ex);
            a.showAndWait();
        }

    }

    @FXML
    private void clkArquivo(ActionEvent event) {
        escolherArquivo(0);
        lerArquivo();
    }

    public void escolherArquivo(int teste) {
        FileChooser arquivo = new FileChooser();

        arquivo.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV(*.csv)", "*.csv"));

        File arq = arquivo.showOpenDialog(new Stage());
        if (teste == 0) {
            nomearquivo = arq.getAbsolutePath();
        } else {
            nomearquivoteste = arq.getAbsolutePath();
        }
    }

    private void criarTabelas() {

        int k = 0;
        tabelas = new ArrayList<>();
        tabelas.clear();

        tbN.getColumns().clear();
        while (!tbN.getColumns().isEmpty()) {
            // for (int i = 0; i < tbN.getColumns().size(); i++) {
            tbN.getColumns().remove(0);
            //}
        }

        /*for (String nomestabela : nomestabelas) {
               
                tabelas.add(new TableColumn<>(nomestabela));
            }*/
        for (int i = 0; i < nomestabelas.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    nomestabelas.get(i)
            );
            column.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tbN.getColumns().add(column);
        }

        for (int i = 0; i < neu.size(); i++) {
            tbN.getItems().add(
                    FXCollections.observableArrayList(
                            neu.get(i).getBaseString()//inserir List String
                    )
            );
        }

    }

    private void mostrar() {

        for (String nomestabela : nomestabelas) {
            System.out.print(nomestabela + "|");
        }
        System.out.println("");
        float vet[];
        for (Neuronios neuronios : neu) {
            vet = neuronios.getBase();
            for (int i = 0; i < vet.length - 1; i++) {
                System.out.print(vet[i] + "|");
            }
            System.out.println(neuronios.getClasse() + "|");

        }
        System.out.println("---------------------------------------NORMALIZADA-------------------------------------");
        for (String nomestabela : nomestabelas) {
            System.out.print(nomestabela + "|");
        }
        System.out.println("");
        for (Neuronios neuronios : normalizado) {
            vet = neuronios.getBase();
            for (int i = 0; i < tam; i++) {
                System.out.print(vet[i] + "|");
            }
            System.out.println(neuronios.getClasse() + "|");
        }
    }

    public void normalizar() {
        float intervalo;
        normalizado = new ArrayList<>();
        normalizado.clear();
        vmaior = new float[tam];
        vmenor = new float[tam];
        float vet[], vet2[];
        for (int i = 0; i < tam; i++) {
            vmaior[i] = -99999;
            vmenor[i] = 99999;

            for (int j = 0; j < neu.size(); j++) {
                vet = neu.get(j).getBase();
                if (vmaior[i] < vet[i]) {
                    vmaior[i] = vet[i];
                }
                if (vmenor[i] > vet[i]) {
                    vmenor[i] = vet[i];
                }
            }
        }

        for (int j = 0; j < neu.size(); j++) {
            vet2 = new float[tam];
            for (int i = 0; i < tam; i++) {
                intervalo = vmaior[i] - vmenor[i];
                vet = neu.get(j).getBase();
                vet2[i] = (vet[i] - vmenor[i]) / intervalo;

            }
            normalizado.add(new Neuronios(vet2, j, neu.get(j).getClasse()));
        }
      //  mostraTesteNormalizado();
        mostrar();
        Exibe();
        // btnProx.setDisable(false);
    }
    //normalizar

    public void Exibe() {
        txErro.setText("0.0001");
        txN.setText("1");
        txIteracao.setText("1000");
        txEntrada.setText(String.valueOf(tam));
        txSaida.setText(String.valueOf(classes.size()));
        txOculta.setText(String.valueOf((int) (tam + classes.size()) / 2));
        btnTreinar.setDisable(false);

    }

    @FXML
    private void clkAvancar1(ActionEvent event) {
        float intervalo;
        normalizado = new ArrayList<>();
        normalizado.clear();
        vmaior = new float[tam];
        vmenor = new float[tam];
        float vet[], vet2[];
        for (int i = 0; i < tam; i++) {
            vmaior[i] = -99999;
            vmenor[i] = 99999;

            for (int j = 0; j < neu.size(); j++) {
                vet = neu.get(j).getBase();
                if (vmaior[i] < vet[i]) {
                    vmaior[i] = vet[i];
                }
                if (vmenor[i] > vet[i]) {
                    vmenor[i] = vet[i];
                }
            }
        }

        for (int j = 0; j < neu.size(); j++) {
            vet2 = new float[tam];
            for (int i = 0; i < tam; i++) {
                intervalo = vmaior[i] - vmenor[i];
                vet = neu.get(j).getBase();
                vet2[i] = (vet[i] - vmenor[i]) / intervalo;

            }
            normalizado.add(new Neuronios(vet2, j, neu.get(j).getClasse()));
        }

        mostrar();

        btnProx.setDisable(false);

    }

    @FXML
    private void clkProx(ActionEvent event) {
        txErro.setText("0.0001");
        txN.setText("1");
        txIteracao.setText("1000");
        txEntrada.setText(String.valueOf(tam));
        txSaida.setText(String.valueOf(classes.size()));
        txOculta.setText(String.valueOf((int) (tam + classes.size()) / 2));
        btnTreinar.setDisable(false);

        iteracoes = Integer.parseInt(txIteracao.getText());
        oculta = Integer.parseInt(txOculta.getText());
        entrada = Float.parseFloat(txEntrada.getText());
        saida = Float.parseFloat(txSaida.getText()); // altura do grafico
        erro = Float.parseFloat(txErro.getText());
        n = Float.parseFloat(txN.getText());

    }

    @FXML
    private void clkTreinar(ActionEvent event) {

        iteracoes = Integer.parseInt(txIteracao.getText());
        oculta = Integer.parseInt(txOculta.getText());
        entrada = Float.parseFloat(txEntrada.getText());
        saida = Float.parseFloat(txSaida.getText()); // altura do grafico
        erro = Float.parseFloat(txErro.getText());
        n = Float.parseFloat(txN.getText());
        
        if (oculta > 2 && oculta < saida * 2) {

            if (iteracoes > 1) {
                if (n > 0 && n <= 1) {

                    treinar();

                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("N invalido");
                    a.showAndWait();
                }
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Iterações invalido");
                a.showAndWait();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Camada Oculta invalida");
            a.showAndWait();
        }

    }

    private void treinar() {

//        ArrayList<Neuronios> neuro = new ArrayList<>();
        int calc = 0;
        if(rbLinear.isSelected())
            calc = 0;
        if(rbHiperbolica.isSelected())
            calc = 2;
        if(rbLogistica.isSelected())
            calc = 1;
        entradas = new ArrayList<>();
        ocultas = new ArrayList<>();
        saidas = new ArrayList<>();
        erros = new float[iteracoes];
        Random gerador = new Random();
        float vet[];

        //entrada
        for (int i = 0; i < entrada; i++) {
            vet = new float[1];
            entradas.add(new Neuronios());
            vet[0] = 1;
            entradas.get(i).setPeso(vet);
        }

        for (int i = 0; i < oculta; i++) {
            vet = new float[tam];
            ocultas.add(new Neuronios());
            for (int j = 0; j < tam; j++) {
                vet[j] = gerador.nextFloat() * 4 - 2;
                //vet[j] = 2;
            }
            ocultas.get(i).setPeso(vet);
        }

        for (int i = 0; i < saida; i++) {
            vet = new float[oculta];
            saidas.add(new Neuronios());
            for (int j = 0; j < oculta; j++) {
                vet[j] = gerador.nextFloat() * 4 - 2;
                //vet[j] = 2;
            }
            saidas.get(i).setPeso(vet);
            saidas.get(i).setClasse(classes.get(i));
        }

        float vet2[];
        boolean flag = true;
        int i = 0;
        int j = 0;

        // grTreinar = new BarChart<Number, Number>();
        try {
            XYChart.Series prod1 = new XYChart.Series();
            //grTreinar.getData().remove(0);
            for (i = 0; i < iteracoes && flag == true; i++) {
                erros[i] = 0;
                for (j = 0; j < normalizado.size(); j++) {

                    for (int k = 0; k < tam; k++) {
                        vet = new float[1];
                        vet2 = normalizado.get(j).getBase();
                        vet[0] = vet2[k];
                        entradas.get(k).setEntrada(vet);
                    }

                    for (int k = 0; k < oculta; k++) {
                        vet = new float[tam];
                        for (int l = 0; l < entradas.size(); l++) {
                            entradas.get(l).CalculaLinear(calc);
                            vet[l] = entradas.get(l).getSaida();
                        }
                        ocultas.get(k).setEntrada(vet);
                    }

                    for (int k = 0; k < saida; k++) {
                        vet = new float[oculta];
                        for (int l = 0; l < oculta; l++) {
                            ocultas.get(l).CalculaLinear(calc);
                            vet[l] = ocultas.get(l).getSaida();
                        }
                        saidas.get(k).setEntrada(vet);
                    }

                    for (int k = 0; k < saida; k++) {
                        saidas.get(k).CalculaLinear(calc);
                    }
                    String clas = "";
                    for (int k = 0; k < saida; k++) {
                        clas = normalizado.get(j).getClasse();
                        if (clas.equals(saidas.get(k).getClasse())) {
                            saidas.get(k).Erro(1,calc);
                        } else {
                            if(calc==2)
                                 saidas.get(k).Erro(-1,calc);
                            else
                            saidas.get(k).Erro(0,calc);
                        }
                    }

                    float som;
                    for (int k = 0; k < oculta; k++) {
                        som = 0;
                        for (int l = 0; l < saida; l++) {
                            vet2 = saidas.get(l).getPeso();
                            som += saidas.get(l).getErro() * vet2[k];
                        }
                        ocultas.get(k).Erro(som,calc);
                    }

                    som = 0;
                    for (int k = 0; k < saida; k++) {
                        som += Math.pow(saidas.get(k).getErro(), 2);
                    }
                    erros[i] += (float) (0.5 * som);
                    if (i == normalizado.size()) {

                        float media = erros[i] / neu.size();
                        if (media < erro) {
                            flag = false;
                        }
                    }
                    for (int k = 0; k < saida; k++) {
                        saidas.get(k).geraN(n);
                    }

                    for (int k = 0; k < oculta; k++) {
                        ocultas.get(k).geraN(n);
                    }
                }

                if (i % 10 == 0) {
                    prod1.getData().add(new XYChart.Data("" + i, erros[i]));
                }

            }
            prod1.setName("Treino");
            grTreinar.getData().add(prod1);
            arquivotreinamento = true;
            if (arquivotreinamento && arquivoteste) {
                btnTeste.setDisable(false);
            }
        } catch (Exception ex) {
            System.out.println("" + i + "|" + j);
        }

        //System.out.println("teste");
    }

    @FXML
    private void clkTeste(ActionEvent event) {
        
        matConfusao();
    }

    public void matConfusao() {
         int calc = 0;
        if(rbLinear.isSelected())
            calc = 0;
        if(rbHiperbolica.isSelected())
            calc = 2;
        if(rbLogistica.isSelected())
            calc = 1;
        String matriz[][] = new String[normalTeste.size()][1];
        String matrizConfusao[][] = new String[classes.size()][classes.size()];
        int i = 0;
        try {

            for (i = 0; i < normalTeste.size(); i++) {
                float vet[];
                for (int j = 0; j < entrada; j++) {
                    vet = new float[1];
                    vet[0] = normalTeste.get(i).getBase()[j];
                    entradas.get(j).setEntrada(vet);
                }

                for (int j = 0; j < oculta; j++) {
                    vet = new float[tam];
                    for (int l = 0; l < entradas.size(); l++) {
                        entradas.get(l).CalculaLinear(calc);
                        vet[l] = entradas.get(l).getSaida();
                    }
                    ocultas.get(j).setEntrada(vet);
                }

                for (int k = 0; k < saida; k++) {
                    vet = new float[oculta];
                    for (int l = 0; l < oculta; l++) {
                        ocultas.get(l).CalculaLinear(calc);
                        vet[l] = ocultas.get(l).getSaida();
                    }
                    saidas.get(k).setEntrada(vet);
                }

                float maior = 0;
                int pos = 0;
                for (int j = 0; j < saida; j++) {
                    saidas.get(j).CalculaLinear(calc);
                    if (saidas.get(j).getSaida() > maior) {
                        pos = j;
                        maior = saidas.get(j).getSaida();
                    }
                }

                matriz[i][0] = saidas.get(pos).getClasse();
            }
        } catch (Exception ex) {
            System.out.println("" + i + "---" + ex);
        }

        int tamanhoC = classes.size();
        try {

            for (i = 0; i < tamanhoC; i++) {
                for (int j = 0; j < tamanhoC; j++) {
                    matrizConfusao[i][j] = "0";
                }
            }
        } catch (Exception ex) {
            System.out.println("erro 1 " + ex);
        }

        try {
            int x, y;
            for (i = 0; i < normalTeste.size(); i++) {
                x = y = 0;
                while (!normalTeste.get(i).getClasse().equals(classes.get(x))) {
                    x++;
                }
                while (!matriz[i][0].equals(classes.get(y))) {
                    y++;
                }
                int val = Integer.parseInt(matrizConfusao[x][y]);
                val++;
                matrizConfusao[x][y] = String.valueOf(val);
            }

            for (i = 0; i < tamanhoC; i++) {
                for (int j = 0; j < tamanhoC; j++) {
                    System.out.print(matrizConfusao[i][j] + " | ");
                }
                System.out.println("");
            }

            exibeMatriz(matrizConfusao);

        } catch (Exception ex) {
            System.out.println("erro 2 " + ex);
        }
    }

    public void normalTeste() {
        float intervalo;
        normalTeste = new ArrayList<>();

        float vet[], vet2[];

        for (int j = 0; j < teste.size(); j++) {
            vet2 = new float[tam];
            for (int i = 0; i < tam; i++) {
                intervalo = vmaior[i] - vmenor[i];
                vet = teste.get(j).getBase();
                vet2[i] = (vet[i] - vmenor[i]) / intervalo;

            }
            normalTeste.add(new Neuronios(vet2, j, teste.get(j).getClasse()));
        }
        //mostraTreinoNormalizado();

    }

    public void abrirArqTeste() {
        BufferedReader br;
        String linha = "";
        nomestabelasteste = new ArrayList<>();
        teste = new ArrayList<Neuronios>();
        try {
            if (nomearquivoteste.isEmpty()) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Arquivo Inexistente");
                a.showAndWait();
            } else {

                br = new BufferedReader(new FileReader(nomearquivoteste));

                linha = br.readLine();
                String l[] = linha.split(",");
                for (int k = 0; k < l.length; k++) {
                    nomestabelasteste.add(l[k]);
                }
                int i = 0;
                int j = 0;
                String lin = "";
                while ((lin = br.readLine()) != null) {
                    String li[] = lin.split(",");
                    float vet[] = new float[li.length];
                    for (i = 0; i < li.length - 1; i++) {
                        vet[i] = Float.parseFloat(li[i]);
                    }
                    teste.add(new Neuronios(vet, j, li[i]));
                    j++;
                }
                txTeste.setText(nomearquivoteste);
                arquivoteste = true;
                if (arquivotreinamento && arquivoteste) {
                    btnTeste.setDisable(false);
                }
                criarTabelasTeste();
            }
            normalTeste();
            // mostrarTeste();
            //   criarTabelas(nomestabelas);
        } catch (Exception ex) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Erro no Arquivo de Teste: " + ex);
            a.showAndWait();
        }
    }

    public void criarTabelasTeste() {
        int k = 0;
        tabelasteste = new ArrayList<>();
        tabelasteste.clear();
         tbTeste.getColumns().clear();
        while (!tbTeste.getColumns().isEmpty()) {
            // for (int i = 0; i < tbN.getColumns().size(); i++) {
            tbTeste.getColumns().remove(0);
            //}
        }
        /*for (String nomestabela : nomestabelas) {
               
                tabelas.add(new TableColumn<>(nomestabela));
            }*/
        for (int i = 0; i < nomestabelasteste.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    nomestabelasteste.get(i)
            );
            column.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tbTeste.getColumns().add(column);
        }

        for (int i = 0; i < teste.size(); i++) {
            tbTeste.getItems().add(
                    FXCollections.observableArrayList(
                            teste.get(i).getBaseString()//inserir List String
                    )
            );
        }
    }

    private void mostrarTeste() {

        System.out.println("--------------------- ARQUIVO TESTE ----------------------");
        System.out.println("");
        for (int i = 0; i < nomestabelas.size(); i++) {

            System.out.print(nomestabelas.get(i) + "|");

        }
        System.out.println("");

        for (Neuronios neuronios : teste) {
            for (int i = 0; i < neuronios.getBase().length - 1; i++) {
                System.out.print(neuronios.getBase()[i] + "|");
            }
            System.out.print(neuronios.getClasse() + "|");
            System.out.println("");
        }

    }

    @FXML
    private void clkArquivoTeste(ActionEvent event) {
        escolherArquivo(1);
        abrirArqTeste();
    }

    private void exibeMatriz(String[][] matrizConfusao) {

        while (!tbConfusao.getColumns().isEmpty()) {
            tbConfusao.getColumns().remove(0);
        }
        for (int i = 0; i < classes.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    classes.get(i)
            );
            column.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tbConfusao.getColumns().add(column);
        }

        ArrayList<String[]> mat = new ArrayList();

        for (int i = 0; i < classes.size(); i++) {
            String vetor[] = matrizConfusao[i];
            mat.add(vetor);
        }

        for (int i = 0; i < classes.size(); i++) {
            tbConfusao.getItems().add(
                    FXCollections.observableArrayList(
                            mat.get(i)
                    //inserir List String
                    )
            );
        }
    }

    @FXML
    private void clkMostrarNormal(ActionEvent event) {
        
        if(flagTab){
            flagTab=false;
            //
           // 
            tbN.setVisible(false);
            tbTeste.setVisible(false);
            tbTesteNormal.setVisible(true);
            tbTreinoNormal.setVisible(true);
        }
        else
        {
            flagTab = true;
            tbN.setVisible(true);
            tbTeste.setVisible(true);
            tbTesteNormal.setVisible(false);
            tbTreinoNormal.setVisible(false);
        }
        
    }
    public void mostraTreinoNormalizado(){
        tabelastestenormalizado = new ArrayList<>();
        tabelastestenormalizado.clear();
        tbTesteNormal.getColumns().clear();
        while (!tbTesteNormal.getColumns().isEmpty()) {
            // for (int i = 0; i < tbN.getColumns().size(); i++) {
            tbTesteNormal.getColumns().remove(0);
            //}
        }
        /*for (String nomestabela : nomestabelas) {
               
                tabelas.add(new TableColumn<>(nomestabela));
            }*/
        for (int i = 0; i < nomestabelasteste.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    nomestabelasteste.get(i)
            );
            column.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tbTesteNormal.getColumns().add(column);
        }

        for (int i = 0; i < normalTeste.size(); i++) {
            tbTesteNormal.getItems().add(
                    FXCollections.observableArrayList(
                            normalTeste.get(i).getBaseString()//inserir List String
                    )
            );
        }
    }
    public void mostraTesteNormalizado(){
        
        tabelasnormalizada = new ArrayList<>();
        tabelasnormalizada.clear();
        tbTreinoNormal.getColumns().clear();
        while (!tbTreinoNormal.getColumns().isEmpty()) {
            // for (int i = 0; i < tbN.getColumns().size(); i++) {
            tbTreinoNormal.getColumns().remove(0);
            //}
        }
        /*for (String nomestabela : nomestabelas) {
               
                tabelas.add(new TableColumn<>(nomestabela));
            }*/
        for (int i = 0; i < nomestabelas.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    nomestabelas.get(i)
            );
            column.setCellValueFactory(param
                    -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            tbTreinoNormal.getColumns().add(column);
        }

        for (int i = 0; i < normalizado.size(); i++) {
            tbTreinoNormal.getItems().add(
                    FXCollections.observableArrayList(
                            normalizado.get(i).getBaseString()//inserir List String
                    )
            );
        }
    }

}
