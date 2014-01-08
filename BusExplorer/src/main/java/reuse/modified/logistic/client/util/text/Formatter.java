package reuse.modified.logistic.client.util.text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import tecgraf.javautils.LNG;

/**
 * Classe com um conjunto de métodos úteis para toda a aplicacão tais como
 * formatacão de números e tratamento de datas.
 */
public class Formatter {
  /** Formatação de número decimal para apresentação no idioma corrente */
  private static DecimalFormat showFormat = null;
  /** Formatação de número decimal para edição no idioma corrente */
  private static EditFloatFormat editFormat = null;
  /** Formatacão de data */
  private static SimpleDateFormat sdf = null;
  /** Formatacão de data sem o ano */
  private static SimpleDateFormat noYearDateFormat = null;
  /** Formatação de data sem o dia */
  private static SimpleDateFormat noDayDateFormat = null;
  /** Formatação de data e hora */
  private static SimpleDateFormat dateHourFormat = null;
  /** Formatação de mês e ano */
  private static SimpleDateFormat monthYearFormat = null;
  /** Formatação de mês e ano MMM/YY */
  private static SimpleDateFormat shortMonthYearFormat = null;
  /** Formatação de mês MMM */
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
   * Formatacão de número decimal no idioma corrente para impressão.
   * 
   * @return formatacão de número decimal no idioma corrente.
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
   * Formatacão de número decimal no idioma corrente para impressão.
   * 
   * @param pattern padrão para impressão.
   * 
   * @return formatacão de número decimal no idioma corrente.
   */
  public static DecimalFormat getNumberShowFormat(String pattern) {
    NumberFormat nf = NumberFormat.getNumberInstance(LNG.getLocale());
    ((DecimalFormat) nf).applyPattern(pattern);
    return (DecimalFormat) nf;
  }

  /**
   * Formatacão de número decimal no idioma corrente para impressão.
   * 
   * @return formatacão de número decimal no idioma corrente.
   */
  public static EditFloatFormat getNumberEditFormat() {
    if (editFormat != null) {
      return editFormat;
    }
    editFormat = new EditFloatFormat();
    return editFormat;
  }

  /**
   * Formatacão de data como dd/MM/yyyy.
   * 
   * @return formatacão de data como dd/MM/yyyy.
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
   * Formatacão de data como dd/MM.
   * 
   * @return formatacão de data como dd/MM.
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
   * Formatacão de data como MM/yyyy.
   * 
   * @return formatacão de data como MM/yyyy.
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
   * Informa o nome do mês no idioma corrente dado o seu número-1 (por exemplo:
   * 0=Janeiro, 1=Fevereiro, etc.).
   * 
   * @param month o número do mês -1.
   * 
   * @return o nome do mês.
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
   * Cria um formatador com mês e ano.
   * 
   * @return formatador com mês e ano.
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
   * Cria um formatador com mês e ano com dois dígitos.
   * 
   * @return formatador com mês e ano com dois dígitos.
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
   * Cria um formatador com mês.
   * 
   * @return formatador com mês.
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
