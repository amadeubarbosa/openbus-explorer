package busexplorer.utils;

import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.panel.providers.ProviderWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsistencyValidationResult {
  private final HashMap<Integer, IntegrationWrapper> inconsistentIntegrations;
  private final List<OfferWrapper> inconsistentOffers;
  private final List<LoginWrapper> inconsistentLogins;
  private final List<EntityWrapper> inconsistentEntities;
  private final List<AuthorizationWrapper> inconsistentAuthorizations;
  private HashMap<String, ProviderWrapper> inconsistentProviders;
  private HashMap<String, ContractWrapper> inconsistentContracts;

  public ConsistencyValidationResult() {
    this.inconsistentIntegrations = new HashMap<Integer, IntegrationWrapper>();
    this.inconsistentOffers =  new ArrayList<>();
    this.inconsistentLogins = new ArrayList<>();
    this.inconsistentEntities = new ArrayList<>();
    this.inconsistentAuthorizations = new ArrayList<>();
    this.inconsistentProviders = new HashMap<String, ProviderWrapper>();
    this.inconsistentContracts = new HashMap<String, ContractWrapper>();
  }

  public HashMap<Integer, IntegrationWrapper> getInconsistentIntegrations() {
    return inconsistentIntegrations;
  }

  public HashMap<String, ProviderWrapper> getInconsistentProviders() {
    return inconsistentProviders;
  }

  public List<OfferWrapper> getInconsistentOffers() {
    return inconsistentOffers;
  }

  public List<LoginWrapper> getInconsistentLogins() {
    return inconsistentLogins;
  }

  public List<EntityWrapper> getInconsistentEntities() {
    return inconsistentEntities;
  }

  public List<AuthorizationWrapper> getInconsistentAuthorizations() {
    return inconsistentAuthorizations;
  }

  public HashMap<String, ContractWrapper> getInconsistentContracts() {
    return inconsistentContracts;
  }

  public boolean isEmpty() {
    return inconsistentProviders.isEmpty() && inconsistentIntegrations.isEmpty()
      && inconsistentOffers.isEmpty() && inconsistentLogins.isEmpty()
      && inconsistentEntities.isEmpty() && inconsistentAuthorizations.isEmpty()
      && inconsistentContracts.isEmpty();
  }
}
