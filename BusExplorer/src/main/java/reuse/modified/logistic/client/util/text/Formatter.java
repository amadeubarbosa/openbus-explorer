package reuse.modified.logistic.client.util.text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import tecgraf.javautils.LNG;

/**
 * Classe com um conjunto de m�todos �teis para toda a aplicac�o tais como
 * formatac�o de n�meros e tratamento de datas.
 */
public class Formatter {
  /** Formata��o de n�mero decimal para apresenta��o no idioma corrente */
  private static DecimalFormat showFormat = null;
  /** Formata��o de n�mero decimal para edi��o no idioma corrente */
  private static EditFloatFormat editFormat = null;
  /** Formatac�o de data */
  private static SimpleDateFormat sdf = null;
  /** Formatac�o de data sem o ano */
  private static SimpleDateFormat noYearDateFormat = null;
  /** Formata��o de data sem o dia */
  private static SimpleDateFormat noDayDateFormat = null;
  /** Formata��o de data e hora */
  private static SimpleDateFormat dateHourFormat = null;
  /** Formata��o de m�s e ano */
  private static SimpleDateFormat monthYearFormat = null;
  /** Formata��o de m�s e ano MMM/YY */
  private static SimpleDateFormat shortMonthYearFormat = null;
  /** Formata��o de m�s MMM */
  private static SimpleDateFormat shortMonthFormat = null;
  /** nomes dos meses do ano no idioma corrente. */
  private static String[] MONTHS_OF_YEAR = new String[] {
      LNG.get("Formatter.january"), LNG.get("Formatter.february"),
      LNG.get("Formatter.march"), LNG.get("Formatter.april"),
      LNG.get("Formatter.may"), LNG.get("Formatter.june"),
      LNG.get("Formatter.july"), LNG.get("Formatter.august"),
      LNG.get("Formatter.september"), LNG.get("Formatter.october"),
      LNG.get("Formatter.november"), LNG.get("Formatter.december") };

  /**
   * Formatac�o de n�mero decimal no idioma corrente para impress�o.
   * 
   * @return formatac�o de n�mero decimal no idioma corrente.
   */
  public static DecimalFormat getNumberShowFormat() {
    if (showFormat != null) {
      return showFormat;
    }
    NumberFormat nf = NumberFormat.getNumberInstance(LNG.getLocale());
    showFormat = (DecimalFormat) nf;
    showFormat.applyPattern("0.0");
    return showFormat;
  }

  /**
   * Formatac�o de n�mero decimal no idioma corrente para impress�o.
   * 
   * @param pattern padr�o para impress�o.
   * 
   * @return formatac�o de n�mero decimal no idioma corrente.
   */
  public static DecimalFormat getNumberShowFormat(String pattern) {
    NumberFormat nf = NumberFormat.getNumberInstance(LNG.getLocale());
    ((DecimalFormat) nf).applyPattern(pattern);
    return (DecimalFormat) nf;
  }

  /**
   * Formatac�o de n�mero decimal no idioma corrente para impress�o.
   * 
   * @return formatac�o de n�mero decimal no idioma corrente.
   */
  public static EditFloatFormat getNumberEditFormat() {
    if (editFormat != null) {
      return editFormat;
    }
    editFormat = new EditFloatFormat();
    return editFormat;
  }

  /**
   * Formatac�o de data como dd/MM/yyyy.
   * 
   * @return formatac�o de data como dd/MM/yyyy.
   */
  public static SimpleDateFormat getDateFormat() {
    if (sdf != null) {
      return sdf;
    }
    sdf = new SimpleDateFormat("dd/MM/yyyy", LNG.getLocale());
    sdf.setLenient(false);
    return sdf;
  }

  /**
   * Formatac�o de data como dd/MM.
   * 
   * @return formatac�o de data como dd/MM.
   */
  public static SimpleDateFormat getNoYearDateFormat() {
    if (noYearDateFormat != null) {
      return noYearDateFormat;
    }
    noYearDateFormat = new SimpleDateFormat("dd/MM", LNG.getLocale());
    noYearDateFormat.setLenient(false);
    return noYearDateFormat;
  }

  /**
   * Formatac�o de data como MM/yyyy.
   * 
   * @return formatac�o de data como MM/yyyy.
   */
  public static SimpleDateFormat getNoDayDateFormat() {
    if (noDayDateFormat != null) {
      return noDayDateFormat;
    }
    noDayDateFormat = new SimpleDateFormat("MM/yyyy", LNG.getLocale());
    noDayDateFormat.setLenient(false);
    return noDayDateFormat;
  }

  /**
   * Informa o nome do m�s no idioma corrente dado o seu n�mero-1 (por exemplo:
   * 0=Janeiro, 1=Fevereiro, etc.).
   * 
   * @param month o n�mero do m�s -1.
   * 
   * @return o nome do m�s.
   */
  public static String getMonth(int month) {
    return MONTHS_OF_YEAR[month];
  }

  /**
   * Informa os nomes dos meses do ano no idioma corrente.
   * 
   * @return array com os nomes dos meses do ano no idioma corrente.
   */
  public static String[] getMonths() {
    return MONTHS_OF_YEAR;
  }

  /**
   * Cria um formatador com data e hora.
   * 
   * @return formatador com data e hora.
   */
  public static DateFormat getDateHourFormat() {
    if (dateHourFormat != null) {
      return dateHourFormat;
    }
    dateHourFormat =
      new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", LNG.getLocale());
    dateHourFormat.setLenient(false);
    return dateHourFormat;
  }

  /**
   * Cria um formatador com m�s e ano.
   * 
   * @return formatador com m�s e ano.
   */
  public static SimpleDateFormat getMonthYearFormat() {
    if (monthYearFormat != null) {
      return monthYearFormat;
    }
    monthYearFormat = new SimpleDateFormat("MMM/yyyy", LNG.getLocale());
    monthYearFormat.setLenient(false);
    return monthYearFormat;
  }

  /**
   * Cria um formatador com m�s e ano com dois d�gitos.
   * 
   * @return formatador com m�s e ano com dois d�gitos.
   */
  public static SimpleDateFormat getShortMonthYearFormat() {
    if (shortMonthYearFormat != null) {
      return shortMonthYearFormat;
    }
    shortMonthYearFormat = new SimpleDateFormat("MMM/yy", LNG.getLocale());
    shortMonthYearFormat.setLenient(false);
    return shortMonthYearFormat;
  }

  /**
   * Cria um formatador com m�s.
   * 
   * @return formatador com m�s.
   */
  public static SimpleDateFormat getShortMonthFormat() {
    if (shortMonthFormat != null) {
      return shortMonthFormat;
    }
    shortMonthFormat = new SimpleDateFormat("MMM", LNG.getLocale());
    shortMonthFormat.setLenient(false);
    return shortMonthFormat;
  }

}
