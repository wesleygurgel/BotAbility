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

    public Bem changeLocation( Integer id_bem , Integer newLocal) throws BemRepository.BemNotFoundException, LocalizacaoRepository.LocalizacaoNotFoundException {
        Bem bem =  bemRepository.findById(id_bem);
        Localizacao local = localizacaoRepository.findById(newLocal);
        if(local != null){
            bem.setLocalizacao(local);
            bemRepository.update(bem);
            return bem;
        }
        return null;
    }

    public List<Bem> getRelatorio(){
        return  bemRepository.findallOrder();
    }
}
