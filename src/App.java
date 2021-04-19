
public class App {
    public static void main(String[] args) {
        Eleicao eleicao = new Eleicao();
        Turnos turno = eleicao.votacao(false);
        if (turno == Turnos.SEGUNDO_TURNO) {
            eleicao.votacao(true);
        }
    }
}