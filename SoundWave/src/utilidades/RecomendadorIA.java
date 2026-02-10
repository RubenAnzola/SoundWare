package utilidades;

import enums.AlgoritmoRecomendacion;
import excepciones.recomendacion.RecomendacionException;
import interfaces.Recomendador;
import modelo.usuarios.Usuario;

import java.util.ArrayList;
import java.util.HashMap;

public class RecomendadorIA implements Recomendador {

    private static final double UMBRAL_DEFAULT = 0.6; // Umbral de similitud por defecto

    //Atributos:
    private HashMap<String, ArrayList<String>> matrizPreferencias; // Matriz de preferencias de los usuarios (usuario -> lista de contenidos preferidos)
    private HashMap<String, ArrayList<Contenido>> historialCompleto;
    private AlgoritmoRecomendacion algoritmo;
    private double umbralSimilitud;
    private boolean modeloEntrenado;
    private ArrayList<Contenido> catalogoReferencia;


    //CONTRUCTOR:

    public RecomendadorIA() {
        this.matrizPreferencias = new HashMap<>();
        this.historialCompleto = new HashMap<>();
        this.algoritmo = AlgoritmoRecomendacion.COLABORATIVO; // Por defecto
        this.umbralSimilitud = UMBRAL_DEFAULT;
        this.modeloEntrenado = false;
        this.catalogoReferencia = new ArrayList<>();
    }

    public RecomendadorIA(AlgoritmoRecomendacion algoritmo) {
        this.matrizPreferencias = new HashMap<>();
        this.historialCompleto = new HashMap<>();
        this.algoritmo = algoritmo;
        this.umbralSimilitud = UMBRAL_DEFAULT;
        this.modeloEntrenado = false;
        this.catalogoReferencia = new ArrayList<>();
    }

    @Override
    public ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException {
        // Validar que el modelo esté entrenado
        if (!this.modeloEntrenado) {
            throw new RecomendacionException("El modelo no está entrenado. Llama a entrenarModelo() primero.");
        }

        // Validar que el usuario no sea null
        if (usuario == null) {
            throw new RecomendacionException("El usuario no puede ser null.");
        }

        // Obtener historial del usuario
        ArrayList<Contenido> historial = usuario.getHistorial();

        // Validar que el usuario tenga historial
        if (historial == null || historial.isEmpty()) {
            throw new RecomendacionException("El usuario no tiene historial de reproducciones.");
        }

        // Obtener ID del usuario
        String idUsuario = usuario.getId();

        // Obtener preferencias del usuario desde la matriz
        ArrayList<String> preferenciasUsuario = this.matrizPreferencias.get(idUsuario);

        // Si no hay preferencias calculadas, actualizarlas primero
        if (preferenciasUsuario == null) {
            actualizarPreferencias(usuario);
            preferenciasUsuario = this.matrizPreferencias.get(idUsuario);
        }

        // Crear una lista para guardar las recomendaciones
        ArrayList<Contenido> recomendaciones = new ArrayList<>();

        // Recorrer el catalogoReferencia
        for (Contenido contenido : this.catalogoReferencia) {
            // Verificar que el contenido NO esté en el historial
            if (!historial.contains(contenido)) {
                // Calcular la similitud del contenido con las preferencias del usuario
                double similitud = calcularSimilitudContenido(contenido, preferenciasUsuario);

                // Si la similitud es >= umbralSimilitud, agregar a recomendaciones
                if (similitud >= this.umbralSimilitud) {
                    recomendaciones.add(contenido);
                }
            }
        }

        // Limitar las recomendaciones a un máximo de 10
        if (recomendaciones.isEmpty()) {
            return new ArrayList<>(); // Retornar lista vacía si no hay recomendaciones
        }

        // Retornar las primeras 10 recomendaciones (o todas si hay menos de 10)
        return new ArrayList<>(recomendaciones.subList(0, Math.min(recomendaciones.size(), 10)));
    }

    @Override
    public ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException {
        return null;
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Actualiza las preferencias de un usuario basándose en su historial
     */
    public void actualizarPreferencias(Usuario usuario) {
        // TODO: Este método extrae géneros/categorías del historial del usuario
        // y los guarda en matrizPreferencias

        // Por ahora, crear una lista vacía para evitar null
        ArrayList<String> preferencias = new ArrayList<>();
        this.matrizPreferencias.put(usuario.getId(), preferencias);
    }

    /**
     * Calcula la similitud entre un contenido y las preferencias del usuario
     * @return valor entre 0.0 y 1.0
     */
    private double calcularSimilitudContenido(Contenido contenido, ArrayList<String> preferencias) {
        // TODO: Implementar lógica de similitud
        // Por ejemplo: contar cuántos géneros/tags del contenido coinciden con preferencias
        return 0.0; // Por ahora retorna 0
    }

}
