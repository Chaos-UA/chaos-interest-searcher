package com.chaos.badoo.searcher.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class PageableResult<T> {
    private String lastEvaluatedKey;
    private Collection<T> items = Collections.emptyList();

    public <R> PageableResult<R> map(Function<T, R> mapper) {
        PageableResult<R> result = new PageableResult<>();
        result.setLastEvaluatedKey(lastEvaluatedKey);
        result.setItems(items.stream().map(mapper).collect(Collectors.toList()));
        return result;
    }
}