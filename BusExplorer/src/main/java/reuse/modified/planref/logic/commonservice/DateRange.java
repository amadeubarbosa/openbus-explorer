package reuse.modified.planref.logic.commonservice;

import java.util.Date;

/**
 * Classe auxiliar de intervalo de datas.
 * 
 * @author Tecgraf
 */
public class DateRange extends Range<Date> {

  /**
   * Construtor padrão com intervalo fechado.
   * 
   * @param startDate data inicial
   * @param endDate data final
   */
  public DateRange(final Date startDate, final Date endDate) {
    super(startDate, endDate);
  }

  /**
   * Cosntrutor padrão com intervalo aberto (data incial em diante).
   * 
   * @param startDate data inicial.
   */
  public DateRange(final Date startDate) {
    super(startDate, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  final public String toString() {
    return getStart().toString() + " <-> " + getEnd().toString();
  }

}
