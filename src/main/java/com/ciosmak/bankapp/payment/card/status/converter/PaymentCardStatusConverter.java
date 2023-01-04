package com.ciosmak.bankapp.payment.card.status.converter;

import com.ciosmak.bankapp.payment.card.status.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PaymentCardStatusConverter implements AttributeConverter<PaymentCardStatus, String>
{
    @Override
    public String convertToDatabaseColumn(PaymentCardStatus attribute)
    {
        if (attribute instanceof Activated)
        {
            return "ACTIVATED";
        }
        else if (attribute instanceof BlockedPermanently)
        {
            return "BLOCKED_PERMANENTLY";
        }
        else if (attribute instanceof BlockedTemporarily)
        {
            return "BLOCKED_TEMPORARILY";
        }
        else if (attribute instanceof NotActivated)
        {
            return "NOT_ACTIVATED";
        }
        else
        {
            throw new UnsupportedOperationException("Nieobsługiwany stan karty płatniczej: " + attribute);
        }
    }

    @Override
    public PaymentCardStatus convertToEntityAttribute(String dbData)
    {
        return switch (dbData)
                {
                    case "ACTIVATED" -> new Activated();
                    case "BLOCKED_PERMANENTLY" -> new BlockedPermanently();
                    case "BLOCKED_TEMPORARILY" -> new BlockedTemporarily();
                    case "NOT_ACTIVATED" -> new NotActivated();
                    default -> throw new UnsupportedOperationException("Nieobsługiwany stan karty płatniczej: " + dbData);
                };
    }
}
