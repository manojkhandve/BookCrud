package controller;

import dao.BookDAO;
import model.Book;
import util.HibernateUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/books/*"})
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo() == null ? "/" : req.getPathInfo();
        System.out.println("GET request received for path: " + path);

        try {
            if (path.equals("/")) {
                System.out.println("Fetching all Books");
                resp.getWriter().write(gson.toJson(bookDAO.getAllBooks()));
            } else if (path.startsWith("/edit/")) {
                int id = Integer.parseInt(path.substring(6));
                System.out.println("Fetching book with ID: " + id);
                Book book = bookDAO.getBook(id);
                if (book != null) {
                    resp.getWriter().write(gson.toJson(book));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"Book not found\"}");
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\":\"Invalid endpoint\"}");
            }
        } catch (Exception e) {
            System.out.println("Error in doGet: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            resp.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String action = req.getParameter("action");
        System.out.println("POST request received with action: " + action);
        Map<String, String> response = new HashMap<>();

        try {
            if ("register".equals(action)) {
                Book book = new Book(
                    req.getParameter("bookName"),
                    req.getParameter("authorName"),
                    Double.parseDouble(req.getParameter("price")),
                    req.getParameter("booktype")
                );
                bookDAO.saveBook(book);
                response.put("message", "Book added successfully");

            } else if ("update".equals(action)) {
                Book book = bookDAO.getBook(Integer.parseInt(req.getParameter("bookId")));
                if (book != null) {
                    book.setBookName(req.getParameter("bookName"));
                    book.setAuthorName(req.getParameter("authorName"));
                    book.setBooktype(req.getParameter("booktype"));
                    book.setPrice(Double.parseDouble(req.getParameter("price")));
                    bookDAO.updateBook(book);
                    response.put("message", "Book updated successfully");
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.put("error", "Book not found");
                }

            } else if ("delete".equals(action)) {
                int bookId = Integer.parseInt(req.getParameter("bookId"));
                bookDAO.deleteBook(bookId);
                response.put("message", "Book deleted successfully");

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("error", "Invalid action");
            }
        } catch (Exception e) {
            System.out.println("Error in doPost: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("error", e.getMessage());
        }

        resp.getWriter().write(gson.toJson(response));
    }

    @Override
    public void destroy() {
        HibernateUtil.shutdown(); // Cleanly close the SessionFactory
        System.out.println("Servlet destroyed, SessionFactory closed");
    }
}
