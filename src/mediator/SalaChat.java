package mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalaChat implements Mediator {
    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public void registrar(Usuario usuario) {
        Objects.requireNonNull(usuario, "O usuario e obrigatorio.");
        if (!usuarios.contains(usuario)) {
            usuarios.add(usuario);
        }
    }

    @Override
    public void enviarMensagem(Usuario remetente, String mensagem) {
        if (!usuarios.contains(remetente)) {
            throw new IllegalArgumentException("O remetente deve estar registrado na sala.");
        }
        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem deve conter texto.");
        }

        for (Usuario usuario : usuarios) {
            if (usuario != remetente) {
                usuario.receberMensagem(remetente.getNome() + ": " + mensagem);
            }
        }
    }
}
