package it.unisa.Storage.DAO;

/**
 * Classe per metodi di utilità per gli oggetti DAO.
 */
public final class DaoUtils {

    /**
     * Verifica se la stringa {@code suspect} può essere usata come clausula ORDER BY di una query SQL.
     * Viene incluso il controllo per i parametri asc e desc di ORDER BY.
     * @param whitelist	{@code String[]} - array di tutti gli attributi della tabella
     * @param suspect {@code String} - stringa da controllare
     * @return {@code true} se la stringa suspect può essere usata in una query SQL, {@code false} altrimenti
     */
    public static boolean checkWhitelist(String[] whitelist, String suspect) {
        if(whitelist == null) return false;
        System.out.println("Sono dentro");
        boolean flag = false;

        for (String w : whitelist){
            if(suspect.trim().equalsIgnoreCase(w + " asc") || suspect.equalsIgnoreCase(w + " desc")) {
                flag = true;
                break;
            }
        }

        return flag;
    }
}
