public class SensorUmidade implements Sensor {

    private double valorAtual = 60;

    @Override
    public double lerValor() {
        valorAtual = valorAtual + (Math.random() * 2 - 1) * 4;
        if (valorAtual < 20) valorAtual = 20;
        if (valorAtual > 100) valorAtual = 100;
        valorAtual = Math.round(valorAtual * 10) / 10.0;
        return valorAtual;
    }

    @Override
    public boolean verificarFuncionamento() {
        return valorAtual >= 20 && valorAtual <= 100;
    }

    @Override
    public String getTipo() {
        return "Umidade";
    }

    @Override
    public String getUnidade() {
        return "%";
    }
}