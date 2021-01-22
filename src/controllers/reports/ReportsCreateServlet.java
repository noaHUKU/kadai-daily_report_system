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

import models.Employee;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsCreateServlet
 */
@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    /* (非 Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");//パラメータからセッションIDを取得
        if(_token != null && _token.equals(request.getSession().getId())) {//送られたセッションIDが存在し、さらにセッションIDと一致している時
            EntityManager em = DBUtil.createEntityManager();

            Report r = new Report();

            r.setEmployee((Employee)request.getSession().getAttribute("login_employee"));//セッションスコープからlogin_employee（ログイン時にセットした従業員情報のオブジェクト）を取り出し、カラムにセット

            Date report_date = new Date(System.currentTimeMillis());//Dateのインスタンス作成
            String rd_str = request.getParameter("report_date");//新規作成ページで入力された日付
            if(rd_str != null && !rd_str.equals("")) {//日付のデータがなく、空欄だった時
                report_date = Date.valueOf(request.getParameter("report_date"));//日付欄をわざと未入力にした→当日の日付を入れるようにしている。
            }
            r.setReport_date(report_date);//日付をカラムにセット

            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            r.setCreated_at(currentTime);
            r.setUpdated_at(currentTime);

            List<String> errors = ReportValidator.validate(r);//エラーリスト作成
            if(errors.size() > 0) {//エラーが1つでもあるとき
                em.close();//まずデータベース閉じる

                request.setAttribute("_token", request.getSession().getId());//セッションIDをリクエストスコープにセット
                request.setAttribute("report", r);//日報情報をリクエストスコープにセット
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");//新規登録ページへリダイレクト
                rd.forward(request, response);
            }else {//エラーがないとき
                em.getTransaction().begin();//処理開始
                em.persist(r);//保存
                em.getTransaction().commit();//確定処理
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");//フラッシュメッセージをセッションスコープにセット

                response.sendRedirect(request.getContextPath() + "/reports/index");//一覧表示画面へリダイレクト
            }

        }

    }

}
