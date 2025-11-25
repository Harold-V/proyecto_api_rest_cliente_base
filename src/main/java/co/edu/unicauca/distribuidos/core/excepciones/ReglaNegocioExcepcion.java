package co.edu.unicauca.distribuidos.core.excepciones;

public class ReglaNegocioExcepcion extends RuntimeException {
    public ReglaNegocioExcepcion(String reglaNegocio) {
        super(reglaNegocio);
    }
}
