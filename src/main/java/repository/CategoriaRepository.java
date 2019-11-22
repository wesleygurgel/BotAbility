package repository;

import model.Categoria;
import model.Localizacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRepository {

    private final Conexao conexaoSQL;

    public CategoriaRepository(Conexao conn){
        conexaoSQL = conn;
    }

    /**
     * Cria tabela de categoria no banco'/'.
     * @return
     */
    public  void criarTabela(){
        String sql = "CREATE TABLE IF NOT EXISTS categoria(\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    nome text,\n"
                + "    descricao text"
                + ");";
        conexaoSQL.connect();
//        System.out.println(sql);
        Statement stm = conexaoSQL.criarStatement();
        try{
            stm.execute(sql);
            stm.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }

    }
    /**
     * Adiciona uma nova categoria no banco'/'.
     * @param categoria
     * @return
     */
    public void inserir(Categoria categoria){
        String sql = "INSERT INTO categoria(nome,descricao) VALUES(?,?)";
        conexaoSQL.connect();
        try{
            PreparedStatement stm = conexaoSQL.getConn().prepareStatement(sql);
            stm.setString(1,categoria.getNome());
            stm.setString(2,categoria.getDescricao());
            stm.executeUpdate();
            stm.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }

    }
    /**
     * Busca todos os dados da tabela categoria'/'.
     * @return lista de categorias
     */
    public List<Categoria> findall(){
        String sql = "SELECT * FROM categoria";

        try {
            conexaoSQL.connect();
            Statement stmt  = conexaoSQL.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            List<Categoria> categorias = new ArrayList<Categoria>();
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao")));
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("nome") + "\t" +
//                        rs.getString("descricao"));
            }
            stmt.close();
            return categorias;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
        return null;
    }
    /**
     * Busca categoria por id'/'.
     * @param id
     * @return categoria
     */
    public Categoria findById(Integer id) throws CategoriaNotFoundException{
        String sql = "SELECT * "
                + "FROM categoria WHERE id = ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();
            Categoria categoria = new Categoria(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"));
            pstmt.close();
            return categoria;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new CategoriaNotFoundException();
        }finally {
            conexaoSQL.desconect();
        }
    }

    public static class CategoriaNotFoundException extends Exception {
        public CategoriaNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Categoria não foi encontrada.";
        }
    }
}
