package enums;

import javax.swing.plaf.PanelUI;

public enum AlgoritmoRecomendacion {

    COLABORATIVO("Basado en usuarios similares"),
    CONTENIDO("Basado en características del contenido"),
    HIBRIDO("Combinación de ambos");

    //Atributos:
    private String descripcion;

    //Constructor
    AlgoritmoRecomendacion(String descripcion){
        this.descripcion = descripcion;
    }

    //GetterSetter:
    public String getDescripcion(){return this.descripcion;}

    //Metodo toString:

    @Override
    public String toString(){
        return this.name() + " " + this.descripcion;
    }

}
