package com.das.das_proyecto1;
/*Esta clase representa cada día. Cada día contiene 5 comidas:
* -Desayuno
* -Almuerzo
* -Comida
* -Merienda
* -Cena
* */
public class Dia {
    private int id; // Primary Key para SQLite
    private String nombreDia;
    private String desayuno;
    private String almuerzo;
    private String comida;
    private String merienda;
    private String cena;
    public Dia() {}//vacío para operaciones desde la bd
    public Dia(int id, String nombreDia, String desayuno, String almuerzo, String comida, String merienda, String cena) {
        this.id = id;
        this.nombreDia = nombreDia;
        this.desayuno = desayuno;
        this.almuerzo = almuerzo;
        this.comida = comida;
        this.merienda = merienda;
        this.cena = cena;
    }

    // Getters y Setters (Necesarios para que el Adapter y el DAO lean/escriban datos)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreDia() { return nombreDia; }
    public void setNombreDia(String nombreDia) { this.nombreDia = nombreDia; }

    public String getDesayuno() { return desayuno; }
    public void setDesayuno(String desayuno) { this.desayuno = desayuno; }

    public String getAlmuerzo() { return almuerzo; }
    public void setAlmuerzo(String almuerzo) { this.almuerzo = almuerzo; }

    public String getComida() { return comida; }
    public void setComida(String comida) { this.comida = comida; }

    public String getMerienda() { return merienda; }
    public void setMerienda(String merienda) { this.merienda = merienda; }

    public String getCena() { return cena; }
    public void setCena(String cena) { this.cena = cena; }
}
