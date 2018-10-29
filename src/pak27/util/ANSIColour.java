package pak27.util;
/**
 * An enumerator containing all the colours supported by most terminals that support colour.
 *
 * @author Panagiotis karapas
 * @version 1.0
 */
public enum ANSIColour {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    CYAN("\u001B[36m"),
    MAGENTA("\u001b[35m");

    private String code = "";

    ANSIColour(String code){
        this.code = code;
    }

    /**
     * Gets the string that needs to be inserted so the text is coloured.
     * @return the string that needs to be inserted so the text is coloured.
     */
    public String getCode(){
        return code;
    }

}
