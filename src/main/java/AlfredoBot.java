import Constants.Constants;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.abilitybots.api.bot.AbilityBot;
import repository.BemRepository;
import repository.CategoriaRepository;
import repository.Conexao;
import repository.LocalizacaoRepository;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Flag.MESSAGE;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

public class AlfredoBot extends AbilityBot{
    private final ResponseHandler responseHandler;

    /**
     * Inicializa o bot.
     */
    public AlfredoBot(){
        super(Constants.BOT_TOKEN, Constants.BOT_USERNAME);
        /**
        * Cria o banco e suas tabelas caso não existam.
        *
        * */
        Conexao conexao = Conexao.getConexao();
        LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository(conexao);
        localizacaoRepository.criarTabela();
        CategoriaRepository categoriaRepository= new CategoriaRepository(conexao);
        categoriaRepository.criarTabela();
        BemRepository bemRepository = new BemRepository(conexao);
        bemRepository.criarTabela();

        /**
         * Cria o responseHandler, uma classe externa para tratar as entradas e saídas.
         */
        responseHandler = new ResponseHandler(sender, db);
    }

    /**
     * Retorna o id do mestre desse robô, configurável em Constants.
     * @return
     */
    public int creatorId() {
        return Constants.CREATOR_ID;
    }

    /**
     * Chama a função replyToButtons na classe do responseHandler para inputs do tipo CALLBACK_QUERY
     * @return
     */
    public Reply replyToButtons(){
        //Consumer: Interface funcional que recebe uma função à ser aplicada depois.
        Consumer<Update> action = upd -> responseHandler.replyToButtons(getChatId(upd), upd.getCallbackQuery().getData());
        //Flag.CALLBACK_QUERY: identifica o tipo de Reply que veio do usuário como CALLBACK_QUERY, o dos botões.
        return Reply.of(action, Flag.CALLBACK_QUERY);
    }

    /**
     * Chama a função receiveInput na classe do responseHandler se o reply for message, tiver texto e não conter '/'.
     * @return
     */
    public Reply receiveInput(){
        Consumer<Update> action = upd -> responseHandler.receiveInput(getChatId(upd), upd.getMessage().getText());
        //Apenas chama o action se o reply for message, tiver texto e não conter /
        return Reply.of(action, MESSAGE, update -> update.getMessage().hasText() && !update.getMessage().getText().contains("/"));
    }

    /**
     * Ao receber o comando start do usuário chama a função waitingForCommand no response handler.
     * @return
     */
    public Ability replyToStart(){
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(ALL)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.waitingForCommand(ctx.chatId()))
                .build();
    }


}
