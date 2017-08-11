package busexplorer.utils;

import busexplorer.Application;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BusQuery {
  final private String expression;
  private final ScriptEngine engine;

  public BusQuery(String str) throws ScriptException {
    this.expression = str == null ? "" : str;
    ScriptEngineManager manager = new ScriptEngineManager();
    engine = manager.getEngineByName("nashorn");
    engine.eval("var openbus = {entity: {id: ''} }; ");
  }

  public boolean isValid() throws ScriptException {
    if (expression != null && !expression.isEmpty()
      && Pattern.compile("[^!=]+=[^=]+").matcher(expression).find()) {
      return false;
    }
    engine.eval(expression);
    return true;
  }

  public Map<RegisteredEntityDesc, List<String>> filterAuthorizations() throws ScriptException, ServiceFailure {
    engine.put("auths", Application.login().admin.getAuthorizations());
    engine.put("expression", expression);
    engine.eval("var found = new java.util.HashMap(); " +
      "auths.forEach(function(key, value) {" +
      "  openbus.entity.id = key.id;" +
      "  var test = function() { return new Function(\"return \"+expression)(); };" +
      "  if (test()) {" +
      //"    print(\"[DEBUG] found:\",key, value);" +
      "    found.put(key,value);" +
      "  } " +
      "});");
    return (Map<RegisteredEntityDesc, List<String>>) engine.get("found");
  }

  public ArrayList<ServiceOfferDesc> filterOffers() throws ScriptException, ServiceFailure {
    engine.put("offers", Application.login().admin.getOffers().toArray());
    engine.put("expression", expression);
    //engine.eval("print('[DEBUG] expression: ', expression);");
    //engine.eval("print('[DEBUG] offers: ', offers.length);");
    engine.eval("var found = new java.util.ArrayList(); " +
      "for (i =0; i < offers.length; i++) {" +
      "  var offer = offers[i];" +
      "  for (j=0; j<offer.properties.length; j++) {" +
      "    var property = offer.properties[j]; " +
      "    if (property.name == \"openbus.offer.entity\") {" +
      "      openbus.entity.id = property.value;" +
      //"      print(\"[DEBUG] openbus.entity.id: \", openbus.entity.id);" +
      "      var test = function() { return new Function(\"return \"+expression)(); };" +
      "      if (test()) {" +
      //"        print(\"[DEBUG] found:\",property.name, property.value);" +
      "        found.add(offer);" +
      "      } " +
      "    }" +
      "  }" +
      "};");
    return (ArrayList<ServiceOfferDesc>) engine.get("found");
  }

  public ArrayList<RegisteredEntityDesc> filterEntities() throws ScriptException, ServiceFailure {
    engine.put("entities", Application.login().admin.getEntities());
    engine.put("expression", expression);
    engine.eval("var found = new java.util.ArrayList(); " +
      "for (i =0; i < entities.length; i++) {" +
      "  openbus.entity.id = entities[i].id;" +
      //"  print(\"[DEBUG] openbus.entity.id: \", openbus.entity.id);" +
      "  var test = function() { return new Function(\"return \"+expression)(); };" +
      "  if (test()) {" +
      //"   print(\"[DEBUG] found:\",entities[i].id, entities[i].name);" +
      "    found.add(entities[i]);" +
      "  } " +
      "};");
    return (ArrayList<RegisteredEntityDesc>) engine.get("found");
  }
}
