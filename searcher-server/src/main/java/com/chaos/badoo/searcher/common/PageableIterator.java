package com.chaos.badoo.searcher.common;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class PageableIterator<T> implements Iterator<PageableResult<T>> {

    private PageableResult<T> currentPageableResult;
    private PageableResult<T> nextPageableResult;

    public PageableIterator() {
        this(true);
    }

    public PageableIterator(boolean initializeFirstPage) {
        if (initializeFirstPage) {
            initializeFirstPage();
        }
    }

    protected void initializeFirstPage() {
        hasNext();
    }

    /**
     * @return indicates whether there is a next non empty page of results.
     */
    @Override
    public boolean hasNext() {
        if (nextPageableResult == null) {
            if (currentPageableResult == null) { // fetch first page
                nextPageableResult = fetchNextPage(null);
            } else if (currentPageableResult.getLastEvaluatedKey() != null) { // fetch next page
                nextPageableResult = fetchNextPage(currentPageableResult.getLastEvaluatedKey());
            } else { // this is a last page
                nextPageableResult = new PageableResult<>();
                nextPageableResult.setItems(Collections.emptyList());
            }
        }

        return CollectionUtils.isNotEmpty(nextPageableResult.getItems());
    }

    /**
     * @return fetches a next non empty page of results.
     */
    @Override
    public PageableResult<T> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There are no more pages");
        }

        currentPageableResult = nextPageableResult;
        nextPageableResult = null;

        return currentPageableResult;
    }

    /**
     * Implementation should put actual logic to retrieve next page of results by given last evaluated key
     * that will be provided from previous pageable result.
     * @param lastEvaluatedKey will be null during first page fetching and for the next pages will be non null last evaluated key from previous request
     * @return first or next page by last evaluated key. If returned pageable result doesn't have last evaluated key then it was last available page
     */
    protected abstract PageableResult<T> fetchNextPage(String lastEvaluatedKey);
}
