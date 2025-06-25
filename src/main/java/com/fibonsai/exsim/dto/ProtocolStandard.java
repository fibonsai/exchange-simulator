package com.fibonsai.exsim.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;

@JsonDeserialize(builder = ProtocolStandard.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProtocolStandard(
        String name
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        @JsonProperty("NAME")
        private String name;

        Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public ProtocolStandard build() {
            return new ProtocolStandard(this.name);
        }

        public String toString() {
            return "ProtocolStandard.Builder(name=" + this.name + ")";
        }
    }
}
