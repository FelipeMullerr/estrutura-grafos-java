package grafos;

import java.util.List;

public abstract class Grafo {

    protected boolean direcionado;
    protected boolean ponderado;

    public Grafo(boolean direcionado, boolean ponderado) {
        this.direcionado = direcionado;
        this.ponderado = ponderado;
    }

    public abstract boolean inserirVertice(String label);
    public abstract boolean removerVertice(int indice);
    public abstract String labelVertice(int indice);
    public abstract void imprimeGrafo();

    public abstract boolean inserirAresta(int origem, int destino, float peso);
    public abstract boolean removerAresta(int origem, int destino);
    public abstract boolean existeAresta(int origem, int destino);
    public abstract float pesoAresta(int origem, int destino);

    public abstract List<Integer> retornarVizinhos(int vertice);
}