export function getCategoryName(category) {
    switch (category) {
        case "KLASSEMENTSRENNER":
            return "Klassementsrenner"
        case "KLASSIEKE_RENNER":
            return "Klassieke renner"
        case "SPRINTER":
            return "Sprinter"
        case "KLIMMER":
            return "Klimmer"
        case "KNECHT":
            return "Knecht"
        case "TIJDRIJDER":
            return "Tijdrijder"
        case "AANVALLER":
            return "Aanvaller"
        default:
            return "Onbekend"
    }
}