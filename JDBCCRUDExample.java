package tast_7;

import java.awt.DefaultFocusTraversalPolicy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCCRUDExample {

	private static final String URL="jdbc:mysql://localhost:3306/testdb";
	
	private static final String USER="root";
	private static final String PASSWORD="root";
	
	public static void main(String[] args) {
		try(Connection conn=DriverManager.getConnection(URL,USER,PASSWORD);
				Scanner sc=new Scanner(System.in))
		{
			System.out.println("Connected to Database !");
			
			while(true)
			{
				System.out.println("\n1.Add User\n2.View Users\n3.Update User\n4.Delete User\5.Exit");
				System.out.println("Choose option:");
				int choice=sc.nextInt();
				sc.nextLine();
				
				switch(choice)
				{
					case 1 ->addUser(conn,sc);
					case 2 ->viewUsers(conn);
					case 3 ->updateUser(conn,sc);
					case 4 ->deleteUser(conn,sc);
					case 5 ->
					{
						System.out.println("Exiting...");
						return ;
					}
					
					default ->System.out.println("Invalid option !");
				
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void addUser(Connection conn,Scanner sc) throws SQLException
	{
		System.out.println("Enter name : ");
		String name=sc.nextLine();
		
		System.out.println("Enter email : ");
		String email=sc.nextLine();
		
		String sql="INSERT INTO users(name,email) VALUES(?,?)";
		try(PreparedStatement stmt=conn.prepareStatement(sql))
		{
			stmt.setString(1, name);
			stmt.setString(2, email);
			int rows=stmt.executeUpdate();
			System.out.println(rows + "user(s) added.");
		}
	
	}
	
	private static void viewUsers(Connection conn) throws SQLException
	{
		String sql="SELECT * FROM users";
		try(Statement stmt=conn.createStatement();
				
				ResultSet rs=stmt.executeQuery(sql))
		{
			while(rs.next())
			{
				System.out.printf("%d | %s | %s%n",
						rs.getInt("id"),rs.getString("name"), rs.getString("email"));
			}
		}
				
				
	}
	
	private static void updateUser(Connection conn, Scanner sc) throws SQLException
	{
		System.out.println("Enter user ID to update : ");
		int id=sc.nextInt();
		sc.nextLine();
		
		System.out.println("Enter new name : ");
		String name=sc.nextLine();
		
		System.out.println("Enter new emai : ");
		String email=sc.nextLine();
		
		String sql="IPDATE users SET name=?,email=? WHERE id=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql))
		{
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setInt(3, id);
			
			int rows=stmt.executeUpdate();
			System.out.println(rows + "user(s) updated");
		}
	}
	
	private static void deleteUser(Connection conn, Scanner sc) throws SQLException
	{
		System.out.println("Enter user ID to delete.");
		int id=sc.nextInt();
		
		String sql="DELETE FROM users WHERE id=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql))
		{
			stmt.setInt(1, id);
			int rows=stmt.executeUpdate();
			System.out.println(rows + "user(s) deleted");
		}
	}
}
