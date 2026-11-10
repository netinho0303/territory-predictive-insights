import java.util.ArrayList;

// Previsão 
public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    private int janela; // atributo próprio 

    public PrevisaoMediaMovel(int janela) {
        super("Média Móvel", janela); // chama o construtor da classe mãe
        this.janela = janela;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        int fim = leituras.size();
        return super.calcularMedia(leituras, fim - janela, fim);
    }

    @Override
    public String explicar(double previsto, String unidade) {
        return "Média das últimas " + janela + " leituras = " + String.format("%.1f", previsto) + " " + unidade;
    }
}