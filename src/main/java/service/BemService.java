package service;

import model.Bem;
import model.Localizacao;
import repository.BemRepository;
import repository.Conexao;
import repository.LocalizacaoRepository;

import java.util.List;

public class BemService {

    private final BemRepository bemRepository;

    private  final LocalizacaoRepository localizacaoRepository;

    public BemService(Conexao conn){
        bemRepository = new BemRepository(conn);
        localizacaoRepository = new LocalizacaoRepository(conn);
    }

    public List<Bem> getRelatorio(){
        return  bemRepository.findallOrder();
    }
}
