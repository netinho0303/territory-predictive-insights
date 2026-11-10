import java.util.HashMap;


public class DadosTerritorio {

    private String regiao;
    private int populacao;
    private String codigoAcesso; 
    
    // limiares de cada sensor
    private HashMap<String, double[]> limiares = new HashMap<>();

    public DadosTerritorio(String regiao, int populacao, String codigoAcesso) {
        this.regiao = regiao;
        this.populacao = populacao;
        this.codigoAcesso = codigoAcesso;

        limiares.put("Chuva", new double[] {10, 25, 50});
        limiares.put("Nivel do Rio", new double[] {150, 250, 350});
        limiares.put("Umidade", new double[] {70, 85, 95});
    }

    // getters 
    public String getRegiao() {
        return regiao;
    }

    public int getPopulacao() {
        return populacao;
    }

    
    public double getLimiar(String tipo, int nivel) {
        return limiares.get(tipo)[nivel - 1];
    }

    //  setters com validação
    public void setPopulacao(int novaPopulacao) {
        if (novaPopulacao <= 0) {
            System.out.println("REJEITADO: a população deve ser maior que zero. Valor mantido: " + populacao);
        } else {
            populacao = novaPopulacao;
            System.out.println("População atualizada.");
        }
    }

    public void setRegiao(String novaRegiao) {
        if (novaRegiao == null || novaRegiao.trim().length() < 3) {
            System.out.println("REJEITADO: a região precisa ter pelo menos 3 letras. Valor mantido: " + regiao);
        } else {
            regiao = novaRegiao.trim();
            System.out.println("Região atualizada.");
        }
    }

    
    public void setLimiares(String tipo, double atencao, double alerta, double critico, String codigo) {
        if (!codigoAcesso.equals(codigo)) {
            System.out.println("REJEITADO: código de acesso incorreto. Limiares mantidos.");
            return;
        }
        if (atencao <= 0 || atencao >= alerta || alerta >= critico) {
            System.out.println("REJEITADO: use valores positivos e ATENÇÃO < ALERTA < CRÍTICO. Limiares mantidos.");
            return;
        }
        if (critico > 1000) {
            System.out.println("REJEITADO: limiar fora da faixa (máximo 1000). Limiares mantidos.");
            return;
        }
        limiares.put(tipo, new double[] {atencao, alerta, critico});
        System.out.println("Limiares de " + tipo + " atualizados.");
    }

    //   classificação 
    // retorna 0 = NORMAL, 1 = ATENÇÃO, 2 = ALERTA, 3 = CRÍTICO
    public int avaliar(String tipo, double valor) {
        double[] l = limiares.get(tipo);
        if (valor >= l[2]) return 3;
        if (valor >= l[1]) return 2;
        if (valor >= l[0]) return 1;
        return 0;
    }
}