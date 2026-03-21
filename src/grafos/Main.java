package grafos;

public class Main {

    public static void main(String[] args) {

        System.out.println("grafo lista direcionada e ponderada");

        Grafo gLista = new GrafoLista(false, true);

        gLista.inserirVertice("A");
        gLista.inserirVertice("B");
        gLista.inserirVertice("C");
        gLista.inserirVertice("D");

        gLista.inserirAresta(0, 1, 2); // A-B
        gLista.inserirAresta(0, 2, 3); // A-C
        gLista.inserirAresta(1, 3, 4); // B-D

        gLista.imprimeGrafo();

        System.out.println("\nverificar se existe aresta entre A e B: " + gLista.existeAresta(0, 1));
        System.out.println("peso da aresta entre A e B: " + gLista.pesoAresta(0, 1));

        System.out.println("vizinhos de A:");
        for (int v : gLista.retornarVizinhos(0)) {
            System.out.print(gLista.labelVertice(v) + " ");
        }

        System.out.println("\n\nremover aresta A-B");
        gLista.removerAresta(0, 1);
        gLista.imprimeGrafo();

        System.out.println("\nremover vertice C");
        gLista.removerVertice(2);
        gLista.imprimeGrafo();

         //
         //
         //
         //

        System.out.println("\n\n\ngrafo matriz nao direcionada e ponderada");

        Grafo gMatriz = new GrafoMatriz(false, true);

        gMatriz.inserirVertice("A");
        gMatriz.inserirVertice("B");
        gMatriz.inserirVertice("C");
        gMatriz.inserirVertice("D");

        gMatriz.inserirAresta(0, 1, 2); // A-B
        gMatriz.inserirAresta(0, 2, 3); // A-C
        gMatriz.inserirAresta(1, 3, 4); // B-D

        gMatriz.imprimeGrafo();

        System.out.println("\nverificar se existe aresta entre A e B: " + gMatriz.existeAresta(0, 1));
        System.out.println("peso da aresta entre A e B: " + gMatriz.pesoAresta(0, 1));

        System.out.println("vizinhos de A:");
        for (int v : gMatriz.retornarVizinhos(0)) {
            System.out.print(gMatriz.labelVertice(v) + " ");
        }

        System.out.println("\n\nremover aresta A-B");
        gMatriz.removerAresta(0, 1);
        gMatriz.imprimeGrafo();

        System.out.println("\nremover vertice C");
        gMatriz.removerVertice(2);
        gMatriz.imprimeGrafo();
    }
}