package com.elasticsearchutil.demo.util;

import cn.hutool.extra.spring.SpringUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.StringReader;

/**
 * a simple elasticsearch client util
 *
 * @author charles
 * @version 0.1
 */
@Slf4j
public class ElasticsearchUtil {

    public static ElasticsearchClient elasticsearchClient;

    static {
        elasticsearchClient = SpringUtil.getBean(ElasticsearchClient.class);
    }

    /**
     * elasticsearch index exists
     *
     * @param indexName index name
     * @return true/false
     */
    public static boolean indexExists(String indexName) {
        try {
            return elasticsearchClient.indices().exists(e -> e.index(indexName)).value();
        } catch (Exception e) {
            log.error("indexExists error", e);
        }
        return false;
    }

    /**
     * create elasticsearch index
     *
     * @param indexName     index name
     * @param configureJson create index configure json
     * @return true/false
     */
    public static boolean createIndex(String indexName, String configureJson) throws IOException {
        try {
            boolean exists = indexExists(indexName);
            if (exists) {
                log.debug("create index {} is already exist", indexName);
                return true;
            }
            if (StringUtils.hasText(configureJson)) {
                elasticsearchClient.indices().create(e -> e.index(indexName).withJson(new StringReader(configureJson)));
                log.debug("create index {} success", indexName);
                return true;
            }
            elasticsearchClient.indices().create(e -> e.index(indexName));
            log.debug("create index {} success", indexName);
            return true;
        } catch (Exception e) {
            log.error("create index error", e);
        }
        return false;
    }

    /**
     * delete elasticsearch index
     *
     * @param indexName index name
     * @return true/false
     */
    public static boolean deleteIndex(String indexName) {
        try {
            boolean exists = indexExists(indexName);
            if (!exists) {
                log.debug("delete index {} is no exist", indexName);
                return true;
            }
            elasticsearchClient.indices().delete(e -> e.index(indexName));
            return true;
        } catch (Exception e) {
            log.error("delete index error", e);
        }
        return false;
    }

    /**
     * delete elasticsearch index
     *
     * @param indexName index name
     * @param id        id
     * @return true/false
     */
    public static boolean deleteIndex(String indexName, String id) {
        try {
            boolean exists = indexExists(indexName);
            if (!exists) {
                log.debug("delete index index is no exist indexName {} id {}", indexName, id);
                return true;
            }
            exists = elasticsearchClient.exists(e -> e.index(indexName).id(id)).value();
            if (!exists) {
                log.debug("delete index {} id {} is no exist", indexName, id);
                return true;
            }
            elasticsearchClient.delete(e -> e.index(indexName).id(id));
            return true;
        } catch (Exception e) {
            log.error("delete index error", e);
        }
        return false;
    }


    /**
     * add elasticsearch index doc
     *
     * @param indexName index name
     * @param id        id
     * @param doc       doc
     * @param clazz     doc class
     * @param <T>       doc class obj
     * @return true/false
     */
    public static <T> boolean addDocument(String indexName, String id, T doc, Class<T> clazz) {
        try {
            boolean exists = indexExists(indexName);
            if (!exists) {
                log.debug("add document index {} is no exist indexName {} id {}", indexName, id, doc);
            }
            exists = elasticsearchClient.exists(e -> e.index(indexName).id(id)).value();
            if (exists) {
                elasticsearchClient.update(update -> update.index(indexName).id(id).doc(doc), clazz);
                return true;
            }
            elasticsearchClient.index(index -> index.index(indexName).id(id).document(doc));
            return true;
        } catch (Exception e) {
            log.error("add document error", e);
        }
        return false;
    }

    /**
     * get elasticsearch index doc
     *
     * @param request search request
     * @param clazz   result class
     * @param <T>     result class obj
     * @return doc
     */
    public static <T> SearchResponse<T> getDocument(SearchRequest request, Class<T> clazz) {
        try {
            return elasticsearchClient.search(request, clazz);
        } catch (Exception e) {
            log.error("get document error", e);
        }
        return null;
    }

    /**
     * get elasticsearch index doc
     *
     * @param indexName        index name
     * @param searchConfigJson search config json
     * @param clazz            result class
     * @param <T>              result class obj
     * @return doc
     */
    public static <T> SearchResponse<T> getDocument(String indexName, String searchConfigJson, Class<T> clazz) {
        try {
            return elasticsearchClient.search(search -> search.index(indexName).withJson(new StringReader(searchConfigJson)), clazz);
        } catch (Exception e) {
            log.error("get document error", e);
        }
        return null;
    }
}
