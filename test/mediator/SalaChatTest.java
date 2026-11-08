package mediator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SalaChatTest {
    @Test
    void deveEntregarMensagemAosOutrosUsuariosSemEnviarAoRemetente() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);
        Usuario carla = new Usuario("Carla", sala);

        ana.enviarMensagem("Ola!");

        assertEquals(Collections.singletonList("Ana: Ola!"), bruno.getMensagensRecebidas());
        assertEquals(Collections.singletonList("Ana: Ola!"), carla.getMensagensRecebidas());
        assertTrue(ana.getMensagensRecebidas().isEmpty());
    }

    @Test
    void devePermitirRespostaEPreservarOrdemDasMensagens() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);
        Usuario carla = new Usuario("Carla", sala);

        ana.enviarMensagem("Ola!");
        bruno.enviarMensagem("Oi, Ana!");

        assertEquals(Collections.singletonList("Bruno: Oi, Ana!"), ana.getMensagensRecebidas());
        assertEquals(Collections.singletonList("Ana: Ola!"), bruno.getMensagensRecebidas());
        assertEquals(Arrays.asList("Ana: Ola!", "Bruno: Oi, Ana!"), carla.getMensagensRecebidas());
    }

    @Test
    void deveManterSalasIndependentes() {
        Usuario ana = new Usuario("Ana", new SalaChat());
        Usuario bruno = new Usuario("Bruno", new SalaChat());

        ana.enviarMensagem("Ola!");

        assertTrue(bruno.getMensagensRecebidas().isEmpty());
        assertTrue(ana.getMensagensRecebidas().isEmpty());
    }

    @Test
    void naoDeveDuplicarEntregaAoRegistrarUsuarioNovamente() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);
        sala.registrar(bruno);

        ana.enviarMensagem("Ola!");

        assertEquals(Collections.singletonList("Ana: Ola!"), bruno.getMensagensRecebidas());
    }

    @Test
    void deveRejeitarRemetenteNaoRegistradoSemEntregarMensagem() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", new SalaChat());

        assertThrows(IllegalArgumentException.class, () -> sala.enviarMensagem(bruno, "Ola!"));
        assertTrue(ana.getMensagensRecebidas().isEmpty());
    }

    @Test
    void deveRejeitarMensagensSemTextoSemAlterarHistorico() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);

        assertThrows(IllegalArgumentException.class, () -> ana.enviarMensagem(null));
        assertThrows(IllegalArgumentException.class, () -> ana.enviarMensagem(""));
        assertThrows(IllegalArgumentException.class, () -> ana.enviarMensagem(" \t\n"));
        assertTrue(bruno.getMensagensRecebidas().isEmpty());
    }

    @Test
    void deveRetornarCopiaImutavelDoHistorico() {
        SalaChat sala = new SalaChat();
        Usuario ana = new Usuario("Ana", sala);
        Usuario bruno = new Usuario("Bruno", sala);
        ana.enviarMensagem("Primeira");
        List<String> historico = bruno.getMensagensRecebidas();

        assertThrows(UnsupportedOperationException.class, () -> historico.add("Mensagem externa"));
        ana.enviarMensagem("Segunda");

        assertEquals(Collections.singletonList("Ana: Primeira"), historico);
        assertEquals(Arrays.asList("Ana: Primeira", "Ana: Segunda"), bruno.getMensagensRecebidas());
    }

    @Test
    void deveRejeitarNomeSemTexto() {
        SalaChat sala = new SalaChat();

        assertThrows(IllegalArgumentException.class, () -> new Usuario(null, sala));
        assertThrows(IllegalArgumentException.class, () -> new Usuario("", sala));
        assertThrows(IllegalArgumentException.class, () -> new Usuario("  ", sala));
    }

    @Test
    void deveRejeitarMediadorOuUsuarioNulo() {
        SalaChat sala = new SalaChat();

        assertThrows(NullPointerException.class, () -> new Usuario("Ana", null));
        assertThrows(NullPointerException.class, () -> sala.registrar(null));
    }
}
