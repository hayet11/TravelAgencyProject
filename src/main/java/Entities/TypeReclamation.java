package Entities;

public enum TypeReclamation {
    PROBLEMES_TECHNIQUES("Problèmes techniques"),
    CONTENU_INEXACT_OBSOLETE("Contenu inexact ou obsolete"),
    FONCTIONNALITES_MANQUANTES("Fonctionnalités manquantes ou non fonctionnelles"),
    EXPERIENCE_UTILISATEUR_DEFICIENTE("Expérience utilisateur déficiente"),
    SERVICE_CLIENT_INSATISFAISANT("Service client insatisfaisant"),
    PROBLEMES_SECURITE_CONFIDENTIALITE("Problèmes de sécurité ou de confidentialité");

    private final String displayName;

    TypeReclamation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // Convert a String to the corresponding Enum value
    public static TypeReclamation fromString(String text) {
        for (TypeReclamation type : TypeReclamation.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}