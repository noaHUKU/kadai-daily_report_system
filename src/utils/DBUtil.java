/*データベースへ接続する部分やSQL文を実行する部分を担うクラス  DAOの作成
 * 　　　（DBとやり取りするオブジェクト）
 * テーブルごとや機能ごとに作成することがある。mainメソッドなし。
 *データベースへの接続関連はHibernate任せ
*/
package utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBUtil {
    private static final String PERSISTENCE_UNIT_NAME = "daily_report_system";
    private static EntityManagerFactory emf;

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    private static EntityManagerFactory getEntityManagerFactory() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }

        return emf;
    }

}
