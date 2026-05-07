package no.ngu.geojson;

/**
 * EPSG codes for coordinate reference systems.
 */
public enum EpsgCode {
  epsg_4326, // WGS84 Geografisk

  epsg_5971, // EUREF89 UTM sone 31, 2d + NN2000
  epsg_5972, // EUREF89 UTM sone 32, 2d + NN2000
  epsg_5973, // EUREF89 UTM sone 33, 2d + NN2000
  epsg_5974, // EUREF89 UTM sone 34, 2d + NN2000
  epsg_5975, // EUREF89 UTM sone 35, 2d + NN2000
  epsg_5976, // EUREF89 UTM sone 36, 2d + NN2000

  epsg_25831, // ETRS89 / UTM zone 31N
  epsg_25832, // ETRS89 / UTM zone 32N
  epsg_25833, // ETRS89 / UTM zone 33N
  epsg_25834, // ETRS89 / UTM zone 34N
  epsg_25835, // ETRS89 / UTM zone 35N
  epsg_25836, // ETRS89 / UTM zone 36N

  epsg_5105, // ETRS89 / NTM zone 5
  epsg_5106, // ETRS89 / NTM zone 6
  epsg_5107, // ETRS89 / NTM zone 7
  epsg_5108, // ETRS89 / NTM zone 8
  epsg_5109, // ETRS89 / NTM zone 9
  epsg_5110, // ETRS89 / NTM zone 10
  epsg_5111, // ETRS89 / NTM zone 11
  epsg_5112, // ETRS89 / NTM zone 12
  epsg_5113, // ETRS89 / NTM zone 13
  epsg_5114, // ETRS89 / NTM zone 14
  epsg_5115, // ETRS89 / NTM zone 15
  epsg_5116, // ETRS89 / NTM zone 16
  epsg_5117, // ETRS89 / NTM zone 17
  epsg_5118, // ETRS89 / NTM zone 18
  epsg_5119, // ETRS89 / NTM zone 19
  epsg_5120, // ETRS89 / NTM zone 20
  epsg_5121, // ETRS89 / NTM zone 21
  epsg_5122, // ETRS89 / NTM zone 22
  epsg_5123, // ETRS89 / NTM zone 23
  epsg_5124, // ETRS89 / NTM zone 24
  epsg_5125, // ETRS89 / NTM zone 25
  epsg_5126, // ETRS89 / NTM zone 26
  epsg_5127, // ETRS89 / NTM zone 27
  epsg_5128, // ETRS89 / NTM zone 28
  epsg_5129, // ETRS89 / NTM zone 29
  epsg_5130, // ETRS89 / NTM zone 30
  ;

  /**
   * Returns the EPSG code as a CRS code.
   *
   * @return the corresponding CRS code
   */
  public String toCrsCode() {
    return "EPSG:" + toCrsNumber();
  }

  /**
   * Returns the EPSG code as a CRS number, by
   * parsing the digits at the end of the EPSG code string.
   * Returns -1 if no digits are found.
   *
   * @param epsgCode the EPSG code to convert
   * @return the corresponding CRS number, or -1 if no digits are found
   */
  public static int toCrsNumber(String epsgCode) {
    int pos = epsgCode.length() - 1;
    while (pos >= 0 && Character.isDigit(epsgCode.charAt(pos))) {
      pos--;
    }
    var digits = epsgCode.substring(pos + 1);
    return digits.isEmpty() ? -1 : Integer.valueOf(epsgCode.substring(pos + 1));
  }

  /**
   * Returns the EPSG code as a CRS number.
   *
   * @return the corresponding CRS number
   */
  public int toCrsNumber() {
    return toCrsNumber(this.name());
  }

  /**
   * Returns the EPSG code corresponding to a CRS code.
   *
   * @param code the code
   * @return the corresponding EPSG code
   * @throws IllegalArgumentException if the code is unknown
   */
  public static EpsgCode fromString(String code) {
    return fromCrsNumber(toCrsNumber(code));
  }

  /**
   * Returns the EPSG code corresponding to a CRS code.
   *
   * @param code the code
   * @return the corresponding EPSG code
   * @throws IllegalArgumentException if the code is unknown
   */
  public static EpsgCode fromCrsNumber(int code) {
    return valueOf("epsg_" + code);
  }
}