package com.cqvip.innocence.framework.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;

/**
 * @ClassName XssStringJsonSerializer
 * @Description json反序列化
 * @Author Innocence
 * @Date 2020/9/21
 * @Version 1.0
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }
    @Override
    public void serialize(String value, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        if (value != null) {
            String encodedValue = StringEscapeUtils.escapeHtml4(value);
            jsonGenerator.writeString(encodedValue);
        }
    }

}
