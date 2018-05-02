


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;

import algo.Test;

public class Testtest
{
    private String laborFee;

    private String productItemId;

    private String recurringFeeTaxRate;

    private String associatedInvoiceItemId;

    public Date getInvoice() {
		return invoice;
	}

	public void setInvoice(Date invoice) {
		this.invoice = invoice;
	}

	private Date invoice;

    private String hourlyRecurringFee;

    private String oneTimeFeeTaxRate;

    private String setupFeeTaxRate;

    private String recurringTaxAmount;

    private String id;

    private String resourceTableId;

    private String parentId;

    private String setupAfterTaxAmount;

    private String billingItemId;

    private String laborTaxAmount;

    private String description;

    private String createDate;

    private String laborFeeTaxRate;

    private String oneTimeTaxAmount;

    private String invoiceId;

    private String recurringFee;

    private String categoryCode;

    private String oneTimeFee;

    private String serviceProviderId;

    private String oneTimeAfterTaxAmount;

    private String recurringAfterTaxAmount;

    private String setupFee;

    private String laborAfterTaxAmount;

    private String setupTaxAmount;
    private String domainName;
    private String hostName;
    
    

    public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getLaborFee ()
    {
        return laborFee;
    }

    public void setLaborFee (String laborFee)
    {
        this.laborFee = laborFee;
    }

    public String getProductItemId ()
    {
        return productItemId;
    }

    public void setProductItemId (String productItemId)
    {
        this.productItemId = productItemId;
    }

    public String getRecurringFeeTaxRate ()
    {
        return recurringFeeTaxRate;
    }

    public void setRecurringFeeTaxRate (String recurringFeeTaxRate)
    {
        this.recurringFeeTaxRate = recurringFeeTaxRate;
    }

    public String getAssociatedInvoiceItemId ()
    {
        return associatedInvoiceItemId;
    }

    public void setAssociatedInvoiceItemId (String associatedInvoiceItemId)
    {
        this.associatedInvoiceItemId = associatedInvoiceItemId;
    }

    

    public String getHourlyRecurringFee ()
    {
        return hourlyRecurringFee;
    }

    public void setHourlyRecurringFee (String hourlyRecurringFee)
    {
        this.hourlyRecurringFee = hourlyRecurringFee;
    }

    public String getOneTimeFeeTaxRate ()
    {
        return oneTimeFeeTaxRate;
    }

    public void setOneTimeFeeTaxRate (String oneTimeFeeTaxRate)
    {
        this.oneTimeFeeTaxRate = oneTimeFeeTaxRate;
    }

    public String getSetupFeeTaxRate ()
    {
        return setupFeeTaxRate;
    }

    public void setSetupFeeTaxRate (String setupFeeTaxRate)
    {
        this.setupFeeTaxRate = setupFeeTaxRate;
    }

    public String getRecurringTaxAmount ()
    {
        return recurringTaxAmount;
    }

