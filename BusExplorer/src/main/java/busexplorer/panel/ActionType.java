package busexplorer.panel;

/** Constantes que definem os tipos de botões que poderão ser utilizados */
public enum ActionType {
  /** Adição */
  ADD,
  /** Edição */
  EDIT,
  /** Remoção */
  REMOVE,
  /** Atualizar */
  REFRESH,
  /** Recarregar */
  RELOAD,
  /** Outros tipos de ações */
  OTHER,
  /** Ação generica que requer um item selecionado */
  OTHER_SINGLE_SELECTION,
  /** Ação generica que requer pelo menos um item selecionado */
  OTHER_MULTI_SELECTION,
  /** Ação generica que requer multiplos itens selecionados */
  OTHER_ONLY_MULTI_SELECTION;
}