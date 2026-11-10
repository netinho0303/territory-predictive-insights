import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// Classe principal
public class SistemaMonitoramento {

    
    static class EstacaoDeColeta extends ComponenteTerritorial {

        private ArrayList<Sensor> sensores = new ArrayList<>();

        public EstacaoDeColeta(String id, String nome, String regiao) {
            super(id, nome, regiao);
        }

        public void adicionarSensor(Sensor sensor) {
            sensores.add(sensor);
        }

        public ArrayList<Sensor> getSensores() {
            return sensores;
        }

        @Override
        public String verificarStatus() {
            int funcionando = 0;
            for (Sensor s : sensores) {
                if (s.verificarFuncionamento()) {
                    funcionando++;
                }
            }
            return "Estação " + getNome() + " (" + getStatus() + ") - "
                    + funcionando + "/" + sensores.size() + " sensores funcionando";
        }
    }

    static final String[] NIVEIS = {"NORMAL", "ATENÇÃO", "ALERTA", "CRÍTICO"};
    static final String[] ORIENTACOES = {
        "Nenhuma ação necessária.",
        "Acompanhar as próximas leituras e avisar a equipe de plantão.",
        "Preparar a equipe, verificar bueiros e avisar moradores das áreas de risco.",
        "Risco alto de alagamento: avaliar com a Defesa Civil rotas de evacuação e abrigos."
    };

    private Scanner entrada = new Scanner(System.in);
    private DadosTerritorio dados;
    private EstacaoDeColeta estacao;
    private HashMap<String, ArrayList<Double>> leituras = new HashMap<>(); // histórico por sensor
    private ArrayList<EstrategiaPrevisao> estrategias = new ArrayList<>();

    public SistemaMonitoramento() {
        dados = new DadosTerritorio("Zona Sul - Vale do Corrego", 12000, "1234");

        estacao = new EstacaoDeColeta("EST-01", "Vale do Corrego", dados.getRegiao());
        estacao.adicionarSensor(new SensorChuva());
        estacao.adicionarSensor(new SensorNivelRio());
        estacao.adicionarSensor(new SensorUmidade());
        estacao.ativar();

        for (Sensor s : estacao.getSensores()) {
            leituras.put(s.getTipo(), new ArrayList<Double>());
        }

        estrategias.add(new PrevisaoMediaMovel(5));
        estrategias.add(new PrevisaoComparacao(3));
    }

    public static void main(String[] args) {
        SistemaMonitoramento sistema = new SistemaMonitoramento();
        sistema.executar();
    }

    // LOOP PRINCIPAL 
    public void executar() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n===== TERRITORY PREDICTIVE INSIGHTS =====");
            System.out.println("1 - Verificar sensores");
            System.out.println("2 - Rodar previsões e alertas");
            System.out.println("3 - Gerenciar dados do território");
            System.out.println("4 - Simular próximas leituras");
            System.out.println("5 - Exibir status completo");
            System.out.println("0 - Sair");
            opcao = lerInt("Escolha: ");

