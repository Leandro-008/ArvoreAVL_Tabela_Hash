import java.util.*;

class Tarefa {
    String descricao;
    int prioridade;
    String detalhes;

    public Tarefa(String descricao, int prioridade, String detalhes) {
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.detalhes = detalhes;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "descricao='" + descricao + '\'' +
                ", prioridade=" + prioridade +
                ", detalhes='" + detalhes + '\'' +
                '}';
    }
}

class NoAVL {
    Tarefa tarefa;
    NoAVL esquerda, direita;
    int altura;

    public NoAVL(Tarefa tarefa) {
        this.tarefa = tarefa;
        this.altura = 1;
    }
}

class ArvoreAVL {
    NoAVL raiz;

    private int altura(NoAVL no) {
        return (no != null) ? no.altura : 0;
    }

    private int fatorBalanceamento(NoAVL no) {
        return (no != null) ? altura(no.esquerda) - altura(no.direita) : 0;
    }

    private void atualizarAltura(NoAVL no) {
        if (no != null) {
            no.altura = Math.max(altura(no.esquerda), altura(no.direita)) + 1;
        }
    }

    private NoAVL rotacionarDireita(NoAVL y) {
        NoAVL x = y.esquerda;
        NoAVL T = x.direita;

        x.direita = y;
        y.esquerda = T;

        atualizarAltura(y);
        atualizarAltura(x);

        return x;
    }

    private NoAVL rotacionarEsquerda(NoAVL x) {
        NoAVL y = x.direita;
        NoAVL T = y.esquerda;

        y.esquerda = x;
        x.direita = T;

        atualizarAltura(x);
        atualizarAltura(y);

        return y;
    }

    private NoAVL inserir(NoAVL raiz, Tarefa tarefa) {
        if (raiz == null) {
            return new NoAVL(tarefa);
        }

        if (tarefa.prioridade < raiz.tarefa.prioridade) {
            raiz.esquerda = inserir(raiz.esquerda, tarefa);
        } else if (tarefa.prioridade > raiz.tarefa.prioridade) {
            raiz.direita = inserir(raiz.direita, tarefa);
        } else {
            // Prioridade já existe
            return raiz;
        }

        atualizarAltura(raiz);

        int fatorBalanceamento = fatorBalanceamento(raiz);

        if (fatorBalanceamento > 1 && tarefa.prioridade < raiz.esquerda.tarefa.prioridade) {
            return rotacionarDireita(raiz);
        }

        if (fatorBalanceamento < -1 && tarefa.prioridade > raiz.direita.tarefa.prioridade) {
            return rotacionarEsquerda(raiz);
        }

        if (fatorBalanceamento > 1 && tarefa.prioridade > raiz.esquerda.tarefa.prioridade) {
            raiz.esquerda = rotacionarEsquerda(raiz.esquerda);
            return rotacionarDireita(raiz);
        }

        if (fatorBalanceamento < -1 && tarefa.prioridade < raiz.direita.tarefa.prioridade) {
            raiz.direita = rotacionarDireita(raiz.direita);
            return rotacionarEsquerda(raiz);
        }

        return raiz;
    }

    public void inserir(Tarefa tarefa) {
        raiz = inserir(raiz, tarefa);
    }

    private Tarefa buscarPorPrioridade(NoAVL raiz, int prioridade) {
        if (raiz == null || raiz.tarefa.prioridade == prioridade) {
            return (raiz != null) ? raiz.tarefa : null;
        }

        if (prioridade < raiz.tarefa.prioridade) {
            return buscarPorPrioridade(raiz.esquerda, prioridade);
        } else {
            return buscarPorPrioridade(raiz.direita, prioridade);
        }
    }

    public Tarefa buscarPorPrioridade(int prioridade) {
        return buscarPorPrioridade(raiz, prioridade);
    }

