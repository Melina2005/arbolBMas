

public class arbolBMas {

    private Nodo raiz;
    private int orden;

    public arbolBMas(int orden) {
        if (orden < 3) {
            throw new IllegalArgumentException("El orden del árbol B+ debe ser al menos 3");
        }
        this.orden = orden;
        this.raiz = new Nodo(true);
    }

    public void insertar(int clave, String valor) {
        try {
            Nodo nodo = raiz;

            while (!nodo.esHoja) {
                int i = 0;
                while (i < nodo.claves.size() && clave >= nodo.claves.get(i)) {
                    i++;
                }

                if (i >= nodo.hijos.size()) {
                    i = nodo.hijos.size() - 1;
                }

                nodo = nodo.hijos.get(i);
            }

            if (nodo.claves.contains(clave)) {
                System.out.println("Inserción cancelada: la clave " + clave + " ya existe en el árbol.");
                return;
            }

            nodo.insertarClave(clave, valor);
            System.out.println("Inserción exitosa: clave " + clave + " agregada correctamente.");

            if (nodo.claves.size() == orden) {
                dividirNodo(nodo);
                System.out.println("Se dividió el nodo debido a exceso de claves (orden " + orden + ").");
            }

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: intento de acceder a un índice fuera de rango durante la inserción de la clave " + clave + ".");
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado al insertar la clave " + clave + ": " + e.getMessage());
        }
    }

    private void dividirNodo(Nodo nodo) {
        Nodo nuevo = nodo.dividir(orden);

        if (nodo.padre == null) {
            Nodo nuevaRaiz = new Nodo(false);

            int claveSube;
            if (nodo.esHoja) {
                claveSube = nuevo.claves.get(0);
            } else {
                claveSube = nuevo.claves.remove(0);
            }

            nuevaRaiz.claves.add(claveSube);
            nuevaRaiz.hijos.add(nodo);
            nuevaRaiz.hijos.add(nuevo);
            nodo.padre = nuevaRaiz;
            nuevo.padre = nuevaRaiz;
            this.raiz = nuevaRaiz;
        } else {
            Nodo padre = nodo.padre;
            int claveSube;

            if (nodo.esHoja) {
                claveSube = nuevo.claves.get(0);
            } else {
                claveSube = nuevo.claves.remove(0);
            }

            int i = 0;
            while (i < padre.claves.size() && claveSube > padre.claves.get(i)) {
                i++;
            }

            padre.claves.add(i, claveSube);

            if (i + 1 > padre.hijos.size()) {
                padre.hijos.add(nuevo);
            } else {
                padre.hijos.add(i + 1, nuevo);
            }

            nuevo.padre = padre;

            if (padre.claves.size() == orden) {
                dividirNodo(padre);
            }
        }
    }

    public String buscar(int clave) {
        Nodo nodo = raiz;

        while (nodo != null) {
            if (nodo.esHoja) {
                for (int i = 0; i < nodo.claves.size(); i++) {
                    if (nodo.claves.get(i) == clave) {
                        return nodo.valores.get(i);
                    }
                }
                return null;
            } else {
                int i = 0;
                while (i < nodo.claves.size() && clave >= nodo.claves.get(i)) {
                    i++;
                }

                if (nodo.hijos == null || nodo.hijos.isEmpty()) {
                    System.out.println("Nodo sin hijos durante búsqueda, estructura inconsistente.");
                    return null;
                }

                if (i >= nodo.hijos.size()) {
                    i = nodo.hijos.size() - 1;
                }

                if (i < 0) {
                    i = 0;
                }

                nodo = nodo.hijos.get(i);
            }
        }

        return null;
    }

    public boolean eliminar(int clave) {
        if (raiz == null) {
            return false;
        }

        boolean eliminado = eliminarRecursivo(raiz, clave);

        if (raiz.claves.isEmpty() && !raiz.esHoja) {
            raiz = raiz.hijos.get(0);
            raiz.padre = null;
        }

        return eliminado;
    }

    private boolean eliminarRecursivo(Nodo nodo, int clave) {
        if (nodo.esHoja) {
            int idx = nodo.claves.indexOf(clave);
            if (idx == -1) {
                return false;
            }

            nodo.claves.remove(idx);
            nodo.valores.remove(idx);
            return true;
        }

        int i = 0;
        while (i < nodo.claves.size() && clave >= nodo.claves.get(i)) {
            i++;
        }

        Nodo hijo = nodo.hijos.get(i);
        boolean eliminado = eliminarRecursivo(hijo, clave);
        if (!eliminado) {
            return false;
        }

        int minClaves = (int) Math.ceil((orden - 1) / 2.0);
        if (hijo.claves.size() < minClaves) {
            rebalancear(nodo, i);
        }

        if (nodo == raiz && nodo.claves.isEmpty() && !nodo.esHoja) {
            raiz = nodo.hijos.get(0);
            raiz.padre = null;
        }

        return true;
    }