            if (opcao == 1) {
                verificarSensores();
            } else if (opcao == 2) {
                rodarPrevisoes();
            } else if (opcao == 3) {
                gerenciarDados();
            } else if (opcao == 4) {
                simularLeituras();
            } else if (opcao == 5) {
                exibirStatus();
            } else if (opcao == 0) {
                System.out.println("Encerrando. Até logo!");
            } else {
                System.out.println("Opção inválida.");
            }
        }
    }

    // SENSORES 
    private void verificarSensores() {
        System.out.println(estacao.verificarStatus());
        for (Sensor s : estacao.getSensores()) {
            double valor = s.lerValor();
            leituras.get(s.getTipo()).add(valor);

            int nivel = dados.avaliar(s.getTipo(), valor);
            System.out.println(s.getTipo() + ": " + valor + " " + s.getUnidade()
                    + " | funcionando: " + s.verificarFuncionamento()
                    + " | situação: " + NIVEIS[nivel]);
            if (nivel > 0) {
                System.out.println("   Passou do limite de " + NIVEIS[nivel] + " ("
                        + dados.getLimiar(s.getTipo(), nivel) + " " + s.getUnidade() + ")");
            }
        }
    }

    //  PREVISÕES E ALERTAS 
    private void rodarPrevisoes() {
        int totalAlertas = 0;
        for (Sensor s : estacao.getSensores()) {
            ArrayList<Double> historico = leituras.get(s.getTipo());
            System.out.println("\n--- " + s.getTipo() + " (" + historico.size() + " leituras) ---");

            for (EstrategiaPrevisao e : estrategias) {
                if (!e.temLeiturasSuficientes(historico)) {
                	
                    // o sistema "se cala" quando há poucos dados
                	
                    System.out.println(e.getNome() + ": sem previsão (precisa de pelo menos "
                            + e.getMinimoLeituras() + " leituras).");
                    continue;
                }
                double previsto = e.prever(historico);
                int nivel = dados.avaliar(s.getTipo(), previsto);

                System.out.println(e.getNome() + ": previsto " + String.format("%.1f", previsto)
                        + " " + s.getUnidade() + " -> " + NIVEIS[nivel]);
                System.out.println("   " + e.explicar(previsto, s.getUnidade()));

                if (nivel > 0) {
                    emitirAlerta(s, e, previsto, nivel);
                    totalAlertas++;
                }
            }
        }
        System.out.println("\nTotal de alertas: " + totalAlertas);
    }

    private void emitirAlerta(Sensor s, EstrategiaPrevisao e, double previsto, int nivel) {
        double limite = dados.getLimiar(s.getTipo(), nivel);
        System.out.println("   !!!!!!!!!! [" + NIVEIS[nivel] + "] !!!!!!!!!!");
        System.out.println("   Sensor: " + s.getTipo());
        System.out.println("   Valor previsto: " + String.format("%.1f", previsto) + " " + s.getUnidade()
                + " (" + e.getNome() + ")");
        System.out.println("   Limite estourado: " + limite + " " + s.getUnidade());
        System.out.println("   Sugestão: " + ORIENTACOES[nivel]);
        System.out.println("   (O sistema só apoia: a decisão final é de quem conhece o território.)");
    }

    //  DADOS DO TERRITÓRIO 
    private void gerenciarDados() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- Dados do território ---");
            System.out.println("1 - Ver dados");
            System.out.println("2 - Alterar população");
            System.out.println("3 - Alterar região");
            System.out.println("4 - Alterar limiares (precisa de código)");
            System.out.println("0 - Voltar");
            op = lerInt("Escolha: ");

            if (op == 1) {
                mostrarDados();
            } else if (op == 2) {
                dados.setPopulacao(lerInt("Nova população: "));
            } else if (op == 3) {
                dados.setRegiao(lerTexto("Nova região: "));
            } else if (op == 4) {
                alterarLimiares();
            } else if (op != 0) {
                System.out.println("Opção inválida.");
            }
        }
    }

    private void alterarLimiares() {
        ArrayList<Sensor> sensores = estacao.getSensores();
        for (int i = 0; i < sensores.size(); i++) {
            System.out.println((i + 1) + " - " + sensores.get(i).getTipo());
        }
        int escolha = lerInt("Sensor: ");
        if (escolha < 1 || escolha > sensores.size()) {
            System.out.println("REJEITADO: sensor inexistente.");
            return;
        }
        String tipo = sensores.get(escolha - 1).getTipo();
        double atencao = lerDouble("Novo limiar ATENÇÃO: ");
        double alerta = lerDouble("Novo limiar ALERTA: ");
        double critico = lerDouble("Novo limiar CRÍTICO: ");
        String codigo = lerTexto("Código de acesso: ");
        dados.setLimiares(tipo, atencao, alerta, critico, codigo);
    }

    private void mostrarDados() {
        System.out.println("Região: " + dados.getRegiao());
        System.out.println("População: " + dados.getPopulacao());
        for (Sensor s : estacao.getSensores()) {
            System.out.println("Limiares de " + s.getTipo() + " (" + s.getUnidade() + "): ATENÇÃO "
                    + dados.getLimiar(s.getTipo(), 1) + " | ALERTA "
                    + dados.getLimiar(s.getTipo(), 2) + " | CRÍTICO "
                    + dados.getLimiar(s.getTipo(), 3));
        }
    }

    //  SIMULAÇÃO
    private void simularLeituras() {
        int quantidade = lerInt("Quantas leituras simular (1 a 50)? ");
        if (quantidade < 1 || quantidade > 50) {
            System.out.println("Quantidade inválida.");
            return;
        }
        for (int i = 0; i < quantidade; i++) {
            for (Sensor s : estacao.getSensores()) {
                leituras.get(s.getTipo()).add(s.lerValor());
            }
        }
        System.out.println(quantidade + " leituras simuladas por sensor.");
        rodarPrevisoes();
    }

    //  STATUS COMPLETO 
    private void exibirStatus() {
        System.out.println("\n===== STATUS COMPLETO =====");
        System.out.println(estacao.verificarStatus());
        mostrarDados();
        for (Sensor s : estacao.getSensores()) {
            ArrayList<Double> historico = leituras.get(s.getTipo());
            String ultima = "-";
            if (!historico.isEmpty()) {
                ultima = historico.get(historico.size() - 1) + " " + s.getUnidade();
            }
            System.out.println(s.getTipo() + ": " + historico.size() + " leituras | última: " + ultima);
        }
        for (EstrategiaPrevisao e : estrategias) {
            System.out.println("Estratégia: " + e.getNome() + " (mínimo " + e.getMinimoLeituras() + " leituras)");
        }
    }

    //   ENTRADA DO USUÁRIO 
    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return entrada.nextLine().trim();
    }

    private int lerInt(String mensagem) {
        try {
            return Integer.parseInt(lerTexto(mensagem));
        } catch (NumberFormatException e) {
            System.out.println("Digite um número inteiro.");
            return -1;
        }
    }

    private double lerDouble(String mensagem) {
        try {
            return Double.parseDouble(lerTexto(mensagem).replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Digite um número.");
            return -1;
        }
    }
}