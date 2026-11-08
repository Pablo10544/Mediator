package mediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private final String nome;
    private final Mediator mediator;
    private final List<String> mensagensRecebidas = new ArrayList<>();

    public Usuario(String nome, Mediator mediator) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome deve conter texto.");
        }
        this.nome = nome;
        this.mediator = Objects.requireNonNull(mediator, "O mediador e obrigatorio.");
        mediator.registrar(this);
    }

    public String getNome() {
        return nome;
    }

    public void enviarMensagem(String mensagem) {
        mediator.enviarMensagem(this, mensagem);
    }

    void receberMensagem(String mensagem) {
        mensagensRecebidas.add(mensagem);
    }

    public List<String> getMensagensRecebidas() {
        return Collections.unmodifiableList(new ArrayList<>(mensagensRecebidas));
    }
}
