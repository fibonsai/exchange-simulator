package com.fibonsai.exsim.xchange_core;

import com.fibonsai.exsim.xchange_core.derivative.OptionsContract;
import com.fibonsai.exsim.xchange_core.util.ObjectMapperHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionsContractTest {

  @Test
  public void testSerializeDeserialize() throws IOException {
    OptionsContract contractCall = new OptionsContract("ETH/USD/210719/34000/C");
    OptionsContract jsonCopy2 = ObjectMapperHelper.viaJSON(contractCall);
    assertThat(jsonCopy2).isEqualTo(contractCall);

    OptionsContract contractPut = new OptionsContract("BTC/USDT/210709/34000/P");
    OptionsContract jsonCopy = ObjectMapperHelper.viaJSON(contractPut);
    assertThat(jsonCopy).isEqualTo(contractPut);
  }
}
