package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private static Conexao conexao = new Conexao();

    private Conexao(){

    }
    /**
     * função que garante a existencia de uma unica chamada do objeto Conexão '/'.
     * @return Objeto Conexao
     */
    public static Conexao getConexao() {
        return conexao;
    }

    private Connection conn ;

    /**
     * Cria conexao com o banco '/'.
     * @return boolean que informa sucesso na conexão
     */
    public  boolean connect() {

        try {
            // db parameters
            String url = "jdbc:sqlite:storage/banco.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);

//            System.out.println("Connection to SQLite has been established.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    /**
     * descnecta com o banco '/'.
     * @return boolean que informa sucesso na ação
     */
    public  boolean desconect(){
        try {
            if (conn != null) {
                conn.close();
            }
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    /**
     * Função de retorn de conexão'/'.
     * @return a interface connection da biblioteca
     */
    public  Connection getConn() {
        return conn;
    }

    public  Statement criarStatement (){

        try {
            return  conn.createStatement();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        return null;
        }
    }
}
