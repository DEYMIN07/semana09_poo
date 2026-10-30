package practicaarchivos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Maneja el archivo de texto donde se guardan los pacientes.
 * Un paciente por línea, con los campos separados por punto y coma.
 */
public class ArchivoPacientes {
    private File archivo;

    public ArchivoPacientes(String ruta) {
        archivo = new File(ruta);
        File carpeta = archivo.getParentFile();
        try {
            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();                 // crea la carpeta si no existe
            }
            if (!archivo.exists()) {
                archivo.createNewFile();          // crea el archivo vacío
            }
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }

    /** Agrega un paciente al final del archivo. */
    public void guardar(Paciente p) {
        if (buscarPorDocumento(p.getNroDocumento()) != null) {
            throw new IllegalArgumentException(
                    "Error: ya existe un paciente con el documento " + p.getNroDocumento());
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true)); // true = agregar al final
            bw.write(p.aLinea());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    /** Lee todo el archivo con Scanner y devuelve los pacientes en una lista. */
    public ArrayList<Paciente> listar() {
        ArrayList<Paciente> lista = new ArrayList<>();
        try {
            Scanner lector = new Scanner(archivo);
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();
                if (linea.trim().length() > 0) {
                    lista.add(Paciente.desdeLinea(linea));
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: no se encontró el archivo " + archivo.getPath());
        }
        return lista;
    }

    public Paciente buscarPorDocumento(String documento) {
        for (Paciente p : listar()) {
            if (p.getNroDocumento().equals(documento)) {
                return p;
            }
        }
        return null;
    }

    /** Reemplaza los datos de un paciente existente. */
    public boolean actualizar(Paciente nuevo) {
        ArrayList<Paciente> lista = listar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNroDocumento().equals(nuevo.getNroDocumento())) {
                lista.set(i, nuevo);
                reescribir(lista);
                return true;
            }
        }
        return false;
    }

    /** Baja lógica: el paciente se marca como inactivo, no se borra. */
    public boolean desactivar(String documento) {
        ArrayList<Paciente> lista = listar();
        for (Paciente p : lista) {
            if (p.getNroDocumento().equals(documento)) {
                p.setActivo(false);
                reescribir(lista);
                return true;
            }
        }
        return false;
    }

    /** Escribe la lista en un archivo temporal, borra el original y renombra el temporal. */
    private void reescribir(ArrayList<Paciente> lista) {
        File temporal = new File(archivo.getPath() + ".tmp");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(temporal));
            for (Paciente p : lista) {
                bw.write(p.aLinea());
                bw.newLine();
            }
            bw.close();
            archivo.delete();
            temporal.renameTo(archivo);
        } catch (IOException e) {
            System.out.println("Error al reescribir: " + e.getMessage());
        }
    }

    /** Copia el archivo, línea por línea, a una carpeta de respaldo. */
    public File respaldar(String carpetaRespaldo) {
        File carpeta = new File(carpetaRespaldo);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        File copia = new File(carpeta, archivo.getName() + ".bak");
        try {
            Scanner lector = new Scanner(archivo);
            BufferedWriter bw = new BufferedWriter(new FileWriter(copia));
            while (lector.hasNextLine()) {
                bw.write(lector.nextLine());
                bw.newLine();
            }
            lector.close();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error al respaldar: " + e.getMessage());
        }
        return copia;
    }

    public File getArchivo() {
        return archivo;
    }
}
