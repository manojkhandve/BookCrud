package model;


import jakarta.persistence.*;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "book_type", nullable = false)
    private String booktype;

	public int getBookId() {
		return bookId;
	}

	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Book( String bookName, String authorName, double price, String booktype) {
	
		this.bookName = bookName;
		this.authorName = authorName;
		this.price = price;
		this.booktype = booktype;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBooktype() {
		return booktype;
	}

	public void setBooktype(String booktype) {
		this.booktype = booktype;
	}

	
    
}