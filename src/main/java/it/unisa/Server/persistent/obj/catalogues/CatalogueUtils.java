package it.unisa.Server.persistent.obj.catalogues;

import java.lang.reflect.Field;
import java.util.List;

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
        Field[] fields = oggettoClass.getDeclaredFields(); // Otteni tutti i campi della classe di appartenenza dell'oggetto
        List<String> excludedFields = List.of(
                "noteaggiuntive", "datascadenzaToken", "trattamento"
        );
        for (Field field : fields) {
            try {
                field.setAccessible(true); // Rendili accessilibi temporaneamente
                Object fieldValue = field.get(oggetto);
                if (excludedFields.contains(field.getName().toLowerCase())) {
                    field.setAccessible(false);
                    continue;
                }
                if (fieldValue == null) {
                    throw new InvalidInputException("Campo: " + field.getName() + " è nullo");
                }
                if (field.getType().equals(String.class) && ((String) fieldValue).isBlank()) {
                    throw new InvalidInputException("Campo: " + field.getName() + " è vuoto");
                }
                field.setAccessible(false); // Rendili di nuovo non accessibili...
            } catch (IllegalAccessException e) {
                field.setAccessible(false); // ...Anche in caso di errore
                e.printStackTrace();
            }

        }

    }
}
