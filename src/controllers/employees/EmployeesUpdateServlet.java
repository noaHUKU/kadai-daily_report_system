//メッセージを修正する機能の作成。update（更新処理）のサーブレット
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
 * Servlet implementation class EmployeesUpdateServlet
 */
@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesUpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = (String)request.getParameter("_token");//パラメータからセッションＩＤ取得
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Employee e = em.find(Employee.class, (Integer)(request.getSession().getAttribute("employee_id")));

         // 現在の値と異なる社員番号が入力されていたら
            // 重複チェックを行う指定をする
            Boolean codeDuplicateCheckFlag = true;
            if(e.getCode().equals(request.getParameter("code"))) {//データベースにある社員番号と送られてきた社員番号が同じ＝重複したとき
                codeDuplicateCheckFlag = false;//現在の値と異なる社員番号ではないので重複チェックは行わない
            }else {
                e.setCode(request.getParameter("code"));//重複していない（社員番号が変更されている）のでカラムにセット→codeDuplicateCheckFlagがtrueになり、バリデーションでチェックされる。
            }

         // パスワード欄に入力があったら
            // パスワードの入力値チェックを行う指定をする
            Boolean passwordCheckFlag = true;
            String password = request.getParameter("password");//パラメータからパスワード取得
            if(password == null || password.equals("")) {//パスワードが入力されていない、または空欄の時
                passwordCheckFlag = false;//入力されていない＝変更しないので入力チェックしない
            }else {
                e.setPassword(//パスワードが入力されていた（変更された）時、ハッシュ化してカラムにセット
                        EncryptUtil.getPasswordEncrypt(
                                password,
                                (String)this.getServletContext().getAttribute("pepper")
                                )

                        );
            }

            e.setName(request.getParameter("name"));//社員名
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));//管理者権限があるかどうか
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));//更新日時
            e.setDelete_flag(0);//削除された従業員かどうか（現役：0、削除済み：1）

            List<String> errors = EmployeeValidator.validate(e, codeDuplicateCheckFlag, passwordCheckFlag);//カラムを持ったクラス、上の社員番号重複チェック、パスワードの入力値チェックしたものを引数に指定
            if(errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);//エラーをリクエストスコープへ

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");//編集画面にリダイレクト
                rd.forward(request, response);
            }else {
                em.getTransaction().begin();
              //データベースから取得したデータに変更をかけてコミットすれば変更が反映されるので em.persist(m); は不要
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                request.getSession().removeAttribute("employee_id");//セッションスコープからid（一件取得のために使った）を削除

                response.sendRedirect(request.getContextPath() + "/employees/index");

            }

        }
    }

}
