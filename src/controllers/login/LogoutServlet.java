//ログアウト
package controllers.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     * 「セッションスコープに login_employee という名前で従業員情報のオブジェクトを持っていること」を
     * ログインしている状態だとしているので、セッションスコープから login_employee を除去することでログアウトした状態にします。
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute("login_employee");//セッションスコープから login_employee を除去

        request.getSession().setAttribute("flush", "ログアウトしました。");
        response.sendRedirect(request.getContextPath() + "/login");//ログイン画面にリダイレクト
    }

}
