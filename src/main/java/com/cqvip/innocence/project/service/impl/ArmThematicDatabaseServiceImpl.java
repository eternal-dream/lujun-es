package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.mapper.ArmThematicDatabaseMapper;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmThematicDatabaseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题数据库 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@Service
public class ArmThematicDatabaseServiceImpl extends ServiceImpl<ArmThematicDatabaseMapper, ArmThematicDatabase> implements ArmThematicDatabaseService {

    @Autowired
    private DocumentService documentService;

    @Override
    public IPage<ArmThematicDatabase> getThematicTypesByPage(ArmThematicDatabase armThematicDatabase, Page page) {
        return baseMapper.getThematicTypesByPage(armThematicDatabase,page);
    }

    @Override
    public Map<String, Object> getThematicArticles(ArmThematicDatabase thematicDatabase, Page page, Integer articleType) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        if(thematicDatabase.getBeginTime() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("pub_year").gte(Integer.parseInt(sdf.format(thematicDatabase.getBeginTime()))));
        }
        if(thematicDatabase.getEndTime() != null){
            boolQueryBuilder.must(QueryBuilders.rangeQuery("pub_year").lte(Integer.parseInt(sdf.format(thematicDatabase.getEndTime()))));
        }
        if(StringUtils.isNotBlank(thematicDatabase.getArticleType())){
            String[] split = thematicDatabase.getArticleType().split(";");
            boolQueryBuilder.must(QueryBuilders.termsQuery("source_type",split));
        }
        if(articleType != null){
            boolQueryBuilder.must(QueryBuilders.termQuery("source_type",articleType));
        }
        if(StringUtils.isNotBlank(thematicDatabase.getExpression())){
            boolQueryBuilder.must(QueryBuilders.queryStringQuery(thematicDatabase.getExpression()));
        }
        //聚合
        AbstractAggregationBuilder aggregationBuilder = AggregationBuilders.terms("articleTypeAgg").field("source_type");
        builder.withQuery(boolQueryBuilder)
         .addAggregation(aggregationBuilder)
//   .withPageable(PageRequest.of((int)pageParams.getCurrent(),(int)pageParams.getSize()))
         .withSort(SortBuilders.fieldSort("pub_year").order(SortOrder.DESC));
        Map pageList = documentService.getPageList(builder.build(), PageRequest.of((int) page.getCurrent()-1, (int) page.getSize()), ArticleDoc.class);
        return pageList;
    }

 @Override
 public List recommendation(ArmThematicDatabase thematicDatabase) {
  String expression = thematicDatabase.getExpression();
  if(StringUtils.isBlank(expression)){
   return new ArrayList();
  }
  NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
  builder.withQuery(QueryBuilders.queryStringQuery(expression))
   .withSort(SortBuilders.fieldSort("pub_year").order(SortOrder.DESC))
   .withPageable(PageRequest.of(1,10));
  List recommendationList = documentService.getInfoList(builder.build(), ArticleDoc.class, false);
  return recommendationList;
 }
}
