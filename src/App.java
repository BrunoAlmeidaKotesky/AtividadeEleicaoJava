
public class App {
    public static void main(String[] args) {
        Eleicao eleicao = new Eleicao();
        //Inicie a votacao como false, pois esta no primeiro turno.
        Turnos turno = eleicao.votacao(false);
        //Se o retorno da funcao votacaof or SEGUNDO_TURNO, execute o metodo novamente, porem desta vez informando que estamos no segundo turno.
        if (turno == Turnos.SEGUNDO_TURNO) {
            eleicao.votacao(true);
        }
    }
}