package com.cqvip.innocence.framework.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;

import java.io.IOException;

/**
 * 自定义ES处理聚合查询结果转json序列化的类
 * @author Administrator
 */
public class ParsedStringTermsBucketSerializer extends StdSerializer<ParsedStringTerms.ParsedBucket> {

    public ParsedStringTermsBucketSerializer(Class<ParsedStringTerms.ParsedBucket> t) {
        super(t);
    }

    @Override
    public void serialize(ParsedStringTerms.ParsedBucket value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("aggregations", value.getAggregations());
        gen.writeObjectField("key", value.getKey());
        gen.writeStringField("keyAsString", value.getKeyAsString());
        gen.writeNumberField("docCount", value.getDocCount());
        gen.writeEndObject();
    }
}
