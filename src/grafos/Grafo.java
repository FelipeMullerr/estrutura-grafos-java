package grafos;

import java.util.*;

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

        String[] arquivos = {
                "coloracao7.txt",
                "coloracao10.txt",
                "coloracao20.txt",
                "coloracao50.txt",
                "coloracao100.txt",
                "coloracaoSlide.txt",
                "coloracao.txt",
                "Coloracao-r250-66-65.txt",
                "prim.txt"
        };

        System.out.println("\n=== Selecione o arquivo de coloração ===");
        for (int i = 0; i < arquivos.length; i++) {
            System.out.println((i + 1) + " - " + arquivos[i]);
        }
        System.out.print("Escolha: ");
        int escolha = scanner.nextInt();
        while (escolha < 1 || escolha > arquivos.length) {
            System.out.print("Entrada inválida. Escolha entre 1 e " + arquivos.length + ": ");
            escolha = scanner.nextInt();
        }
        String nomeArquivo = arquivos[escolha - 1];

        Grafo grafo = null;
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.FileReader("src/grafos/" + nomeArquivo)
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

    // ----------------------------- ALGORITMOS DE AGM (M2) -----------------------------

    private void imprimirResultadoAGM(String nomeAlgoritmo, long tempoExecucaoNanos, float somaArestas) {
        double tempoEmMilissegundos = tempoExecucaoNanos / 1_000_000.0;
        System.out.println("\n>>> Algoritmo: " + nomeAlgoritmo);
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoEmMilissegundos) + " ms");
        System.out.println("Soma das arestas: " + somaArestas);
    }

    public void primAGM() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;
        System.out.println("Direcionado?" + direcionado);
        System.out.println("Ponderado?" + ponderado);
        if (direcionado || !ponderado) {
            System.out.println("Prim requer um grafo nao direcionado e ponderado.");
            return;
        }

        long tempoInicio = System.nanoTime();

        boolean[] controle = new boolean[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            controle[i] = true;
        }

        // Escolhe um vertice arbitrario A como vertice inicial
        controle[0] = false; // Remove A do conjunto de Controle

        int restantes = totalVertices - 1;

        float somaArestas = 0;

        while (restantes > 0) {
            int uEscolhido = -1;
            int vEscolhido = -1;
            float menorPeso = Float.MAX_VALUE;

            // Encontra a menor aresta {u, v} com um no Controle e outro fora do Controle
            for (int u = 0; u < totalVertices; u++) {
                if (controle[u]) continue;
                for (int v : retornarVizinhos(u)) {
                    if (!controle[v]) continue;
                    float peso = pesoAresta(u, v);
                    if (peso < menorPeso) {
                        menorPeso = peso;
                        uEscolhido = u;
                        vEscolhido = v;
                    }
                }
            }

            if (uEscolhido == -1) break;

            somaArestas += menorPeso;
            controle[vEscolhido] = false; // Remove do Controle o vertice da aresta que pertencia a ele
            restantes--;
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoAGM("Prim", tempoFim - tempoInicio, somaArestas);
    }

    public void kruskalAGM() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;

        if (direcionado || !ponderado) {
            System.out.println("Kruskal requer um grafo nao direcionado e ponderado.");
            return;
        }

        long tempoInicio = System.nanoTime();

        List<Integer> arestasU = new ArrayList<>();
        List<Integer> arestasV = new ArrayList<>();
        List<Float> arestasPeso = new ArrayList<>();
        for (int u = 0; u < totalVertices; u++) {
            for (int v : retornarVizinhos(u)) {
                if (u < v) {
                    arestasU.add(u);
                    arestasV.add(v);
                    arestasPeso.add(pesoAresta(u, v));
                }
            }
        }

        // Ordena as arestas pelo peso
        for (int i = 0; i < arestasPeso.size() - 1; i++) {
            for (int j = 0; j < arestasPeso.size() - 1 - i; j++) {
                if (arestasPeso.get(j) > arestasPeso.get(j + 1)) {
                    int uTemp = arestasU.get(j);
                    int vTemp = arestasV.get(j);
                    float pTemp = arestasPeso.get(j);

                    arestasU.set(j, arestasU.get(j + 1));
                    arestasV.set(j, arestasV.get(j + 1));
                    arestasPeso.set(j, arestasPeso.get(j + 1));

                    arestasU.set(j + 1, uTemp);
                    arestasV.set(j + 1, vTemp);
                    arestasPeso.set(j + 1, pTemp);
                }
            }
        }

        int[] floresta = new int[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            floresta[i] = i;
        }

        float somaArestas = 0;

        for (int i = 0; i < arestasPeso.size(); i++) {
            int u = arestasU.get(i);
            int v = arestasV.get(i);
            float peso = arestasPeso.get(i);

            if (floresta[u] != floresta[v]) {
                somaArestas += peso;

                int grupoU = floresta[u];
                int grupoV = floresta[v];

                for (int j = 0; j < totalVertices; j++) {
                    if (floresta[j] == grupoV) {
                        floresta[j] = grupoU;
                    }
                }
            }
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoAGM("Kruskal", tempoFim - tempoInicio, somaArestas);

    }









    // ----------------------------- ALGORITMOS DE COLORAÇÃO (M2) -----------------------------

    private void imprimirResultadoColoracao(String nomeAlgoritmo, int[] arrayCores, long tempoExecucaoNanos, int totalCoresUsadas) {
        int totalVertices = tamanhoGrafo();
        double tempoEmMilissegundos = tempoExecucaoNanos / 1_000_000.0;

        System.out.println("\n>>> Algoritmo: " + nomeAlgoritmo);
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoEmMilissegundos) + " ms");
        System.out.println("Número de cores utilizadas (k): " + totalCoresUsadas);

        if (totalVertices < 10) {
            System.out.println("Detalhamento da Coloração:");
            for (int i = 0; i < totalVertices; i++) {
                System.out.println("  Vértice " + labelVertice(i) + " -> Recebeu Cor " + arrayCores[i]);
            }
        }
    }

    public void coloracaoForcaBruta() {
        int quantidadeVertices = tamanhoGrafo();
        if (quantidadeVertices == 0) return;

        long tempoInicio = System.nanoTime();
        int[] atribuicaoCores = new int[quantidadeVertices];

        // Tenta encontrar a solução começando com 2 cores, depois 3, e assim por diante
        for (int limiteCores = 2; limiteCores <= quantidadeVertices; limiteCores++) {
            if (tentarColorirRecursivo(0, limiteCores, atribuicaoCores)) {
                long tempoFim = System.nanoTime();
                imprimirResultadoColoracao("Força Bruta", atribuicaoCores, tempoFim - tempoInicio, limiteCores);
                return;
            }
        }
    }

    private boolean tentarColorirRecursivo(int indiceVerticeAtual, int maximoCoresPermitidas, int[] atribuicaoCores) {
        int totalVertices = tamanhoGrafo();

        if (indiceVerticeAtual == totalVertices) return true; // Todos os vértices foram coloridos com sucesso

        for (int corCandidata = 1; corCandidata <= maximoCoresPermitidas; corCandidata++) {
            if (validarCorSegura(indiceVerticeAtual, corCandidata, atribuicaoCores)) {
                atribuicaoCores[indiceVerticeAtual] = corCandidata;
                
                // Tenta colorir o próximo vértice
                if (tentarColorirRecursivo(indiceVerticeAtual + 1, maximoCoresPermitidas, atribuicaoCores)) {
                    return true;
                }
                
                // Se não deu certo, limpa a cor
                atribuicaoCores[indiceVerticeAtual] = 0;
            }
        }
        return false;
    }

    private boolean validarCorSegura(int indiceVerticeAlvo, int corParaTestar, int[] atribuicaoCores) {
        for (int indiceVizinho : retornarVizinhos(indiceVerticeAlvo)) {
            if (atribuicaoCores[indiceVizinho] == corParaTestar) {
                return false; // Existe um vizinho com a mesma cor
            }
        }
        return true;
    }

    public void coloracaoWelshPowell() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;

        long tempoInicio = System.nanoTime();

        Integer[] listaVerticesOrdenados = new Integer[totalVertices];
        int[] arrayGraus = new int[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            listaVerticesOrdenados[i] = i;
            arrayGraus[i] = retornarVizinhos(i).size();
        }

        // Ordenar os vértices pelo seu grau em ordem decrescente
        for (int i = 0; i < totalVertices - 1; i++) {
            for (int j = 0; j < totalVertices - 1 - i; j++) {
                int vA = listaVerticesOrdenados[j];
                int vB = listaVerticesOrdenados[j + 1];
                if (arrayGraus[vA] < arrayGraus[vB]) {
                    int temporario = listaVerticesOrdenados[j];
                    listaVerticesOrdenados[j] = listaVerticesOrdenados[j + 1];
                    listaVerticesOrdenados[j + 1] = temporario;
                }
            }
        }

        // Criar um vetor de cores
        // Inicializar todos os vértices como “sem cor”
        int[] atribuicaoCores = new int[totalVertices];
        int corAtual = 0;
        int totalColoridos = 0;

        // Enquanto existir um vértice sem cor no grafo
        while (totalColoridos < totalVertices) {
            // Definir a primeira cor não utilizada ainda como cor atual
            corAtual++;

            // Para cada vértice do grafo sem cor (seguindo a lista ordenada)
            for (int v : listaVerticesOrdenados) {
                if (atribuicaoCores[v] == 0) {
                    // Atribuir a cor atual caso ele não tenha um vértice adjacente com a mesma cor
                    if (validarCorSegura(v, corAtual, atribuicaoCores)) {
                        atribuicaoCores[v] = corAtual;
                        totalColoridos++;
                    }
                }
            }
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoColoracao("Welsh-Powell", atribuicaoCores, tempoFim - tempoInicio, corAtual);
    }

    public void coloracaoDSATUR() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;

        long tempoInicio = System.nanoTime();

        int[] atribuicaoCores = new int[totalVertices];
        int[] arrayGraus = new int[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            arrayGraus[i] = retornarVizinhos(i).size();
        }

        // Colorir o vértice com maior grau com a primeira cor
        int verticeMaiorGrau = 0;
        for (int i = 1; i < totalVertices; i++) {
            if (arrayGraus[i] > arrayGraus[verticeMaiorGrau]) {
                verticeMaiorGrau = i;
            }
        }
        atribuicaoCores[verticeMaiorGrau] = 1;
        int contadorColoridos = 1;
        int maiorIndiceCorUsada = 1;

        while (contadorColoridos < totalVertices) {
            int proximoVerticeParaColorir = -1;
            int maiorGrauSaturacaoEncontrado = -1;

            for (int i = 0; i < totalVertices; i++) {
                if (atribuicaoCores[i] == 0) {
                    int grauSaturacao = calcularGrauSaturacao(i, atribuicaoCores);
                    
                    // Critério principal: Maior saturação
                    if (grauSaturacao > maiorGrauSaturacaoEncontrado) {
                        maiorGrauSaturacaoEncontrado = grauSaturacao;
                        proximoVerticeParaColorir = i;
                    } 
                    // Critério de desempate: Maior grau no grafo original
                    else if (grauSaturacao == maiorGrauSaturacaoEncontrado) {
                        if (proximoVerticeParaColorir == -1 || arrayGraus[i] > arrayGraus[proximoVerticeParaColorir]) {
                            proximoVerticeParaColorir = i;
                        }
                    }
                }
            }

            // Encontra a menor cor disponível para o vértice escolhido
            int corParaAtribuir = 1;
            while (!validarCorSegura(proximoVerticeParaColorir, corParaAtribuir, atribuicaoCores)) {
                corParaAtribuir++;
            }
            
            atribuicaoCores[proximoVerticeParaColorir] = corParaAtribuir;
            if (corParaAtribuir > maiorIndiceCorUsada) maiorIndiceCorUsada = corParaAtribuir;
            contadorColoridos++;
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoColoracao("DSATUR", atribuicaoCores, tempoFim - tempoInicio, maiorIndiceCorUsada);
    }

    private int calcularGrauSaturacao(int indiceVerticeAlvo, int[] atribuicaoCores) {
        Set<Integer> conjuntoCoresVizinhos = new HashSet<>();
        for (int indiceVizinho : retornarVizinhos(indiceVerticeAlvo)) {
            int corDoVizinho = atribuicaoCores[indiceVizinho];
            if (corDoVizinho != 0) {
                conjuntoCoresVizinhos.add(corDoVizinho);
            }
        }
        return conjuntoCoresVizinhos.size(); // Saturação é a quantidade de cores distinstas nos vizinhos
    }

    public void coloracaoSemCriterio() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;

        long tempoInicio = System.nanoTime();
        int[] atribuicaoCores = new int[totalVertices];
        int maiorIndiceCorUsada = 0;

        for (int i = 0; i < totalVertices; i++) {
            int corCandidata = 1;
            while (!validarCorSegura(i, corCandidata, atribuicaoCores)) {
                corCandidata++;
            }
            atribuicaoCores[i] = corCandidata;
            if (corCandidata > maiorIndiceCorUsada) maiorIndiceCorUsada = corCandidata;
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoColoracao("Heurística sem critério", atribuicaoCores, tempoFim - tempoInicio, maiorIndiceCorUsada);
    }


}
