package enums;

public enum TypeVol {

    aller("aller"),retour("retour"),aller_retour("aller_retour") ;

    private final String value;

    TypeVol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}