package haven.mappingtutorial.servlet;

import haven.mappingtutorial.db.DBHelper;
import haven.mappingtutorial.model.CollisionRecord;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<CollisionRecord> collisionRecords = new DBHelper().getCollisionRecords(10);
		PrintWriter out = response.getWriter();
		System.out.println(collisionRecords);
		for (CollisionRecord collisionRecord : collisionRecords) {
			out.print(collisionRecord.getLongitude());
			out.print(collisionRecord.getLatitude());
			out.print(collisionRecord.getDate());
			out.print(collisionRecord.getTime());
			out.print(collisionRecord.getBorough());
			out.println();
		}
	}

}
