package org.fudgemsg.wire.types;
import org.fudgemsg.FudgeFieldType;
import org.fudgemsg.FudgeTypeDictionary;
import org.fudgemsg.types.*;
import org.fudgemsg.types.secondary.JavaSecondaryStringFieldType;

public class FudgeWireType {
    public static final FudgeMsgFieldType SUB_MESSAGE = FudgeMsgFieldType.INSTANCE;
    public static final IndicatorFieldType INDICATOR = IndicatorFieldType.INSTANCE;
    public static final StringFieldType STRING = StringFieldType.INSTANCE;

    public static final FudgeFieldType<Byte> BYTE = PrimitiveFieldTypes.BYTE_TYPE;
    public static final FudgeFieldType<Boolean> BOOLEAN = PrimitiveFieldTypes.BOOLEAN_TYPE;
    public static final FudgeFieldType<Double> DOUBLE = PrimitiveFieldTypes.DOUBLE_TYPE;
    public static final FudgeFieldType<Float> FLOAT = PrimitiveFieldTypes.FLOAT_TYPE;
    public static final FudgeFieldType<Integer> INT = PrimitiveFieldTypes.INT_TYPE;
    public static final FudgeFieldType<Long> LONG = PrimitiveFieldTypes.LONG_TYPE;
    public static final FudgeFieldType<Short> SHORT = PrimitiveFieldTypes.SHORT_TYPE;

    public static final DateFieldType DATE = DateFieldType.INSTANCE;
    public static final DateTimeFieldType DATETIME = DateTimeFieldType.INSTANCE;

    public static final ByteArrayFieldType BYTE_ARRAY = ByteArrayFieldType.VARIABLE_SIZED_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_4 = ByteArrayFieldType.LENGTH_4_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_8 = ByteArrayFieldType.LENGTH_8_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_16 = ByteArrayFieldType.LENGTH_16_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_20 = ByteArrayFieldType.LENGTH_20_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_32 = ByteArrayFieldType.LENGTH_32_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_64 = ByteArrayFieldType.LENGTH_64_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_128 = ByteArrayFieldType.LENGTH_128_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_256 = ByteArrayFieldType.LENGTH_256_INSTANCE;
    public static final ByteArrayFieldType BYTE_ARRAY_512 = ByteArrayFieldType.LENGTH_512_INSTANCE;

    public static final DoubleArrayFieldType DOUBLE_ARRAY = DoubleArrayFieldType.INSTANCE;
    public static final FloatArrayFieldType FLOAT_ARRAY = FloatArrayFieldType.INSTANCE;
    public static final IntArrayFieldType INT_ARRAY = IntArrayFieldType.INSTANCE;
    public static final LongArrayFieldType LONG_ARRAY = LongArrayFieldType.INSTANCE;
    public static final ShortArrayFieldType SHORT_ARRAY = ShortArrayFieldType.INSTANCE;

    public static final byte SUB_MESSAGE_TYPE_ID = FudgeTypeDictionary.FUDGE_MSG_TYPE_ID;
    public static final byte INDICATOR_TYPE_ID = FudgeTypeDictionary.INDICATOR_TYPE_ID;
    public static final byte STRING_TYPE_ID = FudgeTypeDictionary.STRING_TYPE_ID;

    public static final byte BYTE_TYPE_ID = FudgeTypeDictionary.BYTE_TYPE_ID;
    public static final byte BOOLEAN_TYPE_ID = FudgeTypeDictionary.BOOLEAN_TYPE_ID;
    public static final byte DOUBLE_TYPE_ID = FudgeTypeDictionary.DOUBLE_TYPE_ID;
    public static final byte FLOAT_TYPE_ID = FudgeTypeDictionary.FLOAT_TYPE_ID;
    public static final byte INT_TYPE_ID = FudgeTypeDictionary.INT_TYPE_ID;
    public static final byte LONG_TYPE_ID = FudgeTypeDictionary.LONG_TYPE_ID;
    public static final byte SHORT_TYPE_ID = FudgeTypeDictionary.SHORT_TYPE_ID;

    public static final byte DATE_TYPE_ID = FudgeTypeDictionary.DATE_TYPE_ID;
    public static final byte DATETIME_TYPE_ID = FudgeTypeDictionary.DATETIME_TYPE_ID;

    public static final byte BYTE_ARRAY_TYPE_ID = FudgeTypeDictionary.BYTE_ARRAY_TYPE_ID;
    public static final byte BYTE_ARRAY_4_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_4_TYPE_ID;
    public static final byte BYTE_ARRAY_8_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_8_TYPE_ID;
    public static final byte BYTE_ARRAY_16_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_16_TYPE_ID;
    public static final byte BYTE_ARRAY_20_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_20_TYPE_ID;
    public static final byte BYTE_ARRAY_32_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_32_TYPE_ID;
    public static final byte BYTE_ARRAY_64_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_64_TYPE_ID;
    public static final byte BYTE_ARRAY_128_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_128_TYPE_ID;
    public static final byte BYTE_ARRAY_256_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_256_TYPE_ID;
    public static final byte BYTE_ARRAY_512_TYPE_ID = FudgeTypeDictionary.BYTE_ARR_512_TYPE_ID;
    public static final byte DOUBLE_ARRAY_TYPE_ID = FudgeTypeDictionary.DOUBLE_ARRAY_TYPE_ID;
    public static final byte FLOAT_ARRAY_TYPE_ID = FudgeTypeDictionary.FLOAT_ARRAY_TYPE_ID;
    public static final byte INT_ARRAY_TYPE_ID = FudgeTypeDictionary.INT_ARRAY_TYPE_ID;
    public static final byte LONG_ARRAY_TYPE_ID = FudgeTypeDictionary.LONG_ARRAY_TYPE_ID;
    public static final byte SHORT_ARRAY_TYPE_ID = FudgeTypeDictionary.SHORT_ARRAY_TYPE_ID;
}
