package controllers.toppage;

import java.io.IOException;
import java.util.List;

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
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")//http://localhost:8080 という記述のみでトップページにアクセスできるようにするおまじない
                        //index.htmlは省略可だからhttp://localhost:8080/daily_report_system/　だけでアクセスできる
public class TopPageIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();//データベースのデータを使用する

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");//セッションスコープからログインしている証明（login_employee）を取り出す

        int page;//ページ数のための変数

        try{
            page = Integer.parseInt(request.getParameter("page"));//パラメータからページ数取得
        } catch(Exception e) {
            page = 1;
        }
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)//作成者ごとの日報を取得
                                    .setParameter("employee", login_employee)//ログイン情報をパラメータにセット
                                    .setFirstResult(15 * (page - 1))//どこからのデータを取得するか
                                    .setMaxResults(15)//最大15件まで表示
                                    .getResultList();//リスト形式での取得を指定

        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)//作成者ごとの日報の件数を取得
                                        .setParameter("employee", login_employee)//パラメータにログイン情報をセット
                                        .getSingleResult();//
        em.close();

        request.setAttribute("reports", reports);//リクエストスコープに取得した作成者ごとの日報情報をセット
        request.setAttribute("reports_count", reports_count);//リクエストスコープに取得した作成者ごとの日報の件数をセット
        request.setAttribute("page", page);//リクエストスコープにページ数をセット


        if(request.getSession().getAttribute("flush") != null) {//フラッシュメッセージがあるとき
            request.setAttribute("flush", request.getSession().getAttribute("flush"));//セッションスコープからリクエストスコープへ
            request.getSession().removeAttribute("flush");//セッションスコープからフラッシュメッセージ削除
        }


        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }
}
