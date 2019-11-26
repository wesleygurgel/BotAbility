package repository;

import model.Localizacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LocalizacaoRepository {
    private final Conexao conexaoSQL;

    public LocalizacaoRepository(Conexao conn){
        conexaoSQL = conn;
    }

    /**
     * Cria tabela localizção no banco'/'.
     * @return
     */
    public  void criarTabela(){
        String sql = "CREATE TABLE IF NOT EXISTS localizacao(\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    nome text,\n"
                + "    descricao text"
                + ");";
        conexaoSQL.connect();
        System.out.println(sql);
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
     * Adiciona uma nova localizção no banco'/'.
     * @param local
     * @return
     */
    public void inserir(Localizacao local ){
        String sql = "INSERT INTO localizacao(nome,descricao) VALUES(?,?)";
        conexaoSQL.connect();
        try{
            PreparedStatement stm = conexaoSQL.getConn().prepareStatement(sql);
            stm.setString(1,local.getNome());
            stm.setString(2,local.getDescricao());
            stm.executeUpdate();
            stm.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }

    }

    /**
     * Busca todas as localizção no banco'/'.
     * @return lista de localizção
     */
    public  List<Localizacao>  findall(){
        String sql = "SELECT * FROM localizacao";

        try {
            conexaoSQL.connect();
             Statement stmt  = conexaoSQL.getConn().createStatement();
             ResultSet rs    = stmt.executeQuery(sql);
            List<Localizacao> locais = new ArrayList<Localizacao>();
            while (rs.next()) {
                locais.add(new Localizacao(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao")));
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("nome") + "\t" +
//                        rs.getString("descricao"));
            }
            stmt.close();
            return locais;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
        return null;
    }

    /**
     * Busca localização por id'/'.
     * @param id
     * @return localizção
     */
    public Localizacao findById(Integer id) throws LocalizacaoNotFoundException {
        String sql = "SELECT * "
                + "FROM localizacao WHERE id = ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();
            Localizacao local = new Localizacao(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"));
//            while (rs.next()) {
//                System.out.println(rs.getInt("id") +  "\t" +
//                        rs.getString("nome") + "\t" +
//                        rs.getString("descricao"));
//            }
            pstmt.close();
            return local;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new LocalizacaoNotFoundException();
        }finally {
            conexaoSQL.desconect();
        }
    }
    /**
     * Busca todas localização com substring em nome'/'.
     * @param nome
     * @return lista de localizção
     */
    public Localizacao findByName(String nome) throws LocalizacaoNotFoundException{
        String sql = "SELECT * "
                + "FROM localizacao WHERE nome = ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setString(1,nome);
            ResultSet rs  = pstmt.executeQuery();
            Localizacao local = new Localizacao(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"));

            pstmt.close();
            return local;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new LocalizacaoNotFoundException();
        }finally {
            conexaoSQL.desconect();
        }
    }

    /**
     * Deleta uma Localização, caso não existam bens relacionados a ela.
     * Deleta por nome'/'.
     * @param name
     * @throws LocalizacaoNotFoundException
     */
    public void deleteLocalizationByID(String name) throws LocalizacaoNotFoundException{
        String sql = "DELETE FROM localizacao "
                + "WHERE nome = ?";
        try {
            System.out.println("delete11");
            conexaoSQL.connect();
            System.out.println("delete22");
            PreparedStatement pstmt = conexaoSQL.getConn().prepareStatement(sql);
            System.out.println("delete33");
            pstmt.setString(1, name);
            System.out.println("delete44");
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexaoSQL.desconect();
        }
    }

    public static class LocalizacaoNotFoundException extends Exception {
        public LocalizacaoNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Localização não foi encontrada.";
        }
    }
}
