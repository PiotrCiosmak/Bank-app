package com.ciosmak.bankapp.payment.card.status.converter;

import com.ciosmak.bankapp.payment.card.status.*;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * This class is used as a converter for the PaymentCardStatus enum to be stored in the database.
 * The convertToDatabaseColumn method is used to convert a PaymentCardStatus attribute to a String representation in the database.
 * The convertToEntityAttribute method is used to convert a String representation of the PaymentCardStatus in the database to the corresponding PaymentCardStatus enum.
 *
 * @author Author Piotr Ciosmak
 * @version 1.0
 */
@Converter
public class PaymentCardStatusConverter implements AttributeConverter<PaymentCardStatus, String>
{
    /**
     * Converts the PaymentCardStatus attribute to a String representation in the database.
     *
     * @param attribute PaymentCardStatus attribute to be converted
     * @return String representation of the attribute in the database
     * @throws UnsupportedOperationException if attribute is not a supported PaymentCardStatus
     */
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

    /**
     * Converts the String representation of the PaymentCardStatus in the database to the corresponding PaymentCardStatus enum.
     *
     * @param dbData String representation of the PaymentCardStatus in the database
     * @return PaymentCardStatus enum corresponding to the String representation
     * @throws UnsupportedOperationException if dbData is not a supported String representation of PaymentCardStatus
     */
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
