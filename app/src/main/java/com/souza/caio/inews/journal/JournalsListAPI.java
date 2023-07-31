package com.souza.caio.inews.journal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JournalsListAPI
{
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("sources")
    @Expose
    private List<Jornal> sources;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<Jornal> getSources()
    {
        return sources;
    }

    public void setSources(List<Jornal> sources)
    {
        this.sources = sources;
    }
}
