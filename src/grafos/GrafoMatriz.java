package grafos;

import java.util.ArrayList;
import java.util.List;

public class GrafoMatriz extends Grafo {

    private String[] vertices;
    private float[][] matriz;
    private int tamanho;

    public GrafoMatriz(boolean direcionado, boolean ponderado) {
        super(direcionado, ponderado);
        vertices = new String[0];
        matriz = new float[0][0];
        tamanho = 0;
    }

    @Override
    public int tamanhoGrafo() {
        return tamanho;
    }

    @Override
    public int indiceVertice(String label) {
        for (int i = 0; i < tamanho; i++) {
            if (vertices[i].equals(label)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean inserirVertice(String label) {
        // cria novas estruturas com tamanho maior
        String[] novoVertices = new String[tamanho + 1];
        float[][] novaMatriz = new float[tamanho + 1][tamanho + 1];

        // copia a matriz para a nova
        for (int i = 0; i < tamanho; i++) {
            novoVertices[i] = vertices[i];
            for (int j = 0; j < tamanho; j++) {
                novaMatriz[i][j] = matriz[i][j];
            }
        }
        // insere o novo vertice
        novoVertices[tamanho] = label;

        vertices = novoVertices;
        matriz = novaMatriz;
        tamanho++;

        return true;
    }

    @Override
    public boolean removerVertice(int indice) {
        if (indice < 0 || indice >= tamanho) return false;

        String[] novoVertices = new String[tamanho - 1];
        float[][] novaMatriz = new float[tamanho - 1][tamanho - 1];

        int ni = 0;
        for (int i = 0; i < tamanho; i++) {
            if (i == indice) continue;
            novoVertices[ni++] = vertices[i];
        }

        // percore a matriz antiga, inserindo na nova usando ni e nj
        // se o indice for o mesmo do vertice que foi removido, pula
        ni = 0;
        for (int i = 0; i < tamanho; i++) {
            if (i == indice) continue;

            int nj = 0;
            for (int j = 0; j < tamanho; j++) {
                if (j == indice) continue;
                novaMatriz[ni][nj++] = matriz[i][j];
            }
            ni++;
        }

        vertices = novoVertices;
        matriz = novaMatriz;
        tamanho--;

        return true;
    }

    @Override
    public String labelVertice(int indice) {
        return vertices[indice];
    }

    @Override
    public void imprimeGrafo() {
        System.out.print("\t\t  ");
        for (int i = 0; i < tamanho; i++) {
            System.out.printf("%-8s", vertices[i]);
        }
        System.out.println();

        for (int i = 0; i < tamanho; i++) {
            System.out.printf("%-8s", vertices[i]);
            for (int j = 0; j < tamanho; j++) {
                System.out.printf("%-8.1f", matriz[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public boolean inserirAresta(int origem, int destino, float peso) {
        if (!ponderado) peso = 1;

        matriz[origem][destino] = peso;

        if (!direcionado) {
            matriz[destino][origem] = peso;
        }

        return true;
    }

    @Override
    public boolean removerAresta(int origem, int destino) {
        matriz[origem][destino] = 0;

        if (!direcionado) {
            matriz[destino][origem] = 0;
        }

        return true;
    }

    @Override
    public boolean existeAresta(int origem, int destino) {
        return matriz[origem][destino] != 0;
    }

    @Override
    public float pesoAresta(int origem, int destino) {
        return matriz[origem][destino];
    }

    @Override
    public List<Integer> retornarVizinhos(int vertice) {
        List<Integer> vizinhos = new ArrayList<>();

        for (int i = 0; i < tamanho; i++) {
            if (matriz[vertice][i] != 0) {
                vizinhos.add(i);
            }
        }

        return vizinhos;
    }
}