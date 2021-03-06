package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.lang.reflect.Type;
import java.math.BigInteger;

public class BigIntegerCodec implements ObjectDeserializer, ObjectSerializer {
    public static final BigIntegerCodec instance = new BigIntegerCodec();

    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type) {
        SerializeWriter writer = jSONSerializer.getWriter();
        if (obj != null) {
            writer.write(((BigInteger) obj).toString());
        } else if (writer.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
            writer.write('0');
        } else {
            writer.writeNull();
        }
    }

    public <T> T deserialze(DefaultJSONParser defaultJSONParser, Type type, Object obj) {
        return deserialze(defaultJSONParser);
    }

    public static <T> T deserialze(DefaultJSONParser defaultJSONParser) {
        JSONLexer lexer = defaultJSONParser.getLexer();
        if (lexer.token() == 2) {
            String numberString = lexer.numberString();
            lexer.nextToken(16);
            return new BigInteger(numberString);
        }
        Object parse = defaultJSONParser.parse();
        if (parse == null) {
            return null;
        }
        return TypeUtils.castToBigInteger(parse);
    }

    public int getFastMatchToken() {
        return 2;
    }
}
