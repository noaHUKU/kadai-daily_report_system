//getPasswordEncrypt(src) のようにメソッド名をひとつ記述すれば実行できるハッシュ化メソッド
//さまざまなコントローラで使えるよう、DBUtil のようなユーティリティに
//新規登録の際に入力するパスワードに対するセキュリティー
package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class EncryptUtil {
    public static String getPasswordEncrypt(String plain_p, String pepper) {
        //引数で受け取った文字列にペッパー文字列を連結させたものを SHA256 でハッシュ化
        //引数の文字列が何もなければ、空の文字列を返す。
        String ret = "";

        if(plain_p != null && !plain_p.equals("")) {//データがあり、空欄でもない時
            byte[] bytes;
            String password = plain_p + pepper;//パスワードにpepper（リスナーで勝手に読み込まれる）の文字列を足して味付け（高度なパスワードにする）
            try {
                bytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());//ハッシュ化してる？
                ret = DatatypeConverter.printHexBinary(bytes);
            }catch(NoSuchAlgorithmException ex) {}
        }

        return ret;
    }

}