    private NoAVL remover(NoAVL raiz, int prioridade) {
        if (raiz == null) {
            return raiz;
        }

        if (prioridade < raiz.tarefa.prioridade) {
            raiz.esquerda = remover(raiz.esquerda, prioridade);
        } else if (prioridade > raiz.tarefa.prioridade) {
            raiz.direita = remover(raiz.direita, prioridade);
        } else {
            if ((raiz.esquerda == null) || (raiz.direita == null)) {
                NoAVL temp = (raiz.esquerda != null) ? raiz.esquerda : raiz.direita;

                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else {
                    raiz = temp;
                }

                temp = null;
            } else {
                NoAVL temp = menorNo(raiz.direita);
                raiz.tarefa = temp.tarefa;
                raiz.direita = remover(raiz.direita, temp.tarefa.prioridade);
            }
        }

        if (raiz == null) {
            return raiz;
        }

        atualizarAltura(raiz);

        int fatorBalanceamento = fatorBalanceamento(raiz);

        if (fatorBalanceamento > 1 && fatorBalanceamento(raiz.esquerda) >= 0) {
            return rotacionarDireita(raiz);
        }

        if (fatorBalanceamento < -1 && fatorBalanceamento(raiz.direita) <= 0) {
            return rotacionarEsquerda(raiz);
        }

        if (fatorBalanceamento > 1 && fatorBalanceamento(raiz.esquerda) < 0) {
            raiz.esquerda = rotacionarEsquerda(raiz.esquerda);
            return rotacionarDireita(raiz);
        }

        if (fatorBalanceamento < -1 && fatorBalanceamento(raiz.direita) > 0) {
            raiz.direita = rotacionarDireita(raiz.direita);
            return rotacionarEsquerda(raiz);
        }

        return raiz;
    }

    private NoAVL menorNo(NoAVL no) {
        NoAVL atual = no;

        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }

        return atual;
    }

    public void remover(int prioridade) {
        raiz = remover(raiz, prioridade);
    }

    private void travessiaEmOrdem(NoAVL raiz, List<Tarefa> resultado) {
        if (raiz != null) {
            travessiaEmOrdem(raiz.esquerda, resultado);
            resultado.add(raiz.tarefa);
            travessiaEmOrdem(raiz.direita, resultado);
        }
    }

    public List<Tarefa> obterTarefasPorPrioridade() {
        List<Tarefa> resultado = new ArrayList<>();
        travessiaEmOrdem(raiz, resultado);
        return resultado;
    }
}

class TabelaHash {
    private Map<Integer, Tarefa> detalhesTarefa;

    public TabelaHash() {
        detalhesTarefa = new HashMap<>();
    }

    public void adicionarTarefa(Tarefa tarefa) {
        detalhesTarefa.put(tarefa.prioridade, tarefa);
    }

    public void removerTarefa(int prioridade) {
        detalhesTarefa.remove(prioridade);
    }

    public Tarefa obterTarefaPorPrioridade(int prioridade) {
        return detalhesTarefa.get(prioridade);
    }
}

class GerenciadorDeTarefas {
    ArvoreAVL arvoreAVL;
    TabelaHash tabelaHash;

    public GerenciadorDeTarefas() {
        arvoreAVL = new ArvoreAVL();
        tabelaHash = new TabelaHash();
    }

    public void adicionarTarefa(String descricao, int prioridade, String detalhes) {
        Tarefa novaTarefa = new Tarefa(descricao, prioridade, detalhes);
        arvoreAVL.inserir(novaTarefa);
        tabelaHash.adicionarTarefa(novaTarefa);
    }

    public void removerTarefa(int prioridade) {
        arvoreAVL.remover(prioridade);
        tabelaHash.removerTarefa(prioridade);
    }

    public Tarefa obterTarefaPorPrioridade(int prioridade) {
        Tarefa tarefa = tabelaHash.obterTarefaPorPrioridade(prioridade);
        return (tarefa != null) ? tarefa : arvoreAVL.buscarPorPrioridade(prioridade);
    }

    public List<Tarefa> obterTarefasOrdenadasPorPrioridade() {
        return arvoreAVL.obterTarefasPorPrioridade();
    }
}

