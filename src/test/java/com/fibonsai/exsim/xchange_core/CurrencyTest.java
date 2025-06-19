package com.fibonsai.exsim.xchange_core;

import com.fibonsai.exsim.xchange_core.currency.Currency;
import com.fibonsai.exsim.xchange_core.util.ObjectMapperHelper;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CurrencyTest {

  @Test
  public void testCurrencyCode() {
    assertEquals(Currency.CNY.getCodeCurrency("CNY"), Currency.CNY);
    assertEquals(Currency.CNY.getCodeCurrency("cny"), Currency.CNY);
  }

  @Test
  public void testGetInstance() {
    assertEquals(Currency.BTC, Currency.getInstance("BTC"));
    assertEquals(Currency.BTC, Currency.getInstance("btc"));
    assertEquals(new Currency("btc"), Currency.getInstance("BTC"));
  }

  @Test
  public void testGetInstanceNoCreate() {
    assertEquals(Currency.CNY, Currency.getInstanceNoCreate("CNY"));
    assertEquals(Currency.CNY, Currency.getInstanceNoCreate("cny"));
    assertEquals(new Currency("cny"), Currency.getInstanceNoCreate("CNY"));
  }

  @Test
  public void testEquals() {
    assertEquals(Currency.BTC, Currency.XBT);
    assertNotEquals(Currency.LTC, Currency.XBT);

    Currency btc = SerializationUtils.deserialize(SerializationUtils.serialize(Currency.BTC));
    assertEquals(Currency.BTC, btc);
    assertEquals(Currency.XBT, btc);
    assertNotEquals(Currency.LTC, btc);
  }

  @Test
  public void testToString() {
    assertEquals("XBT", Currency.XBT.toString());
    assertEquals("BTC", Currency.BTC.toString());
  }

  @Test
  public void testSerializeDeserialize() throws IOException {
    Currency jsonCopy = ObjectMapperHelper.viaJSON(Currency.XBT);
    assertThat(jsonCopy).isEqualTo(Currency.XBT);
  }
}
