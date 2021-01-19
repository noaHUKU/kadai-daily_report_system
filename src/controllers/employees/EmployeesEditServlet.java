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
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));//送られたパラメータのidをもとにデータを一件取得

        em.close();//取得したらお役御免なので閉じる

        request.setAttribute("employee", e);//一件取得したデータをリクエストスコープへ
        request.setAttribute("_token", request.getSession().getId());//リクエストスコープにセッションＩＤを。セッションスコープじゃないの？
        request.getSession().setAttribute("employee_id", e.getId());//ｉｄをセッションスコープに？？

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");//ビューへリダイレクト
        rd.forward(request, response);

    }

}
