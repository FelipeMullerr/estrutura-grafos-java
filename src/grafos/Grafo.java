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
                "C4000-260-X.txt",
                "k5.txt",
                "k33.txt",
                "kquase5.txt",
                "r250-66-65.txt",
                "r1000-234-234.txt", // coloracao ate aq
                "50vertices25%Arestas.txt",
                "50vertices50%Arestas.txt",
                "50vertices100%Arestas.txt",
                "500vertices25%Arestas.txt",
                "500vertices50%Arestas.txt",
                "500vertices100%Arestas.txt",
                "1000vertices25%Arestas.txt",
                "ford_fulkerson.txt"
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

            for (int i = 0; i < V; i++) {
                grafo.inserirVertice(String.valueOf(i));
            }

            for (int i = 0; i < A; i++) {
                String linha = br.readLine();
                if (linha == null) break;
                String[] dados = linha.trim().split("\\s+");
                int idxAo = Integer.parseInt(dados[0]);
                int idxAd = Integer.parseInt(dados[1]);
                float peso = ponderado ? Float.parseFloat(dados[2].replace(",", ".")) : 1.0f;

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

    private void imprimirResultadoAGM(String nomeAlgoritmo, long tempoExecucaoNanos, float somaArestas, List<String> solucao) {
        double tempoEmMilissegundos = tempoExecucaoNanos / 1_000_000.0;
        System.out.println("\n>>> Algoritmo: " + nomeAlgoritmo);
        System.out.println("Tempo de execução: " + String.format("%.4f", tempoEmMilissegundos) + " ms");
        System.out.println("Soma das arestas: " + somaArestas);
        System.out.println("Solução: " + solucao);
    }

    private void imprimirFlorestaComoArvore(int[] floresta) {
        int totalVertices = floresta.length;
        boolean[] grupoImpresso = new boolean[totalVertices];

        for (int i = 0; i < totalVertices; i++) {
            int grupo = floresta[i];
            if (grupoImpresso[grupo]) continue;
            grupoImpresso[grupo] = true;

            System.out.println(labelVertice(grupo));
            for (int j = 0; j < totalVertices; j++) {
                if (floresta[j] == grupo && j != grupo) {
                    System.out.println("|-- " + labelVertice(j));
                }
            }
        }
    }

    public void primAGM() {
        int totalVertices = tamanhoGrafo();
        if (totalVertices == 0) return;
        //System.out.println("Direcionado?" + direcionado);
        //System.out.println("Ponderado?" + ponderado);
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

        List<String> solucao = new ArrayList<>();
        float somaArestas = 0;

        while (restantes > 0) {
            int uEscolhido = -1;
            int vEscolhido = -1;
            float menorPeso = Float.MAX_VALUE;

            // Encontra a menor aresta {u, v} com um no Controle e outro fora do Controle
            for (int u = 0; u < totalVertices; u++) {
                //System.out.println("Vertice: " + labelVertice(u) + " // Controle: " + controle[u]);
                if (controle[u]) continue; //precisa ser false (nao estar no controle) para ser o vertice u
                for (int v : retornarVizinhos(u)) {
                    if (!controle[v]) continue; //precisa ser true (estar no controle) para ser o vertice v
                    float peso = pesoAresta(u, v); // pega o peso da aresta u-v
                    if (peso < menorPeso) {
                        menorPeso = peso;
                        uEscolhido = u;
                        vEscolhido = v;
                        //System.out.println("--> Aresta candidata: " + labelVertice(u) + " - " + labelVertice(v) + " | peso: " + peso); // remover
                    }
                }
            }

            if (uEscolhido == -1) break;

            somaArestas += menorPeso;
            controle[vEscolhido] = false; // Remove do Controle o vertice da aresta que pertencia a ele
            solucao.add(labelVertice(uEscolhido) + " - " + labelVertice(vEscolhido));
            //System.out.println("------> Aresta adicionada: " + labelVertice(uEscolhido) + " - " + labelVertice(vEscolhido) + " | peso: " + menorPeso); // remover
            restantes--;
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoAGM("Prim", tempoFim - tempoInicio, somaArestas, solucao);
    }

    public void kruskalAGM() {
        int totalVertices = tamanhoGrafo();

        //System.out.println("totalVertices: " + totalVertices);  // remover

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
                //System.out.println("u: " + labelVertice(u) + " | v: " + labelVertice(v) + " | peso: " + pesoAresta(u, v)); // remover
                if (u < v) {
                    arestasU.add(u);
                    arestasV.add(v);
                    float peso = pesoAresta(u, v);
                    arestasPeso.add(peso);
                    //System.out.println("Adicionando aresta: " + labelVertice(u) + " - " + labelVertice(v) + " | peso: " + peso); // remover
                }
            }
        }

        // Ordena as arestas pelo peso
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < arestasPeso.size(); i++) indices.add(i);
        indices.sort((a, b) -> Float.compare(arestasPeso.get(a), arestasPeso.get(b)));

        List<Integer> novoU = new ArrayList<>(arestasU);
        List<Integer> novoV = new ArrayList<>(arestasV);
        List<Float> novoPeso = new ArrayList<>(arestasPeso);

        for (int i = 0; i < indices.size(); i++) {
            int idx = indices.get(i);
            arestasU.set(i, novoU.get(idx));
            arestasV.set(i, novoV.get(idx));
            arestasPeso.set(i, novoPeso.get(idx));
        }

        int[] floresta = new int[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            floresta[i] = i;
        }

        //System.out.println("Floresta inicial: " + Arrays.toString(floresta)); // remover

        //System.out.println("Arestas ordenadas:"); // remover
        //for (int i = 0; i < arestasPeso.size(); i++) {
        //    System.out.println("  " + labelVertice(arestasU.get(i)) + " - " + labelVertice(arestasV.get(i)) + " | peso: " + arestasPeso.get(i)); // remover
        //}

        List<String> solucao = new ArrayList<>();
        float somaArestas = 0;

        for (int i = 0; i < arestasPeso.size(); i++) {
            int u = arestasU.get(i);
            int v = arestasV.get(i);
            float peso = arestasPeso.get(i);

            // System.out.println("Verificando aresta: " + labelVertice(u) + " - " + labelVertice(arestasV.get(i)) + " | peso: " + arestasPeso.get(i)); // remover

            if (floresta[u] != floresta[v]) {
                //System.out.println("Escolhendo aresta: " + labelVertice(u) + " - " + labelVertice(v) + " | peso: " + peso); // remover
                somaArestas += peso;
                solucao.add(labelVertice(u) + " - " + labelVertice(v));

                int grupoU = floresta[u];
                int grupoV = floresta[v];
                //System.out.println("Grupo do vértice " + labelVertice(u) + ": " + grupoU); // remover
                //System.out.println("Grupo do vértice " + labelVertice(v) + ": " + grupoV); // remover

                for (int j = 0; j < totalVertices; j++) {
                    //System.out.println("Verificando vértice " + labelVertice(j) + " | grupo atual: " + floresta[j]); // remover
                    if (floresta[j] == grupoV) {
                        floresta[j] = grupoU;
                        //System.out.println("Atualizando vértice " + labelVertice(j) + " para grupo " + grupoU); // remover
                    }
                }

                //System.out.println("Floresta (hierarquia):");
                //imprimirFlorestaComoArvore(floresta);

                // System.out.println("Aresta escolhida: " + labelVertice(u) + " - " + labelVertice(v) + " | peso: " + peso); // remover
            }
            // System.out.println("Processando aresta: " + labelVertice(u) + " - " + labelVertice(v) + " | peso: " + peso); // remover

        }

        //System.out.println("Floresta final: " + Arrays.toString(floresta)); // remover

        // System.out.println("Solução: " + solucao);
        long tempoFim = System.nanoTime();
        imprimirResultadoAGM("Kruskal", tempoFim - tempoInicio, somaArestas, solucao);

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

        // Ordenar os vértices pelo seu grau em ordem decrescente (Bubble sort)
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

        List<Integer>[] vizinhosCache = new List[totalVertices];
        int[] arrayGraus = new int[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            vizinhosCache[i] = retornarVizinhos(i);
            arrayGraus[i] = vizinhosCache[i].size();
        }

        int[] atribuicaoCores = new int[totalVertices];

        int[] saturacao = new int[totalVertices];
        Set<Integer>[] coresVizinhos = new HashSet[totalVertices];
        for (int i = 0; i < totalVertices; i++) {
            coresVizinhos[i] = new HashSet<>();
        }

        int verticeMaiorGrau = 0;
        for (int i = 1; i < totalVertices; i++) {
            if (arrayGraus[i] > arrayGraus[verticeMaiorGrau]) {
                verticeMaiorGrau = i;
            }
        }
        atribuicaoCores[verticeMaiorGrau] = 1;
        int contadorColoridos = 1;
        int maiorIndiceCorUsada = 1;

        for (int vizinho : vizinhosCache[verticeMaiorGrau]) {
            if (coresVizinhos[vizinho].add(1)) {
                saturacao[vizinho]++;
            }
        }

        while (contadorColoridos < totalVertices) {
            int proximoVertice = -1;
            int maiorSat = -1;

            for (int i = 0; i < totalVertices; i++) {
                if (atribuicaoCores[i] != 0) continue;
                if (saturacao[i] > maiorSat ||
                   (saturacao[i] == maiorSat && (proximoVertice == -1 || arrayGraus[i] > arrayGraus[proximoVertice]))) {
                    maiorSat = saturacao[i];
                    proximoVertice = i;
                }
            }

            boolean[] corUsada = new boolean[maiorIndiceCorUsada + 2];
            for (int vizinho : vizinhosCache[proximoVertice]) {
                int cor = atribuicaoCores[vizinho];
                if (cor > 0) corUsada[cor] = true;
            }
            int corParaAtribuir = 1;
            while (corUsada[corParaAtribuir]) corParaAtribuir++;

            atribuicaoCores[proximoVertice] = corParaAtribuir;
            if (corParaAtribuir > maiorIndiceCorUsada) maiorIndiceCorUsada = corParaAtribuir;
            contadorColoridos++;

            for (int vizinho : vizinhosCache[proximoVertice]) {
                if (atribuicaoCores[vizinho] == 0 && coresVizinhos[vizinho].add(corParaAtribuir)) {
                    saturacao[vizinho]++;
                }
            }
        }

        long tempoFim = System.nanoTime();
        imprimirResultadoColoracao("DSATUR", atribuicaoCores, tempoFim - tempoInicio, maiorIndiceCorUsada);
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

    // ----------------------------- ALGORITMOS DE FLUXO MÁXIMO (M3) -----------------------------

    private boolean dfsCaminhoAumentante(Grafo grafoAuxiliar, int fonte, int sorvedor, int[] anterior) {
        int total = grafoAuxiliar.tamanhoGrafo();
        boolean[] visitado = new boolean[total];

        java.util.Deque<Integer> pilha = new java.util.ArrayDeque<>();
        pilha.push(fonte);
        visitado[fonte] = true;
        anterior[fonte] = -1;

        while (!pilha.isEmpty()) {
            int u = pilha.pop();
            if (u == sorvedor) return true;

            for (int v : grafoAuxiliar.retornarVizinhos(u)) {
                if (!visitado[v]) {
                    visitado[v] = true;
                    anterior[v] = u;
                    pilha.push(v);
                }
            }
        }
        return false;
    }

    private Grafo clonarGrafo() {
        int total = tamanhoGrafo();
        Grafo clone = (this instanceof GrafoLista) ? new GrafoLista(direcionado, ponderado) : new GrafoMatriz(direcionado, ponderado);

        for (int i = 0; i < total; i++)
            clone.inserirVertice(labelVertice(i));

        for (int u = 0; u < total; u++)
            for (int v : retornarVizinhos(u))
                clone.inserirAresta(u, v, pesoAresta(u, v));

        return clone;
    }

    public float fordFulkerson(int fonte, int sorvedor) {
        Grafo grafoAuxiliar = clonarGrafo();
        int total = grafoAuxiliar.tamanhoGrafo();
        int[] anterior = new int[total];
        float S = 0;
        // enquanto existir pelo menos um caminho P de capacidade positiva da fonte até o sorvedor
        while (dfsCaminhoAumentante(grafoAuxiliar, fonte, sorvedor, anterior)) {
            // encontra o menor arco A do caminho P
            float A = Float.MAX_VALUE;
            for (int v = sorvedor; v != fonte; v = anterior[v]) {
                int u = anterior[v];
                if (grafoAuxiliar.pesoAresta(u, v) < A)
                    A = grafoAuxiliar.pesoAresta(u, v);
            }
            S += A;
            // pra cada arco (u,v) no caminho P
            for (int v = sorvedor; v != fonte; v = anterior[v]) {
                int u = anterior[v];

                // subtrai o valor de A no arco (u,v)
                float novaCapacidade = grafoAuxiliar.pesoAresta(u, v) - A;
                // se o valor da aresta for 0, remove a aresta pra ela nao ser usada no DFS para encontrar um novo caminho
                if (novaCapacidade <= 0)
                    grafoAuxiliar.removerAresta(u, v);
                else {
                    grafoAuxiliar.removerAresta(u, v);
                    grafoAuxiliar.inserirAresta(u, v, novaCapacidade);
                }
                // se existe arco (v,u), soma o valor de A, senão cria com valor A
                if (grafoAuxiliar.existeAresta(v, u))
                    grafoAuxiliar.inserirAresta(v, u, grafoAuxiliar.pesoAresta(v, u) + A);
                else
                    grafoAuxiliar.inserirAresta(v, u, A);
            }
        }

        return S;
    }

    public void executarFordFulkerson(int fonte, int sorvedor) {
        float S = fordFulkerson(fonte, sorvedor);
        System.out.println("\n>>> Algoritmo: Ford-Fulkerson");
        System.out.println("Fluxo máximo: " + (int) S);
    }

    public void buscaLocalFluxoMaximo(int fonte, int sorvedor) {
            float fluxoOriginal = fordFulkerson(fonte, sorvedor);
            Grafo solucaoAtual = clonarGrafo();
            float fluxoAtual = fluxoOriginal;
            int passos = 0;
            boolean melhorou = true;

            while (melhorou) {
                melhorou = false;
                int total = solucaoAtual.tamanhoGrafo();

                List<int[]> arestas = new ArrayList<>();
                for (int u = 0; u < total; u++)
                    for (int v : solucaoAtual.retornarVizinhos(u))
                        arestas.add(new int[]{u, v});

                for (int[] aresta : arestas) {
                    int u = aresta[0], v = aresta[1];
                    float peso = solucaoAtual.pesoAresta(u, v);

                    Grafo vizinho = solucaoAtual.clonarGrafo();
                    vizinho.removerAresta(u, v);
                    vizinho.inserirAresta(v, u, peso);

                    float fluxoVizinho = vizinho.fordFulkerson(fonte, sorvedor);
                    float deltaCusto = fluxoVizinho - fluxoAtual;

                    if (deltaCusto > 0) {
                        fluxoAtual = fluxoVizinho;
                        solucaoAtual = vizinho;
                        passos++;
                        melhorou = true;
                        break;
                    }
                }
            }

            System.out.println("\n>>> Algoritmo: Busca Local - Ford-Fulkerson");
            System.out.println("Fluxo máximo original: " + (int) fluxoOriginal);
            System.out.println("Fluxo máximo final: " + (int) fluxoAtual);
            System.out.println("Número de passos: " + passos);
        }
}
