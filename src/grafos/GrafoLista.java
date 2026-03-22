    package grafos;

    import java.util.ArrayList;
    import java.util.List;

    public class GrafoLista extends Grafo {

        private List<String> vertices;
        private List<List<Aresta>> listaAdjacencia;

        public GrafoLista(boolean direcionado, boolean ponderado) {
            super(direcionado, ponderado);
            vertices = new ArrayList<>();
            listaAdjacencia = new ArrayList<>();
        }

        public int tamanhoGrafo() {
            return vertices.size();
        }

        @Override
        public int indiceVertice(String label) {
            for (int i = 0; i < vertices.size(); i++) {
                if (vertices.get(i).equals(label)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public boolean inserirVertice(String label) {
            vertices.add(label);
            listaAdjacencia.add(new ArrayList<>());
            return true;
        }

        @Override
        public boolean removerVertice(int indice) {
            if (indice < 0 || indice >= vertices.size()) return false;

            vertices.remove(indice);
            listaAdjacencia.remove(indice);

            // tirar as arestas de vertices que vao apontar para o vertice removido
            for (List<Aresta> lista : listaAdjacencia) {
                lista.removeIf(a -> a.destino == indice);

                for (Aresta a : lista) {
                    if (a.destino > indice) {
                        a.destino--;
                    }
                }
            }

            return true;
        }

        @Override
        public String labelVertice(int indice) {
            return vertices.get(indice);
        }

        @Override
        public void imprimeGrafo() {
            for (int i = 0; i < listaAdjacencia.size(); i++) {
                System.out.print(vertices.get(i) + " -> ");
                for (Aresta a : listaAdjacencia.get(i)) {
                    System.out.print(vertices.get(a.destino));
                    if (ponderado) System.out.print("(" + a.peso + ")");
                    System.out.print(" ");
                }
                System.out.println();
            }
        }

        @Override
        public boolean inserirAresta(int origem, int destino, float peso) {
            if (!ponderado) peso = 1;

            listaAdjacencia.get(origem).add(new Aresta(destino, peso));

            if (!direcionado) {
                listaAdjacencia.get(destino).add(new Aresta(origem, peso));
            }

            return true;
        }

        @Override
        public boolean removerAresta(int origem, int destino) {
            listaAdjacencia.get(origem).removeIf(a -> a.destino == destino);

            if (!direcionado) {
                listaAdjacencia.get(destino).removeIf(a -> a.destino == origem);
            }

            return true;
        }

        @Override
        public boolean existeAresta(int origem, int destino) {
            for (Aresta a : listaAdjacencia.get(origem)) {
                if (a.destino == destino) return true;
            }
            return false;
        }

        @Override
        public float pesoAresta(int origem, int destino) {
            for (Aresta a : listaAdjacencia.get(origem)) {
                if (a.destino == destino) return a.peso;
            }
            return -1;
        }

        @Override
        public List<Integer> retornarVizinhos(int vertice) {
            List<Integer> vizinhos = new ArrayList<>();

            for (Aresta a : listaAdjacencia.get(vertice)) {
                vizinhos.add(a.destino);
            }

            return vizinhos;
        }
    }