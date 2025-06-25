package com.fibonsai.exsim.dto;

public record ProtocolStandard(
        Protocol name
) {
    public enum Protocol {
        BRC20,
        ARC20,
        RUNE
    }
}
