package dao;


import model.Book;
import util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class BookDAO {
    // No need to create SessionFactory here anymore
    public void saveBook(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.persist(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error saving book: " + e.getMessage());
        }
    }

    public List<Book> getAllBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Book", Book.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching books: " + e.getMessage(), e);
        }
    }


    public Book getBook(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching book: " + e.getMessage());
        }
    }

    public void updateBook(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(book);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error updating book: " + e.getMessage());
        }
    }

    public void deleteBook(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.remove(book);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting book: " + e.getMessage());
        }
    }
}