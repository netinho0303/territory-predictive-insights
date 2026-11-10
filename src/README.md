# Territory Predictive Insights (CitizenTech)
## Clima local: chuva e risco de alagamento

Plataforma de monitoramento territorial em Java (POO) que lê sensores simulados, faz previsões simples e emite alertas explicados.

---

## 1. Público beneficiado e decisão apoiada

- **Quem sofre o problema:** moradores de áreas baixas e próximas ao córrego (ex.: "Zona Sul - Vale do Córrego").
- **Quem usa o sistema:** associação de bairro, equipe comunitária e Defesa Civil municipal.
- **Decisão que o sistema apoia:** "Devemos avisar moradores, vistoriar bueiros ou acionar rotas de evacuação hoje?"

O território é descrito somente por região e equipamento. Nenhum dado pessoal é utilizado.

---

## 2. Como rodar o projeto

**Requisitos:** JDK 11 ou superior.

### Compilação

```bash
javac -encoding UTF-8 *.java
```

### Execução

```bash
java -Dfile.encoding=UTF-8 SistemaMonitoramento
```

**Observação:** No Windows, caso os acentos apareçam incorretamente, execute o comando abaixo antes de rodar o programa:

```bash
chcp 65001
```

### Roteiro rápido de teste

1. **Opção 2:** logo no início, o sistema se cala devido à quantidade insuficiente de leituras.
2. **Opção 4:** digite `20` para simular leituras e executar previsões e alertas automaticamente. A simulação é aleatória, portanto, repita para visualizar diferentes níveis.
3. **Opção 3:** teste população negativa, região vazia e a troca de limiares.

**Código de acesso padrão (somente para teste):** `1234`.

---

## 3. Estrutura e conceitos de POO

| Arquivo | Papel |
|---|---|
| `ComponenteTerritorial` | Classe abstrata (id, nome, região, status; `ativar()`/`desativar()`; método abstrato `verificarStatus()`). |
| `EstacaoDeColeta` | Classe concreta (`extends ComponenteTerritorial`), classe interna de `SistemaMonitoramento.java`. |
| `Sensor` | Interface (`lerValor`, `verificarFuncionamento`, `getTipo`, `getUnidade`). |
| `SensorChuva`, `SensorNivelRio`, `SensorUmidade` | Implementam `Sensor` (`implements Sensor`), com valores simulados por `Math.random()`. |
| `DadosTerritorio` | Encapsulamento: atributos privados, setters que validam e limiares protegidos por código de acesso. |
| `EstrategiaPrevisao` | Classe abstrata da herança (`prever()` e `explicar()` abstratos). |
| `PrevisaoMediaMovel` | Herda; atributo `janela`; utiliza `super(...)` e `super.calcularMedia(...)`. |
| `PrevisaoComparacao` | Herda; atributos `tamanhoPeriodo`, `mediaAtual`, `mediaAnterior`; utiliza `super`. |
| `SistemaMonitoramento` | Classe principal: menu, loop, leituras (`ArrayList`), previsões e alertas. |

---

## 4. Decisões da equipe

- **Recorte:** clima local com três sensores:
  - Chuva (mm/h).
  - Nível do córrego (cm).
  - Umidade (%).

- **Previsões simples:**
  - Média das últimas 5 leituras.
  - Comparação entre períodos de 3 leituras (previsto = média atual + variação).

### Limiares padrão

| Sensor | Atenção | Alerta | Crítico |
|---|---:|---:|---:|
| Chuva | 10 | 25 | 50 |
| Nível | 150 | 250 | 350 |
| Umidade | 70 | 85 | 95 |

*Os valores são ilustrativos e devem ser calibrados com a Defesa Civil.*

Cada alerta apresenta o sensor, o valor previsto, a estratégia utilizada, o limite ultrapassado e a sugestão de ação.

---

## 5. Reflexão crítica

### Quando o sistema deve se calar?

Quando há poucas leituras. A média móvel exige 5 leituras e a comparação de períodos exige 6. Abaixo disso, o sistema informa que não há dados suficientes em vez de "chutar".

Também deveria se calar ou avisar quando um sensor está com defeito, pois uma previsão baseada em leitura ausente ou travada pode transmitir uma falsa sensação de segurança.

### O que os dados não representam?

Um pluviômetro e uma régua de nível por região não mostram diferenças entre ruas, becos e encostas. O alagamento depende de drenagem, lixo nos bueiros, relevo e chuva a montante, fatores que não medimos.

Além disso, nossos dados são simulados e as previsões são lineares e simples, não captando mudanças bruscas de tempestade.

### Por que o alerta apoia e não decide?

Quem conhece o território (moradores, agentes comunitários e Defesa Civil) possui informações que o sistema não tem.

As consequências de evacuar ou não são humanas e sociais. Falsos positivos desgastam a confiança e falsos negativos custam caro.

Por isso, o sistema explica o motivo do alerta e sugere ações, mas a decisão final é da pessoa.

---

## 6. Uso de IA

Utilizamos a IA para ajudar na criação dos códigos, verificar correções de código, solucionar complicações e auxiliar na utilização do `README.md`.

---

## 7. Divisão de tarefas

- **Dicley Lucas Neto:** sensores e `ComponenteTerritorial` / `EstacaoDeColeta`.
- **Nathan de Mello Teixeira:** previsões.
- **Pedro Ivson Falcão de Leucas:** `DadosTerritorio`, menu e alertas.

Todos devem realizar commits no GitHub.

---

## 8. Integrantes do grupo

| Integrante | RM |
|---|---|
| Dicley Lucas Neto | 567588 |
| Pedro Ivson Falcão de Leucas | 568522 |
| Nathan de Mello Teixeira | 567429 |

---

## 9. O que faltou / próximos passos

- Histórico de leituras em arquivo (bônus) e dados reais (ex.: INMET).
- Calibrar limiares com dados históricos do local.
