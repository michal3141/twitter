package postgresDB;

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
}
