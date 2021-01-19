//「アプリケーション全般の設定をテキスト形式のファイルから読み込み、
//(今回はパスワードのハッシュ化に用いるデータをプロパティファイルに入れている)
//アプリケーションスコープに設定値を格納する」リスナー作成
package listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class PropertiesListener
 *
 */
@WebListener
public class PropertiesListener implements ServletContextListener {

    /**
     * Default constructor.
     * 訳）標準、基準、初期設定（Default）のコントラスタ
     */
    public PropertiesListener() {
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     * 「Webアプリケーションが終了したとき」の処理？
     */
    public void contextDestroyed(ServletContextEvent arg0)  {
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     * 「Webアプリケーションが起動したとき」の処理
     */
    public void contextInitialized(ServletContextEvent arg0)  {
        ServletContext context = arg0.getServletContext();//アプリケーションを管理するクラス＝ここにセットする＝アプリケーションスコープ

        String path = context.getRealPath("/META-INF/application.properties");//アプリケーション上の仮想パスを絶対パスに変換するためのメソッド
        try {
            InputStream is = new FileInputStream(path);//ファイル読み込みを行うクラス。引数に上で変換した絶対パスを入れる
            Properties properties = new Properties();//プロパティファイルを読み込みクラス。プログラムの設定値などを保存しておくファイルとして使用。コンパイル（翻訳）なしでプログラムの動作を変更できる？
            properties.load(is);
            is.close();//ファイルの読み込みが終わったら閉じないといけない。

            Iterator<String> pit = properties.stringPropertyNames().iterator();//イテレータ。集合してる要素に対して順番にアクセスする時に使用するインターフェース数とか番号とかわかんなくても全部に順にアクセスできる。調べなくてもいいので便利で高速
            while(pit.hasNext()) {//最後になったらかってにfalseになる。それまではtrue
                String pname = pit.next();//次の要素取得
                context.setAttribute(pname, properties.getProperty(pname));//アプリケーションスコープへ登録
            }

        } catch(FileNotFoundException e) {//指定されたパスのファイルが見つかりません
        }catch(IOException e) {}//出入力処理のエラー



    }

}
