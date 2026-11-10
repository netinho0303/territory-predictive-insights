
public interface Sensor {

    double lerValor();               // faz uma leitura (simulada)

    boolean verificarFuncionamento(); // Verificação do funcionamento do sensor 

    String getTipo();                // "Chuva", "Nivel do Rio", "Umidade"

    String getUnidade();             // mm/h, cm, %
}