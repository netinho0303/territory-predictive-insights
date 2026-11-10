
public abstract class ComponenteTerritorial {

    private String id;
    private String nome;
    private String regiao;
    private String status; // "ATIVO" ou "INATIVO"

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = "INATIVO";
    }

    // métodos concretos
    public void ativar() {
        status = "ATIVO";
        System.out.println(nome + " ativado.");
    }

    public void desativar() {
        status = "INATIVO";
        System.out.println(nome + " desativado.");
    }

    // método abstrato
    public abstract String verificarStatus();

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public String getStatus() {
        return status;
    }
}