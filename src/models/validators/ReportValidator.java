//バリデーション（入力チェック）用クラス
package models.validators;

import java.util.ArrayList;
import java.util.List;

import models.Report;

public class ReportValidator {
    public static List<String> validate(Report r) {
        List<String> errors = new ArrayList<String>();//エラーリスト作成

        String title_error = _validateTitle(r.getTitle());
        if(!title_error.equals("")) {//エラーメッセージがある時リストに追加
            errors.add(title_error);
        }

        String content_error = _validateContent(r.getContent());
        if(!content_error.equals("")) {//エラーメッセージがある時リストに追加
            errors.add(content_error);
        }

        return errors;

    }

    private static String _validateTitle(String title) {
        if(title == null || title.equals("")) {//タイトルがnullと等しい（データが無い）または空欄の時
            return "タイトルを入力してください。";//エラーメッセージを返す
        }

        return "";//空欄を返す
    }

    private static String _validateContent(String content) {
        if(content == null || content.equals("")) {
            return "内容を入力してください。";
        }

        return "";
    }

}
