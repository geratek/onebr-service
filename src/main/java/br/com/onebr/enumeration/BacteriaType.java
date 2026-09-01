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
    EF_SPP_BR(14),
    ECC_BR(15),
    ECC_X(16),          // Enterobacter xiangfangensis
    ECC_C(17),          // Enterobacter cloacae
    ECC_L(18),          // Enterobacter ludwigii
    ECC_I(19),          // Enterobacter intestinihominis
    ECC_R(20),          // Enterobacter roggenkampii
    ECC_K(21),          // Enterobacter kobei
    ECC_B(22),          // Enterobacter bugandensis
    ECC_H(23),          // Enterobacter hormaechei
    ECC_M(24),          // Enterobacter mori
    ECC_A(25),          // Enterobacter adelaidei
    ECC_CD(26),         // Enterobacter chengduensis
    ECC_AB(27),         // Enterobacter asburiae
    ECC_S(28),          // Enterobacter soli
    ECC_HX(29),         // Enterobacter huaxiensis
    ECC_XX(30),         // Enterobacter sp.
    ECC_XXY(31)         // Enterobacter quasiroggenkampii
    ;

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
