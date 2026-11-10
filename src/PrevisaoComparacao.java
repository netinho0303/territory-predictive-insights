import java.util.ArrayList;

// Previsão = compara o período atual com o anterior e repete a variação
public class PrevisaoComparacao extends EstrategiaPrevisao {

    private int tamanhoPeriodo;  // atributo próprio
    private double mediaAtual;   // atributos próprios (guardam o último cálculo)
    private double mediaAnterior;

    public PrevisaoComparacao(int tamanhoPeriodo) {
        super("Comparação de Períodos", tamanhoPeriodo * 2); // precisa de 2 períodos
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        int fim = leituras.size();
        mediaAtual = super.calcularMedia(leituras, fim - tamanhoPeriodo, fim);
        mediaAnterior = super.calcularMedia(leituras, fim - 2 * tamanhoPeriodo, fim - tamanhoPeriodo);

        double previsto = mediaAtual + (mediaAtual - mediaAnterior);
        if (previsto < 0) previsto = 0;
        return previsto;
    }

    @Override
    public String explicar(double previsto, String unidade) {
        String tendencia = "ESTÁVEL";
        if (mediaAtual > mediaAnterior) tendencia = "SUBINDO";
        if (mediaAtual < mediaAnterior) tendencia = "CAINDO";
        return "Período atual (" + String.format("%.1f", mediaAtual) + ") vs anterior ("
                + String.format("%.1f", mediaAnterior) + "): " + tendencia;
    }
}