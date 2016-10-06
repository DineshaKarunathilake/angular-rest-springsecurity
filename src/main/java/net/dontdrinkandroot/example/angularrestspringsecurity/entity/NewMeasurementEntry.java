package net.dontdrinkandroot.example.angularrestspringsecurity.entity;

import net.dontdrinkandroot.example.angularrestspringsecurity.JsonViews;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * JPA Annotated Pojo that represents a blog post.
 *
 * @author Philip W. Sorst <philip@sorst.net>
 */
@javax.persistence.Entity
public class NewMeasurementEntry implements Entity
{
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date date;

    @Column
    private String content;

    @Column
    private String customer;

    @Column
    private String batchno;

    @Column
    private String style;

    @Column
    private String size;

    @Column
    private float gmt1_ChestWidth;

    @Column
    private float gmt1_HemWidth;

    @Column
    private float gmt1_CBLength;

    @Column
    private float gmt1_CFLength;




    public NewMeasurementEntry()
    {
        this.date = new Date();
    }

    @JsonView(JsonViews.Admin.class)
    public Long getId()
    {
        return this.id;
    }

    @JsonView(JsonViews.User.class)
    public Date getDate()
    {
        return this.date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @JsonView(JsonViews.User.class)
    public String getContent()
    {
        return this.content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @JsonView(JsonViews.User.class)
    public String getCustomer()
    {
        return this.customer;
    }

    public void setCustomer(String customer)
    {
        this.customer = customer;
    }

    @JsonView(JsonViews.User.class)
    public String getBatchno()
    {
        return this.batchno;
    }

    public void setBatchno(String batchno)
    {
        this.batchno = batchno;
    }

    @JsonView(JsonViews.User.class)
    public String getStyle()
    {
        return this.style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    @JsonView(JsonViews.User.class)
    public String getSize()
    {
        return this.size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    @JsonView(JsonViews.User.class)
    public float getGmt1_ChestWidth()
    {
        return this.gmt1_ChestWidth;
    }

    public void setGmt1_ChestWidth(float gmt1_ChestWidth)
    {
        this.gmt1_ChestWidth = gmt1_ChestWidth;
    }

    @JsonView(JsonViews.User.class)
    public float getGmt1_HemWidth()
    {
        return this.gmt1_HemWidth;
    }

    public void setGmt1_HemWidth(float gmt1_HemWidth)
    {
        this.gmt1_HemWidth = gmt1_HemWidth;
    }

    @JsonView(JsonViews.User.class)
    public float getGmt1_CBLength()
    {
        return this.gmt1_CBLength;
    }

    public void setGmt1_CBLength(float gmt1_CBLength)
    {
        this.gmt1_CBLength = gmt1_CBLength;
    }

    @JsonView(JsonViews.User.class)
    public float getGmt1_CFLength()
    {
        return this.gmt1_CFLength;
    }

    public void setGmt1_CFLength(float gmt1_CFLength)
    {
        this.gmt1_CFLength = gmt1_CFLength;
    }

    @Override
    public String toString()
    {
        return String.format("NewMeasurementEntry[%d, %s, %s, %s, %s]", this.id, this.customer, this.style, this.batchno, this.size);
    }
}
