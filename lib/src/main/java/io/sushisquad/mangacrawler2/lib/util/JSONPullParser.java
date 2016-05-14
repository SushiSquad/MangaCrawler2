package io.sushisquad.mangacrawler2.lib.util;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.function.Consumer;

public class JSONPullParser {
    private final JsonParser rawParser;

    private Event cur;

    public JSONPullParser(InputStream source) {
        this.rawParser = Json.createParser(source);
        next();
    }

    public JSONPullParser(java.io.Reader source) {
        this.rawParser = Json.createParser(source);
        next();
    }

    public Event next() {
        if (rawParser.hasNext()) {
            return cur = rawParser.next();
        } else {
            return cur = null;
        }
    }

    public Event cur() {
        return cur;
    }

    public void read(Event e) {
        expect(e);
        next();
    }

    public boolean readBool() {
        switch (cur) {
            case VALUE_TRUE:
                next();
                return true;
            case VALUE_FALSE:
                next();
                return false;
            default:
                parseException("Expected boolean but got " + cur.name());
                throw new RuntimeException("Impossible");
        }
    }

    public int readInt() {
        expect(Event.VALUE_NUMBER);
        if (!rawParser.isIntegralNumber()) parseException("Expected int but got float");

        int anInt = rawParser.getInt();
        next();
        return anInt;
    }

    public OptionalInt readIntOrNull() {
        if (cur == Event.VALUE_NULL) {
            next();
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(readInt());
        }
    }

    public long readLong() {
        expect(Event.VALUE_NUMBER);
        if (!rawParser.isIntegralNumber()) parseException("Expected int but got float");

        long aLong = rawParser.getLong();
        next();
        return aLong;
    }

    public OptionalLong readLongOrNull() {
        if (cur == Event.VALUE_NULL) {
            next();
            return OptionalLong.empty();
        } else {
            return OptionalLong.of(readLong());
        }
    }

    public float readFloat() {
        expect(Event.VALUE_NUMBER);

        float aFloat = rawParser.getBigDecimal().floatValue();
        next();
        return aFloat;
    }

    public double readDouble() {
        expect(Event.VALUE_NUMBER);

        double aDouble = rawParser.getBigDecimal().doubleValue();
        next();
        return aDouble;
    }

    public OptionalDouble readDoubleOrNull() {
        if (cur == Event.VALUE_NULL) {
            next();
            return OptionalDouble.empty();
        } else {
            return OptionalDouble.of(readDouble());
        }
    }

    public BigDecimal readBigDecimal() {
        expect(Event.VALUE_NUMBER);

        BigDecimal bigDecimal = rawParser.getBigDecimal();
        next();
        return bigDecimal;
    }

    public Optional<BigDecimal> readBigDecimalOrNull() {
        if (cur == Event.VALUE_NULL) {
            next();
            return Optional.empty();
        } else {
            return Optional.of(readBigDecimal());
        }
    }

    public String readString() {
        if (cur != Event.VALUE_STRING && cur != Event.KEY_NAME) {
            parseException("Expected string but was " + cur.name());
        }

        String string = rawParser.getString();
        next();
        return string;
    }

    public Optional<String> readStringOrNull() {
        if (cur == Event.VALUE_NULL) {
            next();
            return Optional.empty();
        } else {
            return Optional.of(readString());
        }
    }

    public void readArray(Reader reader) {
        read(Event.START_ARRAY);
        while (cur != Event.END_ARRAY) {
            reader.read();
        }
        read(Event.END_ARRAY);
    }

    public void readArrayOrNull(Reader reader) {
        if (cur == Event.VALUE_NULL) return;
        readArray(reader);
    }

    public void readObject(Consumer<String> reader) {
        read(Event.START_OBJECT);
        while (cur != Event.END_OBJECT) {
            expect(Event.KEY_NAME);
            reader.accept(readString());
        }
        read(Event.END_OBJECT);
    }

    public void readObjectOrNull(Consumer<String> reader) {
        if (cur == Event.VALUE_NULL) return;
        readObject(reader);
    }

    public void onKey(String key, Reader reader) {
        readObject(someKey -> {
            if (key.equals(someKey)) {
                reader.read();
            } else {
                skip();
            }
        });
    }

    public void skip() {
        switch (cur) {
            case VALUE_NULL:
            case VALUE_TRUE:
            case VALUE_FALSE:
            case VALUE_NUMBER:
            case VALUE_STRING:
                next();
                break;
            case KEY_NAME:
                read(Event.KEY_NAME);
                skip();
                break;
            case START_ARRAY:
                read(Event.START_ARRAY);
                while (cur != Event.END_ARRAY) {
                    skip();
                }
                read(Event.END_ARRAY);
                break;
            case START_OBJECT:
                read(Event.START_OBJECT);
                while (cur != Event.END_OBJECT) {
                    skip();
                }
                read(Event.END_OBJECT);
                break;
            default:
                unexpectedToken();
        }
    }

    private void unexpectedToken() {
        parseException("Unexpected token: " + cur.name());
    }

    private void expect(Event e) {
        if (cur != e) {
            parseException("Expected " + e.name() + " but was " + cur.name());
        }
    }

    private void parseException(String msg) {
        throw new JsonParsingException(msg, rawParser.getLocation());
    }
}
