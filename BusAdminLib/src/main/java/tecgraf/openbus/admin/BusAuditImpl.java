package tecgraf.openbus.admin;

import org.omg.CORBA.Object;

import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.Connection;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.UnauthorizedOperation;
import tecgraf.openbus.core.v2_1.services.admin.v1_1.AuditConfiguration;
import tecgraf.openbus.core.v2_1.services.admin.v1_1.AuditConfigurationHelper;
import tecgraf.openbus.core.v2_1.services.admin.v1_1.NameValueString;
import tecgraf.openbus.exception.CryptographyException;
import tecgraf.openbus.security.Cryptography;

import scs.core.IComponent;
import scs.core.IComponentHelper;

public class BusAuditImpl implements BusAuditFacade {

  private AuditConfiguration audit = null;
  private IComponent bus;
  private Connection conn;

  public BusAuditImpl(Object reference, Connection conn) {
    this.conn = conn;
    this.bus = IComponentHelper.narrow(reference);
    org.omg.CORBA.Object auditObj = this.bus.getFacet(AuditConfigurationHelper.id());
    if (auditObj != null) {
      this.audit = AuditConfigurationHelper.narrow(auditObj);
    }
  }

  @Override
  public boolean isAuditCapable() {
    return (audit != null);
  }

  /**
   * Garante que se não há referência remota para o serviço será lançada uma {@link IncompatibleBus}.
   * @throws IncompatibleBus caso a referência
   */
  private void assertAuditCapable() throws IncompatibleBus {
    if (!isAuditCapable()) {
      throw new IncompatibleBus(LNG.get("IncompatibleBus.error.auditconfiguration"));
    }
  }

  @Override
  public boolean getAuditEnabled() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditEnabled();
  }

  @Override
  public void setAuditEnabled(boolean b) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditEnabled(b);
  }

  @Override
  public String getAuditHttpProxy() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditHttpProxy();
  }

  @Override
  public void setAuditHttpProxy(String s) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditHttpProxy(s);
  }

  @Override
  public byte[] getAuditHttpAuth() throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    Cryptography crypto = Cryptography.getInstance();
    byte[] bytes = this.audit.getAuditHttpAuth();
    try {
      return crypto.decrypt(bytes, conn.privateKey());
    }
    catch (CryptographyException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setAuditHttpAuth(byte[] bytes) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    Cryptography crypto = Cryptography.getInstance();
    try {
      this.audit.setAuditHttpAuth(crypto.encrypt(bytes, conn.busPublicKey()));
    }
    catch (CryptographyException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getAuditServiceURL() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditServiceURL();
  }

  @Override
  public void setAuditServiceURL(String s) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditServiceURL(s);
  }

  @Override
  public int getAuditFIFOLimit() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditFIFOLimit();
  }

  @Override
  public void setAuditFIFOLimit(int i) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditFIFOLimit(i);
  }

  @Override
  public int getAuditFIFOLength() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditFIFOLength();
  }

  @Override
  public boolean getAuditDiscardOnExit() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditDiscardOnExit();
  }

  @Override
  public void setAuditDiscardOnExit(boolean b) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditDiscardOnExit(b);
  }

  @Override
  public int getAuditPublishingTasks() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditPublishingTasks();
  }

  @Override
  public void setAuditPublishingTasks(int i) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditPublishingTasks(i);
  }

  @Override
  public double getAuditPublishingRetryTimeout() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditPublishingRetryTimeout();
  }

  @Override
  public void setAuditPublishingRetryTimeout(double v) throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditPublishingRetryTimeout(v);
  }

  @Override
  public void setAuditEventTemplate(String name, String value)
  throws IncompatibleBus, ServiceFailure, UnauthorizedOperation {
    assertAuditCapable();
    this.audit.setAuditEventTemplate(name, value);
  }

  @Override
  public NameValueString[] getAuditEventTemplate() throws IncompatibleBus, ServiceFailure {
    assertAuditCapable();
    return this.audit.getAuditEventTemplate();
  }
}
