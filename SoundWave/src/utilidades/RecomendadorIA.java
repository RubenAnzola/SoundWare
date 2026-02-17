package utilidades;

import enums.AlgoritmoRecomendacion;
import excepciones.recomendacion.HistorialVacioException;
import excepciones.recomendacion.ModeloNoEntrenadoException;
import excepciones.recomendacion.RecomendacionException;
import interfaces.Recomendador;
import modelo.contenido.Contenido;
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
        // Primero verifico que el modelo esté entrenado, sino no puedo recomendar nada
        if (!this.modeloEntrenado) {
            throw new ModeloNoEntrenadoException("El modelo no está entrenado. Llama a entrenarModelo() primero.");
        }

        // Me aseguro que me pasaron un usuario válido
        if (usuario == null) {
            throw new RecomendacionException("El usuario no puede ser null.");
        }

        // Obtengo el historial del usuario para saber qué ha escuchado
        ArrayList<Contenido> historial = usuario.getHistorial();

        // Si no tiene historial, no puedo recomendarle nada
        if (historial == null || historial.isEmpty()) {
            throw new HistorialVacioException("El usuario no tiene historial de reproducciones.");
        }

        // Obtengo el ID del usuario para buscar sus preferencias
        String idUsuario = usuario.getId();

        // Busco las preferencias del usuario en mi matriz
        ArrayList<String> preferenciasUsuario = this.matrizPreferencias.get(idUsuario);

        // Si no tengo sus preferencias guardadas, las calculo ahora
        if (preferenciasUsuario == null) {
            actualizarPreferencias(usuario);
            preferenciasUsuario = this.matrizPreferencias.get(idUsuario);
        }

        // Creo una lista donde voy a guardar las recomendaciones
        ArrayList<Contenido> recomendaciones = new ArrayList<>();

        // Reviso todo el catálogo para ver qué le puede gustar
        for (Contenido contenido : this.catalogoReferencia) {
            // Solo recomiendo cosas que NO ha escuchado
            if (!historial.contains(contenido)) {
                // Calculo qué tan parecido es este contenido a sus gustos
                double similitud = calcularSimilitudContenido(contenido, preferenciasUsuario);

                // Si la similitud supera mi umbral, lo agrego a las recomendaciones
                if (similitud >= this.umbralSimilitud) {
                    recomendaciones.add(contenido);
                }
            }
        }

        // Si no encontré nada que recomendar, devuelvo lista vacía
        if (recomendaciones.isEmpty()) {
            return new ArrayList<>();
        }

        // Devuelvo máximo 10 recomendaciones (o todas si hay menos de 10)
        return new ArrayList<>(recomendaciones.subList(0, Math.min(recomendaciones.size(), 10)));
    }

    @Override
    public ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException {
        if (contenido == null) {
            throw new RecomendacionException("El contenido no puede ser null");
        }

        ArrayList<Contenido> similares = new ArrayList<>();
        ArrayList<String> tagsContenido = contenido.getTags();

        // Buscar contenidos similares en el catálogo
        for (Contenido item : catalogoReferencia) {
            if (!item.equals(contenido)) {
                // Calcular similitud basada en tags
                int coincidencias = 0;
                for (String tag : item.getTags()) {
                    if (tagsContenido.contains(tag)) {
                        coincidencias++;
                    }
                }

                // Si hay al menos 1 coincidencia, agregarlo
                if (coincidencias > 0) {
                    similares.add(item);
                }
            }
        }

        // Ordenar por número de reproducciones (más populares primero)
        similares.sort((c1, c2) -> Integer.compare(c2.getReproducciones(), c1.getReproducciones()));

        // Retornar máximo 10
        return new ArrayList<>(similares.subList(0, Math.min(similares.size(), 10)));
    }

    // ========== MÉTODOS PROPIOS ==========

    public void entrenarModelo(ArrayList<Usuario> usuarios) {
        // Si no me pasaron usuarios o la lista está vacía, no hago nada
        if (usuarios == null || usuarios.isEmpty()) {
            return;
        }

        // Limpio lo que tenía guardado antes para empezar de cero
        matrizPreferencias.clear();
        historialCompleto.clear();

        // Proceso cada usuario para aprender sus gustos
        for (Usuario usuario : usuarios) {
            // Guardo una copia de su historial completo
            historialCompleto.put(usuario.getId(), new ArrayList<>(usuario.getHistorial()));

            // Calculo y guardo sus preferencias basadas en lo que ha escuchado
            actualizarPreferencias(usuario);
        }

        // Marco que ya entrené el modelo y está listo para usar
        modeloEntrenado = true;
    }

    public void entrenarModelo(ArrayList<Usuario> usuarios, ArrayList<Contenido> catalogo) {
        // Primero guardo el catálogo de referencia
        setCatalogoReferencia(catalogo);

        // Luego entreno con los usuarios
        entrenarModelo(usuarios);
    }

    public double calcularSimilitud(Usuario u1, Usuario u2) {
        // Si alguno es null, no hay similitud
        if (u1 == null || u2 == null) {
            return 0.0;
        }

        // Obtengo las preferencias de ambos usuarios
        ArrayList<String> pref1 = matrizPreferencias.get(u1.getId());
        ArrayList<String> pref2 = matrizPreferencias.get(u2.getId());

        // Si no tengo preferencias de alguno, tampoco hay similitud
        if (pref1 == null || pref2 == null || pref1.isEmpty() || pref2.isEmpty()) {
            return 0.0;
        }

        // Cuento cuántas preferencias tienen en común
        int coincidencias = 0;
        for (String pref : pref1) {
            if (pref2.contains(pref)) {
                coincidencias++;
            }
        }

        // Calculo la similitud: coincidencias / total único
        // (Índice de Jaccard: intersección / unión)
        int totalUnico = pref1.size() + pref2.size() - coincidencias;
        return totalUnico > 0 ? (double) coincidencias / totalUnico : 0.0;
    }

    public void actualizarPreferencias(Usuario usuario) {
        // Si no hay usuario, no hago nada
        if (usuario == null) {
            return;
        }

        // Obtengo su historial de reproducciones
        ArrayList<Contenido> historial = usuario.getHistorial();

        // Si no tiene historial, guardo preferencias vacías
        if (historial == null || historial.isEmpty()) {
            matrizPreferencias.put(usuario.getId(), new ArrayList<>());
            return;
        }

        // Voy a extraer todos los tags únicos de lo que ha escuchado
        ArrayList<String> preferencias = new ArrayList<>();

        // Recorro todo su historial
        for (Contenido contenido : historial) {
            // Por cada contenido, saco sus tags
            for (String tag : contenido.getTags()) {
                // Solo agrego el tag si no lo tengo ya
                if (!preferencias.contains(tag)) {
                    preferencias.add(tag);
                }
            }
        }

        // Guardo las preferencias del usuario en mi matriz
        matrizPreferencias.put(usuario.getId(), preferencias);
    }

    public HashMap<String, Integer> obtenerGenerosPopulares() {
        HashMap<String, Integer> popularidad = new HashMap<>();

        // Contar frecuencia de cada preferencia
        for (ArrayList<String> preferencias : matrizPreferencias.values()) {
            for (String pref : preferencias) {
                popularidad.put(pref, popularidad.getOrDefault(pref, 0) + 1);
            }
        }

        return popularidad;
    }

    /**
     * Calcula la similitud entre un contenido y las preferencias del usuario
     * @return valor entre 0.0 y 1.0
     */
    private double calcularSimilitudContenido(Contenido contenido, ArrayList<String> preferencias) {
        if (contenido == null || preferencias == null || preferencias.isEmpty()) {
            return 0.0;
        }

        ArrayList<String> tagsContenido = contenido.getTags();
        if (tagsContenido.isEmpty()) {
            return 0.0;
        }

        // Contar coincidencias
        int coincidencias = 0;
        for (String tag : tagsContenido) {
            if (preferencias.contains(tag)) {
                coincidencias++;
            }
        }

        // Similitud = coincidencias / total de tags del contenido
        return (double) coincidencias / tagsContenido.size();
    }

    // ========== GETTERS Y SETTERS ==========

    public AlgoritmoRecomendacion getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(AlgoritmoRecomendacion algoritmo) {
        this.algoritmo = algoritmo;
    }

    public double getUmbralSimilitud() {
        return umbralSimilitud;
    }

    public void setUmbralSimilitud(double umbralSimilitud) {
        this.umbralSimilitud = umbralSimilitud;
    }

    public boolean isModeloEntrenado() {
        return modeloEntrenado;
    }

    public HashMap<String, ArrayList<String>> getMatrizPreferencias() {
        HashMap<String, ArrayList<String>> copia = new HashMap<>();
        for (String key : matrizPreferencias.keySet()) {
            copia.put(key, new ArrayList<>(matrizPreferencias.get(key)));
        }
        return copia;
    }

    public void setCatalogoReferencia(ArrayList<Contenido> catalogo) {
        if (catalogo != null) {
            this.catalogoReferencia = new ArrayList<>(catalogo);
        }
    }
}
