package busexplorer.utils.preferences;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationPreferencesTest {

  static final String APPNAME = "appname";

  static ApplicationPreferences preferences;

  @BeforeAll
  public static void before() {
    preferences = new ApplicationPreferences(APPNAME);
  }

  @AfterAll
  public static void cleanup() {
    preferences.clear();
  }

  @Test
  public void testWriteReadRoot() {
    preferences.write("nome1", "valor1");
    preferences.write("nome2", "valor2");
    preferences.write("nome3", "valor3");
    assertEquals("valor1", preferences.read("nome1"));
    assertEquals("valor2", preferences.read("nome2"));
    assertEquals("valor3", preferences.read("nome3"));
  }

  @Test
  public void testWriteReadPath1() {
    String path1 = "item1";
    preferences.write(path1, "nome1", "item1_valor1");
    preferences.write(path1, "nome2", "item1_valor2");
    preferences.write(path1, "nome3", "item1_valor3");
    assertEquals("item1_valor1", preferences.read(path1, "nome1"));
    assertEquals("item1_valor2", preferences.read(path1, "nome2"));
    assertEquals("item1_valor3", preferences.read(path1, "nome3"));
  }

  @Test
  public void testWriteReadPath2() {
    String path1 = "item1/child1";
    preferences.write(path1, "nome1", "item1_child1_valor1");
    preferences.write(path1, "nome2", "item1_child1_valor2");
    preferences.write(path1, "nome3", "item1_child1_valor3");
    assertEquals("item1_child1_valor1", preferences.read(path1, "nome1"));
    assertEquals("item1_child1_valor2", preferences.read(path1, "nome2"));
    assertEquals("item1_child1_valor3", preferences.read(path1, "nome3"));
  }

  @Test
  public void testWriteReadCollection() {
    String[] diseases = { "Abdominal Aortic Aneurysm", "Acanthamoeba Infection", "ACE (Adverse Childhood Experiences)",
            "Acinetobacter Infection", "Acquired Immunodeficiency Syndrome (AIDS)",
            "Adenovirus Infection", "Adenovirus Vaccination" };
    String prefName = "diseases";
    preferences.write(prefName, Arrays.asList(diseases));
    // Verifica apenas se o item de doenças consta no que foi armazenado
    for (String item: diseases) {
      assertTrue(preferences.readCollection(prefName).stream().filter(it -> it.equals(item)).findFirst().isPresent(), "Item falho: '" + item + "'");
    }
  }

  @Test
  public void testCommaWriteReadCollection() {
    String[] diseases = { "Abdominal, Aortic Aneurysm", "Acantha,moeba Infection", "ACE (Adverse, Childhood Experiences)",
            "Acinetobacter Inf,ection", "Acquired Immunodeficiency Syndrom,e (AIDS)",
            "Adenovirus Infec,tion", "Adenovirus Vac,cination" };
    String prefName = "diseases";
    preferences.write(prefName, Arrays.asList(diseases));
    for (String item: diseases) {
      assertTrue(preferences.readCollection(prefName).stream().filter(it -> it.equals(item)).findFirst().isPresent(), "Item falho: '" + item + "'");
    }
  }

  @Test
  public void addCollectionCheckNonEmptyFirsItem() {
    assertEquals(0, preferences.readCollection("rxy").size());
    preferences.addToCollection("rxy", "primeiro-item", false);
    assertEquals(1, preferences.readCollection("rxy").size());
  }

    @Test
  public void addCollectionWithoutPath() {
    String etmologia = "etmologia";
    String[] items = { "abajour", "açafrão", "açucar", "açude", "alcachofra", "alcalóide", "alcatéia", "álcool" };
    for (String item: items) {
      preferences.addToCollection(etmologia, item, false);
    }
    for (String item: items) {
      assertTrue(preferences.readCollection(etmologia).stream().filter(it -> it.equals(item)).findFirst().isPresent(), "Item falho: '" + item + "'");
    }

  }

  @Test
  public void addCollectionWithPath() {
    String tribos = "tribos";
    String[] acre = { "Amawáka", "Arara", "Deni", "Nawa" };
    String[] amapa = { "Karipuna", "Palikur", "Wayampi" };
    String[] amazonas = { "Kambeba", "Jarawara", "Korubo", "Wanana" };
    String[] para = { "Anambé", "Jaruna", "Kayapó", "Munduruku" };
    String[] rondonia = { "Arara", "Aruá", "Nambikwara", "Tupari" };
    String[] roraima = { "Macuxi", "Yanomami", "Waiwai", "Ingaricô" };
    String[] tocantins = { "Apinaye", "Guarani", "Karaja", "Kraho", "Xerente" };
    Map<String, String[]> map = new HashMap<>();
    map.put("acre", acre);
    map.put("amapa", amapa);
    map.put("amazonas", amazonas);
    map.put("para", para);
    map.put("rondonia", rondonia);
    map.put("roraima", roraima);
    map.put("tocantins", tocantins);
    for (String path: map.keySet()) {
      for (String item: map.get(path)) {
        preferences.addToCollection(path, tribos, item, false);
      }
      for (String item: map.get(path)) {
        assertTrue(preferences.readCollection(path, tribos).stream().filter(it -> it.equals(item)).findFirst().isPresent(), "Item falho: '" + path + "/" + item + "'");
      }
    }

  }


  @Test
  public void readCollectionEmptyNullValue() {
    assertFalse(preferences.readCollection("xxxx") == null);
  }

}
