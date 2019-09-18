package com.chaos.badoo.searcher;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
public class SearchUserParams {

    private String fullTextSearch;
    private Set<String> interests;
    private Set<String> jobs;
    private Set<String> names;

}
