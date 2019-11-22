package model;

public class Bem extends Saveable {
    private Localizacao localizacao;
    private Categoria categoria;


    public Bem(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;

    }
    public Bem(String nome, String descricao, Localizacao localizacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;

    }
    public Bem(String nome, String descricao, Localizacao localizacao, Categoria categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.categoria = categoria;

    }

    public Bem(Integer id, String nome, String descricao, Localizacao localizacao, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.categoria = categoria;
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

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "\n" +
                "<b>CODIGO:</b> " + id + '\n' +
                "<b>NOME:</b> " + nome + '\n' +
                "<b>DESCRIÇÃO: </b>" + descricao + '\n' +
                "<b>LOCALIZACAO:</b>" + localizacao.getNome() + '\n' +
                "<b>CATEGORIA:</b>" + categoria.getNome() + '\n' +
                '\n';
    }
}
