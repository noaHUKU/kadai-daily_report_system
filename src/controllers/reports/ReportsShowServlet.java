//詳細画面サーブレット
package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));//パラメータからidをメソッドにより整数で取得。取得したidを引数にそのidの１件のデータを取得

        em.close();//データを取得しもう用が済んだのでさっさと閉じる

        request.setAttribute("report", r);//取得したidの１件のデータをリクエストスコープにセット
        request.setAttribute("_token", request.getSession().getId());//セクションIDをリクエストスコープにセット

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");//ビューへリダイレクト
        rd.forward(request, response);//フォワードはリクエスト情報（パラメータ）などを引き継げるらしい。リダイレクトとは違う処理が行われているらしい。質問する
    }

}
