//新規登録フォーム（/new.jsp）から入力されたデータを受け取り（doPost）データベースに登録するサーブレット
//データにパスワードが含まれるのでハッシュ化
package controllers.employees;

import java.io.IOException;
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
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesCreateServlet
 */
@WebServlet("/employees/create")
public class EmployeesCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");//hidden要素でおくられた_token＝セッションＩＤをキャストしてStringで取得
        if(_token != null && _token.equals(request.getSession().getId())) {//セッションＩＤのデータがある、さらに取得したセッションＩＤと送られたセッションＩＤが同じの時
            EntityManager em = DBUtil.createEntityManager();//データベース管理する人呼び出し

            Employee e = new Employee();//カラムをもつクラスのインスタンス化

          //各カラムにセット
            e.setCode(request.getParameter("code"));//社員番号
            e.setName(request.getParameter("name"));//社員名
            e.setPassword(
                    EncryptUtil.getPasswordEncrypt(//引数で受け取った文字列にペッパー文字列を連結させたものを SHA256 でハッシュ化引数の文字列が何もなければ、空の文字列を返すメソッド
                                    request.getParameter("password"),//パスワードをパラメータから取得
                                    (String)this.getServletContext().getAttribute("pepper")//pepperの文字を取得
                            )
                    );

            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));//管理者権限があるかどうかのカラム

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            e.setCreated_at(currentTime);//登録日時
            e.setUpdated_at(currentTime);//更新日時
            e.setDelete_flag(0);//削除された従業員かどうか。0は在籍中

            List<String> errors = EmployeeValidator.validate(e, true, true);//入力値チェックのメソッド。第2と第3引数を両方とも true で指定（入力値チェック重複チェックの部分）
            if(errors.size() > 0) {//エラーが一つでもある時
                em.close();//まずデータベースを閉じる

                request.setAttribute("_token", request.getSession().getId());//セッションＩＤをリクエストスコープにセット
                request.setAttribute("employee", e);//カラムを持つクラスをリクエストスコープにセット
                request.setAttribute("errors", errors);//エラーをリクエストスコープにセット

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/new.jsp");//新規登録フォームにリダイレクト
                rd.forward(request, response);

            }else {//エラーが無い時
                em.getTransaction().begin();//トランザクション処理の開始
                em.persist(e);//データベースに保存
                em.getTransaction().commit();//データの新規登録を確定（コミット）
                request.getSession().setAttribute("flush", "登録が完了しました。");//フラッシュメッセージをセッションスコープに追加
                em.close();

                response.sendRedirect(request.getContextPath() + "/employees/index");//一覧表示画面へリダイレクト
            }

        }

    }

}
