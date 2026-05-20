package no.ngu.nadag;

public record NadagProject(
    String lokalid,
    String prosjektnr,
    String prosjektnavn,
    String omradeGeoJson
) {
}