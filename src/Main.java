
import java.util.Scanner;

public class Main {

    private static arbolBMas arbol;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarArbol();
        ejecutarMenu();
    }

    private static void inicializarArbol() {
        System.out.print("Ingrese el orden del Árbol B+ (mínimo 3): ");
        int orden = leerEntero();
        arbol = new arbolBMas(orden);
        System.out.println("\nÁrbol B+ inicializado con orden " + orden + ".\n");
    }

    private static void ejecutarMenu() {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero();
            System.out.println();

            switch (opcion) {
                case 1 ->
                    insertarElemento();
                case 2 ->
                    buscarElemento();
                case 3 ->
                    eliminarElemento();
                case 4 ->
                    recorrerArbol();
                case 5 ->
                    imprimirEstructura();
                case 0 ->
                    System.out.println("Saliendo del programa...");
                default ->
                    System.out.println("Opción no válida. Intente nuevamente.\n");
            }

        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("                 MENÚ                  ");
        System.out.println("═══════════════════════════════════════");
        System.out.println("1. Insertar elemento");
        System.out.println("2. Buscar elemento");
        System.out.println("3. Eliminar elemento");
        System.out.println("4. Recorrer n elementos desde una clave");
        System.out.println("5. Imprimir estructura completa");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void insertarElemento() {
        System.out.print("Ingrese la clave (entero): ");
        int clave = leerEntero();
        System.out.print("Ingrese el valor asociado: ");
        String valor = scanner.nextLine();

        arbol.insertar(clave, valor);
    }

    private static void buscarElemento() {
        System.out.print("Ingrese la clave a buscar: ");
        int clave = leerEntero();

        String resultado = arbol.buscar(clave);
        if (resultado != null) {
            System.out.println("Valor encontrado: " + resultado + "\n");
        } else {
            System.out.println("La clave no existe en el árbol.\n");
        }
    }

    private static void eliminarElemento() {
        System.out.print("Ingrese la clave a eliminar: ");
        int clave = leerEntero();

        boolean eliminado = arbol.eliminar(clave);
        if (eliminado) {
            System.out.println("Elemento eliminado correctamente.\n");
        } else {
            System.out.println("La clave no existe en el árbol.\n");
        }
    }

    private static void recorrerArbol() {
        System.out.print("Ingrese la clave inicial: ");
        int clave = leerEntero();
        System.out.print("Ingrese la cantidad de elementos a mostrar: ");
        int n = leerEntero();

        System.out.println("\nRecorriendo elementos...");
        arbol.recorrer(clave, n);
        System.out.println();
    }

    private static void imprimirEstructura() {
        System.out.println("\nEstructura completa del árbol B+:");
        arbol.imprimirArbol();
        System.out.println();
    }

    private static int leerEntero() {
        while (true) {
            try {
                int num = Integer.parseInt(scanner.nextLine());
                return num;
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número entero: ");
            }
        }
    }
}