    private void rebalancear(Nodo padre, int indiceHijo) {
        if (padre == null || indiceHijo < 0 || indiceHijo >= padre.hijos.size()) {
            return;
        }

        Nodo hijo = padre.hijos.get(indiceHijo);
        Nodo hermanoIzq = (indiceHijo > 0) ? padre.hijos.get(indiceHijo - 1) : null;
        Nodo hermanoDer = (indiceHijo < padre.hijos.size() - 1) ? padre.hijos.get(indiceHijo + 1) : null;

        int minClaves = (int) Math.ceil((orden - 1) / 2.0);

        if (hijo.claves.isEmpty()) {
            if (hermanoIzq != null) {
                fusionar(padre, indiceHijo - 1, hermanoIzq, hijo);
            } else if (hermanoDer != null) {
                fusionar(padre, indiceHijo, hijo, hermanoDer);
            }
            if (padre == raiz && padre.claves.isEmpty() && padre.hijos.size() == 1) {
                raiz = padre.hijos.get(0);
                raiz.padre = null;
            }
            return;
        }

        if (hermanoIzq != null && hermanoIzq.claves.size() > minClaves) {
            if (hijo.esHoja) {
                int clavePrestada = hermanoIzq.claves.remove(hermanoIzq.claves.size() - 1);
                String valorPrestado = hermanoIzq.valores.remove(hermanoIzq.valores.size() - 1);
                hijo.claves.add(0, clavePrestada);
                hijo.valores.add(0, valorPrestado);
                padre.claves.set(indiceHijo - 1, hijo.claves.get(0));
            } else {
                int clavePadre = padre.claves.get(indiceHijo - 1);
                int clavePrestada = hermanoIzq.claves.remove(hermanoIzq.claves.size() - 1);
                Nodo hijoPrestado = hermanoIzq.hijos.remove(hermanoIzq.hijos.size() - 1);
                hijo.claves.add(0, clavePadre);
                hijo.hijos.add(0, hijoPrestado);
                hijoPrestado.padre = hijo;
                padre.claves.set(indiceHijo - 1, clavePrestada);
            }
            return;
        }

        if (hermanoDer != null && hermanoDer.claves.size() > minClaves) {
            if (hijo.esHoja) {
                int clavePrestada = hermanoDer.claves.remove(0);
                String valorPrestado = hermanoDer.valores.remove(0);
                hijo.claves.add(clavePrestada);
                hijo.valores.add(valorPrestado);
                padre.claves.set(indiceHijo, hermanoDer.claves.get(0));
            } else {
                int clavePadre = padre.claves.get(indiceHijo);
                int clavePrestada = hermanoDer.claves.remove(0);
                Nodo hijoPrestado = hermanoDer.hijos.remove(0);
                hijo.claves.add(clavePadre);
                hijo.hijos.add(hijoPrestado);
                hijoPrestado.padre = hijo;
                padre.claves.set(indiceHijo, clavePrestada);
            }
            return;
        }

        if (hermanoIzq != null) {
            fusionar(padre, indiceHijo - 1, hermanoIzq, hijo);
        } else if (hermanoDer != null) {
            fusionar(padre, indiceHijo, hijo, hermanoDer);
        }

        if (padre == raiz && padre.claves.isEmpty() && padre.hijos.size() == 1) {
            raiz = padre.hijos.get(0);
            raiz.padre = null;
        }
    }

    private void fusionar(Nodo padre, int indice, Nodo izquierdo, Nodo derecho) {
        if (izquierdo.esHoja) {
            izquierdo.claves.addAll(derecho.claves);
            izquierdo.valores.addAll(derecho.valores);
            izquierdo.siguiente = derecho.siguiente;
        } else {
            int clavePadre = padre.claves.remove(indice);
            izquierdo.claves.add(clavePadre);
            izquierdo.claves.addAll(derecho.claves);
            for (Nodo hijo : derecho.hijos) {
                izquierdo.hijos.add(hijo);
                hijo.padre = izquierdo;
            }
        }

        padre.hijos.remove(indice + 1);
        if (padre.claves.isEmpty() && padre == raiz && padre.hijos.size() == 1) {
            raiz = padre.hijos.get(0);
            raiz.padre = null;
        }
    }

    public void recorrer(int claveInicial, int n) {
        Nodo nodo = raiz;

        while (!nodo.esHoja) {
            int i = 0;
            while (i < nodo.claves.size() && claveInicial >= nodo.claves.get(i)) {
                i++;
            }
            nodo = nodo.hijos.get(i);
        }

        int idx = 0;
        while (idx < nodo.claves.size() && nodo.claves.get(idx) < claveInicial) {
            idx++;
        }

        int cont = 0;
        while (nodo != null && cont < n) {
            while (idx < nodo.claves.size() && cont < n) {
                System.out.println("Clave: " + nodo.claves.get(idx) + " | Valor: " + nodo.valores.get(idx));
                idx++;
                cont++;
            }
            nodo = nodo.siguiente;
            idx = 0;
        }
    }

    public void imprimirArbol() {
        if (raiz == null) {
            System.out.println("El árbol está vacío.");
            return;
        }

        imprimirNodoRec(raiz, 0, "Raíz");
    }

    private void imprimirNodoRec(Nodo nodo, int nivel, String tipo) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("   ");
        }

        System.out.print(tipo + " ");
        nodo.imprimirNodo();
        System.out.println();

        if (!nodo.esHoja) {
            for (int i = 0; i < nodo.hijos.size(); i++) {
                Nodo hijo = nodo.hijos.get(i);
                String subtipo = (i == nodo.hijos.size() - 1)
                        ? "└── Hijo " + (i + 1)
                        : "├── Hijo " + (i + 1);
                imprimirNodoRec(hijo, nivel + 1, subtipo);
            }
        } else if (nodo.siguiente != null) {
            for (int i = 0; i < nivel + 1; i++) {
                System.out.print("   ");
            }
            System.out.println("↓ enlazado con hoja siguiente: " + nodo.siguiente.claves);
        }
    }
}
