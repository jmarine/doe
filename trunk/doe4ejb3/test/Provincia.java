
import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;


import java.util.Collection;
import java.util.Set;

@Entity()
public class Provincia implements java.io.Serializable
{
   Integer id;
   String  nombre;
   // Collection<Ciudad> ciudades;

   public Provincia() { }

   public Provincia(String nombre) 
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

   /*
   @OneToMany
   @JoinColumn(name = "PROVINCIA")
   public Collection<Ciudad> getCiudades()
   {
      return ciudades;
   }

   public void setCiudades(Collection<Ciudad> ciudades)
   {
      this.ciudades = ciudades;
   }
   */



   public boolean equals(Object o)
   {
      if( (o != null) && (o instanceof Provincia) ) {
	  Provincia p = (Provincia)o;
	  return id.equals(p.getId());
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
