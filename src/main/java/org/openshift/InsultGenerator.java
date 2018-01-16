package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsultGenerator {
	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		
		String dbURL = "jdbc:postgresql://";	
		dbURL += System.getenv("POSTGRESQL_SERVICE_HOST");
		dbURL += "/" + System.getenv("POSTGRESQL_DATABASE");
		System.out.println( "Database URL : " + dbURL );
			
		try {
			String username = System.getenv("POSTGRESQL_USER");
			String pwd = System.getenv("PGPASSWORD");
			
			Connection conn = DriverManager.getConnection(dbURL, username, pwd);
			if ( conn != null ) {
				String sql = "select a.string AS first, b.string AS second, c.string AS noun from short_adjective a, long_adjective b, noun c ORDER BY random() limit 1";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1 ) {
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!",article,rs.getString("first"),rs.getString("second"),rs.getString("noun"));
				}
				rs.close();
				conn.close();
			}
			
		} catch (Exception e) {
			return "Database connxn problem!!! " + dbURL + " - " + e;
		}
		return theInsult;
	}

}
