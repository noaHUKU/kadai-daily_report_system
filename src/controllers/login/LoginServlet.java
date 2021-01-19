//ログイン
package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     * ログイン画面の表示
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());//セッションＩＤをリクエストスコープにセット
        request.setAttribute("hasError", false);//
        if(request.getSession().getAttribute("flush") != null) {//セッションスコープにフラッシュメッセージがある時
            request.setAttribute("flush", request.getSession().getAttribute("flush"));//セッションスコープからリクエストスコープへ入れ替え
            request.getSession().removeAttribute("flush");//セッションスコープからフラッシュメッセージを削除
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");//ログイン画面にリダイレクト
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     * 認証処理
     *  ログイン処理を実行
     * ログインページで入力された社員番号とパスワードをもとにデータベースへ照合し、情報に間違いがなければセッションスコープに login_employee という名前で、その従業員情報のオブジェクトを格納します。
        「セッションスコープに login_employee という名前で従業員情報のオブジェクトが保存されている状態」をログインしている状態
        ソルト文字列を連結した文字列をハッシュ化し、そのデータとデータベース上のデータで照合を行います。

     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     // 認証結果を格納する変数
        Boolean check_result = false;

        String code = request.getParameter("code");//パラメータから社員番号取得
        String plain_pass = request.getParameter("password");//パスワード取得

        Employee e = null;

        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {//ログイン情報がすべて入力されている時
            EntityManager em = DBUtil.createEntityManager();

            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String)this.getServletContext().getAttribute("pepper")
                    );

         // 社員番号とパスワードが正しいかチェックする

            try {
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)//従業員がログインするときに社員番号とパスワードが正しいかをチェックするＳＥＬＥＣＴ文実行
                        .setParameter("code", code)//パラメータにセット
                        .setParameter("pass", password)
                        .getSingleResult();
            }catch(NoResultException ex) {}

            em.close();

            if(e != null) {
                check_result = true;//
            }

        }

        if(!check_result) {//check_resultがfalseだった時
         // 認証できなかったらログイン画面に戻る
            request.setAttribute("_token", request.getSession().getId());//セッションＩＤをリクエストスコープへ
            request.setAttribute("hasError", true);//エラーであることをリクエストスコープにセット
            request.setAttribute("code", code);//社員番号をリクエストスコープにセット

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);

        }else {
         // 認証できたらログイン状態にしてトップページへリダイレクト
            request.getSession().setAttribute("login_employee", e);//従業員情報のオブジェクトを格納

            request.getSession().setAttribute("flush", "ログインしました。");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

}