    public void setRecurringTaxAmount (String recurringTaxAmount)
    {
        this.recurringTaxAmount = recurringTaxAmount;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getResourceTableId ()
    {
        return resourceTableId;
    }

    public void setResourceTableId (String resourceTableId)
    {
        this.resourceTableId = resourceTableId;
    }

    public String getParentId ()
    {
        return parentId;
    }

    public void setParentId (String parentId)
    {
        this.parentId = parentId;
    }

    public String getSetupAfterTaxAmount ()
    {
        return setupAfterTaxAmount;
    }

    public void setSetupAfterTaxAmount (String setupAfterTaxAmount)
    {
        this.setupAfterTaxAmount = setupAfterTaxAmount;
    }

    public String getBillingItemId ()
    {
        return billingItemId;
    }

    public void setBillingItemId (String billingItemId)
    {
        this.billingItemId = billingItemId;
    }

    public String getLaborTaxAmount ()
    {
        return laborTaxAmount;
    }

    public void setLaborTaxAmount (String laborTaxAmount)
    {
        this.laborTaxAmount = laborTaxAmount;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getCreateDate ()
    {
        return createDate;
    }

    public void setCreateDate (String createDate)
    {
        this.createDate = createDate;
    }

    public String getLaborFeeTaxRate ()
    {
        return laborFeeTaxRate;
    }

    public void setLaborFeeTaxRate (String laborFeeTaxRate)
    {
        this.laborFeeTaxRate = laborFeeTaxRate;
    }

    public String getOneTimeTaxAmount ()
    {
        return oneTimeTaxAmount;
    }

    public void setOneTimeTaxAmount (String oneTimeTaxAmount)
    {
        this.oneTimeTaxAmount = oneTimeTaxAmount;
    }

    public String getInvoiceId ()
    {
        return invoiceId;
    }

    public void setInvoiceId (String invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public String getRecurringFee ()
    {
        return recurringFee;
    }

    public void setRecurringFee (String recurringFee)
    {
        this.recurringFee = recurringFee;
    }

    public String getCategoryCode ()
    {
        return categoryCode;
    }

    public void setCategoryCode (String categoryCode)
    {
        this.categoryCode = categoryCode;
    }

    public String getOneTimeFee ()
    {
        return oneTimeFee;
    }

    public void setOneTimeFee (String oneTimeFee)
    {
        this.oneTimeFee = oneTimeFee;
    }

    public String getServiceProviderId ()
    {
        return serviceProviderId;
    }

    public void setServiceProviderId (String serviceProviderId)
    {
        this.serviceProviderId = serviceProviderId;
    }

    public String getOneTimeAfterTaxAmount ()
    {
        return oneTimeAfterTaxAmount;
    }

    public void setOneTimeAfterTaxAmount (String oneTimeAfterTaxAmount)
    {
        this.oneTimeAfterTaxAmount = oneTimeAfterTaxAmount;
    }

    public String getRecurringAfterTaxAmount ()
    {
        return recurringAfterTaxAmount;
    }

    public void setRecurringAfterTaxAmount (String recurringAfterTaxAmount)
    {
        this.recurringAfterTaxAmount = recurringAfterTaxAmount;
    }

    public String getSetupFee ()
    {
        return setupFee;
    }

    public void setSetupFee (String setupFee)
    {
        this.setupFee = setupFee;
    }

    public String getLaborAfterTaxAmount ()
    {
        return laborAfterTaxAmount;
    }

    public void setLaborAfterTaxAmount (String laborAfterTaxAmount)
    {
        this.laborAfterTaxAmount = laborAfterTaxAmount;
    }

    public String getSetupTaxAmount ()
    {
        return setupTaxAmount;
    }

    public void setSetupTaxAmount (String setupTaxAmount)
    {
        this.setupTaxAmount = setupTaxAmount;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [laborFee = "+laborFee+", productItemId = "+productItemId+", recurringFeeTaxRate = "+recurringFeeTaxRate+", associatedInvoiceItemId = "+associatedInvoiceItemId+", invoice = "+invoice+", hourlyRecurringFee = "+hourlyRecurringFee+", oneTimeFeeTaxRate = "+oneTimeFeeTaxRate+", setupFeeTaxRate = "+setupFeeTaxRate+", recurringTaxAmount = "+recurringTaxAmount+", id = "+id+", resourceTableId = "+resourceTableId+", parentId = "+parentId+", setupAfterTaxAmount = "+setupAfterTaxAmount+", billingItemId = "+billingItemId+", laborTaxAmount = "+laborTaxAmount+", description = "+description+", createDate = "+createDate+", laborFeeTaxRate = "+laborFeeTaxRate+", oneTimeTaxAmount = "+oneTimeTaxAmount+", invoiceId = "+invoiceId+", recurringFee = "+recurringFee+", categoryCode = "+categoryCode+", oneTimeFee = "+oneTimeFee+", serviceProviderId = "+serviceProviderId+", oneTimeAfterTaxAmount = "+oneTimeAfterTaxAmount+", recurringAfterTaxAmount = "+recurringAfterTaxAmount+", setupFee = "+setupFee+", laborAfterTaxAmount = "+laborAfterTaxAmount+", setupTaxAmount = "+setupTaxAmount+"]";
    }
    
    public static void main(String [] args) throws IntrospectionException{
    	BeanInfo b = Introspector.getBeanInfo( Testtest.class);
    	PropertyDescriptor[] pds = b.getPropertyDescriptors();
    	
		
		for(PropertyDescriptor pd : pds){
			System.out.println(pd.getName());
		}
	}

}


