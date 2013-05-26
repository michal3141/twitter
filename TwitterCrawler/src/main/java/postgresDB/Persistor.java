package postgresDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import dto.NodeDto;
import dto.UserDto;

public class Persistor {
	public static void save(NodeDto node) {
		Configuration configuration = new Configuration();
		configuration.configure();
		ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder().applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistryBuilder.buildServiceRegistry());

		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.merge((UserDto) node);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public static void save1(String sql){
		//zmienna zawieraj¹ca po³¹czenie do bazy
		Connection dbcon = null;
		try {
		    
		    Class.forName("org.postgresql.Driver").newInstance();
		    dbcon = DriverManager.getConnection("jdbc:postgresql://localhost:5432/baza_testowa", "twit", "twit");
		    Statement st = dbcon.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_UPDATABLE);
		    System.out.println(sql);
		    // dla potrzeb ogolnosci nie wnikamy tu w tabele, a dostarczamy gotowe zapytanie sql
/*		    st.executeUpdate("DROP TABLE IF EXISTS twit");
		    PreparedStatement ps = dbcon.prepareStatement("CREATE TABLE twit ("
                    + "id bigserial NOT NULL PRIMARY KEY,"
                    + "data VARCHAR(64), " 
                    + "name VARCHAR(256),"
                    + "replied_id integer,"
                    + "mentioned_id integer,"
                    + "friends_id integer,"
                    + "followers_id integer)");
		    ps.executeUpdate();
<<<<<<< HEAD
		    
=======
		    
>>>>>>> edb79395f8048d6dcb8014824d15da7f7cdd71c1 */
		    int insertedRows = st.executeUpdate(sql);//,Statement.RETURN_GENERATED_KEYS);
		    System.out.println(insertedRows);
		    
		    dbcon.close();
		}catch(Exception e){
		    System.out.println("B³¹d bazy danych.\n" + e + "\n");
		    System.exit(-1);
		}

	}
}
