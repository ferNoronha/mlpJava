/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redesneurais.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Fernando A. Noronha
 */
public class Neuronios {
    
   
    private float base[], peso[], entrada[], saida, net, inet, erro;
    private int id;
    private String classe;
   

    public Neuronios() {
    }

    public Neuronios(float[] base, int id, String classe) {
        this.base = base;
        this.id = id;
        this.classe = classe;
    }

    public float[] getBase() {
        return base;
    }

    public List<String> getBaseString(){
        List<String> a = new ArrayList();
        
        for (int i=0; i<base.length-1; i++)
            a.add(base[i]+"");
       a.add(getClasse());
       return a;    
    }
    
    public void setBase(float[] base) {
        this.base = new float[base.length];
        this.base = base;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public float[] getPeso() {
        return peso;
    }

    public void setPeso(float[] peso) {
        this.peso = new float[peso.length];
        this.peso = peso;
    }

    public float[] getEntrada() {
        return entrada;
    }

    public void setEntrada(float[] entrada) {
        this.entrada = new float[entrada.length];
        this.entrada = entrada;
    }

    public float getSaida() {
        return saida;
    }

    public void setSaida(float saida) {
        this.saida = saida;
    }

    public float getNet() {
        return net;
    }

    public void setNet(float net) {
        this.net = net;
    }

    public float getInet() {
        return inet;
    }

    public void setInet(float inet) {
        this.inet = inet;
    }

    public float getErro() {
        return erro;
    }

    public void setErro(float erro) {
        this.erro = erro;
    }

    public void CalculaLinear(int calc){
        float total = 0;
        for (int i = 0; i < entrada.length; i++) {
            total+=entrada[i]*peso[i];
        }
        setNet(total);
        if(calc == 0)
        {
            
            setSaida(getNet()/10);
       
        }
        if(calc == 1){
            setSaida((float)(1/(1+Math.pow(Math.E, (-this.getNet())))));
          
        }
        if(calc == 2){
            setSaida((float) ((1 - Math.exp((double)(-2.0 * this.getNet()))) / (1 + Math.exp(((double)-2.0 * this.getNet())))));
           
        }
        
    }
    
    public void Erro(float desejado, int calc){
        conta(calc);
       // setErro(0);
        setErro((desejado-getSaida())*getInet());
    }
    
    public void conta(int calc){
        float teste=0;
        if(calc == 0)
            //teste = (float) ((1.0)/(10.0));
            setInet((float)0.1);
        if(calc == 1)
            //teste = 1-(float)Math.pow(this.getNet(), 2);
             setInet(this.getSaida() * (1 - this.getSaida()));
        if(calc == 2)
            //teste = this.getNet()*(float)(1.0-this.getNet());
            setInet((float)(1 - (Math.pow(this.getSaida(), 2))));
        //setInet(teste);
    }
    
    public void geraN(float n){
        for (int i = 0; i < peso.length; i++) {
            peso[i] = peso[i]+n*getErro()*entrada[i];
        }
    }
    
}
