/*データベースから複数のメッセージ情報を取得して一覧表示するサーブレット→ビューはindex.jsp
 *
 */
package controllers.employees;

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
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

      //開くページ数を取得（デフォルトは１ページ目）
        int page = 1;
        try{
            page = Integer.parseInt(request.getParameter("page"));//パラメーターから数字（String）を取得、整数にキャスト
        } catch(NumberFormatException e) { }//アプリケーションが文字列を数値型に変換しようとしたとき、文字列の形式が正しくない場合にスローされます。

      //最大件数と開始位置を指定してメッセージを取得
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
                                     .setFirstResult(15 * (page - 1))//何件目からデータを取得するか（配列と同じ0番目から数えていきます）
                                     .setMaxResults(15)//「データの最大取得件数（今回は15件で固定）」を設定
                                     .getResultList();

      //全件数を取得
        long employees_count = (long)em.createNamedQuery("getEmployeesCount", Long.class)
                                       .getSingleResult();//getSingleResult() という “1件だけ取得する” という命令を指定

        em.close();

        request.setAttribute("employees", employees);//データベースから取得したメッセージ一覧
        request.setAttribute("employees_count", employees_count);// 全件数
        request.setAttribute("page", page);// ページ数
        
      //フラッシュメッセージがセッションスコープにセットされていたら
        //リクエストスコープに保存する（セッションスコープからは削除）
        if(request.getSession().getAttribute("flush") != null) {//セッションスコープから取り出してnullではない時
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");//セッションスコープのflushを削除
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }


}
