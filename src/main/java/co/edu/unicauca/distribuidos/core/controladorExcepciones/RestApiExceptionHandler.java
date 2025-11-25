package co.edu.unicauca.distribuidos.core.controladorExcepciones;

import co.edu.unicauca.distribuidos.core.fachadaServices.DTO.ErrorDTO;
import co.edu.unicauca.distribuidos.core.excepciones.EntidadNoExisteException;
import co.edu.unicauca.distribuidos.core.excepciones.ReglaNegocioExcepcion;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(ReglaNegocioExcepcion.class)
    public ResponseEntity<ErrorDTO> handleReglaNegocioException(
            ReglaNegocioExcepcion ex, WebRequest request) {

        ErrorDTO errorDTO = new ErrorDTO(
                "GC-0004",
                "GC-0004 Violación a regla de negocio: " + ex.getMessage(),
                400,
                request.getDescription(false).replace("uri=", ""),
                "POST");

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntidadNoExisteException.class)
    public ResponseEntity<ErrorDTO> handleEntidadNoExisteException(
            EntidadNoExisteException ex, WebRequest request) {

        ErrorDTO errorDTO = new ErrorDTO(
                "GC-0003",
                "Entidad no encontrada, " + ex.getMessage(),
                404,
                request.getDescription(false).replace("uri=", ""),
                "PUT");

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errores.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }
}
