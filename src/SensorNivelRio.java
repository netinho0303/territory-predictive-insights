public class SensorNivelRio implements Sensor {

    private double valorAtual = 150;

    @Override
    public double lerValor() {
        valorAtual = valorAtual + (Math.random() * 2 - 1) * 25;
        if (valorAtual < 0) valorAtual = 0;
        if (valorAtual > 500) valorAtual = 500;
        valorAtual = Math.round(valorAtual * 10) / 10.0;
        return valorAtual;
    }

    @Override
    public boolean verificarFuncionamento() {
        return valorAtual >= 0 && valorAtual <= 500;
    }

    @Override
    public String getTipo() {
        return "Nivel do Rio";
    }

    @Override
    public String getUnidade() {
        return "cm";
    }
}