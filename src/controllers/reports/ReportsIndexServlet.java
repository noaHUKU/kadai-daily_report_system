//一覧表示のサーブレット
package controllers.reports;

import java.io.IOException;
import java.util.List;

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
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/reports/index")
public class ReportsIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page;//ページ数入れる箱
        try{
            page = Integer.parseInt(request.getParameter("page"));//パラメータからページ数、メソッドにより整数型で取得
        }catch(Exception e) {//例外処理
            page = 1;//初期値？employeeの時は箱を作った時点で入れていた。
        }
        List<Report> reports = em.createNamedQuery("getAllReports", Report.class)//全データ取得
                                    .setFirstResult(15 * (page - 1))//何件目からデータを取得するか（配列と同じ0番目から数えるので-1している）
                                    .setMaxResults(15)//データの最大取得件数
                                    .getResultList();//SELECTクエリ（クエリ＝データベースに対する命令文）を実行し、クエリの結果を型なしリストとして返す

        long reports_count = (long)em.createNamedQuery("getReportsCount", Long.class)//データの件数取得。
                                        .getSingleResult();//“1件だけ取得する” という命令を指定

        em.close();

        request.setAttribute("reports", reports);//全データをリクエストスコープにセット
        request.setAttribute("reports_count", reports_count);//データの件数をリクエストスコープにセット
        request.setAttribute("page", page);//ページ数をリクエストスコープにセット
        if(request.getSession().getAttribute("flush") != null) {
            //セッションスコープにフラッシュメッセージがある時、リクエストスコープに入れ替えてセッションスコープに入っていたフラッシュメッセージを削除
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/index.jsp");//ビューにリダイレクト
        rd.forward(request, response);

    }

}
