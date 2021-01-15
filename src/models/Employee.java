package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "employees")
@NamedQueries({
    @NamedQuery(
        name = "getAllEmployees",
        query = "SELECT e FROM Employee AS e ORDER BY e.id DESC"//すべての従業員情報を取得
    ),
    @NamedQuery(
        name = "getEmployeesCount",
        query = "SELECT COUNT(e) FROM Employee AS e"//従業員情報の全件数を取得
    ),
    @NamedQuery(
        name = "checkRegisteredCode",
        query = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :code"//指定された社員番号がすでにデータベースに存在しているか
    ),
    @NamedQuery(
        name = "checkLoginCodeAndPassword",
        query = "SELECT e FROM Employee AS e WHERE e.delete_flag = 0 AND e.code = :code AND e.password = :pass"
        //従業員がログインするときに社員番号とパスワードが正しいかをチェックする
    )
})
@Entity
public class Employee {
    @Id
    @Column(name = "id")//リソース内での連番
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//数値型

    @Column(name = "code", nullable = false, unique = true)//社員番号
    //一意制約(unique = true)すでに存在している社員番号は登録できない旨をデータベースに教えてあげるための設定
    private String code;//文字列型

    @Column(name = "name", nullable = false)//社員名
    private String name;

    @Column(name = "password", length = 64, nullable = false)//システムへのログインパスワード
    //length = 64 を入れることで、入力できる文字情報が最大64文字までになる
    /*SHA256 というハッシュ関数を利用してハッシュ化した文字列をデータベースへ保存できるようにします。
     * SHA256 は、どんな文字数の文字列でも必ず、64文字のハッシュ化された文字列にしてくれます。
     * そのため、固定で64文字までという設定を追記したのです。
     * */
    private String password;

    @Column(name = "admin_flag", nullable = false)//管理者権限があるかどうか
    private Integer admin_flag;//数値型（一般：0、管理者：1）

    @Column(name = "created_at", nullable = false)//登録日時
    private Timestamp created_at;//日時型

    @Column(name = "updated_at", nullable = false)//更新日時
    private Timestamp updated_at;//日時型

    @Column(name = "delete_flag", nullable = false)//削除された従業員かどうか
    private Integer delete_flag;//数値型（現役：0、削除済み：1）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAdmin_flag() {
        return admin_flag;
    }

    public void setAdmin_flag(Integer admin_flag) {
        this.admin_flag = admin_flag;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(Integer delete_flag) {
        this.delete_flag = delete_flag;
    }
}