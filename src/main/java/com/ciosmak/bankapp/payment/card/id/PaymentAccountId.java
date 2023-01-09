package com.ciosmak.bankapp.payment.card.id;


public final class PaymentAccountId
{
    private static PaymentAccountId instance;
    public Long id;

    private PaymentAccountId(Long id)
    {
        this.id = id;
    }

    public static PaymentAccountId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new PaymentAccountId(id);
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
