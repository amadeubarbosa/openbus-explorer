package busexplorer.utils;

import busexplorer.panel.authorizations.AuthorizationWrapper;
import busexplorer.panel.contracts.ContractWrapper;
import busexplorer.panel.entities.EntityWrapper;
import busexplorer.panel.integrations.IntegrationWrapper;
import busexplorer.panel.logins.LoginWrapper;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.panel.providers.ProviderWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConsistencyValidationResult {
  private final HashMap<Integer, IntegrationWrapper> inconsistentIntegrations;
  private final HashMap<String, ProviderWrapper> inconsistentProviders;
  private final HashMap<String, ContractWrapper> inconsistentContracts;
  private final HashSet<OfferWrapper> inconsistentOffers;
  private final HashSet<LoginWrapper> inconsistentLogins;
  private final HashSet<EntityWrapper> inconsistentEntities;
  private final HashSet<AuthorizationWrapper> inconsistentAuthorizations;

  public ConsistencyValidationResult() {
    this.inconsistentIntegrations = new HashMap<Integer, IntegrationWrapper>();
    this.inconsistentProviders = new HashMap<String, ProviderWrapper>();
    this.inconsistentContracts = new HashMap<String, ContractWrapper>();
    this.inconsistentOffers =  new HashSet<OfferWrapper>();
    this.inconsistentLogins = new HashSet<LoginWrapper>();
    this.inconsistentEntities = new HashSet<EntityWrapper>();
    this.inconsistentAuthorizations = new HashSet<AuthorizationWrapper>();
  }

  public HashMap<Integer, IntegrationWrapper> getInconsistentIntegrations() {
    return inconsistentIntegrations;
  }

  public HashMap<String, ProviderWrapper> getInconsistentProviders() {
    return inconsistentProviders;
  }

  public HashMap<String, ContractWrapper> getInconsistentContracts() {
    return inconsistentContracts;
  }

  public Set<OfferWrapper> getInconsistentOffers() {
    return inconsistentOffers;
  }

  public Set<LoginWrapper> getInconsistentLogins() {
    return inconsistentLogins;
  }

  public Set<EntityWrapper> getInconsistentEntities() {
    return inconsistentEntities;
  }

  public Set<AuthorizationWrapper> getInconsistentAuthorizations() {
    return inconsistentAuthorizations;
  }

  public boolean isEmpty() {
    return inconsistentProviders.isEmpty() && inconsistentIntegrations.isEmpty()
      && inconsistentOffers.isEmpty() && inconsistentLogins.isEmpty()
      && inconsistentEntities.isEmpty() && inconsistentAuthorizations.isEmpty()
      && inconsistentContracts.isEmpty();
  }
}
