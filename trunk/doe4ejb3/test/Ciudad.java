
import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;


import java.util.Set;

@Entity()
public class Ciudad implements java.io.Serializable
{
   Integer   id;
   String    nombre;
   int       provinciaId;
   Provincia provincia;

   
   public Ciudad() { }

   public Ciudad(String nombre)
   {
	setNombre(nombre);
   }


   @Id
   @Column(name = "ID")
   @GeneratedValue(strategy=GenerationType.TABLE)
   public Integer getId()
   {
      return id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   @Column(name = "NOMBRE", nullable = false, length = 50)
   public String getNombre()
   {
      return nombre;
   }

   public void setNombre(String str)
   {
      nombre = str;
   }
   

   @Column(name = "PROVINCIA")
   public int getProvinciaId()
   {
      return provinciaId;
   }
   
   public void setProvinciaId(int provinciaId)
   {
      this.provinciaId = provinciaId;
   }

   
   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "PROVINCIA", insertable=false, updatable=false)
   public Provincia getProvincia()
   {
      return provincia;
   }

   public void setProvincia(Provincia provincia)
   {
      this.provincia = provincia;
   }

   
   public boolean equals(Object o)
   {
      if( (o != null) && (o instanceof Ciudad) ) {
          Ciudad c = (Ciudad)o;
          return id.equals(c.getId());
      }
      return false;
   }

   public int hashCode()
   {
      return (id != null) ? id.intValue() : 0;
   }

   public String toString()
   {
      return getNombre();
   }
	 
   
}
