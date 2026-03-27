package grafos;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public abstract class Grafo {

    protected boolean direcionado;
    protected boolean ponderado;

    public Grafo(boolean direcionado, boolean ponderado) {
        this.direcionado = direcionado;
        this.ponderado = ponderado;
    }

    public abstract int tamanhoGrafo();

    public abstract int indiceVertice(String label);

    public abstract boolean inserirVertice(String label);

    public abstract boolean removerVertice(int indice);

    public abstract String labelVertice(int indice);

    public abstract void imprimeGrafo();

    public abstract boolean inserirAresta(int origem, int destino, float peso);

    public abstract boolean removerAresta(int origem, int destino);

    public abstract boolean existeAresta(int origem, int destino);

    public abstract float pesoAresta(int origem, int destino);

    public abstract List<Integer> retornarVizinhos(int vertice);

    // BFS
    public void buscaEmLargura(int origem) {
        boolean[] visitado = new boolean[tamanhoGrafo()];
        // estrutura de fila auxiliar
        Queue<Integer> fila = new java.util.LinkedList<>();

        visitado[origem] = true;
        fila.add(origem);

        System.out.print("Busca Largura com iniciando com o vértice " + labelVertice(origem) + " -> ");
        while (!fila.isEmpty()) {
            int vertice = fila.poll();
            System.out.print(labelVertice(vertice) + " ");

            // busca a lista dos vizinhos do vertice que esta sendo verificado
            List<Integer> vizinhos = retornarVizinhos(vertice);

            for (int i = 0; i < vizinhos.size(); i++) {
                int vizinho = vizinhos.get(i);

                if (!visitado[vizinho]) {
                    visitado[vizinho] = true;
                    fila.add(vizinho);
                }
            }
        }
    }

    // DFS
    public void buscaEmProfundidade(int origem) {
        boolean[] visitado = new boolean[tamanhoGrafo()];
        System.out.print("\nResultado Busca em Profundidade no vértice " + labelVertice(origem) + " -> ");
        execBuscaProfundidade(origem, visitado);
    }

    private void execBuscaProfundidade(int vertice, boolean[] visitado) {
        visitado[vertice] = true;
        System.out.print(labelVertice(vertice) + " ");

        // busca a lista dos vizinhos do vertice
        List<Integer> vizinhos = retornarVizinhos(vertice);

        for (int i = 0; i < vizinhos.size(); i++) {
            int vizinho = vizinhos.get(i);

            if (!visitado[vizinho]) {
                execBuscaProfundidade(vizinho, visitado);
            }
        }
    }

    public void executarDijkstra(int origem) {
        int totalVertices = tamanhoGrafo();

        float[] distancia = new float[totalVertices];
        int[] anterior = new int[totalVertices];
        boolean[] fechado = new boolean[totalVertices];

        // estrutura auxiliar para o dijkstra
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            fechado[vertice] = false;
            anterior[vertice] = -1;
            distancia[vertice] = Float.MAX_VALUE;
        }

        distancia[origem] = 0;

        while (true) {
            // definir o vértice aberto com a menor distancia como o vertice atual
            // para a primeira iteracao, sempre sera o vertice de origem
            int verticeAtual = -1;
            for (int vertice = 0; vertice < totalVertices; vertice++) {
                if (!fechado[vertice] && distancia[vertice] != Float.MAX_VALUE) {
                    if (verticeAtual == -1 || distancia[vertice] < distancia[verticeAtual]) {
                        verticeAtual = vertice;
                    }
                }
            }

            // encerra caso nao tenha mais vertices abertos com distancia == infinito
            if (verticeAtual == -1) break;

            // verifica todos os vizinhos do vertice atual
            List<Integer> vizinhos = retornarVizinhos(verticeAtual);
            for (int i = 0; i < vizinhos.size(); i++) {
                int vizinho = vizinhos.get(i);

                float novaDistancia = distancia[verticeAtual] + pesoAresta(verticeAtual, vizinho);
                // verificando se a distancia do vizinho é maior que a distancia do vertice atual + peso da aresta
                if (novaDistancia < distancia[vizinho]) {
                    distancia[vizinho] = novaDistancia;
                    anterior[vizinho] = verticeAtual;
                }
            }
            // fecha o vertice atual e busca outro vertice aberto com a menor distancia
            fechado[verticeAtual] = true;
        }

        System.out.println("\nDijkstra a partir de: " + labelVertice(origem));

        for (int vertice = 0; vertice < totalVertices; vertice++) {
            if (vertice == origem) continue;

            if (distancia[vertice] == Float.MAX_VALUE) {
                System.out.println("  " + labelVertice(vertice) + " | vertice nao conectado a outros vertices (inalcançável)");
                continue;
            }

            List<String> caminho = new ArrayList<>();
            int verticeAtual = vertice;
            while (verticeAtual != -1) {
                caminho.addFirst(labelVertice(verticeAtual));
                verticeAtual = anterior[verticeAtual];
            }

            String sCaminho = "";
            for (int i = 0; i < caminho.size(); i++) {
                if (i > 0) sCaminho += " -> ";
                sCaminho += caminho.get(i);
            }

            System.out.println("  " + labelVertice(vertice) + " | distância: " + distancia[vertice] + " | caminho: " + sCaminho);
        }
    }

    public static Grafo criarGrafoArquivo(int tipoRepresentacao) {
        Scanner scanner = new Scanner(System.in);

        Grafo grafo = null;
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader("/Users/felipemuller/Documents/Univali/7 Semestre/Grafos/Trabalho M1/estrutura-grafos-java/src/grafos/teste.txt")
            );
            String primeiraLinha = br.readLine();

            String[] partes = primeiraLinha.trim().split("\\s+");
            int V = Integer.parseInt(partes[0]);
            int A = Integer.parseInt(partes[1]);
            boolean direcionado = partes[2].equals("1");
            boolean ponderado = partes[3].equals("1");

            if (tipoRepresentacao == 1) {
                grafo = new GrafoLista(direcionado, ponderado);
            } else {
                grafo = new GrafoMatriz(direcionado, ponderado);
            }

            for (int i = 0; i < A; i++) {
                String linha = br.readLine();
                if (linha == null) break;
                String[] dados = linha.trim().split("\\s+");
                String Ao = dados[0];
                String Ad = dados[1];
                float peso = ponderado ? Float.parseFloat(dados[2].replace(",", ".")) : 1.0f;

                // Cria o vertice caso nao exista e posteriormente faz a inserção da aresta
                if (grafo.indiceVertice(Ao) == -1) grafo.inserirVertice(Ao);
                if (grafo.indiceVertice(Ad) == -1) grafo.inserirVertice(Ad);

                int idxAo = grafo.indiceVertice(Ao);
                int idxAd = grafo.indiceVertice(Ad);

                grafo.inserirAresta(idxAo, idxAd, peso);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            System.exit(1);
            return null;
        }
        return grafo;
    }
}
