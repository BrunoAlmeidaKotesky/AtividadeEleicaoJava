import helpers.Inputs;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

enum Turnos {
    PRIMEIRO_TURNO, SEGUNDO_TURNO, JUSTICA_ELEITORAL
}

public class Eleicao {
    public List<Candidato> candidatos = new ArrayList();
    private int numeroDeSegundoTurno = 0;

    public Eleicao() {
        candidatos.add(new Candidato("Rodrigo", 22, 0));
        candidatos.add(new Candidato("Ananias", 98, 0));
        candidatos.add(new Candidato("Rosângela", 73, 0));
        candidatos.add(new Candidato("Augusto", 41, 0));
        candidatos.add(new Candidato("Joana", 28, 0));
        candidatos.add(new Candidato("Eliana", 11, 0));
    }

    public Turnos votacao(boolean segundoTurno) {
        Inputs input = new Inputs();
        boolean votacaoAtiva = true;
        while (votacaoAtiva) {
            int numeroSelecionado = input.intput("Entre com o número do seu candidato: ");
            if (numeroSelecionado == 0) 
                votacaoAtiva = false;
            else {
                boolean numeroCorreto = false;
                Candidato candidatoAtual = null;
                for (Candidato candidato : this.candidatos) {
                    if (candidato.numero == numeroSelecionado) {
                        numeroCorreto = true;
                        candidatoAtual = candidato;
                    }
                }
                if (numeroCorreto) {
                    String confirmado = input.input("Deseja confirmar o voto para " + candidatoAtual.nome + "? (sim)/(nao)");
                    if (confirmado.toLowerCase().equals("sim")) {
                        candidatoAtual.numeroVotos += 1;
                        System.out.println("Você votou com sucesso!");
                    }
                } 
                else {
                    String anular = input.input("Número inválido, deseja anular o seu voto? (sim)/(nao)");
                    if (anular.toLowerCase().equals("sim")) {
                        System.out.println("Você votou nulo com sucesso!");
                    }
                }
            }
        }
        return votacaoNaoAtiva(votacaoAtiva, segundoTurno);
    }

    private Turnos votacaoNaoAtiva(boolean votacaoAtiva, boolean segundoTurno) {
        if (!votacaoAtiva) {
            List<Candidato> escolhidos = maioresEleitos(candidatos);
            int mediaVotos = porcentagemVotos(escolhidos);
            if (escolhidos.size() == 1) {
                if (vencedor().numeroVotos >= mediaVotos) {
                    System.out.println("O candidato " + vencedor().nome + " venceu a eleição");
                    if (segundoTurno)
                        return Turnos.SEGUNDO_TURNO;
                    return Turnos.PRIMEIRO_TURNO;
                }
            } 
            else {
                this.candidatos = escolhidos;
                for (Candidato candidato : candidatos) {
                    candidato.numeroVotos = 0;
                }
                numeroDeSegundoTurno += 1;
                if (numeroDeSegundoTurno > 1) {
                    System.out.println("O caso será julgado pela justiça eleitoral");
                    return Turnos.JUSTICA_ELEITORAL;
                }
                return Turnos.SEGUNDO_TURNO;
            }
        }
        return Turnos.JUSTICA_ELEITORAL;
    }

    private Candidato vencedor() {
        Candidato vencedor = candidatos.stream().findFirst().get();
        return vencedor;
    }

    private List<Candidato> maioresEleitos(List<Candidato> candidatos) {
        int[] totalVotos = new int[6];
        int index = 0;
        for (Candidato candidato : candidatos) {
            totalVotos[index] = candidato.numeroVotos;
            index++;
        }
        int maiorVoto = maximo(totalVotos);
        // JS - Array.filter();
        Predicate<Candidato> maioresVotos = candidato -> candidato.numeroVotos == maiorVoto;

        return candidatos.stream()
                .filter(maioresVotos)
                .collect(Collectors.toList());
    }

    private static int maximo(int[] inputArray) {
        int maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    private int porcentagemVotos(List<Candidato> candidatos) {
        int total = 0;
        for (Candidato candidato : candidatos) {
            total += candidato.numeroVotos;
        }
        return Math.round(total / 2); 
    }
}
