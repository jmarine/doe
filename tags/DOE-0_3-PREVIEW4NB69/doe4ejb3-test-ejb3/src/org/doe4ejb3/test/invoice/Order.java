package org.doe4ejb3.test.invoice;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue; import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "INVOICE")
public class Order implements java.io.Serializable
{
   private int id;
   private double total;
   private Collection<LineItem> lineItems;

   @Id 
   @GeneratedValue(strategy=GenerationType.AUTO)
   public int getId()
   {
      return id;
   }

   public void setId(int id)
   {
      this.id = id;
   }

   public double getTotal()
   {
      return total;
   }

   public void setTotal(double total)
   {
      this.total = total;
   }

   public void addPurchase(String product, int quantity, double price)
   {
      if (lineItems == null) lineItems = new ArrayList<LineItem>();
      LineItem item = new LineItem();
      item.setOrder(this);
      item.setProduct(product);
      item.setQuantity(quantity);
      item.setSubtotal(quantity * price);
      lineItems.add(item);
      total += quantity * price;
   }

   @OneToMany(fetch = FetchType.EAGER, mappedBy="order")
   public Collection<LineItem> getLineItems()
   {
      return lineItems;
   }

   public void setLineItems(Collection<LineItem> lineItems)
   {
      this.lineItems = lineItems;
   }
}
