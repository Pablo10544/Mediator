package mediator;

public interface Mediator {
    void registrar(Usuario usuario);

    void enviarMensagem(Usuario remetente, String mensagem);
}
