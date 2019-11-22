package model;

public class Localizacao extends Saveable {

    public Localizacao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Localizacao(Integer id, String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\n" +
                "<b>Codigo da Localizacao</b>: " + id + '\n' +
                "<b>Nome: </b>" + nome + '\n' +
                "<b>Descrição: </b>" + descricao + '\n' +
                '\n';
    }
}
