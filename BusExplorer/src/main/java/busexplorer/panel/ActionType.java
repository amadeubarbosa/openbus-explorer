package busexplorer.panel;

/** Constantes que definem os tipos de bot�es que poder�o ser utilizados */
public enum ActionType {
  /** Adi��o */
  ADD,
  /** Edi��o */
  EDIT,
  /** Remo��o */
  REMOVE,
  /** Atualizar */
  REFRESH,
  /** Recarregar */
  RELOAD,
  /** Outros tipos de a��es */
  OTHER,
  /** A��o generica que requer um item selecionado */
  OTHER_SINGLE_SELECTION,
  /** A��o generica que requer pelo menos um item selecionado */
  OTHER_MULTI_SELECTION,
  /** A��o generica que requer multiplos itens selecionados */
  OTHER_ONLY_MULTI_SELECTION;
}