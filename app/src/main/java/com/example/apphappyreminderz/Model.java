package com.example.apphappyreminderz;

public class Model {

    private int id;
    private String apellido;
    private String nombre;
    private String telefono;
    private String fechaNac;
    private String edad;
    private String mensaje;
    private String fechaNot;
    private String tiempo;
    private byte[] image;

    public Model(int id, String apellido, String nombre, String telefono, String fechaNac, String edad, String mensaje, String fechaNot, String tiempo, byte[] image) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNac = fechaNac;
        this.edad = edad;
        this.mensaje = mensaje;
        this.fechaNot = fechaNot;
        this.tiempo = tiempo;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFechaNot() {
        return fechaNot;
    }

    public void setFechaNot(String fechaNot) {
        this.fechaNot = fechaNot;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
