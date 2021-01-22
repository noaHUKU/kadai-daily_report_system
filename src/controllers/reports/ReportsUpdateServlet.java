//更新処理のサーブレット
package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");//パラメータから取得したセッションIDを変数に
        if(_token != null && _token.equals(request.getSession().getId())) {//データが存在し、送られたセッションIDと一致するとき
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, (Integer)(request.getSession().getAttribute("report_id")));//リクエストスコープから取得したidをもとにデータべースから一件取得

            //各カラムにセット。データベースを更新する。
            r.setReport_date(Date.valueOf(request.getParameter("report_date")));
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            List<String> errors = ReportValidator.validate(r);
            if(errors.size() > 0) {//エラーメッセージがあるとき
                em.close();//使わないのでデータベース閉じる

                request.setAttribute("_token", request.getSession().getId());//リクエストスコープにセッションIDをセット
                request.setAttribute("report", r);//リクエストスコープに取得したデータをセット
                request.setAttribute("errors", errors);//リクエストスコープにエラーメッセージをセット

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");//ビューへリダイレクト
                rd.forward(request, response);
            }else {
                em.getTransaction().begin();//処理開始
                em.getTransaction().commit();//確定処理
                em.close();//閉じる
                request.getSession().setAttribute("flush", "更新が完了しました。");//フラッシュメッセージをセッションスコープへセット

                request.getSession().removeAttribute("report_id");//セッションスコープから不要になったものを削除（データ取得のための情報なので取得したら不要）

                response.sendRedirect(request.getContextPath() + "/reports/index");//一覧表示画面へリダイレクト
            }

        }

    }

}
