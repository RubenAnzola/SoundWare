package enums;

public enum TipoAnuncio {

    AUDIO(15, 0.05),
    BANNER(0, 0.02),
    VIDEO(30, 0.10);

    //Atributos:
    private int duracionSegundos;
    private double costoPorImpresion;

    //Constructor:
    TipoAnuncio(int duracionSegundos, double costoPorImpresion){
        this.duracionSegundos = duracionSegundos;
        this.costoPorImpresion = costoPorImpresion;
    }

    //GetterSetter:
    public int getDuracionSegundos(){return this.duracionSegundos;}
    public double getCostoPorImpresion(){return this.costoPorImpresion;}

    //Metodo toString:
    @Override
    public String toString(){
        return  this.name() + " (" + this.duracionSegundos + " s";
    }
}
