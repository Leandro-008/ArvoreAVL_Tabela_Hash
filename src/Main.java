import java.util.List;

public class Main {
    public static void main(String[] args) {
        GerenciadorDeTarefas gerenciador = new GerenciadorDeTarefas();

        gerenciador.adicionarTarefa("Estudar Java", 1, "Variáveis e métodos");
        gerenciador.adicionarTarefa("Fazer exercícios", 2, "Exercícios 1,2 e 3");
        gerenciador.adicionarTarefa("Projeto calculadora", 3, "Planejamento do código");
        gerenciador.adicionarTarefa("Revisar código", 4, "Correções e melhorias");

        gerenciador.removerTarefa(3);
 
        Tarefa tarefaPrioridade1 = gerenciador.obterTarefaPorPrioridade(1);
        System.out.println("Tarefa Prioridade 1: " + tarefaPrioridade1);

        List<Tarefa> tarefasOrdenadas = gerenciador.obterTarefasOrdenadasPorPrioridade();
        System.out.println("Tarefas Ordenadas: " + tarefasOrdenadas);
    }
}