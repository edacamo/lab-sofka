package com.edacamo.msaccounts.infrastructure.utils;

import org.springframework.stereotype.Component;

@Component
public class Helpers {

    public static final String tagSpace = ".:--------> ";
    public static final String tagSpace2 = "==========> ";
    public static final String tagEnter1 = "\n";
    public static final String tagEnter2 = "\n\n";
    public static final String tagLinesmd = "----------";
    public static final String tagLineslg = "--------------------";
    public static final String tagLinesxl = "------------------------------";
    public static final String tagSeparator = "<*>==========================================================<*>";
    public static final String tagSubIndice = "\t ==========> ";

    // Método para limpiar el mensaje de error
    public static String cleanErrorMessageDeprecated(String mensajeError) {
        // Eliminar las barras invertidas adicionales y restaurar las comillas dobles
        String mensajeLimpio = mensajeError.replaceAll("\\\\", "").replaceAll("\\\"", "\"");
        // Eliminar el salto de línea al final del mensaje
        mensajeLimpio = mensajeLimpio.trim();
        return mensajeLimpio;
    }

    public static String cleanErrorMessage(String mensajeError) {
        // Eliminar las barras invertidas adicionales y restaurar las comillas dobles
        String mensajeLimpio = mensajeError.replaceAll("\\\\", "").replaceAll("\\\"", "\"");

        // Eliminar el salto de línea al final del mensaje
        mensajeLimpio = mensajeLimpio.trim();

        // Eliminar cualquier stack trace que aparezca (si existe un "at" en el mensaje, es probable que sea parte de un stack trace)
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)\\bat\\b.*", "");

        // Eliminar patrones comunes que pueden contener información sensible (e.g., consultas SQL, detalles de base de datos)
        // Se pueden personalizar estos patrones según el tipo de detalle que se desee filtrar
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)select.*from", "[consulta SQL]");
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)insert.*into", "[consulta SQL]");
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)update.*set", "[consulta SQL]");
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)delete.*from", "[consulta SQL]");
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)Table", "[tabla]");
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)column", "[columna]");

        // Evitar que se exponga la información de clases y métodos en los errores
        mensajeLimpio = mensajeLimpio.replaceAll("(?i)com\\.myapp\\..*\\.", "[clase]");

        return mensajeLimpio;
    }

}
