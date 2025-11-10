
import java.util.ArrayList;
import java.util.List;

public class Nodo {

    List<Integer> claves;
    List<String> valores;
    List<Nodo> hijos;
    boolean esHoja;
    Nodo siguiente;
    Nodo padre;

    public Nodo(boolean esHoja) {
        this.esHoja = esHoja;
        this.claves = new ArrayList<>();
        this.valores = new ArrayList<>();
        this.hijos = new ArrayList<>();
        this.siguiente = null;
        this.padre = null;
    }

    public void insertarClave(int clave, String valor) {
        int i = 0;
        while (i < claves.size() && clave > claves.get(i)) {
            i++;
        }
        claves.add(i, clave);
        if (esHoja) {
            valores.add(i, valor);
        }
    }

    public Nodo dividir(int orden) {
        int mitad = claves.size() / 2;
        Nodo nuevo = new Nodo(esHoja);
        nuevo.padre = padre;

        if (esHoja) {
            nuevo.claves.addAll(claves.subList(mitad, claves.size()));
            nuevo.valores.addAll(valores.subList(mitad, valores.size()));
            claves = new ArrayList<>(claves.subList(0, mitad));
            valores = new ArrayList<>(valores.subList(0, mitad));
            nuevo.siguiente = siguiente;
            siguiente = nuevo;
        } else {
            int claveMedia = claves.get(mitad);
            nuevo.claves.addAll(claves.subList(mitad + 1, claves.size()));
            nuevo.hijos.addAll(hijos.subList(mitad + 1, hijos.size()));
            for (Nodo h : nuevo.hijos) {
                h.padre = nuevo;
            }
            claves = new ArrayList<>(claves.subList(0, mitad));
            hijos = new ArrayList<>(hijos.subList(0, mitad + 1));
            nuevo.insertarClave(claveMedia, null);
        }

        return nuevo;
    }

    public void imprimirNodo() {
        System.out.print("[");
        for (int i = 0; i < claves.size(); i++) {
            System.out.print(claves.get(i));
            if (i < claves.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
        if (esHoja) {
            System.out.print(" -> ");
        }
    }
}
