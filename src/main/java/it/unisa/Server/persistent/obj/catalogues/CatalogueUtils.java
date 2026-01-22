package it.unisa.Server.persistent.obj.catalogues;

import java.lang.reflect.Field;

public class CatalogueUtils {

    /**
     * Verifica se le variabili d'istanza di un oggetto sono nulle (o vuote nel caso particolare delle stringhe)
     * oppure no.
     * @param oggetto un oggetto qualsiasi.
     * @param <T> tipo dell'oggetto.
     * @throws InvalidInputException se un field di un oggetto è {@code null} (o vuoto nel caso che il campo sia una stringa)
     */
    public static <T> void checkNull(T oggetto) throws InvalidInputException {
        Class<?> oggettoClass = oggetto.getClass();
        Field[] fields = oggettoClass.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(oggetto);
                if (fieldValue == null) {
                    throw new InvalidInputException("Campo: " + field.getName() + " è nullo");
                }
                if (field.getType().equals(String.class) && ((String) fieldValue).isEmpty()) {
                    throw new InvalidInputException("Campo: " + field.getName() + " è vuoto");
                }
                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                field.setAccessible(false);
                e.printStackTrace();
            }

        }

    }
}
