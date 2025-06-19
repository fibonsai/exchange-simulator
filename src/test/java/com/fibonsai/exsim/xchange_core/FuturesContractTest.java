package com.fibonsai.exsim.xchange_core;

import com.fibonsai.exsim.xchange_core.derivative.FuturesContract;
import com.fibonsai.exsim.xchange_core.util.ObjectMapperHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FuturesContractTest {

  @Test
  public void testSerializeDeserialize() throws IOException {
    FuturesContract contractExpire = new FuturesContract("XBT/USD/200925");
    FuturesContract jsonCopy = ObjectMapperHelper.viaJSON(contractExpire);
    assertThat(jsonCopy).isEqualTo(contractExpire);

    FuturesContract contractPerpetual = new FuturesContract("XBT/USD/perpetual");
    FuturesContract jsonCopy2 = ObjectMapperHelper.viaJSON(contractPerpetual);
    assertThat(jsonCopy2).isEqualTo(contractPerpetual);
  }
}
