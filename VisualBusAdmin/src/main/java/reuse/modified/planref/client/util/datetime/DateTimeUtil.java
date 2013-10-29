/*
 * $Id$
 */
package reuse.modified.planref.client.util.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;

import reuse.modified.planref.client.util.PlanrefUI;
import reuse.modified.planref.logic.commonservice.DateRange;
import tecgraf.javautils.LNG;

/**
 * @author Tecgraf
 */
public class DateTimeUtil {

  /** Formatador de data no formato dd/mm/aaaa */
  private static SimpleDateFormat dateFormat =
    new SimpleDateFormat("dd/MM/yyyy", LNG.getLocale());

  /**
   * Método de formatação de datas para strings
   * 
   * @param date a data
   * @return o texto.
   */
  static public final String getDateString(final Date date) {
    if (date == null)
      return "--";
    return dateFormat.format(date);
  }

  /**
   * Consulta texto de internacionalização do módulo.
   * 
   * @param tag o tag
   * @return o texto.
   */
  static final private String getString(final String tag) {
    return PlanrefUI.getClassString(DateTimeUtil.class, tag);
  }

  /**
   * Formatador padrão de intervalo de datas.
   * 
   * @param range o intervalo
   * @return o texto
   */
  static public final String getDateRangeString(final DateRange range) {
    final Date start = range.getStart();
    final Date end = range.getEnd();
    if (start == null && end == null) {
      return "--";
    }

    if (end == null) {
      final String FROM = getString("from");
      return FROM + " " + getDateString(start);
    }
    if (start == null) {
      final String TO = getString("to");
      return TO + " " + getDateString(end);
    }
    return getDateString(start) + " <-> " + getDateString(end);
  }
}
