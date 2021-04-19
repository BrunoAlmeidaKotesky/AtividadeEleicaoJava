import helpers.Inputs;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;

enum Turnos {
    PRIMEIRO_TURNO, SEGUNDO_TURNO, JUSTICA_ELEITORAL
}

public class Eleicao {
    //Lista de candidatos iniciada como um ArrayList vazio.
    public List<Candidato> candidatos = new ArrayList();
    private int numeroDeSegundoTurno = 0;

    //Inicia a eleicao coms valores ja atribuidos aos candidatos
    public Eleicao() {
        candidatos.add(new Candidato("Rodrigo", 22, 0));
        candidatos.add(new Candidato("Ananias", 98, 0));
        candidatos.add(new Candidato("Rosângela", 73, 0));
        candidatos.add(new Candidato("Augusto", 41, 0));
        candidatos.add(new Candidato("Joana", 28, 0));
        candidatos.add(new Candidato("Eliana", 11, 0));
    }

    public Turnos votacao(boolean segundoTurno) {
        //Classe helper abstraindo de Scanner
        Inputs input = new Inputs();
        boolean votacaoAtiva = true;
        //Enquanto tiver input de dados da votacao
        while (votacaoAtiva) {
            int numeroSelecionado = input.intput("Entre com o número do seu candidato: ");
            if (numeroSelecionado == 0) 
                votacaoAtiva = false;
            //Se não é zero, pode continuar a votacao
            else {
                boolean numeroCorreto = false;
                //O candiadto que a pessoa selecionou
                Candidato candidatoAtual = null;
                for (Candidato candidato : this.candidatos) {
                    //Acha o candidato selecionado e muda os valores
                    if (candidato.numero == numeroSelecionado) {
                        numeroCorreto = true;
                        candidatoAtual = candidato;
                    }
                }
                if (numeroCorreto) {
                    String confirmado = input.input("Deseja confirmar o voto para " + candidatoAtual.nome + "? (sim)/(nao)");
                    //Adiciona +1 caso o usuario confirme para o usuario selecionado
                    if (confirmado.toLowerCase().equals("sim")) {
                        candidatoAtual.numeroVotos += 1;
                        System.out.println("Você votou com sucesso!");
                    }
                } 
                else {
                    //Anula o voto, ou seja, só printa anulado
                    String anular = input.input("Número inválido, deseja anular o seu voto? (sim)/(nao)");
                    if (anular.toLowerCase().equals("sim")) {
                        System.out.println("Você votou nulo com sucesso!");
                    }
                }
            }
        }
        /*Quando acaba o while, (votacaoAtiva == false) 
        *Retorne o método votacaoNaoAtiva, que também retorna um ENUM de @Turnos 
        */
        return votacaoNaoAtiva(votacaoAtiva, segundoTurno);
    }

    //Ocorre quando a votacao nao esta ativa, quando esta fora do while, recebe votacaoAtiva e se esta no segundo turno ou nao
    private Turnos votacaoNaoAtiva(boolean votacaoAtiva, boolean segundoTurno) {
        if (!votacaoAtiva) {
            //Procura quais sao os candidatos mais votados durante a eleicao
            List<Candidato> escolhidos = maioresEleitos(candidatos);
            //Acha a media desses candidatos escolhidos
            int mediaVotos = porcentagemVotos(escolhidos);
            //Se só possuir um escolhido, verifica se ele esta na mediad e 50% e printe o vencedor. Se estiver no primeiro turno retorna Turnos.PRIMEIRO_TURNO, se não retorna Turnos.SEGUNDO_TURNO
            if (escolhidos.size() == 1) {
                if (vencedor().numeroVotos >= mediaVotos) {
                    System.out.println("O candidato " + vencedor().nome + " venceu a eleição");
                    if (segundoTurno)
                        return Turnos.SEGUNDO_TURNO;
                    return Turnos.PRIMEIRO_TURNO;
                }
            } 
            //Se possuir mais candidatos com votos acima >= media
            else {
                //Altera os candidatos da classe e reseta os votos
                this.candidatos = escolhidos;
                for (Candidato candidato : candidatos) {
                    candidato.numeroVotos = 0;
                }
                //Se iso acontecer, é porque esta no segundo turno ou pós segundo turno (empate no segundo turno). Para isso a variavel numeroDeSegundoTurno é incrementada, evitando que repita o segundo turno para sempre
                numeroDeSegundoTurno += 1;
                //Se já tiver ocorrido o segundo turno e ocorrer empate de novo, o numeroDeSegundoTurno já sera > 1, então entraria como Turnos.JUSTICA_ELEITORAL
                if (numeroDeSegundoTurno > 1) {
                    System.out.println("O caso será julgado pela justiça eleitoral");
                    return Turnos.JUSTICA_ELEITORAL;
                }
                //Se nao retorne que esta no segundo turno
                return Turnos.SEGUNDO_TURNO;
            }
        }
        //Por padrao retorna JusticaEleitoral
        return Turnos.JUSTICA_ELEITORAL;
    }

    //Metodo bem especifico para pegar e um Lista de candidatos, o primeiro valor, isto so é chamado, quando sabemos exatamente que temos 1 candidato.
    private Candidato vencedor() {
        Candidato vencedor = candidatos.stream().findFirst().get();
        return vencedor;
    }

    //Verifica quais candidatos tem mais votos durante uma eleicao
    private List<Candidato> maioresEleitos(List<Candidato> candidatos) {
        int[] totalVotos = new int[6];
        int index = 0;
        //Para cada candidato, coloque o numero de votos no array de totalDeVotos, esta fixo como 6 porque sabemos que sao 6 candidatos
        for (Candidato candidato : candidatos) {
            totalVotos[index] = candidato.numeroVotos;
            index++;
        }
        //Acha o maior voto
        int maiorVoto = maximo(totalVotos);
        //Equivalente a JavaScript - Array.filter();
        Predicate<Candidato> maioresVotos = candidato -> candidato.numeroVotos == maiorVoto;
        //Retorna a Lista de candidatos aplicando o filtro de quem possui os maiores votos, mudando o numero de candidatos
        return candidatos.stream()
                .filter(maioresVotos)
                .collect(Collectors.toList());
    }

    //Método rpa achar o maior valor de um arrayd e inteiros, peguei da internet
    private static int maximo(int[] inputArray) {
        int maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    //Para calcular a porcentagem, pegue os candidatos, e para cada candidato, some um total, com este total, divida o total por 2 = 50%
    private int porcentagemVotos(List<Candidato> candidatos) {
        int total = 0;
        for (Candidato candidato : candidatos) {
            total += candidato.numeroVotos;
        }
        return Math.round(total / 2); 
    }
}
