//編集画面サーブレット
package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsEditServlet
 */
@WebServlet("/reports/edit")
public class ReportsEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsEditServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));//パラメータより取得したidをもとにデータベースから１件のデータを取り出す

        em.close();//用が済んだのでデータベースを閉じる

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");//リクエストスコープからログインしているという証明（login_employee）を取り出しキャストして従業員情報のカラムをもつインスタンスへ
        if(r != null && login_employee.getId() == r.getEmployee().getId()) {//編集したいidのデータがあり、さらにログインしている人と作成者が同じであったとき
            request.setAttribute("report", r);//リクエストスコープへ取得したデータをセット
            request.setAttribute("_token", request.getSession().getId());//リクエストスコープへセッションIDをセット
            request.getSession().setAttribute("report_id", r.getId());//リクエストスコープへ作成者のidをセット
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");//ビューへリダイレクト
        rd.forward(request, response);
    }

}
