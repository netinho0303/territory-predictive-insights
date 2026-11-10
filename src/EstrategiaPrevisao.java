import java.util.ArrayList;

// CLASSE ABSTRATA
public abstract class EstrategiaPrevisao {

    private String nome;
    private int minimoLeituras; 
    
    public EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }

    public String getNome() {
        return nome;
    }

    public int getMinimoLeituras() {
        return minimoLeituras;
    }

    public boolean temLeiturasSuficientes(ArrayList<Double> leituras) {
        return leituras.size() >= minimoLeituras;
    }

    // média dos valores da posição "inicio" até "fim" 
    protected double calcularMedia(ArrayList<Double> leituras, int inicio, int fim) {
        double soma = 0;
        for (int i = inicio; i < fim; i++) {
            soma += leituras.get(i);
        }
        return soma / (fim - inicio);
    }

    // cada filha calcula do seu jeito
    public abstract double prever(ArrayList<Double> leituras);

    // cada filha explica o que o resultado significa
    public abstract String explicar(double previsto, String unidade);
}