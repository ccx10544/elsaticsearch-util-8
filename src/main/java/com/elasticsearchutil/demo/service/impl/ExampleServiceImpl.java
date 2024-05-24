package com.elasticsearchutil.demo.service.impl;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearchutil.demo.entity.ExampleResult;
import com.elasticsearchutil.demo.entity.Student;
import com.elasticsearchutil.demo.service.ExampleService;
import com.elasticsearchutil.demo.util.ElasticsearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ExampleServiceImpl implements ExampleService {

    @Value("${elasticsearch.indexName}")
    private String indexName;

    @Override
    public ExampleResult insert(List<Student> studentList) {
        try {
            boolean created = ElasticsearchUtil.createIndex(indexName, """
                    {
                        "settings": {
                          "analysis": {
                            "analyzer": {
                              "ik_analyzer": {
                                "type": "custom",
                                "tokenizer": "ik_max_word"
                              }
                            }
                          },
                          "similarity": {
                            "my_custom_similarity": {
                              "type": "BM25",
                              "k1": 1.2,
                              "b": 0.75,
                              "discount_overlaps": false
                            }
                          }
                        },
                        "mappings": {
                          "properties": {
                            "remark": {
                                "type": "text",
                                "fields": {
                                  "keyword": {
                                    "type": "keyword",
                                    "ignore_above": 256
                                  }
                                },
                                "similarity" : "my_custom_similarity"
                            }
                          }
                        }
                    }
                    """);
            if (!created) {
                return ExampleResult.fail("创建索引失败", "10001");
            }
            for (Student student : studentList) {
                boolean b = ElasticsearchUtil.addDocument(indexName, student.getId(), student, Student.class);
                log.info("add student : {}", b ? "success" : "fail");
            }
            return ExampleResult.success();
        } catch (Exception e) {
            log.error("insert error", e);
            return ExampleResult.fail("插入失败", "10002");
        }
    }

    @Override
    public ExampleResult findByRemark(String remark) {
        SearchResponse<Student> document = ElasticsearchUtil.getDocument(indexName, """
                {
                  "query": {
                    "bool": {
                      "must": [
                        {
                          "match": {
                            "remark":""" + "\"" + remark + "\"" + """
                          }
                        }
                      ]
                    }
                  }
                }
                """, Student.class);
        List<Student> result = new ArrayList<>();
        for (Hit<Student> hit : document.hits().hits()) {
            result.add(hit.source());
        }
        return ExampleResult.success(result, (long) result.size());
    }

    @Override
    public ExampleResult findBySex(Integer sex) {
        SearchResponse<Student> document = ElasticsearchUtil.getDocument(SearchRequest.of(request ->
                request.query(query ->
                        query.bool(bool ->
                                bool.must(must ->
                                        must.term(term ->
                                                term.field("sex").value(sex)
                                        )
                                )
                        )
                )
        ), Student.class);
        List<Student> result = new ArrayList<>();
        for (Hit<Student> hit : document.hits().hits()) {
            result.add(hit.source());
        }
        return ExampleResult.success(result, (long) result.size());
    }

    @Override
    public ExampleResult deleteById(String id) {
        boolean deleted = ElasticsearchUtil.deleteIndex(indexName, id);
        if (!deleted) {
            return ExampleResult.fail("删除失败", "100002");
        }
        return ExampleResult.success();
    }

    @Override
    public ExampleResult deleteIndex() {
        boolean deleted = ElasticsearchUtil.deleteIndex(indexName);
        if (!deleted) {
            return ExampleResult.fail("删除失败", "100002");
        }
        return ExampleResult.success();
    }
}
