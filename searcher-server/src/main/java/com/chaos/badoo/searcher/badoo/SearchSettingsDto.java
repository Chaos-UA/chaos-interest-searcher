package com.chaos.badoo.searcher.badoo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchSettingsDto {
    private Integer ageStart;
    private Integer ageEnd;
    private String distance = "62_291_4247";
   // private String distance = "a_62_291_4247_100_Km";
}
