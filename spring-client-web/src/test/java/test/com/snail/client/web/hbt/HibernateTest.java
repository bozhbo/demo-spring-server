package test.com.snail.client.web.hbt;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import test.com.snail.client.web.hbt.entity.Bid;
import test.com.snail.client.web.hbt.entity.Item;
import test.com.snail.client.web.hbt.entity.User;

public class HibernateTest {

	public static void main(String[] args) {
		SessionFactory sessionFactory = buildSessionFactory();
		
		Session session = sessionFactory.openSession();
		
		Item item = new Item();
		item.setName("book");
		
		session.save(item);
		
		Bid bid = new Bid();
		bid.setName("bob");
		bid.setItemId(item.getId());
		
		session.save(bid);
		
//		User user = new User();
//		user.setName("wmf");
//		
//		session.save(user);
		
		session.close();

	}

	private static SessionFactory buildSessionFactory() {
		try {
			SessionFactory sessionFactory = new Configuration()
					.addAnnotatedClass(Item.class)
					.addAnnotatedClass(User.class)
					.addAnnotatedClass(Bid.class)
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
			return sessionFactory;
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
}
