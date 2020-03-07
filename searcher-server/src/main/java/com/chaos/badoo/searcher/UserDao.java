package com.chaos.badoo.searcher;

import com.chaos.badoo.searcher.dto.SimpleItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.InternalMappedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.InternalTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserDao {

    public static final String INDEX_NAME = "users";
    public static final String TYPE_NAME = "user";

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void initialize() {
        try {
            String indexMapping = IOUtils.toString(
                    this.getClass().getResource("/elasticsearch/users-index-mapping.json"),
                    StandardCharsets.UTF_8);
            client.admin().indices().prepareCreate(INDEX_NAME)
                    .addMapping(TYPE_NAME, indexMapping, XContentType.JSON).get();
        } catch (ResourceAlreadyExistsException e) {
            LOGGER.info("Users index already exists. Continue");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize schema", e);
        }
    }

    public void save(List<UserModel> users) {
        users.forEach(this::save);
    }

    private void save(UserModel userModel) {
        try {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(INDEX_NAME);
            indexRequest.type(TYPE_NAME);
            indexRequest.source(objectMapper.writeValueAsString(userModel), XContentType.JSON);
            indexRequest.id(userModel.getId());

            client.index(indexRequest, new ActionListener<>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {

                }

                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("Failed to save user: {}", userModel, e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + userModel, e);
        }
    }

    public List<UserModel> searchUsers(SearchUserParams params) {
        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

        rootQuery.mustNot(buildOnlineTextQuery());
        if (Boolean.TRUE.equals(params.getAllowQuickChat())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.ALLOW_QUICK_CHAT, true));
        }

        if (StringUtils.isNotBlank(params.getFullTextSearch())) {
            rootQuery.must(QueryBuilders.queryStringQuery(params.getFullTextSearch()));
        }

        if (CollectionUtils.isNotEmpty(params.getInterests())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.INTERESTS, params.getInterests()));
        }

        if (CollectionUtils.isNotEmpty(params.getJobs())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.JOB, params.getJobs()));
        }

        if (CollectionUtils.isNotEmpty(params.getNames())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.NAME, params.getNames()));
        }

        if (CollectionUtils.isNotEmpty(params.getAges())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.AGE, params.getAges()));
        }

        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.query(rootQuery);

        SearchRequestBuilder searchRequest = client.prepareSearch(INDEX_NAME);
        searchRequest.setSource(searchBuilder);
        searchRequest.setSize(200);

        SearchResponse searchResponse = searchRequest.get();

        SearchHit[] searchHits = searchResponse.getHits().getHits();

        if (ArrayUtils.isEmpty(searchHits)) {
            return Collections.emptyList();
        }

        return Arrays.stream(searchHits).map(this::searchHitToUserModel).collect(Collectors.toList());
    }

    public List<SimpleItem> getInterests(SearchUserParams params) {
        return getUsersCountByField(UserModel.INTERESTS, params);
    }

    public List<SimpleItem> getJobs(SearchUserParams params) {
        return getUsersCountByField(UserModel.JOB, params);
    }

    public List<SimpleItem> getNames(SearchUserParams params) {
        return getUsersCountByField(UserModel.NAME, params);
    }

    public List<SimpleItem> getAges(SearchUserParams params) {
        return getUsersCountByField(UserModel.AGE, params);
    }

    private UserModel searchHitToUserModel(SearchHit searchHit) {
        try {
            return objectMapper.readValue(searchHit.getSourceAsString(), UserModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert search hit: " + searchHit, e);
        }
    }

    private List<SimpleItem> getUsersCountByField(String fieldName, SearchUserParams params) {
        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

        rootQuery.mustNot(buildOnlineTextQuery());
        if (Boolean.TRUE.equals(params.getAllowQuickChat())) {
            rootQuery.must(QueryBuilders.termsQuery(UserModel.ALLOW_QUICK_CHAT, true));
        }

        TermsAggregationBuilder interestsTerms = AggregationBuilders.terms(fieldName);
        interestsTerms.field(fieldName);
        interestsTerms.size(Integer.MAX_VALUE);

        SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
        searchBuilder.query(rootQuery);
        searchBuilder.size(0);

        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
                .setSize(0)
                .setQuery(searchBuilder.query())
                .addAggregation(interestsTerms)
                .get();

        InternalMappedTerms terms = searchResponse.getAggregations().get(fieldName);
        if (terms.getBuckets() != null) {
            List<InternalTerms.Bucket> buckets = terms.getBuckets();
            return buckets.stream().map(v -> {
                SimpleItem interest = new SimpleItem();
                interest.setName(v.getKeyAsString());
                interest.setUsersCount(v.getDocCount());
                return interest;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private TermsQueryBuilder buildOnlineTextQuery() {
        return QueryBuilders.termsQuery(UserModel.ONLINE_TEXT_STATUS, "Online 7+ days ago", "");
    }
}
