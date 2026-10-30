package practicaarchivos;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Paciente de la clínica (versión reducida para la práctica de archivos).
 * Las validaciones se hacen en los setters y lanzan IllegalArgumentException.
 */
public class Paciente {
    private String tipoDocumento;
    private String nroDocumento;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private boolean activo;

    public Paciente(String tipoDocumento, String nroDocumento, String nombres,
                    String apellidos, String fechaNacimiento, String telefono) {
        setTipoDocumento(tipoDocumento);
        setNroDocumento(nroDocumento);
        setNombres(nombres);
        setApellidos(apellidos);
        setFechaNacimiento(fechaNacimiento);
        setTelefono(telefono);
        this.activo = true;
    }

    // ---------- Getters ----------
    public String getTipoDocumento() { return tipoDocumento; }
    public String getNroDocumento() { return nroDocumento; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }

    // ---------- Setters con validación ----------
    public void setTipoDocumento(String tipoDocumento) {
        if (!tipoDocumento.equals("DNI") && !tipoDocumento.equals("CE")) {
            throw new IllegalArgumentException("Error: el tipo de documento debe ser DNI o CE");
        }
        this.tipoDocumento = tipoDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        if (nroDocumento.length() == 0) {
            throw new IllegalArgumentException("Error: el documento no puede estar vacío");
        }
        for (int i = 0; i < nroDocumento.length(); i++) {
            if (!Character.isDigit(nroDocumento.charAt(i))) {
                throw new IllegalArgumentException("Error: el documento solo debe contener números");
            }
        }
        if (tipoDocumento.equals("DNI") && nroDocumento.length() != 8) {
            throw new IllegalArgumentException("Error: el DNI debe tener 8 dígitos");
        }
        this.nroDocumento = nroDocumento;
    }

    public void setNombres(String nombres) {
        validarTexto(nombres, "los nombres");
        this.nombres = nombres.trim();
    }

    public void setApellidos(String apellidos) {
        validarTexto(apellidos, "los apellidos");
        this.apellidos = apellidos.trim();
    }

    public void setFechaNacimiento(String fecha) {
        LocalDate f;
        try {
            f = LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Error: la fecha debe tener el formato AAAA-MM-DD");
        }
        if (f.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Error: la fecha de nacimiento no puede ser futura");
        }
        this.fechaNacimiento = f;
    }

    public void setTelefono(String telefono) {
        if (telefono.length() != 9) {
            throw new IllegalArgumentException("Error: el teléfono debe tener 9 dígitos");
        }
        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                throw new IllegalArgumentException("Error: el teléfono solo debe contener números");
            }
        }
        this.telefono = telefono;
    }

    public void setActivo(boolean activo) { this.activo = activo; }

    private void validarTexto(String texto, String campo) {
        if (texto.trim().length() < 3) {
            throw new IllegalArgumentException("Error: " + campo + " deben tener al menos 3 caracteres");
        }
        for (int i = 0; i < texto.length(); i++) {
            char letra = texto.charAt(i);
            if (!Character.isLetter(letra) && letra != ' ') {
                throw new IllegalArgumentException("Error: " + campo + " solo deben contener letras");
            }
        }
    }

    // ---------- Conversión desde / hacia una línea del archivo ----------
    /** Una línea del archivo: los campos van separados por punto y coma. */
    public String aLinea() {
        return tipoDocumento + ";" + nroDocumento + ";" + nombres + ";" + apellidos + ";"
                + fechaNacimiento + ";" + telefono + ";" + activo;
    }

    public static Paciente desdeLinea(String linea) {
        String[] c = linea.split(";");
        if (c.length != 7) {
            throw new IllegalArgumentException("Error: línea con formato incorrecto");
        }
        Paciente p = new Paciente(c[0], c[1], c[2], c[3], c[4], c[5]);
        p.setActivo(c[6].equals("true"));
        return p;
    }

    public void verDatos() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        String estado = "Inactivo";
        if (activo) {
            estado = "Activo";
        }
        return tipoDocumento + " " + nroDocumento + " | " + nombres + " " + apellidos
                + " | Nac: " + fechaNacimiento + " | Tel: " + telefono + " | " + estado;
    }
}
