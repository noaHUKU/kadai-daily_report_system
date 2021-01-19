//削除機能
/*destroy した従業員情報は削除したとみなしてシステム上で扱うことにして、
 * 従業員情報そのものはデータベースへ残す 形にしています。
 * このような方法を 論理削除 と呼んでいます。
 */
package controllers.employees;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesDestroyServlet
 */
@WebServlet("/employees/destroy")
public class EmployeesDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesDestroyServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));//セッションスコープからidを取得しそれをもとにデータを一件取得
            e.setDelete_flag(1);//削除された従業員かどうか（現役：0、削除済み：1）
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));//更新日時

            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "削除が完了しました。");

            response.sendRedirect(request.getContextPath() + "/employees/index");//一覧画面へリダイレクト
        }


    }

}
