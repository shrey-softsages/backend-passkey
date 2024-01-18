package com.webauthn.app.utility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.yubico.webauthn.data.ByteArray;

import java.util.Base64;

//@Converter(autoApply = true)
public class ByteArrayAttributeConverter implements AttributeConverter<ByteArray, String> {

    @Override
    public String convertToDatabaseColumn(ByteArray attribute) {
        return attribute.getBase64();
    }

    @Override
    public ByteArray convertToEntityAttribute(String dbData) {
        return new ByteArray(Base64.getDecoder().decode(dbData));
    }

}
