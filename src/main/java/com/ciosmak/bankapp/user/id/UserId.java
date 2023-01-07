package com.ciosmak.bankapp.user.id;

public final class UserId
{
    private static UserId instance;
    public Long id;

    private UserId(Long id)
    {
        this.id = id;
    }

    public static UserId getInstance(Long id)
    {
        if (instance == null)
        {
            instance = new UserId(id);
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
