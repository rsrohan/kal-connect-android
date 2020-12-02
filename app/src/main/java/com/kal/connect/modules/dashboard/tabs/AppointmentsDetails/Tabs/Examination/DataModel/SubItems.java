package com.kal.connect.modules.dashboard.tabs.AppointmentsDetails.Tabs.Examination.DataModel;

public class SubItems
{
    public SubItems(String id, String title, String type, String value){
        this.id=id;
        this.title=title;
        this.type=type;
        this.value=value;
    }

    private String id;

    private String title;

    private String type;

    private String value;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getValue ()
    {
        return value;
    }

    public void setValue (String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+", type = "+type+", value = "+value+"]";
    }
}

