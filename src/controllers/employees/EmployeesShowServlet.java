//詳細画面サーブレット
package controllers.employees;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesShowServlet
 */
@WebServlet("/employees/show")
public class EmployeesShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();//データベース管理する人呼び出し

        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));//パラメータからidを取得して整数にし、そのidのデータを一件取得

        em.close();//データベース閉じる

        request.setAttribute("employee", e);//リクエストスコープに取得した一件のデータをセット

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/show.jsp");//詳細画面へリダイレクト
        rd.forward(request, response);
    }

}
