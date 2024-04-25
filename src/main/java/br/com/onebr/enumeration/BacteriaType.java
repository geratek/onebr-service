package br.com.onebr.enumeration;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum BacteriaType {

    EC_BR(1),
    KP_BR(2),
    SE_BR(3),
    AB_BR(4),
    CF_BR(5),
    EF_BR(6),
    KA_BR(7),
    PA_BR(8),
    SA_BR(9),
    SP_BR(10),
    SL_BR(11),
    KO_BR(12),
    CV_19(13),
    EF_SPP_BR(14);

    long id;

    BacteriaType(int id) {
        this.id = id;
    }

    public static BacteriaType fromString(String name) {
        return Arrays.stream(BacteriaType.values())
            .filter(v -> v.name().equals(name.toUpperCase()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("unknown value: " + name));
    }

    public static BacteriaType fromId(long id) {
        return Arrays.stream(BacteriaType.values())
            .filter(v -> v.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("unknown value: " + id));
    }
}
