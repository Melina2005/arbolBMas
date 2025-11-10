import java.util.ArrayList;
import java.util.List;

public class Nodo {
    List<Integer> claves;
    List<String> valores; // Solo se usa si es hoja
    List<Nodo> hijos;
    boolean esHoja;
    Nodo siguiente; // Enlace entre hojas
    Nodo padre;

    public Nodo(boolean esHoja) {
        this.claves = new ArrayList<>();
        this.valores = new ArrayList<>();
        this.hijos = new ArrayList<>();
        this.esHoja = esHoja;
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
        int puntoMedio = claves.size() / 2;

        // Crear nuevo nodo
        Nodo nuevoNodo = new Nodo(this.esHoja);
        nuevoNodo.padre = this.padre;

        // Mover la mitad derecha al nuevo nodo
        if (esHoja) {
            for (int i = puntoMedio; i < claves.size(); i++) {
                nuevoNodo.claves.add(claves.get(i));
                nuevoNodo.valores.add(valores.get(i));
            }

            // Enlazar hojas
            nuevoNodo.siguiente = this.siguiente;
            this.siguiente = nuevoNodo;

            // Eliminar la mitad derecha del nodo actual
            for (int i = claves.size() - 1; i >= puntoMedio; i--) {
                claves.remove(i);
                valores.remove(i);
            }
        } else {
            // Nodo interno: la clave del medio sube al padre
            int claveMedia = claves.get(puntoMedio);

            // Mover claves e hijos derechos al nuevo nodo
            for (int i = puntoMedio + 1; i < claves.size(); i++) {
                nuevoNodo.claves.add(claves.get(i));
            }
            for (int i = puntoMedio + 1; i < hijos.size(); i++) {
                Nodo hijo = hijos.get(i);
                nuevoNodo.hijos.add(hijo);
                hijo.padre = nuevoNodo;
            }

            // Eliminar la mitad derecha del nodo actual
            for (int i = claves.size() - 1; i >= puntoMedio; i--) {
                claves.remove(i);
            }
            for (int i = hijos.size() - 1; i >= puntoMedio + 1; i--) {
                hijos.remove(i);
            }

            // Retornar el nuevo nodo (el valor que sube lo maneja el árbol)
            nuevoNodo.insertarClave(claveMedia, null);
        }

        return nuevoNodo;
    }

    public void imprimirNodo() {
        System.out.print("[");
        for (int i = 0; i < claves.size(); i++) {
            System.out.print(claves.get(i));
            if (i < claves.size() - 1) System.out.print(", ");
        }
        System.out.print("]");
        if (esHoja) System.out.print(" -> ");
    }
}
