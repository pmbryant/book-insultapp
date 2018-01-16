package org.openshift;

import java.util.Random;

public class InsultGenerator {
	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		
		try {
			String dbURL = "jdbc:postgresql://";
			dbURL += System.getenv("POSTGRESQL_SERVICE_HOST");
			dbURL += "/" + System.getenv("POSTGRESQL_SERVICE_PORT");
			
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
			return "Database connxn problem!";
		}
		return theInsult;
	}

}
