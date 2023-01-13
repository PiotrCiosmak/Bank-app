package com.ciosmak.bankapp.payment.card.id;

public final class PaymentCardId
{
    private static PaymentCardId instance;
    public Long id;

    private PaymentCardId(Long id)
    {
        this.id = id;
    }

    public static PaymentCardId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new PaymentCardId(id);
        }
        return instance;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return this.id;
    }
}
