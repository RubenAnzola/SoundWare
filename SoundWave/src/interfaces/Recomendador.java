package interfaces;

import excepciones.recomendacion.RecomendacionException;
import modelo.usuarios.Usuario;

import java.util.ArrayList;
public interface Recomendador {

    ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException;

    ArrayList<Contenido> recomendar(Usuario usuario) throws RecomendacionException;

    ArrayList<Contenido> obtenerSimilares(Contenido contenido) throws RecomendacionException;
}
