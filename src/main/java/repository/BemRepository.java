package repository;

import model.Bem;
import model.Categoria;
import model.Localizacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BemRepository {

    private final Conexao conexaoSQL;
    private  final CategoriaRepository categoriaRepository;
    private  final LocalizacaoRepository localizacaoRepository;

    public BemRepository(Conexao conexaoSQL) {
        this.conexaoSQL = conexaoSQL;
        categoriaRepository = new CategoriaRepository(conexaoSQL);
        localizacaoRepository = new LocalizacaoRepository(conexaoSQL);
    }

    /**
     * Cria tabela de Bem no banco'/'.
     * @return
     */
    public  void criarTabela(){
        String sql = "CREATE TABLE IF NOT EXISTS bem(\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    nome text,\n"
                + "    descricao text,\n"
                + "    localizacao INTEGER,\n"
                + "    categoria INTEGER"
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
     * Insere um bem no tabela Bem'/'.
     * @param bem
     */
    public void inserir(Bem bem ){
        String sql = "INSERT INTO bem(nome,descricao,localizacao,categoria) VALUES(?,?,?,?)";
        conexaoSQL.connect();
        try{
            PreparedStatement stm = conexaoSQL.getConn().prepareStatement(sql);
            stm.setString(1,bem.getNome());
            stm.setString(2,bem.getDescricao());
            stm.setInt(3,bem.getLocalizacao().getId());
            stm.setInt(4,bem.getCategoria().getId());
            stm.executeUpdate();
            stm.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
    }
    /**
     * Busca bem  por codigo(id)'/'.
     * @param id
     * @return Bem
     */
    public Bem findById(Integer id) throws BemNotFoundException{
        String sql = "SELECT * "
                + "FROM bem WHERE id = ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setInt(1,id);
            ResultSet rs  = pstmt.executeQuery();
            Localizacao local = null;
            try {
                local = localizacaoRepository.findById(rs.getInt("localizacao"));
            } catch (LocalizacaoRepository.LocalizacaoNotFoundException e) {
                e.printStackTrace();
            }
            Categoria categoria = null;
            try {
                categoria = categoriaRepository.findById(rs.getInt("categoria"));
            } catch (CategoriaRepository.CategoriaNotFoundException e) {
                e.printStackTrace();
            }
            Bem bem = new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria);
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("nome") + "\t" +
                        rs.getString("descricao"));
            pstmt.close();
            return bem;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new BemNotFoundException();
        }finally {
            conexaoSQL.desconect();
        }
    }
    /**
     * Busca todos os dados na tabela'/'.
     * @return lista de bens
     */
    public List<Bem> findall(){
        String sql = "SELECT * FROM bem";
        Localizacao local;
        Categoria categoria ;
        try {
            conexaoSQL.connect();
            Statement stmt  = conexaoSQL.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            List<Bem> bens = new ArrayList<Bem>();
            while (rs.next()) {
                try {
                    local = localizacaoRepository.findById(rs.getInt("localizacao"));
                    categoria = categoriaRepository.findById(rs.getInt("categoria"));
                    bens.add(new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria));
                    System.out.println(rs.getInt("id") +  "\t" +
                            rs.getString("nome") + "\t" +
                            rs.getString("descricao") + "\t" +
                            "Localizacao: "+ local.getNome());
                } catch (LocalizacaoRepository.LocalizacaoNotFoundException e) {
                    e.printStackTrace();
                }catch (CategoriaRepository.CategoriaNotFoundException e) {
                    e.printStackTrace();
                }
            }
            stmt.close();
            return bens;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
        return null;
    }

    /**
     * Buscar bens por localizacao'/'.
     * @param localizacao
     * @return lista de bens
     */
    public List<Bem> findByLocal (String localizacao) throws BemNotFoundException{
        String sql = "SELECT * FROM bem WHERE localizacao = ?";
        Localizacao local = null;
        try {
            local = localizacaoRepository.findByName(localizacao);
        } catch (LocalizacaoRepository.LocalizacaoNotFoundException e) {
            e.printStackTrace();
        }
        Categoria categoria ;
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setInt(1,local.getId());
            ResultSet rs  = pstmt.executeQuery();
            List<Bem> bens = new ArrayList<Bem>();
            while (rs.next()) {
                try {
                    categoria = categoriaRepository.findById(rs.getInt("categoria"));
                    bens.add(new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria));
                    System.out.println(rs.getInt("id") +  "\t" +
                            rs.getString("nome") + "\t" +
                            rs.getString("descricao"));
                } catch (CategoriaRepository.CategoriaNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if(bens.isEmpty()){
                throw new BemNotFoundException();
            }
            pstmt.close();
            return bens;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
        return null;
    }

    /**
     * Busca bem  por nome'/'.
     * @param nome
     * @return lista de bens com mesmo nome ou sub string do nome
     */
    public List<Bem> findByName(String nome) throws BemNotFoundException{
        String sql = "SELECT * "
                + "FROM bem WHERE nome LIKE ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setString(1,"%"+nome+ "%");
            ResultSet rs  = pstmt.executeQuery();
            Localizacao local = null;
            Categoria categoria = null ;
            List<Bem> bens = new ArrayList<Bem>();
            while (rs.next()) {
                try{
                    local = localizacaoRepository.findById(rs.getInt("localizacao"));
                    categoria = categoriaRepository.findById(rs.getInt("categoria"));
                    bens.add(new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria));
                    System.out.println(rs.getInt("id") +  "\t" +
                            rs.getString("nome") + "\t" +
                            rs.getString("descricao"));
                }catch(LocalizacaoRepository.LocalizacaoNotFoundException e){
                    e.printStackTrace();
                }catch (CategoriaRepository.CategoriaNotFoundException e){
                    e.printStackTrace();
                }
            }
            pstmt.close();
            if(bens.isEmpty()){
                throw new BemNotFoundException();
            }
            return bens;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            conexaoSQL.desconect();
        }
    }
    /**
     * Buscar por substring de descrição'/'.
     * @param descricao
     * @return lista de bens
     */
    public List<Bem> findByDescription(String descricao) throws BemNotFoundException{
        String sql = "SELECT * "
                + "FROM bem WHERE descricao LIKE ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setString(1,"%"+descricao+"%");
            ResultSet rs  = pstmt.executeQuery();
            Localizacao local = null;
            Categoria categoria = null ;
            List<Bem> bens = new ArrayList<Bem>();
            while (rs.next()) {
                try{
                    local = localizacaoRepository.findById(rs.getInt("localizacao"));
                    categoria = categoriaRepository.findById(rs.getInt("categoria"));
                    bens.add(new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria));
                    System.out.println(rs.getInt("id") +  "\t" +
                            rs.getString("nome") + "\t" +
                            rs.getString("descricao"));
                }catch (LocalizacaoRepository.LocalizacaoNotFoundException e){
                    e.printStackTrace();
                }catch (CategoriaRepository.CategoriaNotFoundException e){
                    e.printStackTrace();
                }
            }
            if(bens.isEmpty()){
                throw new BemNotFoundException();
            }
            pstmt.close();
            return bens;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }finally {
            conexaoSQL.desconect();
        }
    }
    /**
     * Buscar todos '/'.
     * @return lista de bens
     */
    public List<Bem> findallOrder(){
        String sql = "SELECT * FROM bem ORDER BY localizacao ASC, categoria ASC, nome ASC;";
        Localizacao local;
        Categoria categoria ;
        try {
            conexaoSQL.connect();
            Statement stmt  = conexaoSQL.getConn().createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            List<Bem> bens = new ArrayList<Bem>();
            try {
                while (rs.next()) {
                    local = localizacaoRepository.findById(rs.getInt("localizacao"));
                    categoria = categoriaRepository.findById(rs.getInt("categoria"));
                    bens.add(new Bem(rs.getInt("id"),rs.getString("nome"),rs.getString("descricao"),local,categoria));
                    System.out.println(rs.getInt("id") +  "\t" +
                            rs.getString("nome") + "\t" +
                            rs.getString("descricao"));
                }

            }catch(LocalizacaoRepository.LocalizacaoNotFoundException e){
                e.printStackTrace();
            }catch (CategoriaRepository.CategoriaNotFoundException e){
                e.printStackTrace();
            }
            stmt.close();
            return bens;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            conexaoSQL.desconect();
        }
        return null;
    }


    public void deleteByID(Integer id) throws BemNotFoundException{
        String sql = "DELETE * "
                + "FROM bem WHERE id = ?";
        try {
            conexaoSQL.connect();
            PreparedStatement pstmt  = conexaoSQL.getConn().prepareStatement(sql);
            pstmt.setInt(1,id);
            pstmt.executeQuery();

        }catch (SQLException e) {
            e.printStackTrace();
            throw new BemNotFoundException();
        }finally {
            conexaoSQL.desconect();
        }
    }

    public static class BemNotFoundException extends Exception {
        public BemNotFoundException() {
            super();
        }

        @Override
        public String getMessage() {
            return "Bem não foi encontrado.";
        }
    }
}
