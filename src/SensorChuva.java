public class SensorChuva implements Sensor {

    private double valorAtual = 5;

    @Override
    public double lerValor() {
    	
        // o valor sobe ou desce um pouco a cada leitura
    	
        valorAtual = valorAtual + (Math.random() * 2 - 1) * 6;
        if (valorAtual < 0) valorAtual = 0;
        if (valorAtual > 80) valorAtual = 80;
        valorAtual = Math.round(valorAtual * 10) / 10.0;
        return valorAtual;
    }

    @Override
    public boolean verificarFuncionamento() {
        return valorAtual >= 0 && valorAtual <= 80;
    }

    @Override
    public String getTipo() {
        return "Chuva";
    }

    @Override
    public String getUnidade() {
        return "mm/h";
    }
}