package com.ciosmak.bankapp.bank.account.id;

public final class BankAccountId
{
    private static BankAccountId instance;
    public Long id;

    private BankAccountId(Long id)
    {
        this.id = id;
    }

    public static BankAccountId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new BankAccountId(id);
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
