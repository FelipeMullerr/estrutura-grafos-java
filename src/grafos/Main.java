package grafos;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo grafo = null;
        boolean ponderado = false;
        boolean direcionado = false;

        System.out.println("=== Configuracao do Grafo ===");
        System.out.println("Tipo de representacao:");
        System.out.println("1 - Lista de Adjacencia");
        System.out.println("2 - Matriz de Adjacencia");
        System.out.print("Escolha: ");
        int tipoRepresentacao = scanner.nextInt();
        while (tipoRepresentacao != 1 && tipoRepresentacao != 2) {
            System.out.print("Entrada invalida. Digite 1 para Lista ou 2 para Matriz: ");
            tipoRepresentacao = scanner.nextInt();
        }

        System.out.println("===Deseja Criar o Grafo a partir de um Arquivo?===");
        System.out.println("1 - Sim");
        System.out.println("2 - Não");
        System.out.print("Escolha: ");
        int tipoCriacao = scanner.nextInt();
        while(tipoCriacao != 1 && tipoCriacao != 2) {
            System.out.print("Entrada invalida. Digite 1 para Lista ou 2 para Matriz: ");
            tipoCriacao = scanner.nextInt();
        }

        if(tipoCriacao == 1) {
            grafo = Grafo.criarGrafoArquivo(tipoRepresentacao);
        } else {
            System.out.print("Direcionado? (1 = sim, 0 = nao): ");
            int entradaDirecionado = scanner.nextInt();
            while (entradaDirecionado != 0 && entradaDirecionado != 1) {
                System.out.print("Entrada invalida. Digite 1 para sim ou 0 para nao: ");
                entradaDirecionado = scanner.nextInt();
            }
            direcionado = entradaDirecionado == 1;

            System.out.print("Ponderado? (1 = sim, 0 = nao): ");
            int entradaPonderado = scanner.nextInt();
            while (entradaPonderado != 0 && entradaPonderado != 1) {
                System.out.print("Entrada invalida. Digite 1 para sim ou 0 para nao: ");
                entradaPonderado = scanner.nextInt();
            }
            ponderado = entradaPonderado == 1;

            if (tipoRepresentacao == 1) {
                grafo = new GrafoLista(direcionado, ponderado);
            } else {
                grafo = new GrafoMatriz(direcionado, ponderado);
            }
        }

        System.out.println("Grafo criado\n");

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n=== Menu ===");
            System.out.println("1  - Inserir vertice");
            System.out.println("2  - Remover vertice");
            System.out.println("3  - Inserir aresta");
            System.out.println("4  - Remover aresta");
            System.out.println("5  - Verificar se aresta existe");
            System.out.println("6  - Retornar vizinhos");
            System.out.println("7  - Imprimir grafo");
            System.out.println("8  - Busca em Largura (BFS)");
            System.out.println("9  - Busca em Profundidade (DFS)");
            System.out.println("10 - Dijkstra");
            System.out.println("0  - Sair");
            System.out.print("Escolha: ");
            opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    System.out.print("Nome do vertice: ");
                    String nomeVertice = scanner.next();
                    grafo.inserirVertice(nomeVertice);
                    System.out.println("Vertice '" + nomeVertice + "' inserid.");
                    break;

                case 2:
                    System.out.print("Nome do vertice a remover: ");
                    String nomeRemover = scanner.next();
                    int indiceRemover = grafo.indiceVertice(nomeRemover);

                    if (indiceRemover == -1) {
                        System.out.println("Vertice '" + nomeRemover + "' nao encontrado.");
                        break;
                    }

                    grafo.removerVertice(indiceRemover);
                    System.out.println("Vertice '" + nomeRemover + "' removido.");
                    break;

                case 3:
                    System.out.print("Vertice de origem: ");
                    String nomeOrigem = scanner.next();
                    System.out.print("Vertice de destino: ");
                    String nomeDestino = scanner.next();
                    int indiceOrigem  = grafo.indiceVertice(nomeOrigem);
                    int indiceDestino = grafo.indiceVertice(nomeDestino);

                    if (indiceOrigem == -1) {
                        System.out.println("Vertice '" + nomeOrigem + "' nao encontrado.");
                        break;
                    }
                    if (indiceDestino == -1) {
                        System.out.println("Vertice '" + nomeDestino + "' nao encontrado.");
                        break;
                    }

                    if (grafo.existeAresta(indiceOrigem, indiceDestino)) {
                        System.out.println("Aresta entre '" + nomeOrigem + "' e '" + nomeDestino + "' ja existe!");
                        break;
                    }

                    float peso = 1;
                    if (grafo.ponderado) {
                        System.out.print("Peso da aresta: ");
                        peso = scanner.nextFloat();
                    }

                    grafo.inserirAresta(indiceOrigem, indiceDestino, peso);
                    System.out.println("Aresta inserida.");
                    break;

                case 4:
                    System.out.print("Vertice de origem: ");
                    String nomeOrigemRemover = scanner.next();
                    System.out.print("Vertice de destino: ");
                    String nomeDestinoRemover = scanner.next();

                    int indiceOrigemRemover  = grafo.indiceVertice(nomeOrigemRemover);
                    int indiceDestinoRemover = grafo.indiceVertice(nomeDestinoRemover);

                    if (indiceOrigemRemover == -1) {
                        System.out.println("Vertice '" + nomeOrigemRemover + "' nao encontrado.");
                        break;
                    }
                    if (indiceDestinoRemover == -1) {
                        System.out.println("Vertice '" + nomeDestinoRemover + "' nao encontrado.");
                        break;
                    }

                    if(!grafo.existeAresta(indiceOrigemRemover,indiceDestinoRemover)) {
                        System.out.println("Nao existe aresta entre os vertices " + nomeOrigemRemover + " e " + nomeDestinoRemover + ".");
                        break;
                    }
                    grafo.removerAresta(indiceOrigemRemover, indiceDestinoRemover);
                    System.out.println("Aresta removida.");
                    break;

                case 5:
                    System.out.print("Vertice de origem: ");
                    String nomeOrigemExiste = scanner.next();
                    System.out.print("Vertice de destino: ");
                    String nomeDestinoExiste = scanner.next();

                    int indiceOrigemExiste  = grafo.indiceVertice(nomeOrigemExiste);
                    int indiceDestinoExiste = grafo.indiceVertice(nomeDestinoExiste);

                    if (indiceOrigemExiste == -1) {
                        System.out.println("Vertice '" + nomeOrigemExiste + "' nao encontrado.");
                        break;
                    }
                    if (indiceDestinoExiste == -1) {
                        System.out.println("Vertice '" + nomeDestinoExiste + "' nao encontrado.");
                        break;
                    }

                    boolean existe = grafo.existeAresta(indiceOrigemExiste, indiceDestinoExiste);
                    System.out.println("Aresta existe: " + existe);
                    break;

                case 6:
                    System.out.print("Nome do vertice: ");
                    String nomeVizinhos = scanner.next();
                    int indiceVizinhos = grafo.indiceVertice(nomeVizinhos);

                    if (indiceVizinhos == -1) {
                        System.out.println("Vertice '" + nomeVizinhos + "' nao encontrado.");
                        break;
                    }

                    System.out.print("Vizinhos de " + nomeVizinhos + ": ");
                    for (int i = 0; i < grafo.retornarVizinhos(indiceVizinhos).size(); i++) {
                        System.out.print(grafo.labelVertice(grafo.retornarVizinhos(indiceVizinhos).get(i)) + " ");
                    }
                    System.out.println();
                    break;

                case 7:
                    grafo.imprimeGrafo();
                    break;

                case 8:
                    System.out.print("Vertice de origem: ");
                    String nomeBFS = scanner.next();
                    int indiceBFS = grafo.indiceVertice(nomeBFS);

                    if (indiceBFS == -1) {
                        System.out.println("Vertice '" + nomeBFS + "' nao encontrado.");
                        break;
                    }

                    grafo.buscaEmLargura(indiceBFS);
                    break;

                case 9:
                    System.out.print("Vertice de origem: ");
                    String nomeDFS = scanner.next();
                    int indiceDFS = grafo.indiceVertice(nomeDFS);

                    if (indiceDFS == -1) {
                        System.out.println("Vertice '" + nomeDFS + "' nao encontrado.");
                        break;
                    }

                    grafo.buscaEmProfundidade(indiceDFS);
                    break;

                case 10:
                    if (!grafo.ponderado) {
                        System.out.println("Dijkstra requer um grafo ponderado.");
                        break;
                    }

                    System.out.print("Vertice de origem: ");
                    String nomeDijkstra = scanner.next();
                    int indiceDijkstra = grafo.indiceVertice(nomeDijkstra);

                    if (indiceDijkstra == -1) {
                        System.out.println("Vertice '" + nomeDijkstra + "' nao encontrado.");
                        break;
                    }

                    grafo.executarDijkstra(indiceDijkstra);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida.");
                    break;
            }
        }

        scanner.close();
    }
}