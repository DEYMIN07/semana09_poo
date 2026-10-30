package practicaarchivos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Prueba de manejo de archivos con pacientes de la clínica. */
public class PracticaArchivos {

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void mostrarLista(ArchivoPacientes repo) {
        ArrayList<Paciente> lista = repo.listar();
        if (lista.isEmpty()) {
            System.out.println("(el archivo no tiene pacientes)");
        }
        for (Paciente p : lista) {
            p.verDatos();
        }
    }

    public static void main(String[] args) {
        // Se empieza con un archivo nuevo para que la prueba sea repetible
        new File("datos_clinica/pacientes.txt").delete();

        titulo("1. Crear carpeta y archivo");
        ArchivoPacientes repo = new ArchivoPacientes("datos_clinica/pacientes.txt");
        File f = repo.getArchivo();
        System.out.println("Archivo: " + f.getPath());
        System.out.println("Existe: " + f.exists() + " | Tamaño: " + f.length() + " bytes");

        titulo("2. Guardar pacientes");
        repo.guardar(new Paciente("DNI", "70000001", "Ana María", "Torres Rojas", "1990-05-14", "987654321"));
        repo.guardar(new Paciente("DNI", "70000002", "Luis", "Vargas Díaz", "1985-11-02", "912345678"));
        repo.guardar(new Paciente("CE", "123456789", "Carla", "Mendoza Ruiz", "2001-03-27", "955512345"));
        System.out.println("Se guardaron 3 pacientes.");
        System.out.println("Tamaño: " + f.length() + " bytes");

        titulo("3. Intentar guardar un documento repetido");
        try {
            repo.guardar(new Paciente("DNI", "70000001", "Otra", "Persona", "1995-01-01", "900000000"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        titulo("4. Intentar guardar datos inválidos");
        try {
            repo.guardar(new Paciente("DNI", "7000ABCD", "Pedro", "Quispe", "1992-08-09", "933333333"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            repo.guardar(new Paciente("DNI", "70000004", "Pedro", "Quispe", "1992-08-09", "12345"));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        titulo("5. Listar pacientes (lectura del archivo)");
        mostrarLista(repo);

        titulo("6. Buscar por documento");
        Paciente encontrado = repo.buscarPorDocumento("70000002");
        if (encontrado != null) {
            System.out.println("Buscando 70000002: " + encontrado);
        } else {
            System.out.println("Buscando 70000002: no encontrado");
        }
        Paciente noExiste = repo.buscarPorDocumento("99999999");
        if (noExiste != null) {
            System.out.println("Buscando 99999999: " + noExiste);
        } else {
            System.out.println("Buscando 99999999: no encontrado");
        }

        titulo("7. Actualizar el teléfono de un paciente");
        Paciente p = repo.buscarPorDocumento("70000002");
        p.setTelefono("999888777");
        System.out.println("Actualizado: " + repo.actualizar(p));
        System.out.println(repo.buscarPorDocumento("70000002"));

        titulo("8. Desactivar un paciente (sin borrarlo)");
        System.out.println("Desactivado: " + repo.desactivar("70000001"));
        mostrarLista(repo);

        titulo("9. Respaldo del archivo");
        File copia = repo.respaldar("datos_clinica/respaldo");
        System.out.println("Respaldo creado: " + copia.getPath());
        System.out.println("Existe: " + copia.exists() + " | Tamaño: " + copia.length() + " bytes");

        titulo("10. Manejo de error: archivo que no existe");
        try {
            Scanner lector = new Scanner(new File("datos_clinica/no_existe.txt"));
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo: datos_clinica/no_existe.txt");
        }
    }
}
