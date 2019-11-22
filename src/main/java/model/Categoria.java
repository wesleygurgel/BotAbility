package model;

public class Categoria extends Saveable {


    public Categoria( String nome, String descricao) {

        this.nome = nome;
        this.descricao = descricao;
    }

    public Categoria(Integer id, String nome, String descricao) {
        this.id = id;

        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "\n" +
                "<b>Codigo da Categoria</b>: " + id + '\n' +
                "<b>Nome: </b>" + nome + '\n' +
                "<b>Descrição: </b>" + descricao + '\n' +
                '\n';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
}
