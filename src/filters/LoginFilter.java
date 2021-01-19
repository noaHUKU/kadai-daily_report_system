package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     * コンストラクタ
     */
    public LoginFilter() {
    }

    /**
     * @see Filter#destroy()
     * （フィルタの処理が不要になったため）フィルタを破棄する
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     * フィルタとしての実行内容
     * chain.doFilter(request, response); という1行
     * 前に書いた処理はサーブレットが処理を実行する 前 にフィルタの処理が実行
     * 後に書いた処理はサーブレットが処理を実行した 後 にフィルタの処理が実行
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String context_path = ((HttpServletRequest)request).getContextPath();//パスの取得？？
        String servlet_path = ((HttpServletRequest)request).getServletPath();

        if(!servlet_path.matches("/css.*")) {       // CSSフォルダ内は認証処理から除外する。ｃｓｓのＵＲＬでない時
            HttpSession session = ((HttpServletRequest)request).getSession();//サーブレット内のセッションスコープ？

         // セッションスコープに保存された従業員（ログインユーザ）情報を取得
            Employee e = (Employee)session.getAttribute("login_employee");

            if(!servlet_path.equals("/login")) {        // ログイン画面以外について　　ログインのＵＲＬではない時
             // ログアウトしている状態であれば
                // ログイン画面にリダイレクト
                if(e == null) {//インスタンスにデータが入っていない時
                    ((HttpServletResponse)response).sendRedirect(context_path + "/login");
                    return;
                }

             // 従業員管理の機能は管理者のみが閲覧できるようにする
                if(servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");//トップページへリダイレクト
                    return;
                }
            }else {   //ログイン画面について
             // ログインしているのにログイン画面を表示させようとした場合は
                // システムのトップページにリダイレクト
                if(e != null) {
                    ((HttpServletResponse)response).sendRedirect(context_path + "/");
                    return;
                }
            }
        }


        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     * フィルタの処理がはじめて実行されるときの処理
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
